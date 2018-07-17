package com.prance.teacher.features.login.view

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.prance.lib.database.UserEntity
import com.prance.lib.qrcode.QrCodeUtils
import com.prance.lib.base.http.ResultException
import com.prance.teacher.features.login.contract.ILoginContract
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.lib.third.inter.PluginsManager
import com.prance.teacher.BuildConfig
import com.prance.teacher.R
import com.prance.teacher.features.login.QrCode
import com.prance.teacher.features.login.presenter.LoginPresenter
import com.prance.teacher.features.main.MainActivity
import com.prance.teacher.features.match.MatchKeyPadActivity
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.concurrent.TimeUnit

/**
 * Description :
 * @author  Sen
 * @date 2018/7/14  下午2:21
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class LoginFragment : BaseFragment(), ILoginContract.View {


    override var mPresenter: ILoginContract.Presenter = LoginPresenter()

    override fun layoutId(): Int = R.layout.fragment_login

    private val CHECK_INTERVAL: Long = 2000

    var mGetNewQrCodeDisposable: Disposable? = null
    var mStartCheckQrCodeDisposable: Disposable? = null

    var mQrCode: QrCode? = null

    var mCheckTimeTemp: Long = 0

    override fun initView(rootView: View, savedInstanceState: Bundle?) {

        LogUtils.d(ScreenUtils.getScreenWidth())
        LogUtils.d(ScreenUtils.getScreenHeight())
        LogUtils.d(ScreenUtils.getScreenDensity())
        LogUtils.d(ScreenUtils.getScreenDensityDpi())

        PluginsManager.bugly?.checkUpdate()

        //显示loading
        showProgress()

        //获取二维码
        getNewQrCode(0)

        //启动主页
        if(BuildConfig.DEBUG) {
            context?.let { startActivity(MatchKeyPadActivity.callingIntent(it)) }

            activity?.finish()
        }
    }

    /**
     * 渲染二维码
     */
    override fun renderQrCode(obj: QrCode) {
        mQrCode = obj

        //过期后重置二维码
        getNewQrCode(obj.getExpireTime() - 2)

        //隐藏loading
        hideProgress()

        //显示二维码
        qrCode.setImageBitmap(QrCodeUtils.createQRImage(obj.toJson(), SizeUtils.dp2px(300f), SizeUtils.dp2px(300f)))

        //开启定时检查二维码有效性
        startCheckQrCode(CHECK_INTERVAL)
    }

    /**
     * 获取二维码
     */
    private fun getNewQrCode(delay: Long) {
        mGetNewQrCodeDisposable = Flowable.timer(delay, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe {
                    stopStartCheckQrCode()
                    mPresenter.loadQrCodeDetail()
                }
    }

    /**
     * 停止获取二维码的计时
     */
    private fun stopGetNewQrCode() {
        mGetNewQrCodeDisposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }

    /**
     * 检查二维码
     */
    private fun startCheckQrCode(delay: Long) {
        mStartCheckQrCodeDisposable = Flowable.timer(delay, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe {
                    mCheckTimeTemp = System.currentTimeMillis()
                    mPresenter.checkQrCode(mQrCode)
                }
    }

    /**
     * 停止检查二维码的计时
     */
    private fun stopStartCheckQrCode() {
        mStartCheckQrCodeDisposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }

    /**
     * 检查二维码结果返回
     */
    override fun checkQrCodeSuccessCallBack(it: UserEntity?) {
        it?.let {
            //设置全局用户信息
            application.mUserInfo = it

            //启动主页
            context?.let { startActivity(MainActivity.callingIntent(it)) }

            activity?.finish()
        }
    }

    /**
     * 检查二维码没有登录成功
     */
    override fun checkQrCodeFailCallBack(it: Throwable) {

        if (it is ResultException) {
            when (it.status) {
                "40015", "40017", "40004", "40005" -> {
                    //重新获取二维码
                    getNewQrCode(0)
                    return
                }
            }
        }

        //没有被扫过
        //计算请求时间差
        var delay = System.currentTimeMillis() - mCheckTimeTemp
        delay = CHECK_INTERVAL - delay
        delay = if (delay < 0) 0 else delay

        //开启定时检查二维码有效性
        startCheckQrCode(delay)
    }

    override fun onDestroy() {
        super.onDestroy()

        stopGetNewQrCode()
        stopStartCheckQrCode()
    }

    override fun onNetworkError(throwable: Throwable): Boolean {
        hideProgress()
        return false
    }
}

