package com.prance.lib.test.setting.features

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.blankj.utilcode.util.ToastUtils
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
        initTextView(mScheme, scheme)
        initTextView(mHost, hosts)
        initTextView(mPort, port)

        scheme.setText(UrlUtil.getScheme())
        hosts.setText(UrlUtil.getHost())
        port.setText(UrlUtil.getPort())
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_test_setting, menu)
    }

    private fun initTextView(data: Array<String>, view: AutoCompleteTextView) {
        val ipAdapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, data)
        view.setAdapter(ipAdapter)
        view.threshold = 1
        view.setOnClickListener({ view -> (view as AutoCompleteTextView).showDropDown() })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.ok -> ok()
        }
        return super.onOptionsItemSelected(item)
    }

    fun ok() {
        val prop = Properties()

        prop["scheme"] = scheme.text.toString()
        prop["host"] = hosts.text.toString()
        prop["port"] = port.text.toString()
//        prop["log"] = mHost.getText().toString()

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

        } else {
            ToastUtils.showShort("保存失败，无SD卡")
        }
    }
}