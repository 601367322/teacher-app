package com.spark.teaching.answertool.usb.model;

import java.util.List;

/**
 * @author hao.chen 【jikechenhao.163.com】
 * @Description:
 * @date 2018年03月23日 14:03
 */
public abstract class DataPackage {


    public abstract List<Byte> encode();
    public abstract void decoding(byte[] data);

    protected byte cmd;
    public byte getCmd() {
        return cmd;
    }

    public void setSeq(byte seq) {
        this.seq = seq;
    }

    private byte seq;
    public byte getSeq(){
        return this.seq;
    }
}
