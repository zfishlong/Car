/**
 * Copyright (c) 2012-2015 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : BaiduNaviUriApi.java
 * @ProjectName : uniCarSolution_oneshot
 * @PakageName : com.unisound.unicar.gui.route.baidu
 * @version : 1.0
 * @Author : Xiaodong.He
 * @CreateDate : 2015-12-18
 */
package com.unisound.unicar.gui.route.baidu;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

import com.unisound.unicar.gui.utils.Logger;

/**
 * BaiduNaviUriApi
 * 
 * work with Baidu Navi app: com.baidu.navi.apk
 * 
 * @author xiaodong.he
 * @date 2015-12-18
 */
public class BaiduNaviUriApi {

    public static final String TAG = BaiduNaviUriApi.class.getSimpleName();

    /**
     * showRoute with Baidu Navi app: com.baidu.navi.apk
     * 
     * @param toLat latitude 纬度
     * @param toLng longitude 经度
     * @param toCity
     * @param toPoi
     */
    public static void showRoute(Context context, double toLat, double toLng, String toCity,
            String toPoi) {
        String uriStr =
                "bdnavi://plan?coordType=wgs84ll&src=" + getBaiduAppKey(context) + "&dest=" + toLat
                        + "," + toLng + "," + toCity + "&strategy=10";
        Logger.d(TAG, "showRoute uri=" + uriStr);
        startBaiduNavi(context, uriStr);
    }

    /**
     * goHome
     * 
     * @param context
     */
    public static void goHome(Context context) {
        String uri = "bdnavi://gohome?src=" + getBaiduAppKey(context);
        startBaiduNavi(context, uri);
    }

    /**
     * goComplay
     * 
     * @param context
     */
    public static void goComplay(Context context) {
        String uri = "bdnavi://gocompany?src=" + getBaiduAppKey(context);
        startBaiduNavi(context, uri);
    }

    /**
     * 
     * @param context
     * @param uriStr
     */
    private static void startBaiduNavi(Context context, String uriStr) {
        Logger.d(TAG, "startBaiduNavi uriStr=" + uriStr);
        Uri uri = Uri.parse(uriStr);
        Intent intent = new Intent("com.baidu.navi.action.START", uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * getBaiduAppKey
     * 
     * @param context
     * @return
     */
    public static String getBaiduAppKey(Context context) {
        String appKey = "";
        try {
            ApplicationInfo appInfo =
                    context.getPackageManager().getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            appKey = appInfo.metaData.getString("com.baidu.lbsapi.API_KEY");
            Logger.d(TAG, "getBaiduAppKey--appKey = " + appKey);
        } catch (NameNotFoundException e) {
            Logger.e(TAG,
                    "getBaiduAppKey from AndroidManifest.xml error, no key com.baidu.lbsapi.API_KEY found.");
        }
        return appKey;
    }
}
