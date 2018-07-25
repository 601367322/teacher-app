package com.prance.lib.socket

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.blankj.utilcode.util.LogUtils
import com.prance.lib.common.utils.Constants
import com.prance.lib.common.utils.UrlUtil
import com.prance.lib.socket.NettyListener.Companion.STATUS_CONNECT_CLOSED
import com.prance.lib.socket.NettyListener.Companion.STATUS_CONNECT_SUCCESS
import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.channel.ChannelFutureListener
import java.util.concurrent.TimeUnit


class PushService : Service() {

    private var mEventLoopGroup: EventLoopGroup? = null
    private var mChannel: Channel? = null
    private lateinit var mSocketThread: Thread

    companion object {
        fun callingIntent(context: Context): Intent {
            return Intent(context, PushService::class.java)
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mSocketThread = SocketThread()
        mSocketThread.start()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (intent.action) {

            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    internal inner class SocketThread : Thread(), NettyListener {

        lateinit var mBootstrap: Bootstrap

        override fun run() {
            mEventLoopGroup = NioEventLoopGroup()

            mBootstrap =
                    Bootstrap().group(mEventLoopGroup)
                            .option(ChannelOption.SO_KEEPALIVE, true)
                            .channel(NioSocketChannel::class.java)
                            .handler(NettyClientInitializer(this))

            doConnect()
        }

        private fun doConnect() {
            if (mChannel != null && mChannel!!.isActive) {
                return
            }

            val future = mBootstrap.connect(UrlUtil.getPropertiesValue(Constants.SOCKET_HOST), UrlUtil.getPropertiesValue(Constants.SOCKET_PORT).toInt())

            future.addListener(ChannelFutureListener { futureListener ->
                if (futureListener.isSuccess) {
                    mChannel = futureListener.channel()
                    LogUtils.d("Connect to server successfully!")
                } else {
                    LogUtils.d("Failed to connect to server, try connect after 3s")
                    futureListener.channel().eventLoop().schedule({ doConnect() }, 3, TimeUnit.SECONDS)
                }
            })
        }

        override fun onMessageResponse(msg: String) {

        }

        override fun onServiceStatusConnectChanged(statusCode: Int) {
            when (statusCode) {
                STATUS_CONNECT_SUCCESS -> {
                    mChannel?.writeAndFlush("链接成功\n")
                }
                STATUS_CONNECT_CLOSED -> {
                    mChannel = null

                    if (mEventLoopGroup != null) {
                        doConnect()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        try {
            mChannel?.close()
            mEventLoopGroup?.shutdownGracefully()
            mChannel = null
            mEventLoopGroup = null
            mSocketThread.interrupt()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}