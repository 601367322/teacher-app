package com.prance.teacher.storage

import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.Utils
import com.prance.lib.base.extension.empty
import com.prance.sharedpreferences.SharedDataUtil


/**
 * 用数据库代替SharedPreferences
 * 防止多线程\多进程操作数据丢失问题
 * 单例模式
 */
object CommonShared {

    var sp: SharedDataUtil = SharedDataUtil.getInstance(Utils.getApp())
    var editor: SharedDataUtil.SharedDataEditor

    init {
        editor = sp.sharedDataEditor
    }

    private var PRE_VERSION = "pre_version";


    fun setPreVersion(version: Int) {
        editor.putInt(PRE_VERSION, version)
        editor.commit()
    }

    fun getPreVersion(): Int {
        return sp.getInt(PRE_VERSION, AppUtils.getAppVersionCode())
    }

}
