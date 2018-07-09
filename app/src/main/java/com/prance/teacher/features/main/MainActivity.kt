package com.prance.teacher.features.main

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.prance.lib.base.platform.BaseActivity
import com.prance.lib.base.platform.BaseFragment
import permissions.dispatcher.*
import java.util.concurrent.atomic.AtomicBoolean
import android.os.Build
import android.provider.Settings
import com.blankj.utilcode.util.ToastUtils
import com.prance.lib.common.utils.ModelUtil


@RuntimePermissions
class MainActivity : BaseActivity() {

    private var requestPermissionFlag = AtomicBoolean(false)

    private var DEFAULT_SETTINGS_REQ_CODE = 1032

    override fun fragment(): BaseFragment = MainFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1 && ModelUtil.isTestModel) {
            if (!Settings.canDrawOverlays(this)) {
                ToastUtils.showLong("请[允许可出现在其他应用上]")
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName"))
                startActivityForResult(intent, 1223)
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        if (requestPermissionFlag.compareAndSet(false, true)) {

            onPermissionsAccessWithPermissionCheck()
        }
    }


    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)
    internal fun onPermissionsAccess() {

    }

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)
    internal fun onShouldShowRequestPermissionRationale(request: PermissionRequest) {
        onShouldShowNeverRequestPermissionRationale()
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)
    internal fun onPermissionsUnAccess() {
        onPermissionsAccessWithPermissionCheck()
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)
    internal fun onShouldShowNeverRequestPermissionRationale() {

//        DialogUtil.show(
//                mContext,
//                "权限申请",
//                "为了更好地为您服务，腾跃校长在线需要获取\"存储空间权限\"和\"电话权限\"来确定设备ID，以保证账号登录的安全性。获取\"拍照权限\"，以保证部分功能正常使用。",
//                "去设置",
//                "退出程序", null,
//                { dialog, which ->
//                    startActivityForResult(
//                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                                    .setData(Uri.fromParts("package", getPackageName(), null)),
//                            DEFAULT_SETTINGS_REQ_CODE)
//                }, { dialog, which -> finish() })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == DEFAULT_SETTINGS_REQ_CODE) {
            onPermissionsAccessWithPermissionCheck()
        }
    }
}