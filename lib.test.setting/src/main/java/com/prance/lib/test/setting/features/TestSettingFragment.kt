package com.prance.lib.test.setting.features

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.blankj.utilcode.util.AppUtils
import com.prance.lib.common.utils.ToastUtils
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.common.utils.UrlUtil
import com.prance.lib.test.setting.R
import com.prance.lib.test.setting.R.array.port
import kotlinx.android.synthetic.main.activity_test_setting.*
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

        systemSettingBtn.setOnClickListener {
            startActivity(Intent(Settings.ACTION_SETTINGS))
        }

        release.setOnClickListener {
            ok(mHost[0], mPort[0], mHost[3], mSocketPort[1])
        }

        staging.setOnClickListener {
            ok(mHost[5], mPort[0], mHost[4], mSocketPort[1])
        }

        test.setOnClickListener {
            ok(mHost[2], mPort[0], mHost[2], mSocketPort[0])
        }

        dev.setOnClickListener {
            ok(mHost[1], mPort[0], mHost[1], mSocketPort[0])
//            ok(mHost[1], mPort[0], mSocketHost[2], mSocketPort[0])
        }

        frode.setOnClickListener {
            ok(mHost[3], mPort[2], mSocketHost[0], mSocketPort[0])
        }

        landy.setOnClickListener {
            ok(mHost[4], mPort[2], mSocketHost[1], mSocketPort[0])
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