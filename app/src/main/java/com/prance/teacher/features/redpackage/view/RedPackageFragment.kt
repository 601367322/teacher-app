package com.prance.teacher.features.redpackage.view

import android.os.*
import android.util.Log
import android.view.View
import cn.sunars.sdk.SunARS
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.features.redpackage.contract.IRedPackageContract
import com.prance.teacher.R
import com.prance.teacher.features.match.view.generateKeyPadId
import com.prance.teacher.features.redpackage.RedPackageActivity
import com.prance.teacher.features.redpackage.presenter.RedPackagePresenter
import com.prance.teacher.features.redpackage.model.RedPackageSetting
import com.prance.teacher.features.redpackage.view.red.RedPackage
import kotlinx.android.synthetic.main.fragment_red_package.*
import java.util.*


/**
 * Description :
 * @author  rich
 * @date 2018/7/26  下午2:34
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class RedPackageFragment : BaseFragment(), IRedPackageContract.View {


    companion object {
        const val mSetTing: String = "settting"
    }

    /**
     * 抢红包的设置参数
     */
    var mSetting: RedPackageSetting? = null

    override fun layoutId(): Int = R.layout.fragment_red_package

    override fun needSunVoteService(): Boolean = true

    override var mPresenter: IRedPackageContract.Presenter = RedPackagePresenter()

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        mSetting = arguments?.getSerializable(mSetTing) as RedPackageSetting
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.startRedPackage(mSetting)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        SunARS.voteStop()
    }

    override fun onShowPackage(redPackage: RedPackage) {
        animGlView?.addItem(redPackage)
        redPackage.startFall()
    }

    override fun onKeyEventCallBack(KeyID: String, iMode: Int, Time: Float, sInfo: String?) {
        Handler(Looper.getMainLooper()).post {
            mPresenter.grabRedPackage(generateKeyPadId(KeyID), sInfo)
        }
    }

    override fun onTimeEnd(resultMaps: HashMap<String, Int>) {
        var rank = arrayOf(0, 0, 0, 0)
        for ((key, value) in resultMaps) {
            rank[0] = value

        }
        for (i in 0 until 2) {
            if (rank[i] > rank[i + 1]) {
                var temp = rank[i]
                rank[i] = rank[i + 1]
                rank[i + 1] = temp
            }
        }
        Log.e("rich", rank.toString())
        (activity as RedPackageActivity).redPackageRank(rank)
    }

    fun redPackageStop() {
        mPresenter.stopRedPackage()
    }

    override fun onDestroy() {
        super.onDestroy()

        mPresenter.detachView()
    }

}

