package com.spark.teaching.answertool.usb.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hao.chen 【jikechenhao.163.com】
 * @Description: 查询设备配置信息指令
 * @date 2018年03月23日 12:17
 */
public class DeviceConfigure extends DataPackage{

    public DeviceConfigure(){
        this.cmd = (byte)0x13;
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
