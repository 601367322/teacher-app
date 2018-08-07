package com.prance.lib.socket

import com.prance.lib.common.utils.http.mySubscribe
import com.prance.lib.database.MessageDaoUtils

/**
 * 消息回执
 */
class MessageResponseCallBack(private val mPushApiService: PushApiService, private val mMessageDaoUtils: MessageDaoUtils) : Thread() {

    override fun run() {
        while (!isInterrupted) {
            val list = mMessageDaoUtils.getAllMessage()
            if (list.isNotEmpty()) {
                var ids = mutableListOf<String>()
                for (message in list) {
                    ids.add(message.msgId)
                }
                mPushApiService.messageReceivedCallBack(PushApiService.messageReceivedCallBack, ids.joinToString())
                        .mySubscribe {
                            for (message in list) {
                                mMessageDaoUtils.deleteMessage(message)
                            }
                        }
            }
            try {
                Thread.sleep(3000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
                break
            }
        }
    }
}