package com.prance.lib.socket

import com.google.gson.Gson
import java.io.Serializable

data class MessageTemp(var cmd: Int, var data: Any) : Serializable

fun generatePostMessage(cmd: Int, obj: Serializable): String {
    return Gson().toJson(MessageTemp(cmd, obj))
}