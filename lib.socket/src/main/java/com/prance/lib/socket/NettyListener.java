package com.prance.lib.socket;

import io.netty.buffer.ByteBuf;

public interface NettyListener {

    public final static byte STATUS_CONNECT_SUCCESS = 1;

    public final static byte STATUS_CONNECT_CLOSED = 0;

    public final static byte STATUS_CONNECT_ERROR = 0;


    /**
     * 对消息的处理
     */
    void onMessageResponse(ByteBuf byteBuf);

    /**
     * 当服务状态发生变化时触发
     */
    public void onServiceStatusConnectChanged(int statusCode);
}