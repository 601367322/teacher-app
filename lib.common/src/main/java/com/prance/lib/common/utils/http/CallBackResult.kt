package com.prance.lib.common.utils.http

import java.io.Serializable

/**
 * Created by shenbingbing on 16/4/22.
 */
data class CallBackResult constructor(var errno: Int? = null,
                                      var status: Int? = null,
                                      var msg: String? = null,
                                      var error: String? = null,
                                      var data: Any? = null) : Serializable