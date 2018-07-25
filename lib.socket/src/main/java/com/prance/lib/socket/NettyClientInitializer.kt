package com.prance.lib.socket

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.codec.string.StringEncoder
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.timeout.IdleStateHandler
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit
import io.netty.handler.codec.Delimiters
import io.netty.handler.codec.DelimiterBasedFrameDecoder




class NettyClientInitializer(private var listener: NettyListener) : ChannelInitializer<SocketChannel>() {

    private val WRITE_WAIT_SECONDS = 30L

    private val READ_WAIT_SECONDS = 15L

    @Throws(Exception::class)
    override fun initChannel(ch: SocketChannel) {
        val sslCtx = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE).build()

        val pipeline = ch.pipeline()
        // 开启SSL
//        pipeline.addLast(sslCtx.newHandler(ch.alloc()))
        //解决粘包问题
        pipeline.addLast("framer", DelimiterBasedFrameDecoder(
                8192, *Delimiters.lineDelimiter()))
        // 开启日志，可以设置日志等级
//        pipeline.addLast(LoggingHandler(LogLevel.INFO))
        //字符串解码器
        pipeline.addLast("decoder",StringDecoder(Charset.forName("utf-8")))
        //字符串编码器
        pipeline.addLast("encoder",StringEncoder(Charset.forName("utf-8")))
        //设置心跳
        pipeline.addLast(IdleStateHandler(READ_WAIT_SECONDS, WRITE_WAIT_SECONDS, 0, TimeUnit.SECONDS))
        //心跳检测
        pipeline.addLast(HeartBeatServerHandler())

        pipeline.addLast(NettyClientHandler(listener))

    }
}