package com.prance.lib.socket

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler


class NettyClientHandler(private val listener: NettyListener) : SimpleChannelInboundHandler<String>() {

    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) {
        listener.onServiceStatusConnectChanged(NettyListener.STATUS_CONNECT_SUCCESS)
    }

    @Throws(Exception::class)
    override fun channelInactive(ctx: ChannelHandlerContext) {
        listener.onServiceStatusConnectChanged(NettyListener.STATUS_CONNECT_CLOSED)
    }

    override fun messageReceived(ctx: ChannelHandlerContext?, msg: String?) {
        msg?.let {
            listener.onMessageResponse(it)
        }
    }
}