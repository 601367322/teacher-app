package com.spark.teaching.answertool.usb.model;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * @author hao.chen 【jikechenhao.163.com】
 * @Description: 上报刷卡绑定结果指令
 * @date 2018年03月23日 12:20
 */
public class ReportBindCard extends DataPackage{

    private Long uid;
    private Long pre_uid;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getPre_uid() {
        return pre_uid;
    }

    public void setPre_uid(Long pre_uid) {
        this.pre_uid = pre_uid;
    }

    @Override
    public List<Byte> encode() {
        throw new RuntimeException("不需要实现此方法");
    }

    @Override
    public void decoding(byte[] data) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        this.uid = byteToInt(byteBuffer);
        this.pre_uid = byteToInt(byteBuffer);
    }

    private long byteToInt(ByteBuffer byteBuffer) {
        byte[] tmps = new byte[4];
        byteBuffer.get(tmps,0,4);
        long result = 0;
        for (int i = tmps.length - 1; i >= 0; i--){
            result += ((long) tmps[i]&0xff) << (i*8) ;
        }
        return result;
    }

    @Override
    public String toString() {
        return "ReportBindCard{" +
                "uid=" + uid +
                ", pre_uid=" + pre_uid +
                '}';
    }
}
