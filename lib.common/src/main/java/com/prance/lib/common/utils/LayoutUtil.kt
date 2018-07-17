package com.prance.lib.common.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * 得到View
 *
 * @param parent 父窗体
 * @param res    资源文件
 * @return
 */
fun getInflate(parent: View, res: Int): View {
    return LayoutInflater.from(parent.context).inflate(res, parent as ViewGroup, false)
}