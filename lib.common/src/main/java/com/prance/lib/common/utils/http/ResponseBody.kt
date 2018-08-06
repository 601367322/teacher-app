package com.prance.lib.common.utils.http

import com.google.gson.annotations.SerializedName

import java.io.Serializable

/**
 * Created by shenbingbing on 16/5/27.
 */
class ResponseBody<T> : Serializable {

    @SerializedName("pageInfo")
    var page: PageBean? = null

    var list: MutableList<T> = mutableListOf()

    class PageBean : Serializable {
        @SerializedName("pn")
        var page: Int = 0
        var total: Int = 0
        @SerializedName("rn")
        var pageCount: Int = 0

        constructor() {}

        constructor(total: Int, page: Int) {
            this.total = total
            this.page = page
        }
    }

    constructor() {}

    constructor(pageBean: PageBean, list: MutableList<T>) {
        this.page = pageBean
        this.list = list
    }
}