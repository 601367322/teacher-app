package com.spark.teaching.answertool.usb.model;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author hao.chen 【jikechenhao.163.com】
 * @Description: 答题器答案上报指令
 * @date 2018年03月23日 12:12
 */
public class ReceiveAnswer  extends DataPackage{

    private byte rssi; // 当前指令的RSSI值
    private Date time; // 当前指令接收器时间
    private Long uid; // 答题器的设备ID
    private Long press_cnt; // 物理按压次数（支持掉电保存）
    private Long key_cnt; // 有效按键次数（支持掉电保存）
    private Long send_cnt; // 有效按键时有效发送次数（支持掉电保存）
    private Long echo_cnt; // 答题器回显接收次数（支持掉电保存）
    private byte type; // 题目类型
    private String answer; // 答案共16个字节，表示答题器提交的答案的内容，不足的补0

    public byte getRssi() {
        return rssi;
    }

    public void setRssi(byte rssi) {
        this.rssi = rssi;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getPress_cnt() {
        return press_cnt;
    }

    public void setPress_cnt(Long press_cnt) {
        this.press_cnt = press_cnt;
    }

    public Long getKey_cnt() {
        return key_cnt;
    }

    public void setKey_cnt(Long key_cnt) {
        this.key_cnt = key_cnt;
    }

    public Long getSend_cnt() {
        return send_cnt;
    }

    public void setSend_cnt(Long send_cnt) {
        this.send_cnt = send_cnt;
    }

    public Long getEcho_cnt() {
        return echo_cnt;
    }

    public void setEcho_cnt(Long echo_cnt) {
        this.echo_cnt = echo_cnt;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public List<Byte> encode() {
        return null;
    }

    @Override
    public void decoding(byte[] data) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        this.rssi = byteBuffer.get();
        String timeStr = "";
        for (int i = 0; i < 9; i++){
            byte value = byteBuffer.get();
            timeStr += value/16;
            timeStr += value%16;
        }
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        try {
            this.time = dateFormater.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.uid = byteToInt(byteBuffer);
        this.press_cnt = byteToInt(byteBuffer);
        this.key_cnt = byteToInt(byteBuffer);
        this.send_cnt = byteToInt(byteBuffer);
        this.echo_cnt = byteToInt(byteBuffer);
        this.type = byteBuffer.get();
        this.answer = "";
        for (int i = 0; i < 16; i++){
            byte value = byteBuffer.get();
            if (value != 0){
                switch (value){
                    case 1:
                        this.answer += "A";
                        break;
                    case 2:
                        this.answer += "B";
                        break;
                    case 3:
                        this.answer += "C";
                        break;
                    case 4:
                        this.answer += "D";
                        break;
                    case 5:
                        this.answer += "E";
                        break;
                    case 6:
                        this.answer += "F";
                        break;
                    case 7:
                        this.answer += "H";
                        break;
                }
            }else {
                break;
            }
        }
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
        return "ReceiveAnswer{" +
                "rssi=" + rssi +
                ", time=" + time +
                ", uid=" + uid +
                ", press_cnt=" + press_cnt +
                ", key_cnt=" + key_cnt +
                ", send_cnt=" + send_cnt +
                ", echo_cnt=" + echo_cnt +
                ", type=" + type +
                ", answer='" + answer + '\'' +
                '}';
    }
}
