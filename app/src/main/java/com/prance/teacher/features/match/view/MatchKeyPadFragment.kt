package com.prance.teacher.features.match.view

import android.os.Bundle
import android.view.View
import com.prance.lib.base.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.match.contract.IMatchKeyPadContract
import com.prance.teacher.features.match.presenter.MatchKeyPadPresenter

class MatchKeyPadFragment : BaseFragment(), IMatchKeyPadContract.View {

    override var mPresenter: IMatchKeyPadContract.Presenter = MatchKeyPadPresenter()

    override fun layoutId(): Int = R.layout.fragment_match_keypad

    override fun initView(rootView: View, savedInstanceState: Bundle?) {


    }
}