package com.prance.teacher.features.redpackage.presenter

import android.os.Handler
import android.os.Looper
import android.util.Log
import cn.sunars.sdk.SunARS
import com.google.gson.Gson
import com.prance.teacher.features.redpackage.contract.IRedPackageContract
import com.prance.lib.base.mvp.BasePresenterKt
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.teacher.features.redpackage.model.RedPackageModel
import io.reactivex.disposables.Disposable
import com.prance.teacher.features.redpackage.model.RedPackageSetting
import com.prance.teacher.features.redpackage.model.RedPackageStatus
import com.prance.teacher.features.redpackage.view.red.RedPackageManager
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


/**
 * Description :
 * @author  rich
 * @date 2018/7/26  下午2:34
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class RedPackagePresenter : BasePresenterKt<IRedPackageContract.View>(), IRedPackageContract.Presenter {

    /**
     * 总时间
     */
    var totalTime: Long = 3000
    var score: Int = 2
    /**
     * 每次生成红包的间隔时间
     */
    var intervalTime: Long = 500
    var disposable: Disposable? = null
    lateinit var mRedPackageManager: RedPackageManager
    var mSetting: RedPackageSetting? = null
    override val mModel: IRedPackageContract.Model = RedPackageModel()

    override fun getStudentList(classId: String) {
        mModel.getStudentList("1").subscribe()
    }

    override fun startRedPackage(mSetting: RedPackageSetting?) {
        mSetting?.let {
            totalTime = it.lastTime!!.toLong() * 1000
            score = it.integrat!!
        }
        this.mSetting = mSetting
        mRedPackageManager = RedPackageManager()
        SunARS.voteStart(SunARS.VoteType_Choice, "1,1,0,0,10,1")
        var time = totalTime / intervalTime
        disposable = Flowable.interval(intervalTime, TimeUnit.MILLISECONDS)
                .take(time)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (--time < 1) {
                        //停止答题
                        stopRedPackage()
                    }
                    val redPackage = mRedPackageManager.generateRedPack()
                    redPackage?.let {
                        mView?.onShowPackage(redPackage)
                    }
                }
    }

    override fun stopRedPackage() {
        stopInterval()
        //发送答题结果
        postRedPackageResult()
    }

    override fun detachView() {
        super.detachView()

        stopInterval()
    }

    private fun stopInterval() {
        //停止接收答题器
        SunARS.voteStop()
        //停止计时器
        disposable?.dispose()
    }


    override fun grabRedPackage(KeyID: String, sInfo: String?) {
        mRedPackageManager.grabRedPackage(KeyID, sInfo)
    }

    /**
     * 发送抢红包信息
     */
    private fun postRedPackageResult() {
        var json = Gson().toJson(mRedPackageManager.results)
        mModel.postRedPackageResult(mSetting!!.classId.toString(), json, mSetting!!.interactId.toString())
                .mySubscribe {
                    Log.e("rich", "success")
                }
    }
}

