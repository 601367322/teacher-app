package com.spark.teaching.answertool.usb.util;

import com.spark.teaching.answertool.usb.constrant.QuestionTypeConstrant;
import com.spark.teaching.answertool.usb.model.DataPackage;
import com.spark.teaching.answertool.usb.model.SendQuestion;
import com.spark.teaching.answertool.util.KLog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hao.chen 【jikechenhao.163.com】
 * @Description:
 * @date 2018年03月26日 17:13
 */
public class EncodeUtil {
    private static final String TAG = "EncodeUtil";

    public static byte[] encode(DataPackage dataPackage) {
        List<Byte> list = new ArrayList<Byte>();
        list.add(dataPackage.getSeq());
        list.add((byte) 1);
        list.add((byte) 1);
        list.add(dataPackage.getCmd());
        List<Byte> dataPackages = dataPackage.encode();
        list.add((byte) (dataPackages.size() & 0xff));
        list.addAll(dataPackages);
        byte xor = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            xor = (byte) (xor ^ list.get(i));
        }
        list.add(xor);
        byte[] bytes = new byte[64];
        int index = 0;
        for (Byte item : list) {
            bytes[index++] = item;
        }

        String cmdName = "";
        switch (dataPackage.getCmd() & 0xff) {
            case 0x11:
                cmdName = "【设置信道指令】";
                break;
            case 0x14:
                cmdName = "【清除设备配置信息指令】";
                break;
            case 0x17:
                cmdName = "【停止刷卡绑定指令】";
                break;
            case 0x13:
                cmdName = "【查询设备配置信息指令】";
                break;
            case 0x03:
                cmdName = "【答题器回显指令】";
                break;
            case 0x15:
                cmdName = "【开启设备绑卡指令】";
                break;
            case 0x01:
                cmdName = "【接收器发送题目指令】";
                break;
        }
        if (dataPackage instanceof SendQuestion && ((SendQuestion) dataPackage).getQuestionType() == QuestionTypeConstrant.STOP_ANSWER) {
            cmdName = "【停止答题指令】";
        }
        StringBuilder stringBuilder = new StringBuilder(cmdName + "发送字节：");
        for (int i = 0; i < 64; i++) {
            stringBuilder.append(String.format("%02X ", bytes[i]));
        }
        KLog.i(TAG, stringBuilder.toString(), false);
        return bytes;
    }

}
