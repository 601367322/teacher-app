package com.prance.lib.sunvote.platform

import com.blankj.utilcode.util.LogUtils
import cn.sunars.sdk.SunARS

class MySunARSListener(private val mUsbManagerInterface: IUsbManagerInterface) : SunARS.SunARSListener {


    override fun onConnectEventCallBack(iBaseID: Int, iMode: Int, sInfo: String) {
        LogUtils.d("onConnectEventCallBack>>$iBaseID $iMode $sInfo")

        SunARS.isConnected = sInfo == "1"
        if (sInfo == "1") {

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

    override fun onHDParamCallBack(iBaseID: Int, iMode: Int, sInfo: String) {
        LogUtils.d("onHDParamCallBack>>$iBaseID $iMode $sInfo")
        if (iMode == SunARS.KeyPad_IdentificationMode) {

        }
    }

    override fun onHDParamBySnCallBack(KeySn: String, iMode: Int, sInfo: String) {
        LogUtils.d("onHDParamBySnCallBack>>$KeySn $iMode $sInfo")

    }

    override fun onVoteEventCallBack(iBaseID: Int, iMode: Int, sInfo: String) {
        LogUtils.d("onVoteEventCallBack>>>$iBaseID $iMode $sInfo")
    }

    override fun onKeyEventCallBack(KeyID: String, iMode: Int, Time: Float, sInfo: String) {
        LogUtils.d("onKeyEventCallBack>>$KeyID $iMode $Time $sInfo")
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