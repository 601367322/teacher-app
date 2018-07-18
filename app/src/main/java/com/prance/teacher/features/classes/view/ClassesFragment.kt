package com.prance.teacher.features.classes.view

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.lib.teacher.base.weight.FocusGridLayoutManager
import com.prance.teacher.BuildConfig
import com.prance.teacher.features.classes.contract.IClassesContract
import com.prance.teacher.R
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.classes.presenter.ClassesPresenter
import kotlinx.android.synthetic.main.fragment_classes.*

/**
 * Description :
 * @author  Sen
 * @date 2018/7/18  上午10:05
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class ClassesFragment : BaseFragment(), IClassesContract.View {

    override var mPresenter: IClassesContract.Presenter = ClassesPresenter()

    var mAdapter: ClassesAdapter = ClassesAdapter()

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        recycler.layoutManager = FocusGridLayoutManager(activity, 3)
        recycler.adapter = mAdapter

        refresh.setOnClickListener {
            loadData()
        }

        refresh.performClick()
    }

    private fun loadData() {
        showProgress()

        if (BuildConfig.DEBUG) {
            mPresenter.getAllClasses("6")
        } else {
            application.mUserInfo?.let {
                mPresenter.getAllClasses(it.id.toString())
            }
        }

    }

    override fun renderClasses(it: MutableList<ClassesEntity>) {
        hideProgress()
        mAdapter.data = it
        mAdapter.notifyDataSetChanged()
    }

    override fun layoutId(): Int = R.layout.fragment_classes

    override fun onNetworkError(throwable: Throwable): Boolean {
        hideProgress()
        return false
    }
}

