package com.prance.lib.socket

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.blankj.utilcode.util.LogUtils
import com.prance.lib.common.utils.Constants
import com.prance.lib.common.utils.UrlUtil
import io.netty.bootstrap.Bootstrap
import io.netty.buffer.ByteBuf
import io.netty.channel.Channel
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel

class PushService : Service(), NettyListener {

    private var mEventLoopGroup: EventLoopGroup? = null
    private var mChannel: Channel? = null
    private lateinit var mSocketThread: Thread


    override fun onBind(p0: Intent?): IBinder? {
        LogUtils.d("onBind")
        return null
    }

    override fun onCreate() {
        super.onCreate()
        LogUtils.d("onCreate")
        mSocketThread = SocketThread(this)
        mSocketThread.interrupt()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        LogUtils.d("onStartCommand")

        intent?.let {
            when (intent.action) {

            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onMessageResponse(byteBuf: ByteBuf?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onServiceStatusConnectChanged(statusCode: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    internal inner class SocketThread(private val nettyListener: NettyListener) : Thread() {

        override fun run() {
            mEventLoopGroup = NioEventLoopGroup()
            val bootstrap =
                    Bootstrap().group(mEventLoopGroup)
                            .option(ChannelOption.SO_KEEPALIVE, true)
                            .channel(NioSocketChannel::class.java)
                            .handler(NettyClientHandler(nettyListener))
            mChannel =
                    bootstrap
                            .connect(UrlUtil.getPropertiesValue(Constants.SOCKET_HOST), UrlUtil.getPropertiesValue(Constants.SOCKET_PORT) as Int)
                            .sync()
                            .channel()
        }
    }

    override fun onDestroy() {
        LogUtils.d("onDestroy")
        super.onDestroy()

        mChannel?.close()
        mEventLoopGroup?.shutdownGracefully()
    }

}