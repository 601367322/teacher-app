package com.prance.teacher.features.classes.view

import android.os.Bundle
import android.text.Html
import android.view.View
import com.prance.lib.spark.SparkService
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.match.model.MatchKeyPadModel
import kotlinx.android.synthetic.main.fragment_classes_next_step_keypad_inadequate.*

/**
 * 答题器数量不足
 */
class ClassesNextStepKeyPadInadequate : BaseFragment() {

    private val mMatchKeyPadModel = MatchKeyPadModel()

    override fun layoutId(): Int = R.layout.fragment_classes_next_step_keypad_inadequate

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        SparkService.mUsbSerialNum?.run {
            keyPadCount.text = Html.fromHtml("""答题器数 <font color="#3AF0EE">${mMatchKeyPadModel.getAllKeyPadByBaseStationSN(this)}</font>""")
        }
    }
}