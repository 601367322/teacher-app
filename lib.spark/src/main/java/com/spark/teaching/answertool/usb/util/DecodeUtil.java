package com.spark.teaching.answertool.usb.util;

import com.blankj.utilcode.util.LogUtils;
import com.spark.teaching.answertool.usb.helper.CommunicateHelper;
import com.spark.teaching.answertool.usb.helper.UsbListener;
import com.spark.teaching.answertool.usb.model.ClearDeviceConfigureRep;
import com.spark.teaching.answertool.usb.model.CloseBindCardRep;
import com.spark.teaching.answertool.usb.model.EchoRep;
import com.spark.teaching.answertool.usb.model.OpenBindCardRep;
import com.spark.teaching.answertool.usb.model.ReceiveAnswer;
import com.spark.teaching.answertool.usb.model.ReceiveAnswerRep;
import com.spark.teaching.answertool.usb.model.ReportBindCard;
import com.spark.teaching.answertool.usb.model.ReportBindCardRep;
import com.spark.teaching.answertool.usb.model.SendQuestionRep;
import com.spark.teaching.answertool.util.KLog;

import java.nio.ByteBuffer;

/**
 * @author hao.chen 【jikechenhao.163.com】
 * @Description:
 * @date 2018年03月26日 14:17
 */
public class DecodeUtil {
    private static final String TAG = "DecodeUtil";

    public static void decode(ByteBuffer buffer, UsbListener usbListener) {
        if (null == buffer) {
            return;
        }

        StringBuilder stringBuilder = new StringBuilder("接收字节：");
        for (int i = 0; i < 64; i++) {
            stringBuilder.append(String.format("%02X  ", buffer.get(i)));
        }
        String msg = null;

        byte seq = buffer.get();
        byte num = buffer.get();
        byte index = buffer.get();
        byte cmd = buffer.get();
        byte len = buffer.get();
        byte[] data = new byte[len];
        buffer.get(data, 0, len);
        byte crc = buffer.get();
        switch (cmd & 0xff) {
            case 0x81://接收器发送题目响应
                SendQuestionRep sendQuestionRep = new SendQuestionRep();
                sendQuestionRep.decoding(data);
                msg = "发送(停止)题目响应：" + sendQuestionRep.toString();
                LogUtils.d( msg + "\n" + stringBuilder.toString(), false);
                break;
            case 0x02://答题器上报答案
                ReceiveAnswer receiveAnswer = new ReceiveAnswer();
                receiveAnswer.decoding(data);
                msg = "答题器上报答案：" + receiveAnswer.toString();
                LogUtils.d( msg + "\n" + stringBuilder.toString(), false);

                // 收到指令后要告诉接收器我收到了
                ReceiveAnswerRep receiveAnswerRep = new ReceiveAnswerRep();
                receiveAnswerRep.setResponseCode((byte) 0);
                CommunicateHelper.getInstance().sendAsync(receiveAnswerRep);

                if (usbListener != null) {
                    usbListener.onAnswerReceived(receiveAnswer);
                }
                break;
            case 0x83://答题器回显设置响应
                EchoRep echoRep = new EchoRep();
                echoRep.decoding(data);
                msg = "答题器回显设置响应：" + echoRep.toString();
                LogUtils.d( msg + "\n" + stringBuilder.toString(), false);
                break;
            case 0x94://清除配置信息指令响应
                ClearDeviceConfigureRep clearDeviceConfigureRep = new ClearDeviceConfigureRep();
                clearDeviceConfigureRep.decoding(data);
                msg = "清除配置信息指令响应：" + clearDeviceConfigureRep.toString();
                LogUtils.d( msg + "\n" + stringBuilder.toString(), false);
                break;
            case 0x95://开启刷卡绑卡指令响应
                OpenBindCardRep openBindCardRep = new OpenBindCardRep();
                openBindCardRep.decoding(data);
                msg = "开启刷卡绑卡指令响应：" + openBindCardRep.toString();
                LogUtils.d( msg + "\n" + stringBuilder.toString(), false);
                break;
            case 0x16://接收器上报刷卡绑定结果
                ReportBindCard reportBindCard = new ReportBindCard();
                reportBindCard.decoding(data);
                msg = "上报刷卡绑定结果：" + reportBindCard.toString();
                LogUtils.d( msg + "\n" + stringBuilder.toString(), false);

                // 收到指令后要告诉接收器我收到了
                ReportBindCardRep reportBindCardRep = new ReportBindCardRep();
                reportBindCardRep.setResponseCode((byte) 0);
                CommunicateHelper.getInstance().sendAsync(reportBindCardRep);

                if (usbListener != null) {
                    usbListener.onCardBind(reportBindCard);
                }
                break;
            case 0x97://停止刷卡绑定指令响应
                CloseBindCardRep closeBindCardRep = new CloseBindCardRep();
                closeBindCardRep.decoding(data);
                msg = "停止刷卡绑定指令响应：" + closeBindCardRep.toString();
                LogUtils.d( msg + "\n" + stringBuilder.toString(), false);
                break;
            default://出现异常指令
                msg = "cmd: " + (cmd & 0xff);
                LogUtils.d( msg + "\n" + stringBuilder.toString(), false);
                break;
        }
    }
}
