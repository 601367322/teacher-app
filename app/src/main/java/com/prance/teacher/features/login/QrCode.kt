package com.prance.teacher.features.login

import com.google.gson.Gson
import com.prance.lib.base.extension.empty

data class QrCode(val signal: String, val token: String, val timestamp: String, val expireTime: String) {

    companion object {
        fun empty() = QrCode(String.empty(), String.empty(), String.empty(), String.empty())
    }

    fun toJson() = Gson().toJson(this)
}