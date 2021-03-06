package com.prance.teacher.features.main

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.prance.lib.base.extension.inTransaction
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.common.utils.weight.AlertDialog
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.R
import com.prance.teacher.core.OnStartClassActivity
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.classes.view.ClassesFragment
import com.prance.teacher.features.login.LoginActivity
import com.prance.teacher.features.main.contract.IMainContract
import com.prance.teacher.features.main.presenter.MainActivityPresenter
import com.prance.teacher.utils.SoundUtils
import com.prance.teacher.weight.FontCustom
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.io.Serializable


class MainActivity : BaseActivity(),IMainContract.MainView {

    lateinit var mConnectivityManager: ConnectivityManager
    lateinit var mNetworkCallback: ConnectivityManager.NetworkCallback
    override var mPresenter: IMainContract.MainPresenter = MainActivityPresenter()
    override fun getContext(): Context? = this

    override fun fragment(): BaseFragment = ClassesFragment()

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

        try {
            mConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            mNetworkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onLost(network: Network) {
                    super.onLost(network)
                    LogUtils.i("onLost")
                    for (activity in ActivityUtils.getActivityList()) {
                        if (activity is OnStartClassActivity) {
                            activity.finish()
                        }
                    }
                }

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    ///网络可用的情况下的方法
                    LogUtils.i("onAvailable")
                }
            }
            mConnectivityManager.requestNetwork(NetworkRequest.Builder().build(), mNetworkCallback)
        } catch (e: Exception) {
            e.printStackTrace()
        }

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

                    mPresenter.logOut()

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

        try {
            mConnectivityManager.unregisterNetworkCallback(mNetworkCallback)
        } catch (e: Exception) {
            e.printStackTrace()
        }
//        FloatIcon.hidePopupWindow()

        EventBus.getDefault().unregister(this)
    }

}