package com.prance.teacher.features.check.view

import android.os.Bundle
import android.view.View
import cn.sunars.sdk.SunARS
import com.blankj.utilcode.util.ToastUtils
import com.prance.lib.database.KeyPadEntity
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.check.contract.ICheckKeyPadContract
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

        if (mAction == ClassesFragment.ACTION_TO_CLASS) {
            jump.visibility = View.VISIBLE
            jump.setOnClickListener {
                context?.run { ClassesActivity.callingIntent(this, ClassesFragment.ACTION_TO_CLASS) }
            }
        }

        check.setOnClickListener {
            if (application.mBaseStation.sn == null) {
                ToastUtils.showShort("请先链接基站")
                return@setOnClickListener
            }
            mPresenter.getMatchedKeyPadByBaseStationId(application.mBaseStation.sn)
        }
    }

    override fun startCheck(it: MutableList<KeyPadEntity>) {
        showProgress()
        SunARS.checkKeyPad()

        Flowable.timer(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe({
                    hideProgress()
                    SunARS.voteStop()

                    mPresenter.generateGroup(mMatchKeyPadEntities!!, mCheckKeyPadEntities)

                    descriptionGroup.visibility = View.GONE
                    tip.visibility = View.VISIBLE
                    recycler.visibility = View.VISIBLE
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
}