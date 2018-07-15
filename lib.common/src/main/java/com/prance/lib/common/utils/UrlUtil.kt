package com.prance.lib.common.utils

import android.content.Context
import android.os.Environment

import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SDCardUtils
import com.blankj.utilcode.util.Utils

import java.io.File
import java.io.FileInputStream
import java.util.Properties

/**
 * Created by shenbingbing on 16/4/18.
 */
object UrlUtil {

    /**
     * 初始属性的文件名
     *
     * @return
     */
    val pathName: String
        get() = "config.properties"

    fun getScheme(): String {
        return getPropertiesValue("scheme")
    }

    fun getHost(): String {
        return getPropertiesValue("host")
    }

    fun getPort(): String {
        return getPropertiesValue("port")
    }

    fun getUrl(): String {
        return """${getScheme()}://${getHost()}"""
    }

    /**
     * 获取Assets目录下prance.properties中的属性
     *
     * @param key
     * @return
     */
    private fun getAssetsProperties(key: String): String {

        var result = ""
        try {
            val properties = Properties()
            val `is` = Utils.getApp().assets.open(pathName)
            properties.load(`is`)
            if (properties.containsKey(key)) {
                result = properties[key].toString()
            }
        } catch (e: Exception) {
            LogUtils.d("getAssetsProperties e[$e]")
        }

        return result
    }

    /**
     * 读取本地SD卡文件下prance.properties中的属性
     *
     * @param
     * @return
     */
    private fun getSDCardProperties(key: String): String {

        var result = ""
        try {
            val properties = Properties()
            val `is` = FileInputStream(getBaseDir() + pathName)
            properties.load(`is`)
            if (properties.containsKey(key)) {
                result = properties[key].toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }


    /**
     * 初始化 prance.properties中的属性
     *
     * @param key
     * @return
     */
    fun getPropertiesValue(key: String): String {

        var result: String

        var isTestModel = ModelUtil.isTestModel

        if (checkSaveLocationExists() && fileIsExists(getBaseDir() + pathName) && isTestModel) {
            result = getSDCardProperties(key)

            if (result == "") {
                result = getAssetsProperties(key)
            }
        } else {
            result = getAssetsProperties(key)
        }
        return result
    }


    /**
     * 判断文件是否存在
     *
     * @return
     */
    private fun fileIsExists(pathName: String): Boolean {
        try {
            val f = File(pathName)
            if (!f.exists()) {
                return false
            }
        } catch (e: Exception) {

            return false
        }

        return true
    }

    /**
     * 检查是否安装SD卡
     *
     * @return
     */
    fun checkSaveLocationExists(): Boolean {
        val sDCardStatus = Environment.getExternalStorageState()
        val status: Boolean
        status = sDCardStatus == Environment.MEDIA_MOUNTED
        return status
    }

    /**
     * 初始化文件保存基本目录
     *
     * @return
     */
    fun getBaseDir(): String {
        return SDCardUtils.getSDCardPaths()[0] + "/prance/"
    }

    /**
     * 初始化文件保存基本目录
     *
     * @return
     */
    fun getBaseCrashDir(): String {
        return getBaseDir()+"crash/"
    }

}
