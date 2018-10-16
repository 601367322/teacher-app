package com.prance.lib.common.utils

import java.text.SimpleDateFormat
import java.util.*

var dateFormat_Min_Second = SimpleDateFormat("mm:ss")
var dateFormat_Hour_Min = SimpleDateFormat("HH:mm")
var dateFormat_Year_Month_Day_Hour_Min = SimpleDateFormat("yyyy年MM月dd日 HH:mm")

fun format(format: SimpleDateFormat, time: Long): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = time
    return format.format(calendar.time)
}