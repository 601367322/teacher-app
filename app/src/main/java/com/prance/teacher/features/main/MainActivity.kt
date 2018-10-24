package com.prance.teacher.features.main

import android.content.BroadcastReceiver
import android.content.Context
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.teacher.base.core.platform.BaseActivity
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.Utils
import com.prance.lib.base.extension.inTransaction
import com.prance.lib.common.utils.weight.AlertDialog
import com.prance.lib.socket.PushService
import com.prance.teacher.R
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.classes.view.ClassesFragment
import com.prance.teacher.features.login.LoginActivity
import com.prance.teacher.features.students.view.StudentsFragment
import com.prance.teacher.utils.SoundUtils
import com.prance.teacher.weight.FloatIcon
import com.prance.teacher.weight.FontCustom
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.io.Serializable

class MainActivity : BaseActivity() {

    override fun fragment(): BaseFragment = ClassesFragment()

    private var mBroadcast: BroadcastReceiver? = null

    companion object {

        fun callingIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    private var mClassesDetailFragment: ClassesDetailFragment? = null

    override fun layoutId(): Int = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        //初始化音乐
        SoundUtils.load()

        //初始化字体
        FontCustom.getCOMICSANSMSGRASFont(Utils.getApp())
        FontCustom.getFZY1JWFont(Utils.getApp())

        val filter = IntentFilter()
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        mBroadcast = NetworkReceiver()
        registerReceiver(mBroadcast, filter)

        EventBus.getDefault().register(this)
    }

    override fun onBackKeyEvent(): Boolean {
        onBackBtnClick(null)
        return true
    }

    override fun onBackBtnClick(view: View?) {
        AlertDialog(this)
                .setMessage("确认退出此账号？")
                .setCancelButton("取消", null)
                .setConfirmButton("退出") { _ ->
                    finish()

                    startActivity(LoginActivity.callingIntent(this))
                }
                .show()
    }

    /**
     * 初始化班级详情
     */
    @Subscribe
    fun onEvent(classes: EventMainClassesEntity) {
        mClassesDetailFragment = ClassesDetailFragment.forClasses(classes.classesEntity)
        supportFragmentManager.inTransaction {
            replace(R.id.classesDetailContainer, mClassesDetailFragment)
        }
    }

    /**
     * 开始上课
     */
    @Subscribe
    fun onEvent(classes: EventMainStartClass) {
        mClassesDetailFragment?.startClass()
    }

    class EventMainClassesEntity(var classesEntity: ClassesEntity) : Serializable

    class EventMainStartClass : Serializable

    override fun onDestroy() {
        super.onDestroy()

        mBroadcast?.run {
            unregisterReceiver(this)
        }

        FloatIcon.hidePopupWindow()

        EventBus.getDefault().unregister(this)
    }

    class NetworkReceiver() : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            action?.run {
                if (this == ConnectivityManager.CONNECTIVITY_ACTION) {
                    val isConnect = NetworkUtils.isConnected()
                    LogUtils.d("网络连接\t$isConnect")
                    if (!isConnect) {

                    }
                }
            }
        }

    }

}