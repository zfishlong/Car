/**
 * Copyright (c) 2012-2015 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * 
 * @FileName : DeviceTool.java
 * @ProjectName : UnicarGUI
 * @PakageName : com.unisound.unicar.gui.utils
 * @Author :
 * @CreateDate : 2014-2-25
 */
package com.unisound.unicar.gui.utils;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.provider.Telephony;
import android.provider.Telephony.Sms;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


@SuppressLint("NewApi")
public class DeviceTool {
    public static final String TAG = "DeviceTool";

    private static final String INVALID_IMEI = "000000000000000";


    /**
     * 获取设备的id
     * @param context 上下文
     * @return
     */
    public static String getDeviceId(Context context) {
        String deviceId = getIMEI(context);
        return (deviceId == null || deviceId.equals("")) ? INVALID_IMEI : deviceId;
    }


    /**
     * 获取设备的IMEI号
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        String imei = "";
        imei =((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();

        if (imei != null && !"".equals(imei) && !imei.equals(INVALID_IMEI)) {
            return imei;
        }

        imei = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        if (imei != null && !"".equals(imei) && !imei.equals(INVALID_IMEI)) {
            return imei;
        }

        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        if (info != null) {
            return info.getMacAddress();
        }
        return INVALID_IMEI;

    }

    /**
     * 获取到设备的Mac地址
     * @param context
     * @return
     */
    public static String getMac(Context context) {
        if (context == null) {
            return INVALID_IMEI;
        }
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        if (info != null) {
            return info.getMacAddress();
        }
        return INVALID_IMEI;
    }

