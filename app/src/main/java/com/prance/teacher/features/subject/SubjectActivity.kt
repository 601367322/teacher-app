package com.prance.teacher.features.subject

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import com.prance.lib.base.extension.inTransaction
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.socket.MessageListener
import com.prance.lib.socket.PushService
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.R
import com.prance.teacher.features.subject.contract.ISubjectContract
import com.prance.teacher.features.subject.presenter.SubjectPresenter
import com.prance.teacher.features.subject.view.SubjectOnCreateFragment
import com.prance.teacher.features.subject.view.SubjectOnDestroyFragment
import com.prance.teacher.features.subject.view.SubjectOnStartFragment
import com.prance.teacher.features.subject.view.SubjectOnStopFragment

/**
 * 上课答题
 */
class SubjectActivity : BaseActivity(), ISubjectContract.View, MessageListener {

    var mPushBinder: PushService.PushServiceBinder? = null

    override fun onMessageResponse(msg: String) {
//        SunARS.voteStart(SunARS.VoteType_Choice,"1,0,0,0,4,1")
    }

    override fun onServiceStatusConnectChanged(statusCode: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {

        fun callingIntent(context: Context) = Intent(context, SubjectActivity::class.java)
    }

    override fun fragment(): BaseFragment = SubjectOnCreateFragment()

    override fun getContext(): Context? = this

    override var mPresenter: ISubjectContract.Presenter = SubjectPresenter()

    private var mPushServiceConnection = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mPushBinder = service as PushService.PushServiceBinder
            mPushBinder?.addListener(this@SubjectActivity)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        bindService(PushService.callingIntent(this), mPushServiceConnection, Service.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()

        mPushBinder?.run {
            removeListener(this@SubjectActivity)
        }
        unbindService(mPushServiceConnection)
    }

    fun onSubjectStart() {
        supportFragmentManager.inTransaction {
            replace(R.id.fragmentContainer, SubjectOnStartFragment())
        }
    }

    fun onSubjectStop() {
        supportFragmentManager.inTransaction {
            replace(R.id.fragmentContainer, SubjectOnStopFragment())
        }
    }

    fun onSubjectDestroy() {
        supportFragmentManager.inTransaction {
            replace(R.id.fragmentContainer, SubjectOnDestroyFragment())
        }
    }
}
