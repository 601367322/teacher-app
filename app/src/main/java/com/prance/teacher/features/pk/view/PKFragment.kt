package com.prance.teacher.features.pk.view

import android.os.Bundle
import android.view.View
import com.prance.lib.sunvote.service.SunARSListenerAdapter
import com.prance.lib.sunvote.service.SunVoteServicePresenter
import com.prance.teacher.features.pk.contract.IPKContract
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.pk.presenter.PKPresenter

/**
 * Description :
 * @author  Sen
 * @date 2018/8/24  下午4:07
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class PKFragment : BaseFragment(), IPKContract.View {

    override var mPresenter: IPKContract.Presenter = PKPresenter()

    private val mSunVoteServicePresenter by lazy {
        SunVoteServicePresenter(context!!, object : SunARSListenerAdapter() {

        })
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {

    }

    override fun layoutId(): Int = R.layout.fragment_pk
}

