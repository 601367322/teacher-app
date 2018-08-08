package com.prance.teacher.features.login.view

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v4.content.FileProvider
import android.view.View
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ServiceUtils
import com.blankj.utilcode.util.ServiceUtils.stopService
import com.blankj.utilcode.util.ServiceUtils.unbindService
import com.leo.download.DownloadError
import com.leo.download.DownloadListener
import com.prance.lib.base.extension.appContext
import com.prance.lib.common.utils.GlideApp
import com.prance.lib.common.utils.weight.AlertDialog
import com.prance.lib.teacher.base.core.platform.BaseFragment
import com.prance.teacher.R
import com.prance.teacher.features.login.model.VersionEntity
import com.prance.teacher.services.UpdateService
import kotlinx.android.synthetic.main.fragment_update.*
import java.io.File

class UpdateFragment : BaseFragment(), DownloadListener {

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

        mVersionEntity = arguments?.getSerializable(VERSION_ENTITY) as VersionEntity

        //加载动图
        GlideApp
                .with(this)
                .asGif()
                .load(R.drawable.update_loading_bar)
                .into(loadingBar)

        desc.text = """您当前的版本为v${AppUtils.getAppVersionName()}，已检测到新版本${mVersionEntity.appVersion}，正在进行更新请稍等……"""

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
    }


    override fun onStart(id: Int, size: Long) {
    }

    override fun onProgress(id: Int, currSize: Long, totalSize: Long) {
    }

    override fun onRestart(id: Int, currSize: Long, totalSize: Long) {
    }

    override fun onPause(id: Int, currSize: Long) {
    }

    override fun onComplete(id: Int, dir: String?, name: String?) {
        startInstall(File(dir, name))

        activity?.run {
            supportFragmentManager.beginTransaction().remove(this@UpdateFragment).commitAllowingStateLoss()
        }
    }

    override fun onCancel(id: Int) {
    }

    override fun onError(id: Int, error: DownloadError?) {
        context?.let {
            AlertDialog(it)
                    .setConfirmButton("确定", {
                        activity?.finish()
                    })
                    .setCancelButton("取消", null)
                    .setMessage("更新失败，可退出程序重新进入 进行更新")
                    .show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        context?.run {
            unbindService(mDownloadServiceConnection)
            stopService(UpdateService.callingIntent(this))
        }
    }
}