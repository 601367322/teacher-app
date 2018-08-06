package com.prance.lib.common.utils.http

import java.io.IOException

data class ResultException(val status: Int, val msg: String?) : IOException(msg)