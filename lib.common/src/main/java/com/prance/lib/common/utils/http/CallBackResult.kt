package com.prance.lib.common.utils.http

import java.io.Serializable

/**
 * Created by shenbingbing on 16/4/22.
 */
data class CallBackResult constructor(val errno: Int = 200,
                                      val error: String? = null,
                                      var data: Any? = null) : Serializable