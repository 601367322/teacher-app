package com.prance.lib.socket

import com.blankj.utilcode.util.LogUtils
import java.util.Date

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.timeout.IdleState
import io.netty.handler.timeout.IdleStateEvent

internal class HeartBeatServerHandler : SimpleChannelInboundHandler<String>() {

    override fun channelRead0(ctx: ChannelHandlerContext?, msg: String?) {
        msg?.let {
            if (it == "#") {
                return
            }
            ctx?.fireChannelRead(it)
        }
    }

    @Throws(Exception::class)
    override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any) {
        if (evt is IdleStateEvent) {
            if (evt.state() == IdleState.WRITER_IDLE) {
                ctx.writeAndFlush("#\n")
            } else if (evt.state() == IdleState.READER_IDLE) {
            }
        }
    }
}