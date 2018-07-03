package com.prance.teacher.lib.sunvote.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Elvis on 2017/8/8.
 * Email:Eluis@psunsky.com
 * Description:鏃ュ織绫?
 */

public class LogUtil {

    private static FileWriter fileWriter;

    public static final int VERBOSE_LEVER = 2;
    public static final int DEBUG_LEVER = 3;
    public static final int INFO_LEVER = 4;
    public static final int WARN_LEVER = 5;
    public static final int ERROR_LEVER = 6;
    public static final int ASSERT_LEVER = 7;

    public static int lever = VERBOSE_LEVER - 1 ;

    private static boolean logToFile = false;
    private static boolean logToLogcat = true ;

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");

    LogUtil() {
        throw new RuntimeException("Stub!");
    }

    public static int v(String tag, String msg) {
        if (VERBOSE_LEVER > lever) {
            if(logToLogcat){
                Log.v(tag, msg);
            }
            inputToFile("(V):" + msg );
        }
        return -1;
    }

    private static void init(){
        if(fileWriter == null){
            synchronized (LogUtil.class) {
                if(fileWriter == null) {
                    try {
                        File path = new File(Environment.getExternalStorageDirectory().getPath() + "/sunvote/");
                        if (!path.exists()) {
                            path.mkdirs();
                        }
                        File oldLogFile = new File(Environment.getExternalStorageDirectory().getPath()
                                + "/sunvote/udpprotocal.log");
                        oldLogFile.deleteOnExit();
                        File file = new File(Environment.getExternalStorageDirectory().getPath()
                                + "/sunvote/log" + simpleDateFormat.format(new Date())+".txt");
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        fileWriter = new FileWriter(file, true);

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        fileWriter = null;
                    }
                }
            }
        }
    }

    public static void enableLogToFile(){
        logToFile = true;
    }

    public static void disabelLogToFile(){
        logToFile = false;
    }

    public static void enableLogToLogcat(){
        logToLogcat = true;
    }

    public static void disableLogToLogcat(){
        logToLogcat = false;
    }

    public static void enableLog(){
        lever = VERBOSE_LEVER - 1;
    }

    public static void disableLog(){
        lever = ASSERT_LEVER ;
    }

    public static int v(String tag, String msg, Throwable tr) {
        if(VERBOSE_LEVER > lever){
            if(logToLogcat) {
                Log.v(tag, msg, tr);
            }
            inputToFile("(V):" + msg + Log.getStackTraceString(tr));
        }
        return -1;
    }

    public static int d(String tag, String msg) {
        if(DEBUG_LEVER > lever){
            if(logToLogcat) {
                Log.d(tag, msg);
            }
            inputToFile("(D):" + msg );
        }
        return -1;
    }

    public static int d(String tag, String msg, Throwable tr) {
        if(DEBUG_LEVER > lever){
            if(logToLogcat){
                Log.d(tag,msg,tr);
            }
            inputToFile("(D):" + msg + Log.getStackTraceString(tr));
        }
        return -1;
    }

    public static int i(String tag, String msg) {
        if(INFO_LEVER > lever){
            if(logToLogcat){
                Log.i(tag,msg);
            }
            inputToFile("(I):" + msg );
        }
        return -1;
    }

    public static int i(String tag, String msg, Throwable tr) {
        if(INFO_LEVER > lever){
            if(logToLogcat){
                Log.i(tag,msg,tr);
            }
            inputToFile("(I):" + msg + Log.getStackTraceString(tr));
        }
        return -1;
    }

    public static int i(String tag,byte[] msg){
        String msgStr = ByteUtils.bytesToHexString(msg);
        return i(tag,msgStr);
    }

    public static int i(String tag,String msgTag, byte[] msg){
        String msgStr = ByteUtils.bytesToHexString(msg);
        return i(tag,msgTag + ":\r\n" + msgStr);
    }

    public static int v(String tag,String msgTag, byte[] msg){
        String msgStr = ByteUtils.bytesToHexString(msg);
        return v(tag,msgTag + ":\r\n" + msgStr);
    }

    public static int i(String tag,String msgTag, byte[] msg,int length){
        String msgStr = ByteUtils.bytesToHexString(msg,length);
        return i(tag,msgTag + ":\r\n" + msgStr);
    }

