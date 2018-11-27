package com.prance.lib.socket

import com.blankj.utilcode.util.LogUtils
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.timeout.IdleState
import io.netty.handler.timeout.IdleStateEvent

internal class NettyHeartBeatHandler : SimpleChannelInboundHandler<String>() {

    private val LINE_SEP = System.getProperty("line.separator")

    override fun messageReceived(ctx: ChannelHandlerContext?, msg: String?) {
        msg?.let {
            if (it == "#") {
                LogUtils.i("接收心跳\t$it")
                return
            }
            ctx?.fireChannelRead(it)
        }
    }

    @Throws(Exception::class)
    override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any) {
        if (evt is IdleStateEvent) {
            if (evt.state() == IdleState.WRITER_IDLE) {
                LogUtils.i("发送心跳\t#")
                ctx.writeAndFlush("#$LINE_SEP")
            } else if (evt.state() == IdleState.READER_IDLE) {
            }
        }
    }
}