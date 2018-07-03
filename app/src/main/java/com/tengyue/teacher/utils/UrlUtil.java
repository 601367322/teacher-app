package com.tengyue.teacher.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.blankj.utilcode.util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by shenbingbing on 16/4/18.
 */
public class UrlUtil {

    /**
     * 获取Assets目录下prance.properties中的属性
     *
     * @param key
     * @return
     */
    public static String getAssetsProperties(String key) {

        String result = "";
        try {
            Properties properties = new Properties();
            InputStream is = Utils.getApp().getAssets().open(getPathName());
            properties.load(is);
            if (properties != null) {
                if (properties.containsKey(key)) {
                    result = properties.get(key).toString();
                }
            }
        } catch (Exception e) {
            LogUtils.d("getAssetsProperties e[" + e + "]");
        }
        return result;
    }

    /**
     * 读取本地SD卡文件下prance.properties中的属性
     *
     * @param
     * @return
     */
    private static String getSDcardProperties(String key) {

        String result = "";
        try {
            Properties properties = new Properties();
            InputStream is = new FileInputStream(getBaseDir(Utils.getApp()) + getPathName());
            properties.load(is);
            if (properties != null) {
                if (properties.containsKey(key)) {
                    result = properties.get(key).toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 初始化 prance.properties中的属性
     *
     * @param key
     * @return
     */
    public static String getPropertiesValue(String key) {

        String result = "";

        boolean settingopen = false;
        try {
            ApplicationInfo appInfo = Utils.getApp().getPackageManager()
                    .getApplicationInfo(Utils.getApp().getPackageName(),
                            PackageManager.GET_META_DATA);
            String msg = appInfo.metaData.getString("TEST_SETTING");
            if (!TextUtils.isEmpty(msg) && msg.equals("TEST_SETTING")) {
                settingopen = true;
            } else {
                settingopen = false;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (checkSaveLocationExists() && fileIsExists(getBaseDir(Utils.getApp()) + getPathName()) && settingopen) {
            result = getSDcardProperties(key);

            if (result.equals("")) {
                result = getAssetsProperties(key);
            }
        } else {
            result = getAssetsProperties(key);
        }
        return result;
    }


    /**
     * 判断文件是否存在
     *
     * @return
     */
    public static boolean fileIsExists(String pathName) {
        try {
            File f = new File(pathName);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {

            return false;
        }
        return true;
    }

    /**
     * 初始属性的文件名
     *
     * @return
     */
    public static String getPathName() {
        return "prance.properties";
    }

    /**
     * 检查是否安装SD卡
     *
     * @return
     */
    public static boolean checkSaveLocationExists() {
        String sDCardStatus = Environment.getExternalStorageState();
        boolean status;
        if (sDCardStatus.equals(Environment.MEDIA_MOUNTED)) {
            status = true;
        } else
            status = false;
        return status;
    }

    /**
     * 初始化文件保存基本目录
     *
     * @param context
     * @return
     */
    public static String getBaseDir(Context context) {
        return SDCardUtils.getSDCardPaths() + "/tengyue";
    }

    private static int analyticsDebugModel = -1;

    /**
     * 获取埋点的开关
     *
     * @param context
     * @return
     */
    public static int getAnalyticsDebugModel(Context context) {

        if (analyticsDebugModel != -1) {
            return analyticsDebugModel;
        }

        int debugModel = Integer.valueOf(UrlUtil.getPropertiesValue("debug_track"));

        try {
            ApplicationInfo appInfo = Utils.getApp().getPackageManager()
                    .getApplicationInfo(Utils.getApp().getPackageName(),
                            PackageManager.GET_META_DATA);
            String analyticsOpen = appInfo.metaData.getString("AnalyticsOpen");
            if (analyticsOpen.equals("false")) {
                debugModel = 1;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return debugModel;
    }
}