    public static int v(String tag,String msgTag, byte[] msg,int length){
        String msgStr = ByteUtils.bytesToHexString(msg,length);
        return v(tag,msgTag + ":\r\n" + msgStr);
    }

    public static int i(String tag,byte[] msg,Throwable tr){
        String msgStr = ByteUtils.bytesToHexString(msg);
        return i(tag,msgStr,tr);
    }

    public static int i(String tag,String msgTag,byte[] msg,Throwable tr){
        String msgStr = ByteUtils.bytesToHexString(msg);
        return i(tag,msgTag + ":\r\n" + msgStr,tr);
    }

    public static int w(String tag, String msg) {
        if(WARN_LEVER > lever){
            if(logToLogcat){
                Log.w(tag,msg);
            }
            inputToFile("(V):" + msg);
        }
        return -1;
    }

    public static int w(String tag, String msg, Throwable tr) {
        if(WARN_LEVER > lever){
            if(logToLogcat){
                Log.w(tag,msg,tr);
            }
            inputToFile("(W):" + msg + Log.getStackTraceString(tr));
        }
        return -1;
    }

    public static boolean isLoggable(String s, int i){
        return Log.isLoggable(s,i);
    }

    public static int w(String tag, Throwable tr) {
        if(WARN_LEVER > lever){
            if(logToLogcat){
                Log.w(tag,tr);
            }
            inputToFile("(W):" + Log.getStackTraceString(tr));
        }
        return -1;
    }

    public static int e(String tag, String msg) {
        if(ERROR_LEVER > lever){
            if(logToLogcat){
                Log.e(tag,msg);
            }
            inputToFile("(E):" + msg);
        }
        return -1;
    }

    public static int e(String tag,Throwable tr){
        String message =  "ERROR" ;
        if(tr != null && tr.getMessage() != null){
            message = tr.getMessage();
        }
        return e(tag,message,tr);
    }

    public static int e(String tag, String msg, Throwable tr) {
        if(ERROR_LEVER > lever){
            if(logToLogcat){
                Log.e(tag,msg,tr);
            }
            inputToFile("(E):" + msg + Log.getStackTraceString(tr));
        }
        return -1;
    }

    public static int wtf(String tag, String msg) {
        if(ASSERT_LEVER > lever){
            if(logToLogcat){
                Log.wtf(tag,msg);
            }
            inputToFile("(WTF):" + msg);
        }
        return -1;
    }

    public static int wtf(String tag, Throwable tr) {
        if(ASSERT_LEVER > lever){
            if(logToLogcat){
                Log.wtf(tag,tr);
            }
            inputToFile("(WTF):" + Log.getStackTraceString(tr));
        }
        return -1;
    }

    public static int wtf(String tag, String msg, Throwable tr) {
       if(ASSERT_LEVER > lever){
           if(logToLogcat){
               Log.wtf(tag,msg,tr);
           }
           inputToFile("(WTF):" + msg + Log.getStackTraceString(tr));
       }
       return -1;
    }

    public static String getStackTraceString(Throwable tr) {
        return Log.getStackTraceString(tr);
    }


    public static void stack(){
        Throwable throwable = new Throwable();
        // 闇?瑕佸鐞員AG 瑕佽鍑轰笂闈lass method鐨勪俊鎭紝鍚庣画娣讳笂
        i("STACK",getStackTraceString(throwable));
    }

    private synchronized static void inputToFile(String msg){
        if(logToFile) {
            String time = simpleDateFormat.format(new Date());
            try {
                init();
                String log = time + "(" + Thread.currentThread().getName() + ",id=" + Thread.currentThread().getId() + ")" + msg + "\r\n";
                if(onLogMessage != null){
                    onLogMessage.onLog(time + ":" + msg + "\r\n");
                }
                fileWriter.write(log);
                fileWriter.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
                if(fileWriter != null){
                    try {
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                fileWriter = null;
            }
        }
    }

    private static OnLogMessage onLogMessage;

    public static void setOnLogMessage(OnLogMessage onLogMessage) {
        LogUtil.onLogMessage = onLogMessage;
    }

    public static interface OnLogMessage{
        void onLog(String log);
    }
}
