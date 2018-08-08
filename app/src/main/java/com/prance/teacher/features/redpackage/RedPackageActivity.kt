package com.prance.teacher.features.redpackage

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.socket.MessageListener
import com.prance.lib.socket.PushService
import com.prance.lib.socket.model.MessageEntity
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.features.classes.model.ClassesEntity
import com.prance.teacher.features.redpackage.model.RedPackageSetting
import com.prance.teacher.features.redpackage.view.RedPackageFragment

class RedPackageActivity : BaseActivity(), MessageListener {

    /**
     * 下发的抢红包设置
     */
    var mSetting: RedPackageSetting? = null
    var mPushBinder: PushService.PushServiceBinder? = null
    /**
     * 抢红包fragment
     */
    var mGrabFragment: RedPackageFragment? = null
    companion object {
        fun callingIntent(context: Context, redPackage: RedPackageSetting): Intent {
            val intent = Intent(context, RedPackageActivity::class.java)
            intent.putExtra(RedPackageFragment.mSetTing, redPackage)
            return intent
        }
    }

    override fun fragment(): BaseFragment{
        if (mGrabFragment == null) {
            mGrabFragment = RedPackageFragment()
        }
        return mGrabFragment!!
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        mSetting = intent?.getSerializableExtra(RedPackageFragment.mSetTing) as RedPackageSetting?
        val bundle = Bundle()
        bundle.putSerializable(RedPackageFragment.mSetTing,mSetting)
        if (mGrabFragment!!.arguments != null){
            mGrabFragment!!.arguments?.putAll(bundle)
        } else {
            mGrabFragment!!.arguments = bundle
        }
        bindService(PushService.callingIntent(this), mPushServiceConnection, Service.BIND_AUTO_CREATE)
    }

    private var mPushServiceConnection = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mPushBinder = service as PushService.PushServiceBinder
            mPushBinder?.addListener(this@RedPackageActivity)
        }
    }

    override fun onMessageResponse(msg: MessageEntity) {
        when(msg.cmd){
            PushService.END_INTERACTN ->{
                mGrabFragment!!.redPackageStop()
                finish()
            }
        }
    }

    override fun onServiceStatusConnectChanged(statusCode: Int) {
    }

    override fun onDestroy() {
        super.onDestroy()

        mPushBinder?.run {
            removeListener(this@RedPackageActivity)
        }
        unbindService(mPushServiceConnection)
    }
}