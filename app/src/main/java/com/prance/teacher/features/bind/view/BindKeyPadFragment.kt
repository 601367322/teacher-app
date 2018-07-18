package com.prance.teacher.features.bind.view

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.prance.lib.base.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.bind.contract.IBindKeyPadContract
import com.prance.teacher.features.bind.presenter.BindKeyPadPresenter
import kotlinx.android.synthetic.main.fragment_match_keypad.*

class BindKeyPadFragment :BaseFragment(), IBindKeyPadContract.View{


    override var mPresenter: IBindKeyPadContract.Presenter = BindKeyPadPresenter()

    override fun layoutId(): Int = R.layout.fragment_bind_keypad

    override fun initView(rootView: View, savedInstanceState: Bundle?) {

    }
}