/**
 * Copyright (C) 2018 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.prance.lib.teacher.base.core.platform

import android.content.Intent
import android.support.v4.app.Fragment
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.Utils.getApp
import com.prance.lib.base.platform.BaseFragment
import com.prance.lib.common.utils.ToastUtils
import com.prance.lib.teacher.base.TeacherApplication

/**
 * Base Fragment class with helper methods for handling views and back button events.
 *
 * @see Fragment
 */
abstract class BaseFragment : BaseFragment() {

    val application by lazy(mode = LazyThreadSafetyMode.NONE) {
        (context?.applicationContext as TeacherApplication)
    }

    override fun exitToLogin() {
        ToastUtils.showShort("登录状态已过期，请重新登录")
        ActivityUtils.finishAllActivities()
        val packageManager = getApp().packageManager
        val intent = packageManager.getLaunchIntentForPackage(getApp().packageName) ?: return
        val componentName = intent.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        getApp().startActivity(mainIntent)
    }
}
