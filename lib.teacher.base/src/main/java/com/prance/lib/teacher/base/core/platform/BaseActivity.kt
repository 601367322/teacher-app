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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.KeyEvent
import android.view.View
import com.prance.lib.base.platform.BaseActivity
import com.blankj.utilcode.util.LogUtils
import com.prance.lib.common.utils.ModelUtil
import com.prance.lib.third.inter.PluginsManager


/**
 * Base Fragment class with helper methods for handling views and back button events.
 *
 * @see Fragment
 */
abstract class BaseActivity : BaseActivity() {

    lateinit var mHomeMenuBroadcastReceiver: HomeMenuBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //注册遥控器home键广播
        registerHomeMenuBroadcastReceiver()
    }

    fun onBackBtnClick(view: View) {
        finish()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        LogUtils.d(keyCode)
        when (keyCode) {
        //调大声音键
            KeyEvent.KEYCODE_VOLUME_UP -> LogUtils.d("voice up--->")
        //降低声音键
            KeyEvent.KEYCODE_VOLUME_DOWN -> LogUtils.d("voice down--->")
        //静音
            KeyEvent.KEYCODE_MUTE -> LogUtils.d("close voice--->")
        //放大
            KeyEvent.KEYCODE_ZOOM_IN -> LogUtils.d("放大--->")
        //缩小
            KeyEvent.KEYCODE_ZOOM_OUT -> LogUtils.d("缩小--->")
        //向上键
            KeyEvent.KEYCODE_DPAD_UP -> {
                if (!onUpKeyEvent()) {
                    return super.onKeyDown(keyCode, event)
                }
            }
        //向下键
            KeyEvent.KEYCODE_DPAD_DOWN ->
                /*    实际开发中有时候会触发两次，所以要判断一下按下时触发 ，松开按键时不触发
                 *    exp:KeyEvent.ACTION_UP
                 */
                if (event.action == KeyEvent.ACTION_DOWN) {
                    if (!onDownKeyEvent()) {
                        return super.onKeyDown(keyCode, event)
                    }
                }
        //向左键
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                if (!onLeftKeyEvent()) {
                    return super.onKeyDown(keyCode, event)
                }
            }
        //向右键
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                if (!onRightKeyEvent()) {
                    return super.onKeyDown(keyCode, event)
                }
            }
        //OK确认
            KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_DPAD_CENTER -> {
                if (!onOkKeyEvent()) {
                    return super.onKeyDown(keyCode, event)
                }
            }
        //返回键
            KeyEvent.KEYCODE_BACK -> {
                if (!onBackKeyEvent()) {
                    return super.onKeyDown(keyCode, event)
                }
            }
        //菜单键
            KeyEvent.KEYCODE_MENU -> {
                if (!onMenuKeyEvent()) {
                    return super.onKeyDown(keyCode, event)
                }
            }
        //数字键1
            KeyEvent.KEYCODE_1 -> {
                if (!onNumberKeyEvent(1)) {
                    return super.onKeyDown(keyCode, event)
                }
            }
        //数字键2
            KeyEvent.KEYCODE_2 -> {
                if (!onNumberKeyEvent(2)) {
                    return super.onKeyDown(keyCode, event)
                }
            }
        //数字键3
            KeyEvent.KEYCODE_3 -> {
                if (!onNumberKeyEvent(3)) {
                    return super.onKeyDown(keyCode, event)
                }
            }
        //数字键4
            KeyEvent.KEYCODE_4 -> {
                if (!onNumberKeyEvent(4)) {
                    return super.onKeyDown(keyCode, event)
                }
            }
        //数字键5
            KeyEvent.KEYCODE_5 -> {
                if (!onNumberKeyEvent(5)) {
                    return super.onKeyDown(keyCode, event)
                }
            }
        //数字键6
            KeyEvent.KEYCODE_6 -> {
                if (!onNumberKeyEvent(6)) {
                    return super.onKeyDown(keyCode, event)
                }
            }
        //数字键7
            KeyEvent.KEYCODE_7 -> {
                if (!onNumberKeyEvent(7)) {
                    return super.onKeyDown(keyCode, event)
                }
            }
        //数字键8
            KeyEvent.KEYCODE_8 -> {
                if (!onNumberKeyEvent(8)) {
                    return super.onKeyDown(keyCode, event)
                }
            }
        //数字键9
            KeyEvent.KEYCODE_9 -> {
                if (!onNumberKeyEvent(9)) {
                    return super.onKeyDown(keyCode, event)
                }
            }
        //数字键0
            KeyEvent.KEYCODE_0 -> {
                if (!onNumberKeyEvent(0)) {
                    return super.onKeyDown(keyCode, event)
                }
            }
        //星号键
            KeyEvent.KEYCODE_STAR -> {
                if (!onStarKeyEvent()) {
                    return super.onKeyDown(keyCode, event)
                }
            }
        //井号键
            KeyEvent.KEYCODE_POUND -> {
                if (!onPoundKeyEvent()) {
                    return super.onKeyDown(keyCode, event)
                }
            }
            else -> {
            }
        }
        return true
    }

    protected open fun onHomeKeyEvent(): Boolean = false

    protected open fun onBackKeyEvent(): Boolean = false

    protected open fun onUpKeyEvent(): Boolean = false

    protected open fun onOkKeyEvent(): Boolean = false

    protected open fun onLeftKeyEvent(): Boolean = false

    protected open fun onRightKeyEvent(): Boolean = false

    protected open fun onDownKeyEvent(): Boolean = false

    protected open fun onMenuKeyEvent(): Boolean {
        if (ModelUtil.isTestModel) {
            startActivity(PluginsManager.testSetting?.callingTestIntent(this))
            return true
        }
        return false
    }

    protected open fun onNumberKeyEvent(number: Int): Boolean = false

    protected open fun onStarKeyEvent(): Boolean = false

    protected open fun onPoundKeyEvent(): Boolean = false


    private fun registerHomeMenuBroadcastReceiver() {
        mHomeMenuBroadcastReceiver = HomeMenuBroadcastReceiver()
        registerReceiver(mHomeMenuBroadcastReceiver, IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
    }

    private fun unRegisterHomeMenuBroadcastReceiver() {
        unregisterReceiver(mHomeMenuBroadcastReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        unRegisterHomeMenuBroadcastReceiver()
    }

    open inner class HomeMenuBroadcastReceiver : BroadcastReceiver() {

        override fun onReceive(p0: Context?, intent: Intent?) {
            LogUtils.d(intent?.action)
            intent?.let {
                val action = intent.action
                if (action == Intent.ACTION_CLOSE_SYSTEM_DIALOGS) {

                    val reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY)
                    if (SYSTEM_DIALOG_REASON_HOME_KEY == reason) {
                        if (onHomeKeyEvent()) {
                            LogUtils.d("abortBroadcast")
                            abortBroadcast
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val SYSTEM_DIALOG_REASON_KEY = "reason"
        const val SYSTEM_DIALOG_REASON_HOME_KEY = "homekey"
    }
}
