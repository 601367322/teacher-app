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
                LogUtils.i("接收心跳 ============ $it")
                return
            }
            ctx?.fireChannelRead(it)
        }
    }

    @Throws(Exception::class)
    override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any) {
        if (evt is IdleStateEvent) {
            //服务端对应着读事件，当为READER_IDLE时触发
            when {
                evt.state() == IdleState.READER_IDLE -> {
                    LogUtils.i("读取超时")
                    ctx.close()
                }
                evt.state() == IdleState.WRITER_IDLE -> {
                    LogUtils.i("发送心跳 --------------- #")
                    ctx.writeAndFlush("#$LINE_SEP")
                }
                else -> super.userEventTriggered(ctx, evt)
            }
        }
    }

}