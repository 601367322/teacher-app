package com.prance.lib.socket

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Message
import com.blankj.utilcode.util.LogUtils
import com.prance.lib.common.utils.Constants
import com.prance.lib.common.utils.UrlUtil
import com.prance.lib.common.utils.http.RetrofitUtils
import com.prance.lib.database.MessageDaoUtils
import com.prance.lib.database.MessageEntity
import com.prance.lib.socket.MessageListener.Companion.STATUS_CONNECT_CLOSED
import io.netty.bootstrap.Bootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.net.InetAddress
import java.util.concurrent.TimeUnit


class PushService : Service() {

    private var mEventLoopGroup: EventLoopGroup? = null
    private var mChannel: Channel? = null
    private lateinit var mSocketThread: SocketThread
    private var mListeners: MutableList<MessageListener> = mutableListOf()
    private val mMessageDaoUtils = MessageDaoUtils()
    private var mPushApiService: PushApiService? = null

    companion object {

        const val CMD = "cmd"

        const val ATTEND_CLASS = 1 //上课
        const val CMD_SEND_QUESTION = 2 //发送练习
        const val CMD_END_QUESTION = 3 //结束联系
        const val QUESTION_RESULT = 4//答题结果
        const val QUIZ = 5//下课反馈提问
        const val QUIZ_RESULT = 6//下课提问结果
        const val INTERACT_START = 7//发起趣味互动
        const val END_INTERACTN = 8//结束趣味互动

        const val SEND_RESPONSE_MESSAGE_WHAT = 1
        const val SEND_RESPONSE_MESSAGE_INTERVAL = 1000

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

        mPushApiService = RetrofitUtils.getApiService(PushApiService::class.java)

        startPostResponseMessage(null)
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

            LogUtils.d(UrlUtil.getPropertiesValue(Constants.SOCKET_HOST) + "\n" + UrlUtil.getPropertiesValue(Constants.SOCKET_PORT).toInt() + "\n" + InetAddress.getByName(UrlUtil.getPropertiesValue(Constants.SOCKET_HOST)).hostAddress)

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

    /**
     * 循环发送回执消息
     */
    private val mMessageResponseCallBackHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            val startTime = System.currentTimeMillis()

            when (msg.what) {
                SEND_RESPONSE_MESSAGE_WHAT -> {
                    val list = mMessageDaoUtils.getAllMessage()
                    if (list.isNotEmpty()) {
                        val ids = mutableListOf<String>()
                        for (message in list) {
                            ids.add(message.msgId)
                        }
                        mPushApiService?.messageReceivedCallBack(PushApiService.messageReceivedCallBack, ids.joinToString())!!
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .subscribe({
                                    for (message in list) {
                                        mMessageDaoUtils.deleteMessage(message)
                                    }

                                    //算时间间隔
                                    val interval = System.currentTimeMillis() - startTime
                                    if (interval >= SEND_RESPONSE_MESSAGE_INTERVAL) {
                                        //如果请求时间大于1秒，则立即发送
                                        startPostResponseMessage(null)
                                    } else {
                                        //成功后，减去请求时间，重发
                                        startPostResponseMessage(SEND_RESPONSE_MESSAGE_INTERVAL - interval)
                                    }
                                }, {
                                    //失败后隔一秒重发
                                    startPostResponseMessage(SEND_RESPONSE_MESSAGE_INTERVAL.toLong())
                                })
                    } else {
                        startPostResponseMessage(SEND_RESPONSE_MESSAGE_INTERVAL.toLong())
                    }
                }
            }
        }
    }

    /**
     * 开始发送回执
     */
    fun startPostResponseMessage(interval: Long?) {
        if (interval != null) {
            mMessageResponseCallBackHandler.sendEmptyMessageDelayed(SEND_RESPONSE_MESSAGE_WHAT, interval)
        } else {
            mMessageResponseCallBackHandler.sendEmptyMessage(SEND_RESPONSE_MESSAGE_WHAT)
        }
    }

    /**
     * 停止发送回执
     */
    private fun stopPostResponseMessage() {
        mMessageResponseCallBackHandler.removeMessages(SEND_RESPONSE_MESSAGE_WHAT)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopPostResponseMessage()
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