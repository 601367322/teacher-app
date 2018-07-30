package com.prance.teacher.features.redpackage.view

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.features.redpackage.contract.IRedPackageContract
import com.prance.teacher.R
import com.prance.teacher.features.redpackage.model.RedPackageWrapper
import com.prance.teacher.features.redpackage.presenter.RedPackagePresenter

/**
 * Description :
 * @author  rich
 * @date 2018/7/26  下午2:34
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class RedPackageFragment : BaseFragment(), IRedPackageContract.View {

    lateinit var mRoad1: LinearLayout
    lateinit var mRoad2: LinearLayout
    lateinit var mRoad3: LinearLayout
    lateinit var mRoad4: LinearLayout
    lateinit var mRoad5: LinearLayout
    lateinit var mRoad6: LinearLayout

    override fun layoutId(): Int = R.layout.fragment_red_package

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
        mPresenter.startRedPackage()
    }

    override var mPresenter: IRedPackageContract.Presenter = RedPackagePresenter()

    override fun onShowPackage(redPackageWrapper: RedPackageWrapper) {
        when (redPackageWrapper.model.roadPosition) {
            0 -> {
                mRoad1.addView(redPackageWrapper.view)
            }
            1 -> {
                mRoad2.addView(redPackageWrapper.view)
            }
            2 -> {
                mRoad3.addView(redPackageWrapper.view)
            }
            3 -> {
                mRoad4.addView(redPackageWrapper.view)
            }
            4 -> {
                mRoad5.addView(redPackageWrapper.view)
            }
            5 -> {
                mRoad6.addView(redPackageWrapper.view)
            }
        }
    }

    override fun onKeyEventCallBack(KeyID: String, iMode: Int, Time: Float, sInfo: String?) {
        super.onKeyEventCallBack(KeyID, iMode, Time, sInfo)
        mPresenter.grabRedPackage(KeyID, sInfo)
    }
}

