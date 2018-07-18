package com.prance.teacher.features.login.model

import com.google.gson.Gson
import com.prance.lib.base.extension.empty
import java.io.Serializable

class QrCodeEntity constructor(val signal: String, val token: String, val timestamp: Long, val expireTime: String) : Serializable {

    fun getExpireTime(): Long {
        return expireTime.toLong() * 1000
    }

    companion object {
        fun empty() = QrCodeEntity(String.empty(), String.empty(), 0, String.empty())
    }

    fun toJson() = Gson().toJson(this)
}