package com.prance.teacher.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SDCardUtils;
import com.leo.download.DownloadError;
import com.leo.download.DownloadListener;
import com.leo.download.DownloadManager;

import java.io.File;

/**
 * Created by bingbing on 2016/10/20.
 */

public class UpdateService extends Service {

    public static final String DIR = "app_update";
    public static final String DOWNLOAD_BROADCAST_CANCEL_ACTION = "com.prance.lib.update.UpdateService.DownloadBroadcast.cancel";
    public static final String DOWNLOAD_BROADCAST_INSTALL_ACTION = "com.prance.lib.update.UpdateService.DownloadBroadcast.install";
    private DownloadBroadcast broadcast;
    private String url;
    private int notifyId = 20002;
    private File downloadFile = null;

    public static final String DOWNLOAD_BROADCAST_ACTIVITY_ACTION_DISMISS = "com.prance.lib.update.DownloadAlertActivity.dismiss";
    public static final String DOWNLOAD_BROADCAST_ACTIVITY_ACTION_PROGRESS = "com.prance.lib.update.DownloadAlertActivity.progress";
    public static final String DOWNLOAD_BROADCAST_ACTIVITY_ACTION_ERROR = "com.prance.lib.update.DownloadAlertActivity.error";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent == null || intent.getExtras() == null) {
            return super.onStartCommand(intent, flags, startId);
        }
        String temp = intent.getExtras().getString("url");

        if (temp.equals(url)) {
            return super.onStartCommand(intent, flags, startId);
        } else {
            url = temp;
        }

        if (broadcast == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(DOWNLOAD_BROADCAST_CANCEL_ACTION);
            filter.addAction(DOWNLOAD_BROADCAST_INSTALL_ACTION);
            registerReceiver(broadcast = new DownloadBroadcast(), filter);
        }

        File parentFile = new File(SDCardUtils.getSDCardPathByEnvironment()).getParentFile();
        File updateDir = new File(parentFile, DIR);

        if (!updateDir.exists()) {
            updateDir.mkdirs();
        }

        DownloadManager.getInstance(getApplicationContext()).enquene(url, updateDir.getPath(), new DownloadListener() {

            long currentTime = 0;

            @Override
            public void onStart(int id, long size) {
                LogUtils.d("onStart");
                currentTime = System.currentTimeMillis();

            }

            @Override
            public void onProgress(int id, long currSize, long totalSize) {
                if (System.currentTimeMillis() - currentTime >= 1000) {
                    currentTime = System.currentTimeMillis();


                }
            }

            @Override
            public void onRestart(int id, long currSize, long totalSize) {
                LogUtils.d("onRestart");
            }

            @Override
            public void onPause(int id, long currSize) {
                LogUtils.d("onPause");
            }

            @Override
            public void onComplete(int id, String dir, String name) {
                LogUtils.d("onComplete");

                downloadFile = new File(dir, name);
                url = null;


                NotificationManagerCompat
                        .from(getApplicationContext()).cancel(notifyId);

                LocalBroadcastManager.getInstance(UpdateService.this).sendBroadcast(new Intent(DOWNLOAD_BROADCAST_ACTIVITY_ACTION_DISMISS));

                Intent cancelIntent = new Intent(DOWNLOAD_BROADCAST_INSTALL_ACTION);
                sendBroadcast(cancelIntent);
            }

            @Override
            public void onCancel(int id) {
                LogUtils.d("onCancel");
            }

            @Override
            public void onError(int id, DownloadError error) {
                LogUtils.d("onError");
                url = null;

                Intent progressIntent = new Intent(DOWNLOAD_BROADCAST_ACTIVITY_ACTION_ERROR);
                LocalBroadcastManager.getInstance(UpdateService.this).sendBroadcast(progressIntent);
            }
        });

        return super.onStartCommand(intent, 0, startId);
    }

    class DownloadBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtils.d(intent.getAction());

            try {
                if (intent.getAction().equals(DOWNLOAD_BROADCAST_CANCEL_ACTION)) {
                    DownloadManager.getInstance(context).cancel(url, null);
                    DownloadManager.getInstance(context).deleteRecord(url);
                    NotificationManagerCompat
                            .from(getApplicationContext()).cancel(notifyId);

                    LocalBroadcastManager.getInstance(UpdateService.this).sendBroadcast(new Intent(DOWNLOAD_BROADCAST_ACTIVITY_ACTION_DISMISS));

                    stopSelf();
                } else if (intent.getAction().equals(DOWNLOAD_BROADCAST_INSTALL_ACTION) && downloadFile != null) {
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    File apkFile = downloadFile;
                    //判断是否是AndroidN以及更高的版本
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName()+".fileprovider", apkFile);
                        install.setDataAndType(contentUri, "application/vnd.android.package-archive");
                    } else {
                        install.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                    }
                    context.startActivity(install);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            url = null;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (broadcast != null) {
            unregisterReceiver(broadcast);
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(DOWNLOAD_BROADCAST_ACTIVITY_ACTION_DISMISS));
    }
}
