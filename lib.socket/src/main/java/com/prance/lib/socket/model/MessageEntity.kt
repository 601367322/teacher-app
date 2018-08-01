package com.prance.lib.socket.model

import com.google.gson.Gson
import java.io.Serializable

class MessageEntity : Serializable {

    var cmd: Int? = null
    var data: Any? = null
    var msgId: String? = null

    fun <T> create(clazz: Class<T>): T {
        return Gson().fromJson<T>(Gson().toJson(data), clazz)
    }

}