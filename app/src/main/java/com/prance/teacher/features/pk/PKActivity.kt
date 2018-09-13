package com.prance.teacher.features.pk

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.prance.lib.base.extension.inTransaction
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.common.utils.http.mySubscribe
import com.prance.lib.database.MessageEntity
import com.prance.lib.socket.MessageListener
import com.prance.lib.socket.PushService
import com.prance.lib.socket.PushServicePresenter
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.BuildConfig
import com.prance.teacher.R
import com.prance.teacher.features.classes.view.ClassesDetailFragment
import com.prance.teacher.features.pk.contract.IPKResultContract
import com.prance.teacher.features.pk.model.PKResult
import com.prance.teacher.features.pk.presenter.PKPresenter
import com.prance.teacher.features.pk.presenter.PKResultPresenter
import com.prance.teacher.features.pk.view.PKFragment
import com.prance.teacher.features.pk.view.PKRankFragment
import com.prance.teacher.features.pk.view.PKWaitingFragment
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit

class PKActivity : BaseActivity(), MessageListener, IPKResultContract.View {

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

    override var mPresenter: IPKResultContract.Presenter = PKResultPresenter()

    var mSetting: ClassesDetailFragment.Question? = null

    override fun fragment(): BaseFragment? = PKFragment.forSetting(intent.getSerializableExtra(PKFragment.SETTING) as ClassesDetailFragment.Question)

    override fun initView(savedInstanceState: Bundle?) {
        mSetting = intent.getSerializableExtra(PKFragment.SETTING) as ClassesDetailFragment.Question?
        mPushServicePresenter.bind()
        inited()

        super.initView(savedInstanceState)

        if (BuildConfig.DEBUG) {
            Flowable.timer(38, TimeUnit.SECONDS)
                    .mySubscribe {
                        onMessageResponse(MessageEntity(0, PushService.CMD_END_QUESTION, null, null))
                    }
        }
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
                mPresenter.getPKResult(mSetting?.questionId!!)
//                renderRank( PKResult(
//                        mutableListOf(
//                                PKPresenter.PKResultMessage.ClassVO(PKPresenter.PKResultMessage.IDEntity(1, "第一名"), 2.1F, 90.0F),
//                                PKPresenter.PKResultMessage.ClassVO(PKPresenter.PKResultMessage.IDEntity(2, "第二名"), 12.1F, 60.0F),
//                                PKPresenter.PKResultMessage.ClassVO(PKPresenter.PKResultMessage.IDEntity(31, "第三名"), 3.1F, 70.0F),
//                                PKPresenter.PKResultMessage.ClassVO(PKPresenter.PKResultMessage.IDEntity(4, "第四名"), 4.1F, 10.0F),
//                                PKPresenter.PKResultMessage.ClassVO(PKPresenter.PKResultMessage.IDEntity(5, "第无名"), 25.1F, 920.0F)
//                        ),
//                        mutableListOf(
//                                PKResult.Question(3, 100),
//                                PKResult.Question(4, 200),
//                                PKResult.Question(5, 300)
//                        )
//                ))
            }
        }
        return super.onMessageResponse(msg)
    }

    var result: PKResult? = null

    override fun renderRank(it: PKResult) {
        result = it
        supportFragmentManager?.inTransaction {
            replace(R.id.fragmentContainer, PKRankFragment.forPKResult(it))
        }
    }

    override fun getContext(): Context? {
        return this
    }

    fun endPk() {
        Flowable.timer(1, TimeUnit.SECONDS)
                .mySubscribe {
                    if (result == null)
                        this.supportFragmentManager.inTransaction {
                            replace(R.id.fragmentContainer, PKWaitingFragment())
                        }
                }
    }
}