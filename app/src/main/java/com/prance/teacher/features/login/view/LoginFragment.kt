package com.prance.teacher.features.login.view

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.SizeUtils
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.prance.lib.base.extension.inTransaction
import com.prance.lib.common.utils.GlideApp
import com.prance.lib.common.utils.GlideOptions
import com.prance.lib.database.UserEntity
import com.prance.lib.qrcode.QrCodeUtils
import com.prance.lib.common.utils.http.ResultException
import com.prance.lib.common.utils.ToastUtils
import com.prance.teacher.features.login.contract.ILoginContract
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.BuildConfig
import com.prance.teacher.R
import com.prance.teacher.features.login.model.QrCodeEntity
import com.prance.teacher.features.login.model.VersionEntity
import com.prance.teacher.features.login.presenter.LoginPresenter
import com.prance.teacher.features.main.MainActivity
import com.prance.teacher.storage.CommonShared
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
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

    var mQrCode: QrCodeEntity? = null

    var mCheckTimeTemp: Long = 0

    override fun initView(rootView: View, savedInstanceState: Bundle?) {

        //显示loading
        showProgress()

        //检查版本更新
        mPresenter.checkVersion()

        //新版本提示
        if (CommonShared.getPreVersion() != AppUtils.getAppVersionCode()) {
            ToastUtils.showShort("已更新为最新版本v" + AppUtils.getAppVersionName())
        }
        CommonShared.setPreVersion(AppUtils.getAppVersionCode())

        versionName.text = "版本号：v" + AppUtils.getAppVersionName()
        //启动主页
        if (BuildConfig.DEBUG) {
//            val classes = ClassesEntity(7)
//            context?.let { startActivity(ClassesActivity.callingIntent(it)) }
//            context?.let { startActivity(ClassesDetailActivity.callingIntent(it,classes)) }
            context?.let { startActivity(MainActivity.callingIntent(it)) }
//            context?.let { startActivity(CheckKeyPadActivity.callingIntent(it)) }
//            context?.let { startActivity(Intent(it,DanmuTestActivity::class.java)) }

//            var question = ClassesDetailFragment.Question(1,10,"1,0,0,0,4,1",1)
//            context?.let { startActivity(SubjectActivity.callingIntent(it,question)) }

//            val feedBack = FeedBack(1,1)
//            context?.let { startActivity(AfterClassActivity.callingIntent(it,feedBack)) }

//            val redConfig = RedPackageSetting(1,30,1,1)
//            context?.let { startActivity(RedPackageActivity.callingIntent(it,redConfig)) }
            activity?.finish()
        }
//        activity?.finish()
//        context?.let { startActivity(MainActivity.callingIntent(it)) }
    }

    /**
     * 渲染二维码
     */
    override fun renderQrCode(code: QrCodeEntity) {
        mQrCode = code

        //过期后重置二维码
        getNewQrCode(code.getExpireTime() - 2)

        //隐藏loading
        hideProgress()

        GlideApp.with(this)
                .load(QrCodeUtils.createQRImage(code.toJson(), SizeUtils.dp2px(300f), SizeUtils.dp2px(300f)))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .apply(GlideOptions.bitmapTransform(RoundedCornersTransformation(resources.getDimensionPixelOffset(R.dimen.m16_0), 0)))
                .into(qrCode)

        //显示二维码
//        qrCode.setImageBitmap(QrCodeUtils.createQRImage(code.toJson(), SizeUtils.dp2px(300f), SizeUtils.dp2px(300f)))

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

            activity?.finish()

            //启动主页
            context?.let { startActivity(MainActivity.callingIntent(it)) }
        }
    }

    /**
     * 检查二维码没有登录成功
     */
    override fun checkQrCodeFailCallBack(it: Throwable) {

        if (it is ResultException) {
            when (it.status) {
                40015, 40017, 40004, 40005 -> {
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

    /**
     * 检查更新之后
     */
    override fun checkVersionCallBack(versionEntity: VersionEntity?) {
        //显示更新提示
        versionEntity?.let {
            if (versionEntity.codeVersion > AppUtils.getAppVersionCode()) {
                activity?.supportFragmentManager?.inTransaction {
                    add(R.id.fragmentContainer, UpdateFragment.forVersion(it))
                }
            }
        }

        //获取二维码
        getNewQrCode(0)
    }
}

