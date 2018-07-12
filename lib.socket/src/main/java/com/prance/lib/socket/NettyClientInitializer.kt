package com.prance.lib.socket

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import io.netty.channel.ChannelPipeline
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.SslContext
import io.netty.handler.codec.string.StringEncoder
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.timeout.IdleStateHandler
import java.util.concurrent.TimeUnit


class NettyClientInitializer(private var listener: NettyListener) : ChannelInitializer<SocketChannel>() {

    private val WRITE_WAIT_SECONDS = 10

    private val READ_WAIT_SECONDS = 13

    @Throws(Exception::class)
    override fun initChannel(ch: SocketChannel) {
        val sslCtx = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE).build()

        val pipeline = ch.pipeline()
        // 开启SSL
        pipeline.addLast(sslCtx.newHandler(ch.alloc()))
        // 开启日志，可以设置日志等级
        pipeline.addLast(LoggingHandler(LogLevel.INFO))
        //设置心跳
        pipeline.addLast(IdleStateHandler(0, 30, 0, TimeUnit.SECONDS))
        //字符串解码器
        pipeline.addLast(StringDecoder())
        //字符串编码器
        pipeline?.addLast(StringEncoder())
        //心跳检测
        pipeline.addLast(HeartBeatServerHandler())

        listener.let { pipeline.addLast(NettyClientHandler(it)) }

    }
}