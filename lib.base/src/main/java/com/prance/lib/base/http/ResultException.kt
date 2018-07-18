package com.prance.lib.base.http

import java.io.IOException

data class ResultException(val status: Int, val msg: String?) : IOException(msg)