package com.spark.teaching.answertool.usb.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hao.chen 【jikechenhao.163.com】
 * @Description: 接收器设置信道指令
 * @date 2018年03月23日 12:15
 */
public class Channel extends DataPackage{

    public Channel(){
        this.cmd = (byte)0x11;
    }


    private byte channel;

    public byte getChannel() {
        return channel;
    }

    public void setChannel(byte channel) {
        this.channel = channel;
    }

    @Override
    public List<Byte> encode() {
        List<Byte> list = new ArrayList<Byte>();
        list.add(channel);
        return list;
    }

    @Override
    public void decoding(byte[] data) {
        throw new RuntimeException("不需要实现此方法");
    }
}
