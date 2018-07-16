package com.prance.lib.teacher.base.http

import java.io.Serializable

/**
 * Created by shenbingbing on 16/4/22.
 */
data class CallBackResult constructor(val status: String = "200",
                                      val msg: String? = null,
                                      val data: Any? = null) : Serializable