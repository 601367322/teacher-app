package com.prance.lib.socket

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler


class NettyClientHandler(private val listener: NettyListener) : SimpleChannelInboundHandler<ByteBuf>() {

    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) {
        listener.onServiceStatusConnectChanged(NettyListener.STATUS_CONNECT_SUCCESS.toInt())
    }

    @Throws(Exception::class)
    override fun channelInactive(ctx: ChannelHandlerContext) {
        listener.onServiceStatusConnectChanged(NettyListener.STATUS_CONNECT_CLOSED.toInt())
    }

    @Throws(Exception::class)
    override fun channelRead0(channelHandlerContext: ChannelHandlerContext, byteBuf: ByteBuf) {
        listener.onMessageResponse(byteBuf)
    }

}