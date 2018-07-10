package com.prance.lib.socket

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import io.netty.channel.ChannelPipeline
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.SslContext


class NettyClientInitializer(private var listener: NettyListener) : ChannelInitializer<SocketChannel>() {

    private val WRITE_WAIT_SECONDS = 10

    private val READ_WAIT_SECONDS = 13

    @Throws(Exception::class)
    override fun initChannel(ch: SocketChannel) {
        val sslCtx = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE).build()

        val pipeline = ch.pipeline()
        pipeline.addLast(sslCtx.newHandler(ch.alloc()))    // 开启SSL
        pipeline.addLast(LoggingHandler(LogLevel.INFO))    // 开启日志，可以设置日志等级
        //        pipeline.addLast(new IdleStateHandler(30, 60, 100));
        listener?.let { pipeline.addLast(NettyClientHandler(it)) }

    }
}