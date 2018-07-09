package com.prance.lib.sunvote.features;

import java.util.List;

 

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;

import com.prance.lib.sunvote.utils.LogUtil;


public class App extends Application {
	//public static XPadApi XPad;
	private static final String TAG = "App";
	
	@Override
	public void onCreate() {

		super.onCreate();

		//XPad = XPadApi.getInstance();
		LogUtil.enableLogToFile();
		final Thread.UncaughtExceptionHandler uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
				LogUtil.e(TAG,"carsh(thread id:" + thread.getId() + ",thread name:" + thread.getName() + ")");
				LogUtil.e(TAG,ex);
				if(uncaughtExceptionHandler != null){
					uncaughtExceptionHandler.uncaughtException(thread,ex);
				}
				System.exit(0);
			}
		});
	}
	
	public static boolean isBackground(Context context) {
	    ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
	    for (RunningAppProcessInfo appProcess : appProcesses) {
	         if (appProcess.processName.equals(context.getPackageName())) {
	                if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
	                         // Log.i("��̨", appProcess.processName);
	                          return true;
	                }else{
	                         // Log.i("ǰ̨", appProcess.processName);
	                          return false;
	                }
	           }
	    }
	    return false;
	}
}
