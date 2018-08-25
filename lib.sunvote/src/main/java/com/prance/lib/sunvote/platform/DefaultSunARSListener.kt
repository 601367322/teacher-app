package com.prance.lib.sunvote.platform

import com.blankj.utilcode.util.LogUtils
import cn.sunars.sdk.SunARS
import cn.sunars.sdk.SunARS.*
import com.blankj.utilcode.util.Utils

class DefaultSunARSListener(private val mUsbManagerInterface: IUsbManagerInterface) : SunARS.SunARSListener {


    override fun onConnectEventCallBack(iBaseID: Int, iMode: Int, sInfo: String) {
        LogUtils.d("onConnectEventCallBack>>$iBaseID $iMode $sInfo")

        when (iMode) {
            SunARS.BaseStation_Connected_Model -> {
                SunARS.isConnected = sInfo == SunARS.BaseStation_Connected
                when (sInfo) {
                    SunARS.BaseStation_Connected -> {
                        SunARS.voteStop()
                        //自由链接模式
                        writeHDParam(iBaseID, SunARS.KeyPad_WorkingMode, "2")
                        //键盘识别模式为ID
                        writeHDParam(iBaseID, KeyPad_IdentificationMode, "0")
                        //中文模式
                        writeHDParam(iBaseID, KeyPad_Config, "0,10,1,0,0,0,1")

                        //基站识别ID
                        readHDParam(0, SunARS.BaseStation_ID)
                        //读取基站信息
                        readHDParam(0, SunARS.BaseStation_Channel)
                    }
                    SunARS.BaseStation_DisConnected -> {
                        //关闭连接
                        //断开引用
                        mUsbManagerInterface.closeUsb()

                        //重连
                        mUsbManagerInterface.checkUsbDevice(Utils.getApp())
                    }
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
        try {
            when (iMode) {
            //基站主信道
                SunARS.BaseStation_Channel -> {
                    UsbManagerImpl.baseStation.stationChannel = sInfo.toLong()
                }
                SunARS.BaseStation_ID -> {
                    UsbManagerImpl.baseStation.stationId = sInfo.toInt()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
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

        if (KeyID.length > 10) {
            writeHDParam(0, KeyPad_IdentificationMode, "1")
            writeHDParam(0, KeyPad_IdentificationMode, "0")
        }

        when (iMode) {
        //答题结果
            SunARS.KeyResult_info -> {

            }
        }
    }

    override fun onStaEventCallBack(sInfo: String) {
        LogUtils.d(sInfo)

    }

    override fun onLogEventCallBack(sInfo: String) {
        println("SDK Log:$sInfo")
    }

    override fun onDataTxEventCallBack(sendData: ByteArray, dataLen: Int) {
        mUsbManagerInterface.sendDataBulkTransfer(sendData)
    }


}