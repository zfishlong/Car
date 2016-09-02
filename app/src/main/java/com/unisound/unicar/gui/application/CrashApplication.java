package com.unisound.unicar.gui.application;

import org.xutils.x;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.unisound.unicar.gui.preference.UserPerferenceUtil;
import com.unisound.unicar.gui.utils.LogcatHelper;

public class CrashApplication extends Application {
    public static LogcatHelper mLogcatHelper = null;
    private static Context appContext;
    private static long appStartTime;

    public static Context getAppContext() {
        return appContext;
    }

    public static long getAppStartTime() {
        return appStartTime;
    }

    public static long getTimeAfterAppStop() {
        return System.currentTimeMillis() - appStartTime;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appStartTime = System.currentTimeMillis();
        appContext = this;
        SDKInitializer.initialize(this);
        x.Ext.init(this);
        CrashHandler crashHandler = CrashHandler.getInstance();
        // 指定Crash时的处理程序
        crashHandler.setCrashHanler(getApplicationContext());

        mLogcatHelper = LogcatHelper.getInstance(this);
        if (UserPerferenceUtil.getLogcatEnable(this)) {
            if (!mLogcatHelper.isRunning()) {
                mLogcatHelper.start();
            }
        }
    }

}
