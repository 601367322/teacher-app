package com.prance.teacher.features.match

import android.content.Context
import android.content.Intent
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.common.utils.Constants.CLASSES
import com.prance.lib.server.vo.teacher.ClassVo
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.features.match.view.MatchKeyPadFragment

class MatchKeyPadActivity : BaseActivity() {

    companion object {

        fun callingIntent(context: Context, classes: ClassVo?): Intent {
            val intent = Intent(context, MatchKeyPadActivity::class.java)
            classes?.run {
                intent.putExtra(CLASSES, classes)
            }
            return intent
        }
    }

    override fun fragment(): BaseFragment = MatchKeyPadFragment.forClasses(intent?.getSerializableExtra(CLASSES)?.run { this as ClassVo })

}