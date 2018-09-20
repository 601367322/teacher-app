package com.spark.teaching.answertool.usb.model;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hao.chen 【jikechenhao.163.com】
 * @Description: 上报刷卡绑定结果指令响应
 * @date 2018年03月23日 12:20
 */
public class ReportBindCardRep extends DataPackage{

    public ReportBindCardRep(){
        this.cmd = (byte)0x96;
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
