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
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.prance.lib.base.mvp.ITopView
import java.lang.ref.WeakReference

/**
 * Base Fragment class with helper methods for handling views and back button events.
 *
 * @see Fragment
 */
abstract class BaseFragment : Fragment(), ITopView {

    private var inited = false

    protected var mRootView: WeakReference<View>? = null

    abstract fun layoutId(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mFragmentView: View? = null
        if (mRootView == null || mRootView!!.get() == null) {
            inited = false
            mFragmentView = inflater.inflate(layoutId(), container, false)
            mRootView = WeakReference(mFragmentView)
        } else {
            val parent = mRootView!!.get()?.parent as ViewGroup?
            parent?.removeView(mRootView!!.get())
            mFragmentView = mRootView!!.get()
        }
        return mRootView!!.get()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (!inited) {
            val rootView = mRootView!!.get()
            rootView?.let {
                initView(it, savedInstanceState)
                inited()
                inited = true
            }
        }
    }

    open fun onBackPressed() {}

    protected fun firstTimeCreated(savedInstanceState: Bundle?) = savedInstanceState == null

    protected fun showProgress() = progressStatus(View.VISIBLE)

    protected fun hideProgress() = progressStatus(View.GONE)

    private fun progressStatus(viewStatus: Int) {
        with(activity) {
            if (this is BaseActivity) {
                when (viewStatus) {
                    View.VISIBLE -> {
                        if (!this.progress.isShowing) {
                            this.progress.show()
                        }
                    }
                    View.GONE -> {
                        if (this.progress.isShowing) {
                            this.progress.dismiss()
                        }
                    }
                }
            }
        }
    }

    /**
     * mFragmentView创建完成后,初始化具体的view 只会调用一次
     */
    protected abstract fun initView(rootView: View, savedInstanceState: Bundle?)
}