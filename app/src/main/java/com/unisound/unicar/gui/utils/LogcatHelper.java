package com.unisound.unicar.gui.utils;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * 日志的记录 -->这玩意一般在调试的时候用到
 * 上线时候应该去掉否则太消耗性能了
 */
public class LogcatHelper {

    private static LogcatHelper INSTANCE = null;
    private static String PATH_LOGCAT;
    private int mPId;
    private LogDumper mLogDumper;

    private LogcatHelper(Context context) { //私有构造函数
        init(context);                      //初始化目录
        mPId = android.os.Process.myPid();
    }

    public static LogcatHelper getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LogcatHelper(context);
        }
        return INSTANCE;
    }


    /**
     * 初始化目录
     */
    public void init(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中
            PATH_LOGCAT =
                    Environment.getExternalStorageDirectory().getAbsolutePath() +
                            "/YunZhiSheng/logcat/uniCarSolution/";
        } else {                        // 如果SD卡不存在，就保存到本应用的目录下
            PATH_LOGCAT =
                    context.getFilesDir().getAbsolutePath() + File.separator +
                            "YunZhiSheng/logcat/uniCarSolution/";
        }

        File file = new File(PATH_LOGCAT);
        if (!file.exists()) {
            file.mkdirs();
        }
    }


    //开始
    public synchronized void start() {
        stop();
        mLogDumper = new LogDumper(String.valueOf(mPId), PATH_LOGCAT);
        mLogDumper.start();
    }

    //停止
    public synchronized void stop() {
        if (mLogDumper != null) {
            mLogDumper.stopLogs();
            mLogDumper = null;
        }
    }

    //是否在运行
    public boolean isRunning() {
        if (mLogDumper != null) {
            return mLogDumper.getThreadState();
        } else {
            return false;
        }
    }

    //log
    private class LogDumper extends Thread {
        private Process logcatProc;
        private BufferedReader mReader = null;
        private boolean mRunning = true;
        String cmds = null;
        private String mPID;
        private FileOutputStream out = null;


        public LogDumper(String pid, String dir) {
            try {
                mPID = pid;
                File file = new File(dir + "/" + FileHelper.generateFileName(".log"));
                if (!file.exists()) {
                    file.createNewFile();
                }
                out = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            cmds = "logcat -v time | grep " + mPID;
        }

        public void stopLogs() {
            mRunning = false;
        }

        public boolean getThreadState() {
            return mRunning;
        }

        @Override
        public void run() {
            try {
                logcatProc = Runtime.getRuntime().exec(cmds);
                mReader =
                        new BufferedReader(new InputStreamReader(logcatProc.getInputStream()), 1024);
                String line = null;
                while (mRunning && (line = mReader.readLine()) != null) {
                    if (!mRunning) {
                        break;
                    }
                    if (line.length() == 0) {
                        continue;
                    }
                    if (out != null && line.contains(mPID)) {
                        out.write((line + "\n").getBytes());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (logcatProc != null) {
                    logcatProc.destroy();
                    logcatProc = null;
                }
                if (mReader != null) {
                    try {
                        mReader.close();
                        mReader = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    out = null;
                }
            }
        }
    }
}
