package com.prance.teacher.features.main.view

import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.spark.SparkService
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.R
import com.prance.teacher.features.main.MainActivity
import kotlinx.android.synthetic.main.fragment_main.*

class ExitActivity : BaseActivity() {

    override fun fragment(): BaseFragment? = null

    override fun layoutId(): Int = R.layout.activity_exit

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        exit.setOnClickListener {
            ActivityUtils.finishActivity(MainActivity::class.java)
            ActivityUtils.finishAllActivities()
            finish()
            stopService(SparkService.callingIntent(this))
        }
    }
}