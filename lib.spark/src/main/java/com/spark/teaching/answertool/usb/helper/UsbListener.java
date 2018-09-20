package com.spark.teaching.answertool.usb.helper;

import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;

import com.spark.teaching.answertool.usb.model.ReceiveAnswer;
import com.spark.teaching.answertool.usb.model.ReportBindCard;

/**
 * usb接口
 */

public interface UsbListener {

    /**
     * 接收器连接成功（主线程回调）
     *
     * @param usbDeviceConnection
     * @param in
     * @param out
     * @param serialNum
     */
    void onConnected(UsbDeviceConnection usbDeviceConnection, UsbEndpoint in, UsbEndpoint out, String serialNum);

    /**
     * 接收器断开连接（本来就没连接成功，拔出usb，也会回调这个方法）（主线程回调）
     */
    void onDisConnected();

    /**
     * 接收到来自答题器的答案（非主线程回调）
     *
     * @param receiveAnswer
     */
    void onAnswerReceived(ReceiveAnswer receiveAnswer);

    /**
     * 接收到绑卡指令（非主线程回调）
     *
     * @param reportBindCard
     */
    void onCardBind(ReportBindCard reportBindCard);
}
