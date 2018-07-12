package com.prance.lib.socket

import java.util.Date

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.handler.timeout.IdleState
import io.netty.handler.timeout.IdleStateEvent

internal class HeartBeatServerHandler : ChannelInboundHandlerAdapter() {

    @Throws(Exception::class)
    override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any) {
        println("客户端循环心跳监测发送: " + Date())
        if (evt is IdleStateEvent) {
            if (evt.state() == IdleState.WRITER_IDLE) {
                ctx.writeAndFlush("biubiu")
            }
        }
    }

}