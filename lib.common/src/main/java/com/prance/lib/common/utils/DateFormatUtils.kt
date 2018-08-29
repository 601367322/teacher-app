package com.prance.lib.common.utils

import java.text.SimpleDateFormat
import java.util.*

var dateFormat_Min_Second = SimpleDateFormat("mm:ss")

fun format(format: SimpleDateFormat, time: Long): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = time
    return format.format(calendar.time)
}