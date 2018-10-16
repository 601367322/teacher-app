package com.prance.teacher.features.check.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbEndpoint
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.prance.lib.base.extension.invisible
import com.prance.lib.base.extension.visible
import com.prance.lib.common.utils.UrlUtil
import com.prance.lib.spark.SparkListenerAdapter
import com.prance.lib.spark.SparkService
import com.prance.lib.spark.SparkServicePresenter
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.BuildConfig
import com.prance.teacher.R
import com.prance.teacher.features.check.contract.ICheckKeyPadContract
import com.prance.teacher.features.check.presenter.CheckKeyPadPresenter
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.login.LoginActivity
import com.prance.teacher.features.students.StudentsActivity
import com.prance.teacher.utils.ping.PingNet
import com.prance.teacher.utils.ping.PingNetEntity
import kotlinx.android.synthetic.main.fragment_check_keypad.*

/**
 * 答题器检测
 */
class CheckKeyPadFragment : BaseFragment(), ICheckKeyPadContract.View {

    override var mPresenter: ICheckKeyPadContract.Presenter = CheckKeyPadPresenter()

    override fun layoutId(): Int = R.layout.fragment_check_keypad

    private val mSparkServicePresenter: SparkServicePresenter by lazy {
        SparkServicePresenter(context!!, object : SparkListenerAdapter() {
            override fun onConnected(usbDeviceConnection: UsbDeviceConnection, `in`: UsbEndpoint, out: UsbEndpoint, serialNum: String) {
                super.onConnected(usbDeviceConnection, `in`, out, serialNum)


            }
        })
    }

    lateinit var pingNetEntity: PingNetEntity

    override fun initView(rootView: View, savedInstanceState: Bundle?) {

        versionName.text = "版本号：v" + AppUtils.getAppVersionName()

        mSparkServicePresenter.bind()

        nextStep.setOnClickListener {
            context?.run {
                startActivity(LoginActivity.callingIntent(this))
            }
            activity?.finish()
        }

        reCheck.setOnClickListener {
            if (BuildConfig.DEBUG) {
                context?.run {
                    startActivity(LoginActivity.callingIntent(this))
                }
//                context?.run {
//                    startActivity(StudentsActivity.callingIntent(this,ClassesEntity(1)))
//                }
                activity?.finish()
                return@setOnClickListener
            }
            check()
        }

        reCheck.performClick()

    }

    private fun check() {
        nextStep.invisible()
        reCheck.invisible()

        stationProgress.startAnim(object : AnimatorListenerAdapter() {
        })

        networkProgress.startAnim(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)

                if (!pingNetEntity.isResult) {
                    networkProgress.error()
                } else {
                    networkProgress.ok()
                }

                if (SparkService.mUsbSerialNum == null) {
                    stationProgress.error()
                } else {
                    stationProgress.ok()
                }

                if (pingNetEntity.isResult && !TextUtils.isEmpty(SparkService.mUsbSerialNum)) {
                    nextStep.visible()
                    nextStep.requestFocus()
                } else {
                    reCheck.visible()
                    reCheck.requestFocus()
                }
            }
        })

        Thread {
            pingNetEntity = PingNetEntity(UrlUtil.getHost(), 3, 5, StringBuffer())
            pingNetEntity = PingNet.ping(pingNetEntity)
            LogUtils.d(pingNetEntity.pingTime)
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        mSparkServicePresenter.unBind()
    }

}