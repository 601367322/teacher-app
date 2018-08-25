package com.prance.lib.socket

import com.google.gson.Gson
import org.json.JSONObject
import java.io.Serializable

fun generatePostMessage(cmd: Int, obj: Serializable): String {
    val json = JSONObject()
    json.put("cmd",cmd)
    json.put("data", Gson().toJson(obj))
    return json.toString()
}