package com.prance.teacher.features.replacekeypad.view

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import com.prance.lib.common.utils.ToastUtils
import com.prance.lib.base.http.ResultException
import com.prance.teacher.features.replacekeypad.contract.IReplaceKeyPadContract
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.replacekeypad.presenter.ReplaceKeyPadPresenter
import com.prance.teacher.features.students.view.StudentsFragment
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

    lateinit var mClassesEntity: ClassesEntity

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        mClassesEntity = arguments?.getSerializable(StudentsFragment.CLASSES) as ClassesEntity

        complete.setOnClickListener {
            AlertDialog.Builder(context)
                    .setMessage("是否确定进行替换？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", { _, _ ->
                        showProgress()
                        application.mBaseStation.sn?.let {
                            mPresenter.replaceKeyPad(it, mClassesEntity.klass?.id.toString(), oldKeyPad.text.toString(), newKeyPad.text.toString())
                        }
                    })
                    .show()
        }
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

