package com.prance.teacher.features.exit

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.AppUtils
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import kotlinx.android.synthetic.main.fragment_exit.*

class ExitFragment : BaseFragment() {

    override fun layoutId(): Int = R.layout.fragment_exit

    override fun initView(rootView: View, savedInstanceState: Bundle?) {

        back.setOnClickListener {
            activity?.finish()
        }

        exit.setOnClickListener {
            AlertDialog.Builder(context)
                    .setMessage("确定退出答题器程序？")
                    .setNegativeButton("确定", { _, _ ->
                        AppUtils.exitApp()
                    })
                    .setPositiveButton("取消", null)
                    .create()
                    .show()
        }
    }
}