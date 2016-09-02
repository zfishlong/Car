/**
 * Copyright (c) 2012-2015 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : GaodeMap.java
 * @ProjectName : uniCarSolution
 * @PakageName : com.unisound.unicar.gui.route.operation
 * @version : v1.4
 * @Author : Alieen
 * @CreateDate : 2015-07-08
 * @ModifiedBy : xiaodong.he
 * @ModifiedDate: 2016-01-06
 */
package com.unisound.unicar.gui.route.operation;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.Toast;

import com.unisound.unicar.gui.R;
import com.unisound.unicar.gui.uninavi.UniCarNaviUtil;
import com.unisound.unicar.gui.utils.DeviceTool;
import com.unisound.unicar.gui.utils.GUIConfig;
import com.unisound.unicar.gui.utils.Logger;

/**
 * 
 * @CreateDate : 2015-07-08
 * @ModifiedBy : xiaodong.he
 * @ModifiedDate: 2016-01-06
 */
public class GaodeMap {
    public static final String TAG = "GaodeMap";
    public static final String ROUTE_MODE_WALKING = "walking"; // 步行
    public static final String ROUTE_MODE_DRIVING = "driving"; // 驾车
    public static final String ROUTE_MODE_TRANSIT = "transit"; // 公交
    private static boolean mHasGaodeMapClient = false;
    private static boolean mHasGaodeNavClient = false;

    /**
     * 
     * @param context
     * @return
     */
    public static boolean hasUniCarNaviClient(Context context) {
        return DeviceTool.checkApkExist(context, GUIConfig.PACKAGE_NAME_UNICAR_NAVI);
    }

    public static boolean hasGaodeMapClient(Context context) {
        return DeviceTool.checkApkExist(context, "com.autonavi.minimap");
    }

    public static boolean hasGaodeNavClient(Context context) {
        PackageInfo packageInfo;
        try {
            packageInfo =
                    context.getPackageManager().getPackageInfo("com.autonavi.xmgd.navigator", 0);
        } catch (NameNotFoundException e) {
            packageInfo = null;
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @deprecated
     */
    private static void showRoute(Context context, String mode, double toLat, double toLng,
            String toCity, String toPoi, int style) {
        if (context == null) {
            Logger.e(TAG, "showRoute:context null!");
            return;
        }

        mHasGaodeMapClient = hasGaodeMapClient(context);
        mHasGaodeNavClient = hasGaodeNavClient(context);
        if (mHasGaodeNavClient) {
            GaodeUriApi.startNavi2(context, toLat, toLng, toPoi, style, 0);
        } else if (mHasGaodeMapClient) {
            GaodeUriApi.startNavi(context, toLat, toLng, toPoi, style, 0);
        } else {
            Toast.makeText(context, R.string.gaode_nofind_map, Toast.LENGTH_LONG).show();
            // TODO:
            // GaodeMapSdk.showRoute(context, mode, fromLat, fromLng, "", "", toLat, toLng, toCity,
            // toPoi);
        }
    }

    /**
     * showRoute XD added 20180911
     * 
     * @param context
     * @param mode: "driving"
     * @param fromLat
     * @param fromLng
     * @param fromCity
     * @param fromPoi
     * @param toLat
     * @param toLng
     * @param toCity
     * @param toName
     * @param toAddress
     * @param style: route plan
     */
    public static void showRoute(Context context, String mode, double fromLat, double fromLng,
            String fromCity, String fromPoi, double toLat, double toLng, String toCity,
            String toName, String toAddress, int style) {
        if (context == null) {
            Logger.e(TAG, "showRoute--new--:context null!");
            return;
        }

        mHasGaodeMapClient = hasGaodeMapClient(context);
        mHasGaodeNavClient = hasGaodeNavClient(context);
        if (hasUniCarNaviClient(context)) {
            Logger.d(TAG, "showRoute--hasUniCarNaviClient");
            UniCarNaviUtil.sendStartNaviAction(context, UniCarNaviUtil.getStartNaviJson(mode,
                    fromLat, fromLng, fromCity, fromPoi, toLat, toLng, toCity, toName, toAddress,
                    style));
        } else if (mHasGaodeNavClient) {
            GaodeUriApi.startNavi2(context, toLat, toLng, toName, style, 0);
        } else if (mHasGaodeMapClient) {
            GaodeUriApi.startNavi(context, toLat, toLng, toName, style, 0);
        } else {
            Toast.makeText(context, R.string.gaode_nofind_map, Toast.LENGTH_LONG).show();
            GaodeMapSdk.showRoute(context, mode, fromLat, fromLng, fromCity, fromPoi, toLat, toLng,
                    toCity, toName);
        }

    }


    public static void openAMapClient(Context context) {
        if (context == null) {
            return;
        }

        mHasGaodeMapClient = hasGaodeMapClient(context);
        if (mHasGaodeMapClient) {
            GaodeUriApi.openAMap(context);
        } else {
            Toast.makeText(context, R.string.gaode_nofind_map, Toast.LENGTH_LONG).show();
        }
    }
}
