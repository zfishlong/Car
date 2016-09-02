/**
 * Copyright (c) 2012-2013 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * 
 * @FileName : LocalSearchRouteConfirmShowSession.java
 * @ProjectName : uniCarSolution
 * @PakageName : com.unisound.unicar.gui.session
 * @Author : Alieen
 * @CreateDate : 2015-07-21
 */
package com.unisound.unicar.gui.session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import com.unisound.unicar.gui.location.operation.LocationModelProxy;
import com.unisound.unicar.gui.model.LocationInfo;
import com.unisound.unicar.gui.preference.SessionPreference;
import com.unisound.unicar.gui.preference.UserPerferenceUtil;
import com.unisound.unicar.gui.route.baidu.BaiduMap;
import com.unisound.unicar.gui.route.operation.GaodeMap;
import com.unisound.unicar.gui.utils.JsonTool;
import com.unisound.unicar.gui.utils.Logger;
import com.unisound.unicar.gui.utils.StringUtil;
import com.unisound.unicar.gui.view.LocalSearchRouteConfirmView;
import com.unisound.unicar.gui.view.LocalSearchRouteConfirmView.IRouteWaitingContentViewListener;

/**
 * Navigation & Local Search route show session
 * 
 * @Module : 隶属模块名
 * @Comments : 描述
 * @Author : Alieen
 * @CreateDate : 2015-07-2
 * @ModifiedBy : xiaodong
 * @ModifiedDate: 2015-09-14
 * @Modified:
 */
public class LocalSearchRouteConfirmShowSession extends BaseSession {
    public static final String TAG = "LocalSearchRouteConfirmShowSession";

    private Context mContext;

    private LocalSearchRouteConfirmView mRouteContentView = null;

    private double toLat = 0.0;
    private double toLng = 0.0;
    private String toCity = "";
    /** to name */
    private String toPoi = "";
    /** toAddress XD added 20160107 */
    private String toAddress = "";

    private final int AMAP_INDEX = 1;
    private final int BAIDU_INDEX = 2;
    private final int MAPBAR_INDEX = 3;
    private final int RITU_INDEX = 4;

    private LocationInfo mLocationInfo; // XD added 20150911
    private double mFromLat = 0.0;
    private double mFromLng = 0.0;
    private String mFromPoi = "";
    private String mFromeCity = "";
    private String mCondition = "";
    private String mConditionTitle = "躲避拥堵 ";// XD added 20160322

    /** XDMark: GaodeMap Condition style */
    private int mStyle = 4;

    // add tyz 20160129 途经点类表
    /**
     * [] or ["aa","bb"]
     */
    private String mPathPoints = "";

    public LocalSearchRouteConfirmShowSession(Context context, Handler sessionManagerHandler) {
        super(context, sessionManagerHandler);
        this.mContext = context;
    }

