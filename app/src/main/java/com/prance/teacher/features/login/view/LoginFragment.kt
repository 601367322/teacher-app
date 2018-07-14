package com.prance.teacher.features.login.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.prance.lib.database.UserEntity
import com.prance.lib.qrcode.QrCodeUtils
import com.prance.lib.teacher.base.http.ResultException
import com.prance.teacher.features.login.contract.ILoginContract
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.login.QrCode
import com.prance.teacher.features.login.presenter.LoginPresenter
import com.prance.teacher.features.main.MainActivity
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * Description :
 * @author  Sen
 * @date 2018/7/14  下午2:21
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class LoginFragment : BaseFragment(), ILoginContract.View {


    override var mPresenter: ILoginContract.Presenter = LoginPresenter()

    override fun layoutId(): Int = R.layout.fragment_login

    private val CHECK_INTERVAL = 2
    private val CHECK_MSG_WHAT = 10
    private val RESET_QRCODE_WHAT = 11

    var mQrCode: QrCode? = null

    var mCheckTimeTemp: Long = 0.toLong()

    override fun initView(rootView: View, savedInstanceState: Bundle?) {

        //返回按钮
        back.setOnClickListener {
            activity?.finish()
        }

        //显示loading
        showProgress()

        //获取二维码
        getNewQrCode(0)
    }

    override fun renderQrCode(obj: QrCode) {
        mQrCode = obj

        //过期后重置二维码
        getNewQrCode(obj.expireTime.toLong() - 2)

        //隐藏loading
        hideProgress()

        //显示二维码
        qrCode.setImageBitmap(QrCodeUtils.createQRImage(obj.toJson(), SizeUtils.dp2px(300f), SizeUtils.dp2px(300f)))

        //开启定时检查二维码有效性
        startCheckQrCode(CHECK_INTERVAL.toLong())
    }

    /**
     * 获取二维码
     */
    private fun getNewQrCode(delay: Long) {
        mHandler.run {
            sendMessageDelayed(obtainMessage(RESET_QRCODE_WHAT), delay * 1000)
        }
    }

    /**
     * 检查二维码
     */
    private fun startCheckQrCode(delay: Long) {
        mHandler.run {
            sendMessageDelayed(obtainMessage(CHECK_MSG_WHAT), delay * 1000)
        }
    }

    private var mHandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {

        override fun handleMessage(msg: Message?) {
            when (msg?.what) {
                CHECK_MSG_WHAT -> {
                    mCheckTimeTemp = System.currentTimeMillis()
                    mPresenter.checkQrCode(mQrCode)
                }
                RESET_QRCODE_WHAT -> {
                    removeMessages(CHECK_MSG_WHAT)
                    mPresenter.loadQrCodeDetail()
                }
            }
        }
    }

    /**
     * 检查二维码结果返回
     */
    override fun checkQrCodeSuccessCallBack(it: UserEntity?) {
        it?.let {
            //设置全局用户信息
            application.userInfo = it

            //启动主页
            context?.let { startActivity(MainActivity.callingIntent(it)) }

            activity?.finish()
        }
    }

    override fun checkQrCodeFailCallBack(it: Throwable) {

        if (it is ResultException) {
            when (it.status) {
                "40015", "40017", "40004", "40005" -> {
                    //重新获取二维码
                    mHandler.removeMessages(RESET_QRCODE_WHAT)
                    startCheckQrCode(0)
                    return
                }
            }
        }

        //没有被扫过
        //计算请求时间差
        var delay = System.currentTimeMillis() - mCheckTimeTemp
        delay = CHECK_INTERVAL * 1000 - delay
        delay = if (delay < 0) 0 else delay

        //开启定时检查二维码有效性
        startCheckQrCode(delay / 1000)
    }

    override fun onDestroy() {
        super.onDestroy()

        mHandler.removeMessages(CHECK_MSG_WHAT)
        mHandler.removeMessages(RESET_QRCODE_WHAT)
    }
}

