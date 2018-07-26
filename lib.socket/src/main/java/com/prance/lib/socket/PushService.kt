package com.prance.lib.socket

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.webkit.DownloadListener
import com.blankj.utilcode.util.LogUtils
import com.prance.lib.common.utils.Constants
import com.prance.lib.common.utils.UrlUtil
import com.prance.lib.socket.MessageListener.Companion.STATUS_CONNECT_CLOSED
import com.prance.lib.socket.MessageListener.Companion.STATUS_CONNECT_SUCCESS
import io.netty.bootstrap.Bootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import java.util.concurrent.TimeUnit


class PushService : Service() {

    private var mEventLoopGroup: EventLoopGroup? = null
    private var mChannel: Channel? = null
    private lateinit var mSocketThread: Thread
    private var mListeners: MutableList<MessageListener> = mutableListOf()

    companion object {
        fun callingIntent(context: Context): Intent {
            return Intent(context, PushService::class.java)
        }
    }

    //用于和外界交互
    private val binder = PushServiceBinder()

    inner class PushServiceBinder : Binder() {

        fun addListener(listener: MessageListener) {
            this@PushService.mListeners.add(listener)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
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

    internal inner class SocketThread : Thread(), MessageListener {

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

            future.addListener(object : ChannelFutureListener {
                override fun operationComplete(futureListener: ChannelFuture?) {
                    futureListener?.run {
                        if (isSuccess) {
                            mChannel = channel()
                            LogUtils.d("Connect to server successfully!")
                        } else {
                            LogUtils.d("Failed to connect to server, try connect after 3s")
                            channel().eventLoop().schedule({ doConnect() }, 3, TimeUnit.SECONDS)
                        }
                    }
                }
            })
        }

        override fun onMessageResponse(msg: String) {
            for(listener in mListeners){
                try {
                    listener.onMessageResponse(msg)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        public fun sendMessage(msg: String) {
            mChannel?.writeAndFlush("$msg\n")
        }

        override fun onServiceStatusConnectChanged(statusCode: Int) {
            for(listener in mListeners){
                try {
                    listener.onServiceStatusConnectChanged(statusCode)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            when (statusCode) {
                STATUS_CONNECT_SUCCESS -> {
                    sendMessage("链接成功")
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