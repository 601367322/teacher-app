package com.spark.teaching.answertool.usb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hao.chen 【jikechenhao.163.com】
 * @Description: 答题器回显指令
 * @date 2018年03月23日 12:14
 */
public class Echo extends DataPackage {

    public Echo() {
        this.cmd = (byte) 0x03;
    }

    private Long uid;
    private String show_info; // 注意不要用中文标点符号

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getShow_info() {
        return show_info;
    }

    public void setShow_info(String show_info) {
        this.show_info = show_info;
    }

    @Override
    public List<Byte> encode() {
        List<Byte> list = new ArrayList<Byte>();
        list.addAll(intToByte4(this.uid.intValue()));
        String jineiMa = getJineiMa(this.show_info);
        for (int i = 0; i < jineiMa.length(); i++) {
            if (i % 2 == 1) {
                Integer tmp = 0;
                if (jineiMa.charAt(i - 1) >= 'A' && jineiMa.charAt(i - 1) <= 'Z') {
                    tmp += ((jineiMa.charAt(i - 1) - 'A') + 10) * 16;
                } else if (jineiMa.charAt(i - 1) >= '0' && jineiMa.charAt(i - 1) <= '9') {
                    tmp += (jineiMa.charAt(i - 1) - '0') * 16;
                }
                if (jineiMa.charAt(i) >= 'A' && jineiMa.charAt(i) <= 'Z') {
                    tmp += ((jineiMa.charAt(i) - 'A') + 10);
                } else if (jineiMa.charAt(i) >= '0' && jineiMa.charAt(i) <= '9') {
                    tmp += (jineiMa.charAt(i) - '0');
                }
                list.add((byte) (tmp & 0xff));
            }
        }
        while (list.size() < 52) {
            list.add((byte) 0);
        }
        return list;
    }

    @Override
    public void decoding(byte[] data) {
        throw new RuntimeException("不需要实现此方法");
    }

    private List<Byte> intToByte4(int i) {
        List<Byte> targets = new ArrayList<Byte>();
        targets.add((byte) (i & 0xFF));
        targets.add((byte) (i >> 8 & 0xFF));
        targets.add((byte) (i >> 16 & 0xFF));
        targets.add((byte) (i >> 24 & 0xFF));
        return targets;
    }

    /**
     * 获取机内码
     *
     * @param chineseName
     * @return 汉字的机内码  String类型
     */
    private String getJineiMa(String chineseName) {
        StringBuffer sb = new StringBuffer();
        try {
            char[] ch = chineseName.toCharArray();
            for (char c : ch) {
                if (isCharacter(String.valueOf(c))) {
                    if (sb.length() / 2 % 16 == 15) { // 要换行了，一个字节的位置放不下中文，不然会乱码
                        byte b = (byte) ' ';
                        sb.append(Integer.toHexString(b & 0xff));
                    }
                    byte[] by = String.valueOf(c).getBytes("GBK");
                    for (byte b : by) {
                        sb.append(Integer.toHexString(b & 0xff));
                    }
                } else {
                    byte b = (byte) c;
                    sb.append(Integer.toHexString(b & 0xff));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String str = sb.toString().toUpperCase().trim();
        if (str.length() > 96) { // 最多显示3行
            str = str.substring(0, 96);
        }
        return str;
    }

    /**
     * 判断str是否为汉字
     *
     * @param str 待检测值
     * @return true 是 false 不是
     */
    private boolean isCharacter(String str) {
        Pattern p_str = Pattern.compile("[\\u4e00-\\u9fa5]+");
        Matcher m = p_str.matcher(str);
        if (m.find() && m.group(0).equals(str)) {
            return true;
        }
        return false;
    }
}
