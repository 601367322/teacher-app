package com.prance.lib.common.utils.http

import java.io.Serializable

/**
 * Created by shenbingbing on 16/4/22.
 */
data class CallBackResult constructor(val status: Int = 200,
                                      val msg: String? = null,
                                      val data: Any? = null) : Serializable