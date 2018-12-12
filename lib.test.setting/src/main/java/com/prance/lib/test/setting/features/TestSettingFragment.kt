package com.prance.lib.test.setting.features

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.blankj.utilcode.util.ZipUtils
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.common.utils.ToastUtils
import com.prance.lib.common.utils.UrlUtil
import com.prance.lib.common.utils.http.RetrofitUtils
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.lib.test.setting.R
import com.prance.lib.test.setting.api.TestApiService
import kotlinx.android.synthetic.main.activity_test_setting.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.util.*


class TestSettingFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.activity_test_setting

    private val mScheme by lazy(mode = LazyThreadSafetyMode.NONE) { resources.getStringArray(R.array.scheme) }
    private val mHost by lazy(mode = LazyThreadSafetyMode.NONE) { resources.getStringArray(R.array.host) }
    private val mSocketHost by lazy(mode = LazyThreadSafetyMode.NONE) { resources.getStringArray(R.array.socket_host) }
    private val mPort by lazy(mode = LazyThreadSafetyMode.NONE) { resources.getStringArray(R.array.port) }
    private val mSocketPort by lazy(mode = LazyThreadSafetyMode.NONE) { resources.getStringArray(R.array.socket_port) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {

        current.text = "当前环境：\t" + UrlUtil.getHost()

        uploadLog.setOnClickListener { it ->

            it.isEnabled = false

            ToastUtils.showLong("正在上传日志")

            Thread {
                val dir = File("/data/log/")
                if (dir.exists()) {

                    //所有Logcat输出日志
                    val zipFilesPath = mutableListOf(
                            "logcat_full.log",
                            "logcat_full.log.1",
                            "logcat_full.log.2",
                            "logcat_full.log.3",
                            "logcat_full.log.4",
                            "logcat_full.log.5",
                            "logcat_full.log.6",
                            "logcat_full.log.7",
                            "logcat_full.log.8",
                            "logcat_full.log.9"
                    )

                    val zipFiles = mutableListOf<File>()

                    for (f in zipFilesPath) {
                        val logFile = File(dir, f)
                        if (logFile.exists()) {
                            zipFiles.add(logFile)
                        }
                    }

                    //纯本应用日志
                    val mDefaultDir = if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() && Utils.getApp().externalCacheDir != null)
                        File(Utils.getApp().externalCacheDir, "log")
                    else {
                        File(Utils.getApp().cacheDir, "log")
                    }

                    val appLogs = mDefaultDir.listFiles()
                    appLogs?.run {
                        for (i in Math.max(this.size - 3, 0) until this.size) {
                            zipFiles.add(this[i])
                        }
                    }

                    val zipLogFile = File(dir, "logs.zip")
                    ZipUtils.zipFiles(zipFiles, File(dir, "logs.zip"))

                    val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), zipLogFile)
                    val body = MultipartBody.Part.createFormData("file",
                            zipLogFile.name, requestFile)

                    RetrofitUtils.getApiService(TestApiService::class.java).uploadLog(body)
                            .flatMap {
                                LogUtils.i(it.url)
                                return@flatMap RetrofitUtils.getApiService(TestApiService::class.java).uploadLogInfo(Build.SERIAL, it.url)
                            }
                            .mySubscribe({
                                it.printStackTrace()
                                ToastUtils.showLong("上传日志失败")
                                uploadLog.isEnabled = true
                            }) {
                                LogUtils.i("上传日志地址成功")
                                ToastUtils.showLong("上传日志成功")
                                zipLogFile.delete()
                                uploadLog.isEnabled = true
                            }

                }
            }.start()
        }

        systemSettingBtn.setOnClickListener {
            startActivity(Intent(Settings.ACTION_SETTINGS))
        }

        release.setOnClickListener {
            ok(mHost[0], mPort[0], mSocketHost[3], mSocketPort[1])
        }

        staging.setOnClickListener {
            ok(mHost[5], mPort[0], mSocketHost[4], mSocketPort[1])
        }

        test.setOnClickListener {
            ok(mHost[2], mPort[0], mSocketHost[2], mSocketPort[0])
        }

        dev.setOnClickListener {
            ok(mHost[1], mPort[0], mSocketHost[5], mSocketPort[0])
        }
        r4.setOnClickListener {
            ok(mHost[7], mPort[0], mSocketHost[7], mSocketPort[1])
        }
    }

    fun ok(host: String) {
        val prop = Properties()

        prop["host"] = host

        saveChangeData(prop)
    }

    fun ok(host: String, port: String, socketHost: String, socketPort: String) {
        val prop = Properties()

        prop["host"] = host
        prop["port"] = port
        prop["socket_host"] = socketHost
        prop["socket_port"] = socketPort

        saveChangeData(prop)
    }

    private fun saveChangeData(prop: Properties) {

        if (UrlUtil.checkSaveLocationExists()) {

            val file = File(UrlUtil.getBaseDir())
            if (!file.exists()) {
                file.mkdirs()
            }
            try {
                val fos = FileOutputStream(UrlUtil.getBaseDir() + UrlUtil.pathName, false)
                prop.store(fos, null)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            //退出账号
            AppUtils.relaunchApp()
        } else {
            ToastUtils.showShort("保存失败，无SD卡")
        }
    }
}