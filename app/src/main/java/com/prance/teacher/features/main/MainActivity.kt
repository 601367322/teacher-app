package com.prance.teacher.features.main

import android.content.Context
import android.os.Bundle
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.teacher.base.core.platform.BaseActivity
import android.content.Intent
import android.content.Intent.ACTION_CLOSE_SYSTEM_DIALOGS
import android.content.Intent.ACTION_MAIN


//@RuntimePermissions
class MainActivity : BaseActivity() {

    override fun fragment(): BaseFragment = MainFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {

        fun callingIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

//    private var requestPermissionFlag = AtomicBoolean(false)
//
//    private var DEFAULT_SETTINGS_REQ_CODE = 1032
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        try {
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1 && ModelUtil.isTestModel) {
//                if (!Settings.canDrawOverlays(this)) {
//                    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//                            Uri.parse("package:$packageName"))
//                    startActivityForResult(intent, 1223)
//                    ToastUtils.showLong("请[允许可出现在其他应用上]")
//                }
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    override fun onWindowFocusChanged(hasFocus: Boolean) {
//        super.onWindowFocusChanged(hasFocus)
//
//        if (requestPermissionFlag.compareAndSet(false, true)) {
//
//            onPermissionsAccessWithPermissionCheck()
//        }
//    }
//
//
//    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)
//    internal fun onPermissionsAccess() {
//
//    }
//
//    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)
//    internal fun onShouldShowRequestPermissionRationale(request: PermissionRequest) {
//        onShouldShowNeverRequestPermissionRationale()
//    }
//
//    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)
//    internal fun onPermissionsUnAccess() {
//        onPermissionsAccessWithPermissionCheck()
//    }
//
//    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE)
//    internal fun onShouldShowNeverRequestPermissionRationale() {
//
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        onRequestPermissionsResult(requestCode, grantResults)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == DEFAULT_SETTINGS_REQ_CODE) {
//            onPermissionsAccessWithPermissionCheck()
//        }
//    }
}