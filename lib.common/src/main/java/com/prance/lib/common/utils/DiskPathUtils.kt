package com.prance.lib.common.utils

import android.content.Context
import android.os.Environment

import java.io.File
import java.io.IOException

/**
 * Created by shenbingbing on 16/4/19.
 */
object DiskPathUtils {

    //自定义文件夹名称
    val DIR = "prance"

    /**
     * 获取Sd卡的根♂路径
     *
     * @return
     */
    val sdCardPath: String
        get() = Environment.getExternalStorageDirectory().path

    /**
     * 获取自定义的目录文件夹
     * 不会被系统清除
     *
     * @return
     */
    val customDir: String
        get() {
            val customDir = File(sdCardPath + File.separator + DIR + File.separator)
            if (!customDir.exists()) {
                customDir.mkdirs()
                val noMediaFile = File(customDir, ".nomedia")
                if (!noMediaFile.exists()) {
                    try {
                        noMediaFile.createNewFile()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }
            return sdCardPath + File.separator + DIR + File.separator
        }

    /**
     * 获取程序默认缓存地址
     * 可能会被系统清除
     *
     * @param context
     * @return
     */
    fun getDiskCacheDir(context: Context): String {
        var cachePath: String? = null
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
            cachePath = context.externalCacheDir!!.path
        } else {
            cachePath = context.cacheDir.path
        }
        return cachePath
    }

    /**
     * 获取程序默认文件地址
     * 可能会被系统清除
     *
     * @param context
     * @return
     */
    fun getDiskFileDir(context: Context): String {
        var filePath: String? = null
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
            filePath = context.getExternalFilesDir(null)!!.path
        } else {
            filePath = context.filesDir.path
        }
        return filePath
    }

}
