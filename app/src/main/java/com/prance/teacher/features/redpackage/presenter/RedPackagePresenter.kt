package com.prance.teacher.features.redpackage.presenter

import cn.sunars.sdk.SunARS
import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import com.prance.teacher.features.redpackage.contract.IRedPackageContract
import com.prance.lib.base.mvp.BasePresenterKt
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.teacher.features.redpackage.model.RedPackageModel
import com.prance.teacher.features.redpackage.model.RedPackageRecord
import io.reactivex.disposables.Disposable
import com.prance.teacher.features.redpackage.model.RedPackageSetting
import com.prance.teacher.features.redpackage.view.red.RedPackageManager
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.Serializable
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
    private var totalTime: Long = 3000
    /**
     * 每次生成红包的间隔时间
     */
    private var intervalTime: Long = 500

    private var disposable: Disposable? = null

    private lateinit var mRedPackageManager: RedPackageManager

    private var mSetting: RedPackageSetting? = null

    override val mModel: IRedPackageContract.Model = RedPackageModel()

    override fun getStudentList(classId: String) {
        mModel.getStudentList("1").subscribe()
    }

    override fun startRedPackage(mSetting: RedPackageSetting?) {
        //应用服务端设置
        mSetting?.let {
            totalTime = it.lastTime!!.toLong() * 1000
            RedPackageManager.DEFAULT_SCORE = it.integrat!!
        }

        totalTime -= RedPackageManager.translationDurationTime

        this.mSetting = mSetting

        //初始化红包管理类
        mRedPackageManager = RedPackageManager(mView?.getContext()!!)

        //基站发送命令，可重复的单选题
        SunARS.voteStart(SunARS.VoteType_Choice, "1,1,0,0,10,1")

        //开始倒计时，take总次数
        var time = totalTime / intervalTime
        disposable = Flowable.interval(intervalTime, TimeUnit.MILLISECONDS)
                .take(time)
                .mySubscribe {
                    //生成红包
                    val redPackage = mRedPackageManager.generateRedPack()
                    redPackage?.let {
                        mView?.onShowPackage(redPackage)
                    }

                    //最后一个红包
                    if (it == time - 1) {
                        //所有红包落下之后关闭界面
                        Flowable.timer(RedPackageManager.translationDurationTime, TimeUnit.MILLISECONDS)
                                .mySubscribe {
                                    stopRedPackage()
                                }
                    }
                }
    }

    override fun stopRedPackage() {
        stopInterval()
        //发送答题结果
        postRedPackageResult()
        //转到排行榜界面
        mView?.onTimeEnd(mRedPackageManager.studentScores)
    }

    override fun detachView() {
        super.detachView()
        mRedPackageManager.destroy()
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
        val list = mutableListOf<RedPackageRecord>()
        for (score in mRedPackageManager.studentScores) {
            list.add(RedPackageRecord(score))
        }
        mModel.postRedPackageResult(mSetting?.classId.toString(), Gson().toJson(list), mSetting?.interactId.toString())
                .mySubscribe {
                    LogUtils.d("发送抢红包结果成功")
                }
    }
}

