package com.spark.teaching.answertool.usb.model;

import java.util.List;

/**
 * @author hao.chen 【jikechenhao.163.com】
 * @Description: 查询设备配置信息指令响应
 * @date 2018年03月23日 12:17
 */
public class DeviceConfigureRep extends DataPackage{
    private Integer uid;
    private String sw;
    private String hw;
    private String com;
    private byte ch;
    private byte tx_power;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getSw() {
        return sw;
    }

    public void setSw(String sw) {
        this.sw = sw;
    }

    public String getHw() {
        return hw;
    }

    public void setHw(String hw) {
        this.hw = hw;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public byte getCh() {
        return ch;
    }

    public void setCh(byte ch) {
        this.ch = ch;
    }

    public byte getTx_power() {
        return tx_power;
    }

    public void setTx_power(byte tx_power) {
        this.tx_power = tx_power;
    }

    @Override
    public List<Byte> encode() {
        throw new RuntimeException("不需要实现此方法");
    }

    @Override
    public void decoding(byte[] data) {

    }

    @Override
    public String toString() {
        return "DeviceConfigureRep{" +
                "uid=" + uid +
                ", sw='" + sw + '\'' +
                ", hw='" + hw + '\'' +
                ", com='" + com + '\'' +
                ", ch=" + ch +
                ", tx_power=" + tx_power +
                '}';
    }
}
