package com.prance.teacher.features.replacekeypad.view

import android.app.Activity
import android.os.Bundle
import android.os.Message
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.prance.lib.common.utils.ToastUtils
import com.prance.lib.common.utils.weight.AlertDialog
import com.prance.lib.spark.SparkListenerAdapter
import com.prance.lib.spark.SparkService
import com.prance.lib.spark.SparkServicePresenter
import com.prance.teacher.features.replacekeypad.contract.IReplaceKeyPadContract
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.replacekeypad.presenter.ReplaceKeyPadPresenter
import com.prance.teacher.features.students.view.StudentsFragment
import com.prance.teacher.features.subject.model.KeyPadResult
import com.spark.teaching.answertool.usb.model.ReceiveAnswer
import com.spark.teaching.answertool.usb.model.ReportBindCard
import kotlinx.android.synthetic.main.fragment_replace.*

/**
 * Description :
 * @author  Sen
 * @date 2018/7/19  下午12:34
 * 								 - generate by MvpAutoCodePlus plugin.
 */

class ReplaceKeyPadFragment : BaseFragment(), IReplaceKeyPadContract.View {
    override var mPresenter: IReplaceKeyPadContract.Presenter = ReplaceKeyPadPresenter()

    companion object {

        fun forClasses(classes: ClassesEntity): ReplaceKeyPadFragment {
            val fragment = ReplaceKeyPadFragment()
            val arguments = Bundle()
            arguments.putSerializable(StudentsFragment.CLASSES, classes)
            fragment.arguments = arguments
            return fragment
        }
    }

    private val mSparkServicePresenter: SparkServicePresenter by lazy {
        SparkServicePresenter(context!!, object : SparkListenerAdapter() {

            override fun onCardBind(reportBindCard: ReportBindCard) {
                val keyId = reportBindCard.uid.toString()

                if (oldKeyPad.hasFocus()) {
                    oldKeyPad.setText(keyId)
                    oldKeyPad.setSelection(keyId.length)
                } else if (newKeyPad.hasFocus()) {
                    newKeyPad.setText(keyId)
                    newKeyPad.setSelection(keyId.length)
                }
            }
        })
    }

    lateinit var mClassesEntity: ClassesEntity

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        mClassesEntity = arguments?.getSerializable(StudentsFragment.CLASSES) as ClassesEntity

        complete.setOnClickListener {
            AlertDialog(context!!)
                    .setMessage("是否确定进行替换？")
                    .setCancelButton("取消", null)
                    .setConfirmButton("确定") { _ ->
                        showProgress()
                        SparkService.mUsbSerialNum?.let {
                            mPresenter.replaceKeyPad(it, mClassesEntity.klass?.id.toString(), oldKeyPad.text.toString(), newKeyPad.text.toString())
                        }
                    }
                    .show()
        }

        mSparkServicePresenter.bind()
    }

    override fun onDestroy() {
        super.onDestroy()

        mSparkServicePresenter.unBind()
    }

    override fun replaceSuccess() {
        hideProgress()
        ToastUtils.showShort("已完成替换")
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }

    override fun onNetworkError(throwable: Throwable): Boolean {
        hideProgress()
        return false
    }

    override fun layoutId(): Int = R.layout.fragment_replace

}

