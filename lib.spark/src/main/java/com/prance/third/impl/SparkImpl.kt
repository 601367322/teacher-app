package com.prance.third.impl

import android.content.Context
import com.prance.lib.spark.SparkService
import com.prance.lib.third.inter.ISpark
import com.spark.teaching.answertool.usb.helper.CommunicateHelper
import com.spark.teaching.answertool.usb.helper.ConnectHelper

class SparkImpl : ISpark {
    override fun stopService(context: Context) {
        ConnectHelper.getInstance().onServiceDestroy(context)
        CommunicateHelper.getInstance().stop()
        context.stopService(SparkService.callingIntent(context))
    }
}