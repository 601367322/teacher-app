package com.prance.teacher.features.login

import com.google.gson.Gson
import com.prance.lib.base.extension.empty
import java.io.Serializable

class QrCode constructor(val signal: String, val token: String, val timestamp: Long, val expireTime: String) : Serializable {

    companion object {
        fun empty() = QrCode(String.empty(), String.empty(), 0, String.empty())
    }

    fun toJson() = Gson().toJson(this)
}