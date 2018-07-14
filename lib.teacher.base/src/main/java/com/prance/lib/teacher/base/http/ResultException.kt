package com.prance.lib.teacher.base.http

import java.io.IOException

data class ResultException(val errCode: String, val msg: String?) : IOException(msg)