    public void putProtocol(JSONObject jsonProtocol) {
        super.putProtocol(jsonProtocol);
        // addQuestionViewText(mQuestion);
        Logger.d(TAG, "putProtocol : " + jsonProtocol.toString());
        try {
            JSONObject data = jsonProtocol.getJSONObject(SessionPreference.KEY_DATA);
            toPoi = data.getString(SessionPreference.KEY_NAME);
            JSONObject poiInfo = new JSONObject(data.getString("location"));
            toLat = poiInfo.getDouble("lat");
            toLng = poiInfo.getDouble("lng");
            toCity = poiInfo.getString("city");
            toPoi = poiInfo.getString("name");

            toAddress = poiInfo.getString("address"); // XD added 20160107
            mCondition = JsonTool.getJsonValue(poiInfo, "condition", "ECAR_AVOID_TRAFFIC_JAM");
            mPathPoints = JsonTool.getJsonValue(poiInfo, "pathPoints", "");
            changeConditionToStyle(mCondition);
            Logger.d(TAG, "mCondition:" + mCondition + "; mPathPoints = " + mPathPoints);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getCurrentLocation();

        // addAnswerViewText(mAnswer);
        mRouteContentView = new LocalSearchRouteConfirmView(mContext);
        mRouteContentView.setListener(mRouteViewListener);
        mRouteContentView.setEndPOI(toPoi);

        showConditionTitle();
        showPathPointsTitle();

        addAnswerView(mRouteContentView);
    }

    /**
     * @author xiaodong.he
     */
    private void showConditionTitle() {
        if (!TextUtils.isEmpty(mConditionTitle)) {
            mRouteContentView.setCondition(mConditionTitle);
        }
    }

    /**
     * @author xiaodong.he
     */
    private void showPathPointsTitle() {
        JSONArray pathPointArray = JsonTool.parseToJSONOArray(mPathPoints);
        String pathPoints = StringUtil.getJSONArrayStringValues(pathPointArray);
        if (!TextUtils.isEmpty(pathPoints)) {
            mRouteContentView.setPathPoint(pathPoints);
        }
    }

    /**
     * EBUS_NO_SUBWAY：不要坐地铁 EBUS_WALK_FIRST：最少步行 EBUS_TRANSFER_FIRST：最少换乘 ECAR_DIS_FIRST：最短路程
     * ECAR_FEE_FIRST: 较少费用 TIME_FIRST：最少时间 style:导航方式(0 速度快; 1 费用少; 2 路程短; 3 不走高速；4 躲避拥堵；5
     * 不走高速且避免收费；6 不走高速且躲避拥堵；7 躲避收费和拥堵；8 不走高速躲避收费和拥堵))
     * 
     * XD mark used: ECAR_FEE_FIRST: 避免收费; ECAR_DIS_FIRST：最短路程; TIME_FIRST：最少时间;
     * 
     * @param condition
     */
    // 速度优先、时间优先（时间最短且避让拥堵）、距离优先（距离最短）、费用优先（花费最少）、
    // 躲避拥堵且普通路优先（不走快速路，包含高速路）、躲避拥堵且不走收费道路，满足各种使用导航需求
    /**
     * 0.最少时间,1.较少费用 ，2最短路程 ，4.躲避拥堵，5.高速优先，6.躲避收费，7.不要坐地铁，,8.最少步行，9.最少换乘，10.推荐路线
     */
    // ECAR_FEE_FIRST不走高速
    // ECAR_HIGHWAY_FIRST高速优先
    // ECAR_AVOID_TRAFFIC_JAM躲避拥堵
    // ECAR_AVOID_TOLLS避免收费

    // ECAR_AVOID_TOLLS 躲避收费
    private void changeConditionToStyle(String condition) {
        Logger.d(TAG, "condition = " + condition);
        // TODO:
        mStyle = 4;
        mConditionTitle = "躲避拥堵 ";
        if ("TIME_FIRST".equals(condition)) {
            mStyle = 0;
            mConditionTitle = "最少时间";
        } else if ("ECAR_FEE_FIRST".equals(condition)) {// 不走高速，少走高速 较少费用1,3
            mStyle = 1;
            mConditionTitle = "较少费用";
        } else if ("ECAR_DIS_FIRST".equals(condition)) {// 2
            mStyle = 2;
            mConditionTitle = "最短路程 ";
        } else if ("ECAR_AVOID_TRAFFIC_JAM".equals(condition)) {// 4
            mStyle = 4;
            mConditionTitle = "躲避拥堵 ";
        } else if ("ECAR_HIGHWAY_FIRST".equals(condition)) {
            mStyle = 5;
            mConditionTitle = "高速优先 ";
        } else if ("ECAR_AVOID_TOLLS".equals(condition)) {
            mStyle = 6;
            mConditionTitle = "躲避收费";
        } else if ("EBUS_NO_SUBWAY".equals(condition)) {
            mStyle = 7;
            mConditionTitle = "不要坐地铁";
        } else if ("EBUS_WALK_FIRST".equals(condition)) {
            mStyle = 8;
            mConditionTitle = "最少步行";
        } else if ("EBUS_TRANSFER_FIRST".equals(condition)) {
            mStyle = 9;
            mConditionTitle = "最少换乘";
        } else if ("SUGGESTED".equals(condition)) {
            mStyle = 10;
            mConditionTitle = "推荐路线";
        }
    }

    /**
     * XD added 20150915
     */
    private void getCurrentLocation() {
        Logger.d(TAG, "!--->getCurrentLocation--");
        mLocationInfo = LocationModelProxy.getInstance(mContext).getLastLocation();
        // mLocationInfo = WindowService.mLocationInfo;
        if (mLocationInfo != null) {
            mFromLat = mLocationInfo.getLatitude();
            mFromLng = mLocationInfo.getLongitude();
            mFromeCity = mLocationInfo.getCity();
            mFromPoi = mLocationInfo.getProvince();
        }
    }

    // public static void setFromLat(double fromLat){
    // Logger.d(TAG, "RouteShowSession set fromLat : " + fromLat);
    // mFromLat = fromLat;
    // }
    // public static void setFromLng(double fromLng){
    // Logger.d(TAG, "RouteShowSession set fromLng : " + fromLng);
    // mFromLng = fromLng;
    // }
    // public static void setFromPoi(String fromPoi){
    // Logger.d(TAG, "RouteShowSession set fromPoi : " + fromPoi);
    // mFromPoi = "出发地";
    // }

    protected IRouteWaitingContentViewListener mRouteViewListener =
            new IRouteWaitingContentViewListener() {

                @Override
                public void onCancel() {
                    Logger.d(TAG, "!--->mCallContentViewListener---onCancel()-----");
                    onUiProtocal(SessionPreference.EVENT_NAME_ON_CONFIRM_CANCEL,
                            SessionPreference.EVENT_PROTOCAL_ON_CONFIRM_CANCEL_CALL);
                    mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_SESSION_DONE);
                }

                @Override
                public void onOk() {
                    Logger.d(TAG, "!--->mCallContentViewListener---onOk()-----");
                    onUiProtocal(SessionPreference.EVENT_NAME_ON_CONFIRM_OK,
                            SessionPreference.EVENT_PROTOCAL_ON_CONFIRM_OK);
                    mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_SESSION_DONE);
                    showRoute(mStyle);
                }

