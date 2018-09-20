package com.spark.teaching.answertool.usb.model;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * @author hao.chen 【jikechenhao.163.com】
 * @Description: 停止刷卡绑定指令响应
 * @date 2018年03月23日 12:21
 */
public class CloseBindCardRep extends DataPackage{
    private byte responseCode;

    @Override
    public List<Byte> encode() {
        throw new RuntimeException("不需要实现此方法");
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

    @Override
    public String toString() {
        return "CloseBindCardRep{" +
                "responseCode=" + responseCode +
                '}';
    }
}
