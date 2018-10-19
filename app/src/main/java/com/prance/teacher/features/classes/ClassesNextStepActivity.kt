package com.prance.teacher.features.classes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.prance.lib.base.extension.inTransaction
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.common.utils.Constants.CLASSES
import com.prance.lib.server.vo.teacher.ClassVo
import com.prance.lib.spark.SparkService
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.R
import com.prance.teacher.features.classes.contract.IClassesNextStepContract
import com.prance.teacher.features.classes.presenter.ClassesNextStepPresenter
import com.prance.teacher.features.classes.view.ClassesNextStepExistUnBindStudents
import com.prance.teacher.features.classes.view.ClassesNextStepKeyPadInadequate
import com.prance.teacher.features.classes.view.ClassesNextStepOK
import com.prance.teacher.features.main.MainActivity
import com.prance.teacher.features.match.MatchKeyPadActivity
import com.prance.teacher.features.students.StudentsActivity
import com.prance.teacher.weight.CountTimeButton
import org.greenrobot.eventbus.EventBus

class ClassesNextStepActivity : BaseActivity(), IClassesNextStepContract.View, CountTimeButton.CountTimButtonListener {

    override fun getContext(): Context? = this

    override var mPresenter: IClassesNextStepContract.Presenter = ClassesNextStepPresenter()

    companion object {

        fun callingIntent(context: Context, classVo: ClassVo): Intent {
            val intent = Intent(context, ClassesNextStepActivity::class.java)
            intent.putExtra(CLASSES, classVo)
            return intent
        }
    }

    override fun fragment(): BaseFragment? = null

    var autoNextStep: (() -> Unit)? = null

    lateinit var mClassesEntity: ClassVo

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        mClassesEntity = intent.getSerializableExtra(CLASSES) as ClassVo

        SparkService.mUsbSerialNum?.run {
            var keyPadCount = mPresenter.getKeyPadCount(this)

            mClassesEntity.run {
                when {
                    studentCount in (bindingCount + 1)..keyPadCount -> {
                        //如未做过学生与答题器的绑定，且答题器数量>=学生数量，则进入5.答题器检测页面，检测完进入6 绑定页面
                        //如果不是所有人都绑定了答题器或班级是第一次绑定答题器，则进入绑定页面继续绑定学生或开始绑定学生
                        supportFragmentManager.inTransaction {
                            replace(R.id.fragmentContainer, ClassesNextStepExistUnBindStudents())
                        }

                        autoNextStep = {
                            startActivity(StudentsActivity.callingIntent(this@ClassesNextStepActivity, this))
                        }
                    }
                    keyPadCount < studentCount -> {
                        //若答题器数量<学生数量则提示跳转至4.1继续配对答题器页面继续配对答题器
                        supportFragmentManager.inTransaction {
                            replace(R.id.fragmentContainer, ClassesNextStepKeyPadInadequate())
                        }

                        autoNextStep = {
                            startActivity(MatchKeyPadActivity.callingIntent(this@ClassesNextStepActivity, this))
                        }
                    }
                    else -> {
                        //果已配对过答题器且所有人都已绑定好答题器，则直接开始上课
                        supportFragmentManager.inTransaction {
                            replace(R.id.fragmentContainer, ClassesNextStepOK())
                        }

                        autoNextStep = {
                            EventBus.getDefault().post(MainActivity.EventMainStartClass())
                        }
                    }
                }
            }
        }

    }

    override fun onTimeEnd() {
        autoNextStep?.invoke()
        finish()
    }
}