package com.prance.lib.sunvote.service;

import cn.sunars.sdk.SunARS;

public abstract class SunARSListenerAdapter implements SunARS.SunARSListener {

    public void onServiceConnected() {
    }

    @Override
    public void onConnectEventCallBack(int iBaseID, int iMode, String sInfo) {
    }

    @Override
    public void onHDParamCallBack(int iBaseID, int iMode, String sInfo) {
    }

    @Override
    public void onHDParamBySnCallBack(String KeySn, int iMode, String sInfo) {
    }

    @Override
    public void onVoteEventCallBack(int iBaseID, int iMode, String sInfo) {
    }

    @Override
    public void onKeyEventCallBack(String KeyID, int iMode, float Time, String sInfo) {
    }

    @Override
    public void onStaEventCallBack(String sInfo) {
    }

    @Override
    public void onLogEventCallBack(String sInfo) {
    }

    @Override
    public void onDataTxEventCallBack(byte[] sendData, int dataLen) {
    }
}