    /**
     * 获取到程序的完整名称
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageInfo packageInfo =context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取到程序的包名信息
     * @param context
     * @return
     */
    public static String getAppPackageName(Context context) {
        String packageName = "";
        try {
            PackageInfo packageInfo =context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            packageName = packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packageName;
    }


    /**
     * is UnicarService Installed
     * 
     * @param context
     * @return
     */
    public static boolean isUnicarServiceInstalled(Context context) {
        return checkApkExist(context, GUIConfig.PACKAGE_NAME_UNICAR_VUI);
    }


    /**
     * check Apk Exist added by xiaodong 20150706
     * 
     * @param context
     * @param packageName
     * @return
     */
    @SuppressWarnings("unused")
    public static boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName)) return false;
        try {
            ApplicationInfo info =
                    context.getPackageManager().getApplicationInfo(packageName,
                            PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (NameNotFoundException e) {
            Logger.w(TAG, e.toString());
            return false;
        }
    }

    /**
     * 判断SDCard是否存在 [当没有外挂SD卡时，内置ROM也被识别为存在sd卡]
     * 
     * @return
     */
    public static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * SD 卡的空间是否满足某一个值
     * @param sizeMb : MIN size
     * @return
     */
    public static boolean isAvailableSDcardSpace(int sizeMb) {
        boolean isHasSpace = false;
        if (isSdCardExist()) {
            String sdcard = Environment.getExternalStorageDirectory().getPath();
            StatFs statFs = new StatFs(sdcard);
            long blockSize = statFs.getBlockSize();
            long blocks = statFs.getAvailableBlocks();
            long availableSpare = (blocks * blockSize) / (1024 * 1024);
            Logger.d(TAG, "SDCard availableSpare = " + availableSpare + " MB");
            if (availableSpare > sizeMb) {
                isHasSpace = true;
            }
        } else {
            Logger.e(TAG, "No sdcard!");
        }
        return isHasSpace;
    }




    /**
     * 内部内存是否可用
     * @param sizeMb: MIN limit size
     * @return
     */
    public static boolean isAvailableInternalMemory(int sizeMb) {
        long availableSpare = getAvailableInternalMemorySize();
        if (availableSpare > sizeMb) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取设备的内部可用内存
     * @return MB
     */
    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        long availableSpare = (availableBlocks * blockSize) / (1024 * 1024);
        Logger.d(TAG, "Device Available InternalMemorySize = " + availableSpare + " MB");
        return availableSpare;
    }


    /**
     * @return 版本号
     * 10 Android 2.3.3-2.3.7;
     * 13 Android 3.2;
     * 14 Android 4.0;
     * 19 Android 4.4 KitKat
     */
    public static int getDeviceSDKVersion() {
        int sdkVersion = Build.VERSION.SDK_INT;// Integer.parseInt(Build.VERSION.SDK);
        return sdkVersion;
    }

    /**
     * 获取屏幕的显示信息
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        Resources resources = context.getResources();
        return resources.getDisplayMetrics();
    }

    /**
     * 获取屏幕的高度
     * @param context
     * @return
     */
    public static int getScreenHight(Context context) {
        DisplayMetrics dm = getDisplayMetrics(context);
        return dm.heightPixels;
    }

    /**
     * 获取屏幕的宽度
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = getDisplayMetrics(context);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕的密度
     * @param context
     * @return
     */
    public static float getScreenDensity(Context context) {
        DisplayMetrics dm = getDisplayMetrics(context);
        return dm.density;
    }

    /**
     * 获取状态栏的高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 屏幕是否是横屏
     * @param context
     * @return
     */
    public static boolean isScreenLandscape(Context context) {
        int orientation = context.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return true;
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            return false;
        }
        return false;
    }


    /**
     * 获取默认的短信程序名称
     * @param context 上下文
     * @return
     */
    public static String getDefaultSmsAppName(Context context) {
        String smsApp = Telephony.Sms.getDefaultSmsPackage(context);
        Logger.d(TAG, "!--->getDefaultSmsAppName:" + smsApp);
        return smsApp;
    }


    /**
     * 改变默认的短信应用
     * @param context
     * @param packageName
     */
    public static void changeDefaultSMSApp(Context context, String packageName) {
        Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Sms.Intents.EXTRA_PACKAGE_NAME, packageName);
        context.startActivity(intent);
    }

    /**
     * change GUI To Default Sms App for Android 4.4
     * 
     * @param context
     */
    public static void changeGUIToDefaultSmsApp(Context context) {
        Logger.d(TAG, "!--->changeGUIToDefaultSmsApp-----");
        if (getDeviceSDKVersion() < 19) {
            Logger.d(TAG,"!---> DeviceSDKVersion lesstion than 19, no neeed changeGUIToDefaultSmsApp");
            return;
        }

        String pkgName = context.getPackageName();
        if (!pkgName.equals(DeviceTool.getDefaultSmsAppName(context))) {
            Logger.d(TAG, "!--->UnicarGUI is not default SMS app, change to default SMS app.");
            DeviceTool.changeDefaultSMSApp(context, pkgName);
        }
    }

    /**
     * reset default Sms App for Android 4.4
     * 
     * @param context
     */
    public static void resetDefaultSmsApp(Context context) {
        Logger.d(TAG, "!--->resetDefaultSmsApp-----");
        if (getDeviceSDKVersion() < 19) {
            Logger.d(TAG, "!---> DeviceSDKVersion lesstion than 19, no neeed resetDefaultSmsApp.");
            return;
        }

        String defaultSMSPkgName = "com.android.contacts";
        if (!defaultSMSPkgName.equals(DeviceTool.getDefaultSmsAppName(context))) {
            Logger.d(TAG, "!--->---" + defaultSMSPkgName
                    + " is not default SMS app, change to default SMS app.");
            DeviceTool.changeDefaultSMSApp(context, defaultSMSPkgName);
        }
    }

    /* XD added 20150826 for default SMS App End > */


    /**
     * 控制某一个editText上键盘输入是否可用
     * @param editText
     * @param isShow
     */
    public static void showEditTextKeyboard(EditText editText, boolean isShow) {
        Logger.d(TAG, "showKeyboard isShow = " + isShow);
        if (null == editText) {
            return;
        }
        InputMethodManager imm =
                (InputMethodManager) editText.getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        if (isShow) {
            imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED); // show
        } else {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0); // hide
        }
    }

}
