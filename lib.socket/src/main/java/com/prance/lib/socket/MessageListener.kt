package com.prance.lib.socket

import com.prance.lib.database.MessageEntity


interface MessageListener {


    /**
     * 对消息的处理
     */
    fun onMessageResponse(msg: MessageEntity) {}

    /**
     * 当服务状态发生变化时触发
     */
    fun onServiceStatusConnectChanged(statusCode: Int) {}

    companion object {

        val STATUS_CONNECT_SUCCESS: Int = 1

        val STATUS_CONNECT_CLOSED: Int = 0

        val STATUS_CONNECT_ERROR: Int = 0
    }
}