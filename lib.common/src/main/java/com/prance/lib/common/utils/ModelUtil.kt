package com.prance.lib.common.utils

/**
 * Created by bingbing on 2016/10/15.
 */

object ModelUtil {

    /**
     * 是否是测试模式
     *
     * @return
     */
    val isTestModel: Boolean
        get() {
            try {
                val msg = MetaDataUtil.getMetaDataInApp(Constants.TEST_SETTING)
                return msg == Constants.OPEN
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return false
        }
}
