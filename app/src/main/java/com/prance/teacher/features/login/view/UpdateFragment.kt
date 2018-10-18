package com.prance.teacher.features.login.view

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.FragmentActivity
import android.support.v4.content.FileProvider
import android.view.View
import android.widget.LinearLayout
import com.prance.lib.base.extension.appContext
import com.prance.lib.base.extension.inTransaction
import com.prance.lib.base.extension.invisible
import com.prance.lib.base.extension.visible
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.common.NetErrorFragment
import com.prance.teacher.features.login.model.VersionEntity
import com.prance.teacher.services.UpdateService
import kotlinx.android.synthetic.main.fragment_update.*
import java.io.File

class UpdateFragment : BaseFragment(), UpdateService.MyDownloadListener {

    lateinit var mVersionEntity: VersionEntity

    companion object {

        const val VERSION_ENTITY = "entity"

        fun forVersion(versionEntity: VersionEntity): UpdateFragment {
            val fragment = UpdateFragment()
            val arguments = Bundle()
            arguments.putSerializable(VERSION_ENTITY, versionEntity)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.fragment_update

    override fun initView(rootView: View, savedInstanceState: Bundle?) {
        arguments?.run {
            if (containsKey(VERSION_ENTITY)) {
                mVersionEntity = getSerializable(VERSION_ENTITY) as VersionEntity

                startUpdate(mVersionEntity)
            }
        }
    }

    fun startUpdate(ve: VersionEntity) {
        mVersionEntity = ve

        updateLayout.visible()
        loadingImg.invisible()

        desc.text = """检测到新版本，版本号${mVersionEntity.appVersion}，自动更新中…"""

        //启动下载服务
        activity?.bindService(UpdateService.callingIntent(context!!), mDownloadServiceConnection, Service.BIND_AUTO_CREATE)
    }

    private val mDownloadServiceConnection = object : ServiceConnection {

        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            //绑定监听事件
            (service as UpdateService.UpdateServiceBinder).setListener(this@UpdateFragment)
            //开始下载
            activity?.startService(UpdateService.callingIntent(context!!, mVersionEntity.path))
        }
    }

    private fun startInstall(downloadFile: File) {
        try {
            val install = Intent(Intent.ACTION_VIEW)
            install.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            //判断是否是AndroidN以及更高的版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                val contentUri = FileProvider.getUriForFile(appContext, "${appContext.packageName}.fileProvider", downloadFile)
                install.setDataAndType(contentUri, "application/vnd.android.package-archive")
            } else {
                install.setDataAndType(Uri.fromFile(downloadFile), "application/vnd.android.package-archive")
            }
            startActivity(install)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onProgress(currSize: Long, totalSize: Long) {
        loadingProgress?.run {
            loadingProgress.max = totalSize.toInt()
            loadingProgress.progress = currSize.toInt()

            progressTip.text = (currSize.toFloat() / totalSize.toFloat() * 100).toInt().toString() + "%"

            val maxWidth = resources.getDimensionPixelOffset(R.dimen.m1120_0)
            val nowWidth = maxWidth.toFloat() * (currSize.toFloat() / totalSize.toFloat())
            val params = progressTip.layoutParams as LinearLayout.LayoutParams
            params.leftMargin = nowWidth.toInt() + resources.getDimensionPixelOffset(R.dimen.m130_0)
            progressTip.layoutParams = params
        }

    }

    override fun onComplete(dir: String, name: String) {
        startInstall(File(dir, name))

        activity?.finish()
    }


    override fun onError() {
        context?.let {
            (activity as FragmentActivity).supportFragmentManager.inTransaction {
                replace(R.id.fragmentContainer, NetErrorFragment.callIntent {
                    (it as FragmentActivity).supportFragmentManager.inTransaction {
                        replace(R.id.fragmentContainer, forVersion(mVersionEntity))
                    }
                })
            }
        }

    }

    lateinit var mActivity: FragmentActivity

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        mActivity = context as FragmentActivity
    }

    override fun onDestroy() {
        mActivity?.run {
            try {
                unbindService(mDownloadServiceConnection)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            stopService(UpdateService.callingIntent(this))
        }
        super.onDestroy()
    }
}