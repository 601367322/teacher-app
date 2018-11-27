package com.prance.lib.spark


import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbEndpoint
import com.blankj.utilcode.util.LogUtils

import com.spark.teaching.answertool.usb.helper.UsbListener
import com.spark.teaching.answertool.usb.model.ReceiveAnswer
import com.spark.teaching.answertool.usb.model.ReportBindCard

abstract class SparkListenerAdapter(var duplicate: Boolean = false) : UsbListener {

    var answerList = mutableListOf<Long>()

    open fun onServiceConnected() {}

    override fun onConnected(usbDeviceConnection: UsbDeviceConnection, `in`: UsbEndpoint, out: UsbEndpoint, serialNum: String) {

    }

    override fun onDisConnected() {

    }

    override fun onAnswerReceived(answer: ReceiveAnswer) {
        if (!duplicate) {
            //防止重复提交
            if (answerList.contains(answer.uid)) {
                return
            }
            answerList.add(answer.uid)
        }


        LogUtils.i(answer)
        onAnswer(answer)
    }

    open fun onAnswer(answer: ReceiveAnswer) {}

    override fun onCardBind(reportBindCard: ReportBindCard) {

    }
}