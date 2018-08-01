package com.prance.teacher.features.redpackage.view

import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.features.redpackage.contract.IRedPackageContract
import com.prance.teacher.R
import com.prance.teacher.features.redpackage.presenter.RedPackagePresenter
import android.util.DisplayMetrics
import com.prance.teacher.utils.DimenUtils


/**
 * Description :
 * @author  rich
 * @date 2018/7/26  下午2:34
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class RedPackageFragment : BaseFragment(), IRedPackageContract.View {
    var screenWith: Int = 0
    var screenHeight: Int = 0
    var roadMargin:Float = 30f

    lateinit var mRoad1: RelativeLayout
    lateinit var mRoad2: RelativeLayout
    lateinit var mRoad3: RelativeLayout
    lateinit var mRoad4: RelativeLayout
    lateinit var mRoad5: RelativeLayout
    lateinit var mRoad6: RelativeLayout

    override fun layoutId(): Int = R.layout.fragment_red_package
    override fun needSunVoteService(): Boolean = true
    override var mPresenter: IRedPackageContract.Presenter = RedPackagePresenter()

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        mRoad1 = rootView.findViewById(R.id.road1)
        mRoad2 = rootView.findViewById(R.id.road2)
        mRoad3 = rootView.findViewById(R.id.road3)
        mRoad4 = rootView.findViewById(R.id.road4)
        mRoad5 = rootView.findViewById(R.id.road5)
        mRoad6 = rootView.findViewById(R.id.road6)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dm = resources.displayMetrics
        screenHeight = dm.heightPixels
        screenWith = dm.widthPixels
        var roadWidth = (screenWith - DimenUtils.dip2px(context!!,roadMargin*2)) / 6
        RedPackageView.redPackageMargin = (roadWidth - DimenUtils.dip2px(context!!,RedPackageView.redPackageWidth.toFloat())) /2
        mPresenter.startRedPackage()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mPresenter.stopRedPackage()
    }

    override fun onShowPackage(redPackageView: RedPackageView) {
        when (redPackageView.roadPosition) {
            0 -> {
                mRoad1.addView(redPackageView)
            }
            1 -> {
                mRoad2.addView(redPackageView)
            }
            2 -> {
                mRoad3.addView(redPackageView)
            }
            3 -> {
                mRoad4.addView(redPackageView)
            }
            4 -> {
                mRoad5.addView(redPackageView)
            }
            5 -> {
                mRoad6.addView(redPackageView)
            }
        }
        redPackageView.startFall()
    }

    override fun onKeyEventCallBack(KeyID: String, iMode: Int, Time: Float, sInfo: String?) {
        mPresenter.grabRedPackage(KeyID, sInfo)
    }

    override fun onTimeEnd(resultMaps: HashMap<String,Int>) {
        Log.e("rich",resultMaps.toString())
        var rank = arrayOf(0,0,0)
        for ((key,value)in resultMaps){
            for (item in rank ){
                if (value > item){

                }
            }
        }
    }
}

