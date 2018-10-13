package com.prance.teacher.features.welcome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.AppUtils
import com.prance.lib.base.extension.inTransaction
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.common.utils.http.ResultException
import com.prance.lib.teacher.base.core.platform.BaseActivity
import com.prance.teacher.R
import com.prance.teacher.features.check.CheckKeyPadActivity
import com.prance.teacher.features.common.NetErrorFragment
import com.prance.teacher.features.login.LoginActivity
import com.prance.teacher.features.login.model.VersionEntity
import com.prance.teacher.features.login.view.UpdateFragment

class WelcomeActivity : BaseActivity(), IWelcomeContract.View {

    override fun getContext(): Context = this

    override var mPresenter: IWelcomeContract.Presenter = WelcomePresenter()

    override fun initView(savedInstanceState: Bundle?) {
        //防止重复启动
        if (!this.isTaskRoot) { //判断该Activity是不是任务空间的源Activity，“非”也就是说是被系统重新实例化出来
            //如果你就放在launcher Activity中话，这里可以直接return了
            val mainIntent = intent
            val action = mainIntent.action
            if (mainIntent.hasCategory(Intent.CATEGORY_LEANBACK_LAUNCHER) && action == Intent.ACTION_MAIN) {
                finish()
                return //finish()之后该活动会继续执行后面的代码，你可以logCat验证，加return避免可能的exception
            }
        }
        super.initView(savedInstanceState)

        inited()
        mPresenter.checkVersion()
    }

    override fun fragment(): BaseFragment = WelcomeFragment()

    override fun checkVersionCallBack(versionEntity: VersionEntity) {
        //显示更新提示
        versionEntity.let {
            if (versionEntity.codeVersion > AppUtils.getAppVersionCode()) {
                var updateFragment: UpdateFragment? = null
                for (fragment in supportFragmentManager.fragments) {
                    if (fragment is UpdateFragment) {
                        updateFragment = fragment
                    }
                }
                updateFragment?.startUpdate(versionEntity)
                        ?: supportFragmentManager?.inTransaction {
                            replace(R.id.fragmentContainer, UpdateFragment.forVersion(it))
                        }
            } else {
                nextStep()
            }
        }
    }

    override fun onNetworkError(throwable: Throwable): Boolean {
        if (throwable is ResultException) {
            nextStep()
            return true
        }
        supportFragmentManager.inTransaction {
            replace(R.id.fragmentContainer, NetErrorFragment.callIntent(NetErrorFragment.Retry {
                retry()
                mPresenter.checkVersion()
            }))
        }
        return true
    }

    private fun nextStep(){
        startActivity(CheckKeyPadActivity.callingIntent(this))
        finish()
    }
}