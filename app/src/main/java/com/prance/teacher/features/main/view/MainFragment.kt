package com.prance.teacher.features.main.view

import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.View
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ScreenUtils
import com.prance.lib.common.utils.ToastUtils
import com.prance.lib.sunvote.platform.UsbManagerImpl
import com.prance.lib.sunvote.service.SunARSListenerAdapter
import com.prance.lib.sunvote.service.SunVoteServicePresenter
import com.prance.teacher.R
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.BuildConfig
import com.prance.teacher.features.main.contract.IMainContract
import com.prance.teacher.features.main.presenter.MainPresenter
import kotlinx.android.synthetic.main.fragment_main.*
import com.prance.teacher.features.classes.ClassesActivity
import com.prance.teacher.features.classes.ClassesDetailActivity
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.classes.view.ClassesFragment
import com.prance.teacher.features.match.MatchKeyPadActivity
import com.prance.teacher.features.pk.PKActivity
import com.prance.teacher.features.redpackage.RedPackageActivity
import com.prance.teacher.features.redpackage.model.RedPackageSetting
import com.prance.teacher.features.students.StudentsActivity
import com.prance.teacher.features.subject.SubjectActivity

/**
 * 首页
 */
class MainFragment : BaseFragment(), IMainContract.View {

    override var mPresenter: IMainContract.Presenter = MainPresenter()

    override fun layoutId(): Int = R.layout.fragment_main

    private val mSunVoteServicePresenter: SunVoteServicePresenter by lazy { SunVoteServicePresenter(context!!, object : SunARSListenerAdapter() {}) }

    override fun initView(rootView: View, savedInstanceState: Bundle?) {

        /**
         * 整体居中
         */
        val marginStart = (ScreenUtils.getScreenWidth() - (resources.getDimensionPixelOffset(R.dimen.m520_0) +
                resources.getDimensionPixelOffset(R.dimen.m50_0) +
                resources.getDimensionPixelOffset(R.dimen.m960_0))) / 2
        (startLesson.layoutParams as ConstraintLayout.LayoutParams).marginStart = marginStart


        startLesson.setOnClickListener {

            if (BuildConfig.DEBUG) {

//                val classes = ClassesEntity(1)
//                context?.let { startActivity(ClassesDetailActivity.callingIntent(it, classes)) }

                context?.let { startActivity(PKActivity.callingIntent(it, ClassesDetailFragment.Question(1, 10, "1,0,0,0,4,1", 1, "A", 5))) }
//                context?.let { startActivity(StudentsActivity.callingIntent(it, ClassesEntity(1))) }

//            context?.let { startActivity(CheckKeyPadActivity.callingIntent(it)) }

//                var question = ClassesDetailFragment.Question(1, 10, "1,0,0,0,4,1", 1, "A",5)
//                context?.let { startActivity(SubjectActivity.callingIntent(it, question)) }

//                val redConfig = RedPackageSetting(1,60,1,1)
//                context?.let { startActivity(RedPackageActivity.callingIntent(it,redConfig)) }

//                context?.let {
//                    startActivity(Intent(it,DanmuTestActivity::class.java))
//                }
                return@setOnClickListener
            }
            context?.let {
                startActivity(ClassesActivity.callingIntent(it, ClassesFragment.ACTION_TO_CLASS))
            }
        }

        matchKeyPad.setOnClickListener {
            if (BuildConfig.DEBUG) {

//                context?.let { startActivity(PKActivity.callingIntent(it, ClassesDetailFragment.Question(1, 10, "1,0,0,0,4,1", 1, "A",30))) }
//                val redConfig = RedPackageSetting(1,10,1,1)
//                context?.let { startActivity(RedPackageActivity.callingIntent(it,redConfig)) }

//                context?.let {
//                    startActivity(Intent(it,DanmuTestActivity::class.java))
//                }
//                return@setOnClickListener
            }
            if (UsbManagerImpl.baseStation.sn == null) {
                ToastUtils.showShort("请先连接基站")
            } else {
                context?.let {
                    startActivity(MatchKeyPadActivity.callingIntent(it))
                }
            }
        }

        bindKeyPad.setOnClickListener {
            context?.let {
                startActivity(ClassesActivity.callingIntent(it, ClassesFragment.ACTION_TO_BIND))
            }
        }

        exit.setOnClickListener {
            startActivity(Intent(activity, ExitActivity::class.java))
        }

        versionName.text = "版本号：v" + AppUtils.getAppVersionName()

        mSunVoteServicePresenter.bind()
    }

    override fun onDestroy() {
        super.onDestroy()

        mSunVoteServicePresenter.unBind()
    }

}