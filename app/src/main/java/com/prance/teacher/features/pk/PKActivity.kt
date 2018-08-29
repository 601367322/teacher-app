package com.prance.teacher.features.pk

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.prance.lib.base.extension.inTransaction
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.database.MessageEntity
import com.prance.lib.socket.MessageListener
import com.prance.lib.socket.PushService
import com.prance.lib.socket.PushServicePresenter
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.R
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.pk.view.PKFragment
import com.prance.teacher.features.pk.view.PKWaitingFragment

class PKActivity : BaseActivity(), MessageListener {

    companion object {

        fun callingIntent(context: Context, setting: ClassesDetailFragment.Question): Intent {
            val intent = Intent(context, PKActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            intent.putExtra(PKFragment.SETTING, setting)
            return intent
        }
    }

    val mPushServicePresenter by lazy {
        PushServicePresenter(this, this)
    }

    var mSetting: ClassesDetailFragment.Question? = null

    override fun fragment(): BaseFragment? = PKFragment.forSetting(intent.getSerializableExtra(PKFragment.SETTING) as ClassesDetailFragment.Question)

    override fun initView(savedInstanceState: Bundle?) {
        mSetting = intent.getSerializableExtra(PKFragment.SETTING) as ClassesDetailFragment.Question?
        mPushServicePresenter.bind()

        super.initView(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()

        mPushServicePresenter.unBind()
    }

    override fun onMessageResponse(msg: MessageEntity): Boolean {
        val fragments = supportFragmentManager.fragments
        for (fragment in fragments) {
            if (fragment is MessageListener) {
                if (fragment.onMessageResponse(msg)) {
                    return super.onMessageResponse(msg)
                }
            }
        }

        when (msg.cmd) {
            PushService.CMD_END_QUESTION -> {
                supportFragmentManager.inTransaction {
                    replace(R.id.fragmentContainer, PKWaitingFragment.callingIntent(true, mSetting?.questionId!!))
                }
            }
        }
        return super.onMessageResponse(msg)
    }
}