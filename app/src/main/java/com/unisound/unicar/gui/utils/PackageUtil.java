/**
 * Copyright (c) 2012-2013 Yunzhisheng(Shanghai) Co.Ltd. All right reserved.
 * 
 * @FileName : PackageUtil.java
 * @ProjectName : Voizard
 * @PakageName : cn.yunzhisheng.voizard.utils
 * @Author : Brant
 * @CreateDate : 2013-5-29
 */
package com.unisound.unicar.gui.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;

/**
 * @Module : 隶属模块名
 * @Comments : 描述
 * @Author : Brant
 * @CreateDate : 2013-5-29
 * @ModifiedBy : Brant
 * @ModifiedDate: 2013-5-29
 * @Modified: 2013-5-29: 实现基本功能
 */
public class PackageUtil {
    @SuppressWarnings("unused")
    private static final String TAG = "PackageUtil";

    public static List<String> getLauncherPackages(Context context) {
        List<String> packages = new ArrayList<String>();
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo =
                packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo info : resolveInfo) {
            packages.add(info.activityInfo.packageName);
        }
        return packages;
    }

    public static String getCurrentTasks(Context context) {
        String currentTask = "";
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RecentTaskInfo> recentTasks = am.getRecentTasks(1, 0);
        for (RecentTaskInfo info : recentTasks) {
            currentTask = info.baseIntent.getComponent().getPackageName();
            break;
        }
        return currentTask;
    }

    public static boolean isHome(Context context, List<String> launchers) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            List<RunningTaskInfo> rti = activityManager.getRunningTasks(1);
            if (launchers != null && launchers.size() > 0 && rti != null && rti.size() > 0) {
                return launchers.contains(rti.get(0).topActivity.getPackageName());
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static String getAppVersionName(Context context) {
        try {
            PackageInfo packageInfo =
                    context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static int getAppVersionCode(Context context) {
        try {
            PackageInfo packageInfo =
                    context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * 获取当前的栈顶的activity
     */
    public static String getTopActivityName(Context context) {
        String topActivityClassName = null;
        ActivityManager activityManager =(ActivityManager) (context.getSystemService(android.content.Context.ACTIVITY_SERVICE));

        List<RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            ComponentName f = runningTaskInfos.get(0).topActivity;
            topActivityClassName = f.getClassName();
        }
        return topActivityClassName;
    }

    /**
     * 应用是否运行在前台
     */
    public static boolean isRunningForeground(Context context) {
        String topActivityClassName = getTopActivityName(context);
        if (topActivityClassName != null&& topActivityClassName.startsWith("com.unisound.unicar.gui")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 应用是否运行在前台
     */
    public static boolean isAppRunningForeground(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        String topActivityClassName = getTopActivityName(context);
        // Logger.d(TAG,
        // "isAppRunningForeground---topActivityClassName = "+topActivityClassName);
        if (topActivityClassName != null && topActivityClassName.startsWith(packageName)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * is App Running
     * 
     * @author xiaodong.he
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppRunning(Context context, String packageName) {
        boolean isAppRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(500);
        for (RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(packageName)
                    && info.baseActivity.getPackageName().equals(packageName)) {
                Logger.d(TAG, "isAppRunning " + packageName + " is running.");
                isAppRunning = true;
                break;
            }
        }
        return isAppRunning;
    }

    /**
     * 
     * @author xiaodong.he
     * @date 20160310
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppRunning2(Context context, String packageName) {
        ActivityManager mActivityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> mActivitys = mActivityManager.getRunningAppProcesses();
        for (int i = 0; i < mActivitys.size(); i++) {
            RunningAppProcessInfo info = mActivitys.get(i);
            if (packageName.equals(info.processName)) {
                Logger.d(TAG, "isAppRunning2: " + packageName + " is Running.");
                return true;
            }
        }
        return false;
    }


    /**
     * is Need hide Float View XD added 20150810
     * 
     * @param context
     * @return
     */
    public static boolean isNeedHideFloatView(Context context) {
        String topActivityClassName = getTopActivityName(context);
        // Logger.d(TAG,
        // "!--->----isNeedHideFloatView-------topActivityClassName = "+topActivityClassName);
        if (topActivityClassName != null
                && (topActivityClassName.startsWith("com.unisound.unicar.gui")
                        || topActivityClassName.startsWith("com.android.phone")
                        || topActivityClassName.equals(GUIConfig.ACTIVITY_NAME_UNICARNAVI_ROUTE) || topActivityClassName
                            .equals(GUIConfig.ACTIVITY_NAME_BLUETOOTH_CALL))) {
            return true;
        } else {
            return false;
        }
    }

    // public final static boolean isScreenLocked(Context c) {
    // KeyguardManager mKeyguardManager = (KeyguardManager)
    // c.getSystemService(Context.KEYGUARD_SERVICE);
    // return !mKeyguardManager.inKeyguardRestrictedInputMode();
    // }

    /**
     * add by ch 判断是否安装了某个apk 参数：包名
     */
    @SuppressWarnings("unused")
    public static boolean isAppInstalled(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }
}
