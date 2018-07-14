package com.prance.lib.sunvote.platform

import com.blankj.utilcode.util.LogUtils
import cn.sunars.sdk.SunARS
import cn.sunars.sdk.SunARS.*

class MySunARSListener(private val mUsbManagerInterface: IUsbManagerInterface) : SunARS.SunARSListener {


    override fun onConnectEventCallBack(iBaseID: Int, iMode: Int, sInfo: String) {
        LogUtils.d("onConnectEventCallBack>>$iBaseID $iMode $sInfo")

        SunARS.isConnected = sInfo == "1"
        if (sInfo == "1") {
            //自由链接模式
            writeHDParam(iBaseID, SunARS.KeyPad_WorkingMode, "2")
            //键盘识别模式为ID
            writeHDParam(0, KeyPad_IdentificationMode, "0")
            //中文模式
            writeHDParam(0, KeyPad_Config, "0,10,1,0,0,0,1")

            SunARS.checkKeyPad()
        } else {
            if (iMode == 5) {
                val map = mUsbManagerInterface.getDeviceList()
                LogUtils.d("devicelist cont:" + map?.size)
                if (map?.size == 0) {
                    LogUtils.d("oncennect event: closeUsb")
                    mUsbManagerInterface.closeUsb()
                }
            }
        }


    }


    /**
     * 设置参数读写回调函数
     * 应用说明:此函数用于将客户编写的参数读写事件回调函数的引用或指针传给 SDK，SDK
     * 在收到参数读写事件时调用该回调函数，二次开发客户在回调函数内部处理参数读写事件。
     */
    override fun onHDParamCallBack(iBaseID: Int, iMode: Int, sInfo: String) {
        LogUtils.d("onHDParamCallBack>>$iBaseID $iMode $sInfo")
        if (iMode == SunARS.KeyPad_IdentificationMode) {

        }
    }

    override fun onHDParamBySnCallBack(KeySn: String, iMode: Int, sInfo: String) {
        LogUtils.d("onHDParamBySnCallBack>>$KeySn $iMode $sInfo")

    }

    /**
     * 设置投票回调函数
     * 应用说明:此函数用于将客户编写的投票事件回调函数的引用或指针传给 SDK，SDK 在收
     * 到投票事件时调用该回调函数，二次开发客户在回调函数内部处理投票事件。
     */
    override fun onVoteEventCallBack(iBaseID: Int, iMode: Int, sInfo: String) {
        LogUtils.d("onVoteEventCallBack>>>$iBaseID $iMode $sInfo")
    }

    /**
     * 设置键盘事件回调函数
     *
     * 应用说明:此函数用于将客户编写的键盘事件回调函数的引用或指针传给 SDK，SDK 在收到
     * 键盘事件时调用该回调函数，二次开发客户在回调函数内部处理键盘事件。
     */
    override fun onKeyEventCallBack(KeyID: String, iMode: Int, Time: Float, sInfo: String) {
        LogUtils.d("onKeyEventCallBack>>$KeyID $iMode $Time $sInfo")

        when (iMode) {
        //键盘输入频道与基站进行配对
            SunARS.KeyResult_loginInfo, SunARS.KeyResult_match -> {

            }
        //键盘检测结果
            SunARS.KeyResult_status -> {

            }
        //答题结果
            SunARS.KeyResult_info -> {

            }
        }
    }

    override fun onStaEventCallBack(sInfo: String) {
        LogUtils.d(sInfo)

    }

    override fun onLogEventCallBack(sInfo: String) {
        LogUtils.d("SDK Log:$sInfo")
    }

    override fun onDataTxEventCallBack(sendData: ByteArray, dataLen: Int) {
        mUsbManagerInterface.sendDataBulkTransfer(sendData)
    }


}