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
package com.prance.lib.base.platform

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.prance.lib.base.R
import com.prance.lib.base.extension.inTransaction
import com.prance.lib.common.utils.weight.LoadingDialog

abstract class BaseActivity : FragmentActivity() {

    val progress by lazy(mode = LazyThreadSafetyMode.NONE) {
        LoadingDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
        addFragment(savedInstanceState)
    }

    override fun onBackPressed() {
        (supportFragmentManager.findFragmentById(
                R.id.fragmentContainer) as BaseFragment).onBackPressed()
        super.onBackPressed()
    }

    private fun addFragment(savedInstanceState: Bundle?) =
            savedInstanceState ?: supportFragmentManager.inTransaction {
                add(R.id.fragmentContainer, fragment())
            }

    abstract fun fragment(): BaseFragment

    open fun layoutId(): Int = R.layout.activity_layout
}
