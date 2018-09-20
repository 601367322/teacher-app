package com.prance.teacher.features.main.view

import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.View
import com.prance.lib.common.utils.ToastUtils
import com.prance.teacher.R
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.BuildConfig
import com.prance.teacher.features.main.contract.IMainContract
import com.prance.teacher.features.main.presenter.MainPresenter
import kotlinx.android.synthetic.main.fragment_main.*
import com.prance.teacher.features.classes.ClassesActivity
import com.prance.teacher.features.classes.view.ClassesFragment
import com.prance.teacher.features.match.MatchKeyPadActivity
import com.blankj.utilcode.util.*
import com.google.gson.Gson
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.lib.spark.SparkListenerAdapter
import com.prance.lib.spark.SparkService
import com.prance.lib.spark.SparkServicePresenter
import com.prance.teacher.features.classes.ClassesDetailActivity
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.pk.presenter.PKPresenter
import com.prance.teacher.features.redpackage.RedPackageActivity
import com.prance.teacher.features.redpackage.model.RedPackageSetting
import com.prance.teacher.features.students.StudentsActivity
import com.prance.teacher.features.students.model.StudentsEntity
import com.prance.teacher.features.subject.SubjectActivity
import com.prance.teacher.features.subject.model.KeyPadResult
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * 首页
 */
class MainFragment : BaseFragment(), IMainContract.View {

    override var mPresenter: IMainContract.Presenter = MainPresenter()

    override fun layoutId(): Int = R.layout.fragment_main

    private val mSparkServicePresenter by lazy { SparkServicePresenter(context!!, object : SparkListenerAdapter() {}) }

    class  o {
        constructor(s: String) {
            this.s = s
        }

        var s = "";
    }

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

                var mResult = mutableListOf(
                        KeyPadResult("123123","1", Date().time),
                        KeyPadResult("123123","2", Date().time),
                        KeyPadResult("123123","2", Date().time),
                        KeyPadResult("123123","1", Date().time),
                        KeyPadResult("123123","1", Date().time)
                )
                var result = KeyPadResult("123123","1", Date().time);

                LogUtils.d(o(result.answer).s)

                LogUtils.d(result.answer)
                LogUtils.d(Gson().toJson(mResult))

//                context?.let { startActivity(ClassesDetailActivity.callingIntent(it, ClassesEntity(1))) }

//                context?.let { startActivity(PKActivity.callingIntent(it, ClassesDetailFragment.Question(1, 10, "1,0,0,0,4,1", 1, "A", 30))) }
//                context?.let { startActivity(StudentsActivity.callingIntent(it, ClassesEntity(1))) }

//            context?.let { startActivity(CheckKeyPadActivity.callingIntent(it)) }

                var question = ClassesDetailFragment.Question(1, 5, "1,0,0,0,4,1", 1, "A", mutableListOf(
                        StudentsEntity(1,"呵呵","呵呵")
                ))
                context?.let { startActivity(SubjectActivity.callingIntent(it, question)) }

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
//                context?.let { startActivity(ClassesDetailActivity.callingIntent(it, ClassesEntity(1))) }
//                context?.let { startActivity(PKActivity.callingIntent(it, ClassesDetailFragment.Question(1, 10, "1,0,0,0,4,1", 1, "A",30))) }
//                val redConfig = RedPackageSetting(1,10,1,1)
//                context?.let { startActivity(RedPackageActivity.callingIntent(it,redConfig)) }

//                context?.let {
//                    startActivity(Intent(it,DanmuTestActivity::class.java))
//                }
//                return@setOnClickListener
            }
            if (SparkService.mUsbSerialNum == null) {
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

        mSparkServicePresenter.bind()

        disposable = Flowable.interval(3, TimeUnit.SECONDS)
                .mySubscribe {
                    val M = 1024 * 1024
                    val r = Runtime.getRuntime()

//                    var sb = StringBuilder()
//                    sb.append("最大可用内存:" + r.maxMemory() / M + "M\n")
//                    sb.append("当前可用内存:" + r.totalMemory() / M + "M\n")
//                    sb.append("当前空闲内存:" + r.freeMemory() / M + "M\n")
//                    sb.append("当前已使用内存:" + (r.totalMemory() - r.freeMemory()) / M + "M\n")
//                    LogUtils.d(sb.toString())

                }
    }

    var disposable: Disposable? = null

    override fun onDestroy() {
        super.onDestroy()

        mSparkServicePresenter.unBind()

        disposable?.dispose()
    }


}