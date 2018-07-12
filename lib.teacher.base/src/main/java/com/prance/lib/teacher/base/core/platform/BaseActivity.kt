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
import com.prance.lib.base.platform.BaseActivity
import com.blankj.utilcode.util.LogUtils


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
                LogUtils.d("up--->")
                return onUpKeyEvent()
            }
        //向下键
            KeyEvent.KEYCODE_DPAD_DOWN ->
                /*    实际开发中有时候会触发两次，所以要判断一下按下时触发 ，松开按键时不触发
                 *    exp:KeyEvent.ACTION_UP
                 */
                if (event.action == KeyEvent.ACTION_DOWN) {
                    LogUtils.d("down--->")
                    return onDownKeyEvent()
                }
        //向左键
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                LogUtils.d("left--->")
                return onLeftKeyEvent()
            }
        //向右键
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                LogUtils.d("right--->")
                return onRightKeyEvent()
            }
        //OK确认
            KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_DPAD_CENTER -> {
                LogUtils.d("enter--->")
                return onOkKeyEvent()
            }
        //返回键
            KeyEvent.KEYCODE_BACK -> {
                LogUtils.d("back--->")
                return onBackKeyEvent()
            }
        //菜单键
            KeyEvent.KEYCODE_MENU -> {
                LogUtils.d("menu--->")
                return onMenuKeyEvent()
            }
        //数字键1
            KeyEvent.KEYCODE_1 -> {
                LogUtils.d("1--->")
                return onNumberKeyEvent(1)
            }
        //数字键2
            KeyEvent.KEYCODE_2 -> {
                LogUtils.d("2--->")
                return onNumberKeyEvent(2)
            }
        //数字键3
            KeyEvent.KEYCODE_3 -> {
                LogUtils.d("3--->")
                return onNumberKeyEvent(3)
            }
        //数字键4
            KeyEvent.KEYCODE_4 -> {
                LogUtils.d("4--->")
                return onNumberKeyEvent(4)
            }
        //数字键5
            KeyEvent.KEYCODE_5 -> {
                LogUtils.d("5--->")
                return onNumberKeyEvent(5)
            }
        //数字键6
            KeyEvent.KEYCODE_6 -> {
                LogUtils.d("6--->")
                return onNumberKeyEvent(6)
            }
        //数字键7
            KeyEvent.KEYCODE_7 -> {
                LogUtils.d("7--->")
                return onNumberKeyEvent(7)
            }
        //数字键8
            KeyEvent.KEYCODE_8 -> {
                LogUtils.d("8--->")
                return onNumberKeyEvent(8)
            }
        //数字键9
            KeyEvent.KEYCODE_9 -> {
                LogUtils.d("9--->")
                return onNumberKeyEvent(9)
            }
        //数字键0
            KeyEvent.KEYCODE_0 -> {
                LogUtils.d("0--->")
                return onNumberKeyEvent(0)
            }
        //星号键
            KeyEvent.KEYCODE_STAR -> {
                LogUtils.d("星号键--->")
                return onStarKeyEvent()
            }
        //井号键
            KeyEvent.KEYCODE_POUND -> {
                LogUtils.d("井号键--->")
                return onPoundKeyEvent()
            }
            else -> {
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    protected open fun onHomeKeyEvent(): Boolean = false

    protected open fun onBackKeyEvent(): Boolean = false

    protected open fun onUpKeyEvent(): Boolean = false

    protected open fun onOkKeyEvent(): Boolean = false

    protected open fun onLeftKeyEvent(): Boolean = false

    protected open fun onRightKeyEvent(): Boolean = false

    protected open fun onDownKeyEvent(): Boolean = false

    protected open fun onMenuKeyEvent(): Boolean = false

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

    inner class HomeMenuBroadcastReceiver : BroadcastReceiver() {

        override fun onReceive(p0: Context?, intent: Intent?) {
            intent?.let {
                val action = intent.action
                if (action == Intent.ACTION_CLOSE_SYSTEM_DIALOGS) {

                    val reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY)
                    if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
                        if (onHomeKeyEvent())
                            abortBroadcast
                    }
                }
            }
        }

        private val SYSTEM_DIALOG_REASON_KEY = "reason"
        private val SYSTEM_DIALOG_REASON_HOME_KEY = "homekey"
    }
}
