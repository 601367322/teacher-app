package com.prance.lib.spark

import android.content.Context
import android.content.Intent
import com.prance.lib.base.service.BaseServicePresenter

class SparkServicePresenter(context: Context, private var listener: SparkListenerAdapter) : BaseServicePresenter<SparkService.SparkServiceBinder>(context) {

    override var mService: SparkService.SparkServiceBinder? = null

    override var intent: Intent = SparkService.callingIntent(context)

    override fun onServiceConnected() {
        mService?.addListener(listener)
        this.listener.onServiceConnected()
    }

    override fun unBind() {
        mService?.removeListener(listener)
        super.unBind()
    }

    fun stopAnswer() {
        mService?.stopAnswer()
    }

    fun sendQuestion(type: SparkService.QuestionType) {
        mService?.sendQuestion(type)
    }

    fun sendData(data: String, uid: String) {
        mService?.sendData(data, uid)
    }
}