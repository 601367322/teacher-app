package com.prance.lib.socket

import com.prance.lib.database.MessageDaoUtils
import io.reactivex.schedulers.Schedulers

/**
 * 消息回执
 */
class MessageResponseCallBack(private val mPushApiService: PushApiService, private val mMessageDaoUtils: MessageDaoUtils) : Thread() {

    override fun run() {
        while (!isInterrupted) {
            if (!doPost()) {
                break
            }
        }
    }

    private fun doPost(): Boolean {
        val list = mMessageDaoUtils.getAllMessage()
        if (list.isNotEmpty()) {
            var ids = mutableListOf<String>()
            for (message in list) {
                ids.add(message.msgId)
            }
            mPushApiService.messageReceivedCallBack(PushApiService.messageReceivedCallBack, ids.joinToString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe({
                        for (message in list) {
                            mMessageDaoUtils.deleteMessage(message)
                        }
                    })
        }
        return try {
            Thread.sleep(1000)
            true
        } catch (e: InterruptedException) {
            e.printStackTrace()
            false
        }
    }
}