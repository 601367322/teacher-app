package com.prance.teacher.features.redpackage.presenter

import cn.sunars.sdk.SunARS
import com.prance.teacher.features.redpackage.contract.IRedPackageContract
import com.prance.lib.base.mvp.BasePresenterKt
import com.prance.teacher.features.redpackage.model.RedPackageModel
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import com.prance.teacher.features.redpackage.RedPackageManager
import com.prance.teacher.features.redpackage.model.RedPackageStatus


/**
 * Description :
 * @author  rich
 * @date 2018/7/26  下午2:34
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class RedPackagePresenter : BasePresenterKt<IRedPackageContract.View>(), IRedPackageContract.Presenter {

    var packageTotal: Long = 30
    var disposable: Disposable? = null
    lateinit var redPackageManager: RedPackageManager

    override val mModel: IRedPackageContract.Model = RedPackageModel()

    override fun startRedPackage() {
        redPackageManager = RedPackageManager(getContext())
        SunARS.voteStart(SunARS.VoteType_Choice,"1,1,0,0,10,1")
        disposable = Flowable.interval(500, TimeUnit.MILLISECONDS)
                .take(packageTotal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val redPackage = redPackageManager.obtainPackage()
                    if (redPackage != null){
                        redPackage.mStatus = RedPackageStatus.CANNOTGRAB
                        mView!!.onShowPackage(redPackage)
                    }
                }
    }

    override fun stopRedPackage() {
        SunARS.voteStop()
        disposable?.dispose()
    }

    override fun grabRedPackage(KeyID: String, sInfo: String?) {
        redPackageManager.grabRedPackage(KeyID,sInfo)
    }
}

