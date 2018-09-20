package com.spark.teaching.answertool.usb.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author hao.chen 【jikechenhao.163.com】
 * @Description:接收器发送题目指令
 * @date 2018年03月23日 12:07
 */
public class SendQuestion extends DataPackage {

    public SendQuestion() {
        this.cmd = (byte) 0x01;
    }

    private Date time;

    private byte questionType;


    @Override
    public List<Byte> encode() {
        List<Byte> list = new ArrayList<Byte>();
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timeStr = dateFormater.format(this.time);
        for (int i = 0; i < timeStr.length(); i++) {
            if (i % 2 == 1) {
                Short tmp = Short.valueOf(String.format("%s%s", timeStr.charAt(i - 1), timeStr.charAt(i)), 16);
                list.add((byte) (tmp & 0xff));
            }
            if (i + 1 == timeStr.length()) {
                Short tmp = Short.valueOf(String.format("%s%s", "0", timeStr.charAt(i)), 16);
                list.add((byte) (tmp & 0xff));
            }
        }
        list.add(this.questionType);
        return list;
    }

    @Override
    public void decoding(byte[] data) {
        throw new RuntimeException("不需要实现此方法");
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public byte getQuestionType() {
        return questionType;
    }

    public void setQuestionType(byte questionType) {
        this.questionType = questionType;
    }
}
