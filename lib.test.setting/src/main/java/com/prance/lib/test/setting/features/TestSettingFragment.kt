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
import kotlinx.android.synthetic.main.activity_test_setting.*
import java.io.File
import java.io.FileOutputStream
import java.util.*

class TestSettingFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.activity_test_setting

    private val mScheme by lazy(mode = LazyThreadSafetyMode.NONE) { resources.getStringArray(R.array.scheme) }
    private val mHost by lazy(mode = LazyThreadSafetyMode.NONE) { resources.getStringArray(R.array.host) }
    private val mPort by lazy(mode = LazyThreadSafetyMode.NONE) { resources.getStringArray(R.array.port) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {

        current.text = UrlUtil.getHost()

        systemSettingBtn.setOnClickListener {
            startActivity(Intent(Settings.ACTION_SETTINGS))
        }

        release.setOnClickListener {
            ok(mHost[0])
        }

        test.setOnClickListener {
            ok(mHost[2])
        }

        dev.setOnClickListener {
            ok(mHost[1])
        }
    }

    private fun initTextView(data: Array<String>, view: AutoCompleteTextView) {
        val ipAdapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, data)
        view.setAdapter(ipAdapter)
        view.threshold = 1
        view.setOnClickListener({ view -> (view as AutoCompleteTextView).showDropDown() })
    }

    fun ok(host: String) {
        val prop = Properties()

        prop["host"] = host

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