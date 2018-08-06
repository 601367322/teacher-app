package com.prance.lib.socket

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.blankj.utilcode.util.LogUtils
import com.prance.lib.common.utils.Constants
import com.prance.lib.common.utils.UrlUtil
import com.prance.lib.common.utils.http.RetrofitUtils
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.lib.database.MessageDaoUtils
import com.prance.lib.database.MessageEntity
import com.prance.lib.socket.MessageListener.Companion.STATUS_CONNECT_CLOSED
import io.netty.bootstrap.Bootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import java.util.concurrent.TimeUnit


class PushService : Service() {

    private var mEventLoopGroup: EventLoopGroup? = null
    private var mChannel: Channel? = null
    private lateinit var mSocketThread: SocketThread
    private lateinit var mMessageResponseCallBack: MessageResponseCallBack
    private var mListeners: MutableList<MessageListener> = mutableListOf()
    private val mMessageDaoUtils = MessageDaoUtils()

    companion object {

        const val CMD = "cmd"

        const val ATTEND_CLASS = 1 //上课
        const val CMD_SEND_QUESTION = 2 //发送练习
        const val CMD_END_QUESTION = 3 //结束联系
        const val QUESTION_RESULT = 4//答题结果

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

        fun removeListener(listener: MessageListener) {
            this@PushService.mListeners.remove(listener)
        }

        fun sendMessage(msg: String) {
            mSocketThread.sendMessage(msg)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        mSocketThread = SocketThread()
        mSocketThread.start()
        mMessageResponseCallBack = MessageResponseCallBack(RetrofitUtils.getApiService(PushApiService::class.java), mMessageDaoUtils)
        mMessageResponseCallBack.start()
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

            LogUtils.d(UrlUtil.getPropertiesValue(Constants.SOCKET_HOST) + "\n" + UrlUtil.getPropertiesValue(Constants.SOCKET_PORT).toInt())

            val future = mBootstrap.connect(UrlUtil.getPropertiesValue(Constants.SOCKET_HOST), UrlUtil.getPropertiesValue(Constants.SOCKET_PORT).toInt())

            future.addListener(object : ChannelFutureListener {
                override fun operationComplete(futureListener: ChannelFuture?) {
                    futureListener?.run {
                        if (isSuccess) {
                            mChannel = channel()
                            LogUtils.d("连接成功")
                        } else {
                            LogUtils.d("连接失败，3秒后重新链接")
                            channel().eventLoop().schedule({ doConnect() }, 3, TimeUnit.SECONDS)
                        }
                    }
                }
            })
        }

        override fun onMessageResponse(msg: MessageEntity) {

            //检查是否重复消息
            val existsMessage = mMessageDaoUtils.getMessageByMsgId(msg.msgId)
            if (existsMessage != null) {
                return
            }

            //保存消息
            mMessageDaoUtils.saveMessage(msg)

            for (listener in mListeners) {
                try {
                    listener.onMessageResponse(msg)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        public fun sendMessage(msg: String) {
            LogUtils.d(msg)
            mChannel?.writeAndFlush("$msg\n")
        }

        override fun onServiceStatusConnectChanged(statusCode: Int) {
            if (statusCode == STATUS_CONNECT_CLOSED) {
                LogUtils.d("断开连接")
            }

            for (listener in mListeners) {
                try {
                    listener.onServiceStatusConnectChanged(statusCode)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            when (statusCode) {
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
            mMessageResponseCallBack.interrupt()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}