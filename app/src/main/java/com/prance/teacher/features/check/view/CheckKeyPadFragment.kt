package com.prance.teacher.features.check.view

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import cn.sunars.sdk.SunARS
import com.blankj.utilcode.util.ToastUtils
import com.prance.lib.database.KeyPadEntity
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.lib.teacher.base.weight.FocusGridLayoutManager
import com.prance.teacher.R
import com.prance.teacher.features.check.contract.ICheckKeyPadContract
import com.prance.teacher.features.check.model.CheckKeyPadGroupTitle
import com.prance.teacher.features.check.presenter.CheckKeyPadPresenter
import com.prance.teacher.features.classes.ClassesActivity
import com.prance.teacher.features.classes.view.ClassesFragment
import com.prance.teacher.features.match.view.generateKeyPadId
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_check_keypad.*
import java.util.concurrent.TimeUnit

class CheckKeyPadFragment : BaseFragment(), ICheckKeyPadContract.View {

    override fun layoutId(): Int = R.layout.fragment_check_keypad

    private var mAction: Int = ACTION_JUST_CHECK
    override var mPresenter: ICheckKeyPadContract.Presenter = CheckKeyPadPresenter()

    private var mMatchKeyPadEntities: MutableList<KeyPadEntity>? = null
    private var mCheckKeyPadEntities: MutableList<KeyPadEntity> = mutableListOf()

    private var mAdapter = CheckKeyPadAdapter()

    companion object {

        const val ACTION_JUST_CHECK = 3

        fun forAction(action: Int?): CheckKeyPadFragment {
            val fragment = CheckKeyPadFragment()
            action?.let {
                val arguments = Bundle()
                arguments.putInt(ClassesFragment.ACTION, action)
                fragment.arguments = arguments
            }
            return fragment
        }
    }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        arguments?.run {
            mAction = getInt(ClassesFragment.ACTION, ACTION_JUST_CHECK)
        }

        recycler.adapter = mAdapter
        val layoutManager = FocusGridLayoutManager(context, 7)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (position < mAdapter.data.size) {
                    if (mAdapter.data[position] is CheckKeyPadGroupTitle) {
                        return 7
                    }
                }
                return 1
            }
        }
        recycler.layoutManager = layoutManager

        if (mAction == ClassesFragment.ACTION_TO_CLASS) {
            jump.visibility = View.VISIBLE
            jump.setOnClickListener {
                context?.run { startActivity(ClassesActivity.callingIntent(this, ClassesFragment.ACTION_TO_CLASS)) }
            }
            classTip.text = "上课提示"
        } else {
            classTip.text = "检测提示"
        }

        check.setOnClickListener {
            if (application.mBaseStation.sn == null) {
                ToastUtils.showShort("请先链接基站")
                return@setOnClickListener
            }
            showProgress()

            descriptionGroup.visibility = View.VISIBLE
            tip.visibility = View.GONE
            recycler.visibility = View.GONE
            checkCompleteGroup.visibility = View.GONE

            mPresenter.getMatchedKeyPadByBaseStationId(application.mBaseStation.sn)
        }
    }

    override fun renderKeyPads(it: MutableList<Any>) {
        hideProgress()

        if (it.isEmpty()) {
            checkCompleteGroup.visibility = View.VISIBLE

            if (mAction == ACTION_JUST_CHECK)
                back.requestFocus()
            else
                jump.requestFocus()
        } else {
            descriptionGroup.visibility = View.GONE
            tip.visibility = View.VISIBLE
            recycler.visibility = View.VISIBLE

            mAdapter.data = it
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun startCheck(it: MutableList<KeyPadEntity>) {

        mMatchKeyPadEntities = it

        SunARS.checkKeyPad()

        Flowable.timer(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe({
                    SunARS.voteStop()

                    mPresenter.generateGroup(mMatchKeyPadEntities!!, mCheckKeyPadEntities)
                })
    }

    override fun needSunVoteService(): Boolean = true

    override fun onKeyEventCallBack(KeyID: String, iMode: Int, Time: Float, sInfo: String?) {
        //4 2 0.0 0,0,2.92,34
        val keyId = generateKeyPadId(KeyID)
        when (iMode) {
        //键盘检测结果
            SunARS.KeyResult_status -> {
                sInfo?.let {
                    val status = it.split(",")
                    var battery = status[2].toFloat()
                    mCheckKeyPadEntities.add(KeyPadEntity(keyId, battery))
                }
            }
        }

    }

    override fun onNetworkError(throwable: Throwable): Boolean {
        hideProgress()
        return false
    }
}