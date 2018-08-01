package com.prance.lib.socket

import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import com.prance.lib.socket.model.MessageEntity
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler


class NettyClientHandler(private val listener: MessageListener) : SimpleChannelInboundHandler<String>() {

    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) {
        listener.onServiceStatusConnectChanged(MessageListener.STATUS_CONNECT_SUCCESS)
    }

    @Throws(Exception::class)
    override fun channelInactive(ctx: ChannelHandlerContext) {
        listener.onServiceStatusConnectChanged(MessageListener.STATUS_CONNECT_CLOSED)
    }

    override fun messageReceived(ctx: ChannelHandlerContext?, msg: String?) {
        msg?.let {
            LogUtils.d(msg)
            try {
                listener.onMessageResponse(Gson().fromJson(msg, MessageEntity::class.java))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}