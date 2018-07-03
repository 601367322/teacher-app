package com.prance.teacher.utils;

/**
 * Created by bingbing on 2016/10/15.
 */

public class ModelUtil {

    /**
     * 是否是测试模式
     *
     * @return
     */
    public static boolean isTestModel() {
        try {
            String msg = MetaUtil.getMetaValue(Constants.TEST_SETTING);
            if (msg.equals(Constants.OPEN)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
