package com.spark.teaching.answertool.usb.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hao.chen 【jikechenhao.163.com】
 * @Description: 清除设备配置信息指令，这个指令不能调用，会影响信道
 * @date 2018年03月23日 12:18
 */
public class ClearDeviceConfigure extends DataPackage {

    public ClearDeviceConfigure() {
        this.cmd = (byte) 0x14;
    }

    @Override
    public List<Byte> encode() {
        List<Byte> list = new ArrayList<Byte>();
        return list;
    }

    @Override
    public void decoding(byte[] data) {
        throw new RuntimeException("不需要实现此方法");
    }

}
