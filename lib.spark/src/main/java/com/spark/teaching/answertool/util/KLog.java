package com.spark.teaching.answertool.util;

import android.util.Log;

public class KLog {


    public static void d(String TAG, String str)
    {
        Log.d(TAG,str);
    }
    public static void i(String TAG, String str,boolean t)
    {
        Log.i(TAG,str);
    }


    public static void  throwable(Throwable e)
    {
        Log.d("klog",e.getMessage());
    }
}
