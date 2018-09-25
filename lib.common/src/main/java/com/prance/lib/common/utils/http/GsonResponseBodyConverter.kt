/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.prance.lib.common.utils.http

import com.google.gson.Gson
import com.google.gson.TypeAdapter

import java.io.IOException

import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Converter

internal class GsonResponseBodyConverter<T>(private val gson: Gson, private val adapter: TypeAdapter<T>) : Converter<ResponseBody, T> {

    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T {
        value.use { value ->
            val resultStr = value.string()
            val result = gson.fromJson(resultStr, CallBackResult::class.java)

            return if (result.errno == StatusConstant.SUCCESS) {
                if(result.data == null || result.data == ""){
                    result.data = JSONObject()
                }
                adapter.fromJson(gson.toJson(result.data))
            } else {
                throw ResultException(result.errno, result.error)
            }
        }
    }
}