package com.prance.lib.teacher.base.core.di

/**
 * Created by shenbingbing on 16/4/22.
 */
data class CallBackResult( val status: String = "200",
                       val msg: String? = null,
                       val data: Any? = null)