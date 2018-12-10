package com.prance.lib.spark

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbEndpoint
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import com.blankj.utilcode.util.LogUtils
import com.spark.teaching.answertool.usb.helper.CommunicateHelper
import com.spark.teaching.answertool.usb.helper.ConnectHelper
import com.spark.teaching.answertool.usb.helper.UsbListener
import com.spark.teaching.answertool.usb.model.*
import java.util.*

class SparkService : Service() {

    //用于和外界交互
    private val binder = SparkServiceBinder()

    var mListener = mutableListOf<SparkListenerAdapter>()

    enum class QuestionType {
        SINGLE, MULTI, YES_OR_NO, COMMON, MULTI_SINGLE, RED_PACKAGE, SEND_DATA
    }

    inner class SparkServiceBinder : Binder() {

        fun addListener(listener: SparkListenerAdapter) {
            mListener.add(listener)
        }

        fun removeListener(listener: SparkListenerAdapter) {
            mListener.remove(listener)
        }

        fun stopAnswer() {
            this@SparkService.stopAnswer()
        }

        fun sendQuestion(type: QuestionType) {
            when (type) {
                QuestionType.SINGLE -> sendSingle()
                QuestionType.MULTI -> sendMulti()
                QuestionType.YES_OR_NO -> sendYesOrNo()
                QuestionType.COMMON -> sendCommon()
                QuestionType.MULTI_SINGLE -> sendMultiSingle()
                QuestionType.RED_PACKAGE -> sendPackage()
            }
        }

        fun sendData(data: String, uid: String) {
            this@SparkService.sendData(data, uid)
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    override fun onCreate() {
        super.onCreate()

        ConnectHelper.getInstance().onServiceCreate(this, mUsbListener)
    }

    private var mUidArray = mutableListOf<Long>()

    private var mHandler = Handler(Looper.getMainLooper())

    private val mUsbListener = object : UsbListener {
        override fun onConnected(usbDeviceConnection: UsbDeviceConnection, `in`: UsbEndpoint, out: UsbEndpoint, serialNum: String) {

            mIsUsbConnected = true
            mUsbSerialNum = serialNum

            // 可以开始通信了
            CommunicateHelper.getInstance().setData(usbDeviceConnection, `in`, out, this)
            CommunicateHelper.getInstance().start()

            //开始绑定
            startBindCard()

            mHandler.post {
                mListener.forEach { i ->
                    i.onConnected(usbDeviceConnection, `in`, out, serialNum)
                }
            }
        }

        override fun onDisConnected() {

            mIsUsbConnected = false
            mUsbSerialNum = null

            CommunicateHelper.getInstance().setData(null, null, null, this)
            CommunicateHelper.getInstance().stopSendDispatcher()
            CommunicateHelper.getInstance().stopDecodeDispatcher()
            // 不stop

            mHandler.post {
                mListener.forEach { i ->
                    i.onDisConnected()
                }
            }
        }

        override fun onAnswerReceived(receiveAnswer: ReceiveAnswer) {

            if (!canReceiveAnswer) {
                return
            }

            // 这里是非主线程
            val uid = receiveAnswer.uid!!
            var answer = receiveAnswer.answer

            //兼容旧答题器对错题
            when (answer) {
                "E" -> receiveAnswer.answer = "1"
                "F" -> receiveAnswer.answer = "2"
                "H" -> answer = " 红包"
            }

            addUid(uid)

            mHandler.post {
                mListener.forEach { i ->
                    i.onAnswerReceived(receiveAnswer)
                }
            }
        }

        override fun onCardBind(reportBindCard: ReportBindCard) {

            // 这里是非主线程
            val uid = reportBindCard.uid!!
            val puid = reportBindCard.pre_uid!!


            addUid(uid)

            mHandler.post {
                mListener.forEach { i ->
                    i.onCardBind(reportBindCard)
                }
            }
        }

        private fun startBindCard() {
            val openBindCard = OpenBindCard()
            CommunicateHelper.getInstance().sendAsync(openBindCard)
        }

        private fun addUid(uid: Long) {
            if (!mUidArray.contains(uid)) {
                mUidArray.add(uid)
            }
        }
    }

    var canReceiveAnswer = false

    /**
     * 停止作答
     */
    private fun stopAnswer() {
        canReceiveAnswer = false
        val ques = SendQuestion()
        ques.questionType = 0x80.toByte()
        ques.time = Date(System.currentTimeMillis())
        ques.seq = 1.toByte()
        CommunicateHelper.getInstance().sendAsync(ques)
    }

    /**
     * 单选题
     */
    private fun sendSingle() {
        val ques = SendQuestion()
        ques.questionType = 0x01.toByte()
        ques.time = Date()
        ques.seq = 1.toByte()
        send(ques)
    }

    /**
     * 对错题
     */
    private fun sendYesOrNo() {
        val ques = SendQuestion()
        ques.questionType = 0x02.toByte()
        ques.time = Date()
        ques.seq = 1.toByte()
        send(ques)
    }

    /**
     * 多选题
     */
    private fun sendMulti() {
        val ques = SendQuestion()
        ques.questionType = 0x04.toByte()
        ques.time = Date()
        ques.seq = 1.toByte()
        send(ques)
    }

    /**
     * 通用、可重复答题
     */
    private fun sendCommon() {
        val ques = SendQuestion()
        ques.questionType = 0x06.toByte()
        ques.time = Date()
        ques.seq = 1.toByte()
        send(ques)
    }

    /**
     * 多个 单选题
     */
    private fun sendMultiSingle() {
        val ques = SendQuestion()
        ques.questionType = 0x05.toByte()
        ques.time = Date(System.currentTimeMillis())
        ques.seq = 1.toByte()
        send(ques)
    }

    /**
     * 发红包
     */
    private fun sendPackage() {
        val ques = SendQuestion()
        ques.questionType = 0x03.toByte()
        ques.time = Date(System.currentTimeMillis())
        ques.seq = 1.toByte()
        send(ques)
    }

    /**
     * 发送数据
     */
    private fun sendData(data: String, uid: String) {
        val ra = Echo()
        ra.uid = uid.toLong()
        ra.show_info = data

        CommunicateHelper.getInstance().sendAsync(ra)
    }

    private fun send(dp: DataPackage) {
        canReceiveAnswer = true
        CommunicateHelper.getInstance().sendAsync(dp)
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtils.i("onDestroy")
        ConnectHelper.getInstance().onServiceDestroy(this)
        CommunicateHelper.getInstance().stop()
    }

    companion object {

        var mIsUsbConnected: Boolean = false

        var mUsbSerialNum: String? = null

        fun callingIntent(context: Context): Intent = Intent(context, SparkService::class.java)
    }
}

