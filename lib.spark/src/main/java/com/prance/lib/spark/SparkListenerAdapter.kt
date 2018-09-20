package com.prance.lib.spark


import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbEndpoint

import com.spark.teaching.answertool.usb.helper.UsbListener
import com.spark.teaching.answertool.usb.model.ReceiveAnswer
import com.spark.teaching.answertool.usb.model.ReportBindCard

abstract class SparkListenerAdapter : UsbListener {

    open fun onServiceConnected() {}

    override fun onConnected(usbDeviceConnection: UsbDeviceConnection, `in`: UsbEndpoint, out: UsbEndpoint, serialNum: String) {

    }

    override fun onDisConnected() {

    }

    override fun onAnswerReceived(answer: ReceiveAnswer) {

    }

    override fun onCardBind(reportBindCard: ReportBindCard) {

    }
}