package com.prance.teacher.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder

import com.blankj.utilcode.util.LogUtils
import com.leo.download.BuildConfig
import com.leo.download.DownloadError
import com.leo.download.DownloadListener
import com.leo.download.DownloadManager
import com.prance.lib.common.utils.DiskPathUtils

import java.io.File

/**
 * Created by bingbing on 2016/10/20.
 */

class UpdateService : Service() {

    private var url: String? = null
    private var downloadFile: File? = null
    private var mListener: DownloadListener? = null

    //用于和外界交互
    private val binder = UpdateServiceBinder()

    inner class UpdateServiceBinder : Binder() {

        fun setListener(listener: DownloadListener) {
            this@UpdateService.mListener = listener
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent == null || intent.extras == null) {
            return super.onStartCommand(intent, flags, startId)
        }
        val temp = intent.extras!!.getString(URL)

        if (temp == url) {
            return super.onStartCommand(intent, flags, startId)
        } else {
            url = temp
        }

        val parentFile = File(DiskPathUtils.getDiskFileDir(this)).parentFile
        val updateDir = File(parentFile, DIR)

        if (!updateDir.exists()) {
            updateDir.mkdirs()
        }

        if (BuildConfig.DEBUG) {
            DownloadManager.getInstance(applicationContext).deleteRecord(url)
        }


        DownloadManager.getInstance(applicationContext).enquene(url, updateDir.path, object : DownloadListener {

            var currentTime: Long = 0

            override fun onStart(id: Int, size: Long) {
                LogUtils.d("onStart")
                currentTime = System.currentTimeMillis()

                mListener?.onStart(id, size)
            }

            override fun onProgress(id: Int, currSize: Long, totalSize: Long) {
                if (System.currentTimeMillis() - currentTime >= 1000) {
                    currentTime = System.currentTimeMillis()
                }

                mListener?.onProgress(id, currSize, totalSize)
            }

            override fun onRestart(id: Int, currSize: Long, totalSize: Long) {
                LogUtils.d("onRestart")

                mListener?.onRestart(id, currSize, totalSize)
            }

            override fun onPause(id: Int, currSize: Long) {
                LogUtils.d("onPause")

                mListener?.onPause(id, currSize)
            }

            override fun onComplete(id: Int, dir: String, name: String) {
                LogUtils.d("onComplete")

                downloadFile = File(dir, name)
                url = null

                mListener?.onComplete(id, dir, name)
            }

            override fun onCancel(id: Int) {
                LogUtils.d("onCancel")

                mListener?.onCancel(id)
            }

            override fun onError(id: Int, error: DownloadError) {
                LogUtils.d("onError")
                url = null

                mListener?.onError(id, error)
            }
        })

        return super.onStartCommand(intent, 0, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtils.d("onDestroy")
    }

    companion object {

        const val DIR = "app_update"
        const val URL = "url"

        fun callingIntent(context: Context, url: String): Intent {
            val intent = Intent(context, UpdateService::class.java)
            intent.putExtra(URL, url)
            return intent
        }

        fun callingIntent(context: Context): Intent {
            return Intent(context, UpdateService::class.java)
        }
    }
}
