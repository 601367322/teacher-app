package com.prance.teacher.features.afterclass.presenter

import cn.sunars.sdk.SunARS
import com.prance.teacher.features.afterclass.contract.IAfterClassContract
import com.prance.lib.base.mvp.BasePresenterKt
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.teacher.features.afterclass.model.AfterClassModel
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Description :
 * @author  rich
 * @date 2018/7/25  下午5:15
 * 								 - generate by MvpAutoCodePlus plugin.
 */
class AfterClassPresenter : BasePresenterKt<IAfterClassContract.View>(), IAfterClassContract.Presenter {
    var mTime: Int = 30
    var mDisposable: Disposable? = null
    override val mModel: IAfterClassContract.Model = AfterClassModel()
    override fun startReceive(feedback: ClassesDetailFragment.Question) {
        SunARS.voteStart(SunARS.VoteType_Choice, "1,0,0,0,4,1")
        mDisposable = Flowable.interval(1000, TimeUnit.MILLISECONDS)
                .take(30)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    --mTime
                    if (mTime < 10) {
                        mView?.onTimeChange("00:0$mTime")
                    } else {
                        mView?.onTimeChange("00:$mTime")
                    }
                    if (mTime < 1) {
                        mDisposable()
                        mView?.showLoading()
                        mModel.confirmChoose(feedback.classId!!, feedback.questionId!!).mySubscribe(onSubscribeError, {
                            mView?.confirmChooseSuccess()
                        })
                    }
                }
    }

    fun mDisposable() {
        mDisposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }

    override fun stopReceive() {
        SunARS.voteStop()
        mDisposable()
    }

    override fun saveChoose(deviceId: String, choose: String) {
        mModel.saveChoose(deviceId, choose)
    }
}

