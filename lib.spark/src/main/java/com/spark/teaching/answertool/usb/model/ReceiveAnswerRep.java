package com.spark.teaching.answertool.usb.model;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hao.chen 【jikechenhao.163.com】
 * @Description: 答题器答案上报指令
 * @date 2018年03月23日 12:13
 */
public class ReceiveAnswerRep extends DataPackage{

    public ReceiveAnswerRep(){
        this.cmd = (byte)0x82;
    }

    private byte responseCode;

    @Override
    public List<Byte> encode() {
        List<Byte> list = new ArrayList<Byte>();
        list.add(responseCode);
        return list;
    }

    @Override
    public void decoding(byte[] data) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        this.responseCode = byteBuffer.get();
    }

    public byte getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(byte responseCode) {
        this.responseCode = responseCode;
    }
}
