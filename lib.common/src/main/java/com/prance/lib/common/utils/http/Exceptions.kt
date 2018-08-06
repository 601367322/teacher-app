package com.prance.lib.common.utils.http

/**
 * @author shenbingbing
 * @date 16/4/22
 */
object Exceptions {
    fun illegalArgument(msg: String, vararg params: Any) {
        throw IllegalArgumentException(String.format(msg, *params))
    }
}