                @Override
                public void onTimeUp() {

                    Logger.d(TAG, "!--->mCallContentViewListener---onTimeUp()-----");
                    onUiProtocal(SessionPreference.EVENT_NAME_ON_CONFIRM_TIME_UP,
                            SessionPreference.EVENT_PROTOCAL_ON_CONFIRM_TIME_UP);
                    // mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_SESSION_DONE);
                    showRoute(mStyle);
                }
            };

    private void showRoute(int style) {
        int mapIndex = UserPerferenceUtil.getMapChoose(mContext);
        switch (mapIndex) {
            case AMAP_INDEX:
                Logger.d(TAG, "mapIndex : " + mapIndex + " use amap route ...");
                // XD modify 20150914
                GaodeMap.showRoute(mContext, "driving", mFromLat, mFromLng, mFromeCity, mFromPoi,
                        toLat, toLng, toCity, toPoi, toAddress, style);

                // GaodeMap.showRoute(mContext, "driving", toLat, toLng,toCity, toPoi);
                break;
            case BAIDU_INDEX:
                Logger.d(TAG, "mapIndex : " + mapIndex + " use baidu route ...");
                // XD modify 20150914
                BaiduMap.showRoute(mContext, BaiduMap.ROUTE_MODE_DRIVING, mFromLat, mFromLng,
                        mFromeCity, mFromPoi, toLat, toLng, toCity, toPoi);

                // startNavi(mFromLat, mFromLng, toLat, toLng, mFromPoi, toPoi);
                break;
            case MAPBAR_INDEX:
                Logger.d(TAG, "mapIndex : " + mapIndex + " use mapbar route ...");
                JSONObject mJson = new JSONObject();
                try {
                    mJson.put("toLatitude", toLat);
                    mJson.put("toLongtitude", toLng);
                    mJson.put("toPoi", toPoi);
                    Logger.d(TAG, "msg = " + mJson.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent mIntent = new Intent();
                String action = "android.intent.action.SEND_POIINFO";
                mIntent.setAction(action);
                mIntent.putExtra("poi_info", mJson.toString());
                Logger.d(TAG, "msg = " + action.toString());
                mContext.sendBroadcast(mIntent);
                break;
            case RITU_INDEX:
                Logger.d(TAG, "mapIndex : " + mapIndex + " use ritu route ...");
                Intent intent = new Intent("android.intent.action.ritu.keyword.name");

                double mlng = toLng;
                double mlat = toLat;

                if (toLng < 1000) {
                    mlng = mlng * 3600 * 2560;
                    mlat = mlat * 3600 * 2560;
                }
                int lng = (int) mlng;
                int lat = (int) mlat;

                Logger.d(TAG, "mapIndex : " + mapIndex + " use ritu route ...====lng:" + lng
                        + "======lat:" + lat);
                intent.putExtra("navi_keyword_name", lng + "," + lat + "," + toPoi);
                mContext.sendBroadcast(intent);
                break;

            default:
                break;
        }
    }


    /*
     * public void startNavi(double fromLat, double fromLng, double toLat, double toLng, String
     * fromPoi, String toPoi) { fromLat =
     * LocationModelProxy.getInstance(mContext).getLastLocation().getLatitude(); fromLng =
     * LocationModelProxy.getInstance(mContext).getLastLocation().getLongitude(); fromPoi =
     * LocationModelProxy.getInstance(mContext).getLastLocation().getAddress();
     * 
     * //先将高德坐标转换为百度坐标 LatLng pt1 = new LatLng(fromLat, fromLng); LatLng pt2 = new LatLng(toLat,
     * toLng);
     * 
     * Logger.d(TAG, "from lng : [ " + fromLat + " -- " + fromLng + " ]"); Logger.d(TAG,
     * "to lng : [ " + toLat + " -- " + toLng + " ]"); Logger.d(TAG, "from poi : " + fromPoi +
     * " to poi : " + toPoi);
     * 
     * // 构建 导航参数 NaviPara para = new NaviPara(); para.startPoint = pt1; para.startName = fromPoi;
     * para.endPoint = pt2; para.endName = toPoi;
     * 
     * try { BaiduMapNavigation.openBaiduMapNavi(para, mContext); } catch
     * (BaiduMapAppNotSupportNaviException e) { e.printStackTrace(); } }
     */

    @Override
    public void onTTSEnd() {
        super.onTTSEnd();
        Logger.d(TAG, "!--->mCallContentViewListener---onTTSEnd()-----");
        // mRouteContentView.startCountDownTimer(GUIConfig.TIME_DELAY_AUTO_CONFIRM);
        showRoute(mStyle);
        mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_SESSION_DONE);
    }

    @Override
    public void release() {
        Logger.d(TAG, "!--->release");
        super.release();
        if (mRouteContentView != null) {
            mRouteContentView.cancelCountDownTimer();
        }
        mRouteContentView.setListener(null);
    }

}
