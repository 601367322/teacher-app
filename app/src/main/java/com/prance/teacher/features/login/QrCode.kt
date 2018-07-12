package com.prance.teacher.features.login

import com.prance.lib.base.extension.empty

data class QrCode(val url: String) {

    companion object {
        fun empty() = QrCode(String.empty())
    }
}