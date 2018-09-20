package com.spark.teaching.answertool.usb.constrant;

/**
 * @author hao.chen 【jikechenhao.163.com】
 * @Description:
 * @date 2018年03月23日 17:04
 */
public class QuestionTypeConstrant {

    /**
     * TYPE:题目类型
     * 0x01:单题单选
     * 0x02:是非判断
     * 0x03:抢红包
     * 0x04:单题多选
     * 0x05:多题单选
     * 0x06:通用数据类型（所有按键都可以按，按下之后立刻提交）
     * 0x07:6键单选题（对错键复用EF键）
     * <p>
     * 0x80:停止作答（类型为0x80之后的为控制指令） 答题器接收到此指令之后，会关闭提交答案功能
     */

    public final static byte SINGLE_QUESTION_SINGLE_SELECTION = 0x01;
    public final static byte NON_JUDGMENT = 0x02;
    public final static byte RED_PACKETS = 0x03;
    public final static byte SINGLE_QUESTION_MULTIPLE_SELECTION = 0x04;
    public final static byte MULTIPLE_QUESTION_SINGLE_SELECTION = 0x05;
    public final static byte General = 0x06;
    public final static byte SIX_SINGLE_SELECTION = 0x07;
    public final static byte STOP_ANSWER = (byte) 0x80;
}
