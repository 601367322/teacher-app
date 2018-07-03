package com.prance.teacher.utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.blankj.utilcode.util.Utils;

public class MetaUtil {

    /**
     * 获取在AndroidManifest中的某个mate值
     *
     * @param metaKey
     * @return
     */
    public static String getMetaValue(String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai =
                    Utils.getApp().getPackageManager().getApplicationInfo(Utils.getApp().getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.get(metaKey).toString();
            }
        } catch (Exception e) {

        }
        return apiKey;
    }
}
