package com.prance.teacher.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder

import com.blankj.utilcode.util.LogUtils
import com.liulishuo.okdownload.DownloadTask
import com.prance.lib.common.utils.DiskPathUtils

import java.io.File
import com.liulishuo.okdownload.core.cause.ResumeFailedCause
import com.liulishuo.okdownload.core.listener.DownloadListener3
import com.prance.teacher.BuildConfig
import java.lang.Exception


/**
 * Created by bingbing on 2016/10/20.
 */

class UpdateService : Service() {

    private var url: String? = null
    private var downloadFile: File? = null
    private var mListener: MyDownloadListener? = null
    private var mTask: DownloadTask? = null
    //用于和外界交互
    private val binder = UpdateServiceBinder()

    inner class UpdateServiceBinder : Binder() {

        fun setListener(listener: MyDownloadListener) {
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

        url?.run {

            if(!this.startsWith("http")){
                mListener?.onError()
                return@run
            }

            mTask = DownloadTask.Builder(this, updateDir)
                    .setFilename("""${this.hashCode()}.apk""")
                    // the minimal interval millisecond for callback progress
                    .setMinIntervalMillisCallbackProcess(50)
                    // do re-download even if the task has already been completed in the past.
                    .setPassIfAlreadyCompleted(true)
                    .build()

            mTask?.run {
                if (BuildConfig.DEBUG) {
//                    OkDownload.with().downloadDispatcher().cancel(id)
//                    OkDownload.with().breakpointStore().remove(id)
//                    file?.delete()
                }

                enqueue(object : DownloadListener3() {

                    override fun warn(task: DownloadTask) {

                    }

                    override fun started(task: DownloadTask) {
                        mListener?.onStartDownload()
                    }

                    override fun completed(task: DownloadTask) {
                        task.filename?.run {
                            mListener?.onComplete(task.parentFile.path, this)
                        }
                    }

                    override fun error(task: DownloadTask, e: Exception) {
                        mListener?.onError()
                    }

                    override fun canceled(task: DownloadTask) {

                    }

                    override fun connected(task: DownloadTask, blockCount: Int, currentOffset: Long, totalLength: Long) {

                    }

                    override fun retry(task: DownloadTask, cause: ResumeFailedCause) {

                    }

                    override fun progress(task: DownloadTask, currentOffset: Long, totalLength: Long) {
                        mListener?.onProgress(currentOffset, totalLength)
                    }
                })
            }

        }

        return super.onStartCommand(intent, 0, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        mTask?.cancel()
        LogUtils.d("onDestroy")
    }

    interface MyDownloadListener {
        fun onStartDownload() {}
        fun onProgress(currSize: Long, totalSize: Long)
        fun onComplete(dir: String, name: String)
        fun onError()
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
