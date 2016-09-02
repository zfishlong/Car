/**
 * Copyright (c) 2012-2016 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : UniCarNaviUtil.java
 * @ProjectName : uniCarSolution_oneshot
 * @PakageName : com.unisound.unicar.gui.uninavi
 * @version : 1.0
 * @Author : Xiaodong.He
 * @CreateDate : 2016-1-6
 */
package com.unisound.unicar.gui.uninavi;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;

import com.unisound.unicar.gui.preference.SessionPreference;
import com.unisound.unicar.gui.ui.MessageSender;
import com.unisound.unicar.gui.ui.WindowService;
import com.unisound.unicar.gui.utils.GUIConfig;
import com.unisound.unicar.gui.utils.GuiProtocolUtil;
import com.unisound.unicar.gui.utils.JsonTool;
import com.unisound.unicar.gui.utils.Logger;
import com.unisound.unicar.gui.utils.PackageUtil;

/**
 * UniCarNaviUtil
 * 
 * @author xiaodong.he
 * @date 2016-1-6
 */
public class UniCarNaviUtil {

    private static int sUniCarNaviStatus = UniCarNaviConstant.CallbackConstant.APP_STATE_IDEL;

    public static int getUniCarNaviStatus() {
        return sUniCarNaviStatus;
    }

    public static void setUniCarNaviStatus(int sUniCarNaviStatus) {
        UniCarNaviUtil.sUniCarNaviStatus = sUniCarNaviStatus;
    }

    /**
     * @author xiaodong.he
     * @return
     */
    public static boolean isUniCarNaviWorking() {
        Logger.d("UniCarNavi Status: " + sUniCarNaviStatus);
        if (sUniCarNaviStatus == UniCarNaviConstant.CallbackConstant.APP_STATE_MAP
                || sUniCarNaviStatus == UniCarNaviConstant.CallbackConstant.APP_STATE_NAVI) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 
     * @author xiaodong.he
     * @param context
     * @return
     */
    public static boolean isUniCarNaviEnable(Context context) {
        boolean isUniCarNaviAppRuning =
                PackageUtil.isAppRunning2(context, GUIConfig.PACKAGE_NAME_UNICAR_NAVI);
        boolean isUniCarNaviWorking = isUniCarNaviWorking();
        Logger.d("isUniCarNaviAppRuning:" + isUniCarNaviAppRuning + "; isUniCarNaviWorking: "
                + isUniCarNaviWorking);
        if (!isUniCarNaviAppRuning
                && UniCarNaviUtil.getUniCarNaviStatus() != UniCarNaviConstant.CallbackConstant.APP_STATE_IDEL) {
            setUniCarNaviStatus(UniCarNaviConstant.CallbackConstant.APP_STATE_IDEL);
        }
        if (isUniCarNaviAppRuning && isUniCarNaviWorking) {
            return true;
        } else {
            return false;
        }
    }


    // UniCarNaviGuiService Call back EVENT to VUI Begin---------

    public static void sendRouteStartedEvent(Context context) {
        sendProtocalEvent(context, SessionPreference.EVENT_NAME_UNICAR_NAVI_STARTED,
                SessionPreference.PARAM_TYPE_ROUTE_STARTED);
    }

    public static void sendRouteQuitEvent(Context context) {
        sendProtocalEvent(context, SessionPreference.EVENT_NAME_UNICAR_NAVI_STARTED,
                SessionPreference.PARAM_TYPE_ROUTE_QUIT);
    }

    public static void sendNaviStartedEvent(Context context) {
        sendProtocalEvent(context, SessionPreference.EVENT_NAME_UNICAR_NAVI_STARTED,
                SessionPreference.PARAM_TYPE_NAVI_STARTED);
    }

    /**
     * 
     * @param context
     * @param eventName
     * @param type
     */
    private static void sendProtocalEvent(Context context, String eventName, String type) {
        if (null == context) {
            return;
        }
        Intent intent = new Intent(WindowService.ACTION_SEND_PROTOCAL_EVENT);
        intent.putExtra(SessionPreference.KEY_BUNDLE_EVENT_NAME, eventName);
        intent.putExtra(SessionPreference.KEY_BUNDLE_PROTOCAL,
                GuiProtocolUtil.getTypeParamProtocol(type));
        intent.setClass(context, WindowService.class);
        context.startService(intent);
    }

    // -----------Session message to UniCarNaviGuiService Begin---------

    /**
     * Start Navi
     * 
     * @param context
     * @param json
     */
    public static void sendStartNaviAction(Context context, String json) {
        Intent intent = new Intent(UniCarNaviConstant.ACTION_START_UNICARNAVI);
        intent.putExtra(UniCarNaviConstant.KEY_NAVI_PARAMS, json);
        MessageSender messageSender = new MessageSender(context);
        messageSender.sendOrderedMessage(intent, null);
    }

    /**
     * 
     * @param context
     */
    public static void sendBeginNavigationAction(Context context) {
        Intent intent = new Intent(UniCarNaviConstant.ACTION_BEGIN_NAVIGAION);
        MessageSender messageSender = new MessageSender(context);
        messageSender.sendOrderedMessage(intent, null);
    }

    /**
     * send Close Navi Action 关闭|退出导航
     * 
     * @param context
     * @param isPlayTTS
     */
    public static void sendCloseNaviAction(Context context, boolean isPlayTTS) {
        Intent intent = new Intent(UniCarNaviConstant.ACTION_EXIT);
        intent.putExtra(UniCarNaviConstant.KEY_IS_PLAY_TTS, isPlayTTS);
        MessageSender messageSender = new MessageSender(context);
        messageSender.sendOrderedMessage(intent, null);
    }

    /**
     * 
     * @param context
     * @param routeplan {@link UniCarNaviUtil#VALUE_ROUTE_PLAN_*}
     */
    public static void sendControlRoutePlan(Context context, int routeplan) {
        Intent intent = new Intent(UniCarNaviConstant.ACTION_SET_ROUTE_PLAN);
        intent.putExtra(UniCarNaviConstant.KEY_ROUTE_PLAN, routeplan);
        MessageSender messageSender = new MessageSender(context);
        messageSender.sendOrderedMessage(intent, null);
    }

    /**
     * Control Navi
     * 
     * @param context
     * @param operation
     */
    public static void sendControlNaviAction(Context context, int operation) {
        Intent intent = new Intent(UniCarNaviConstant.ACTION_CONTROL_UNICARNAVI);
        intent.putExtra(UniCarNaviConstant.DATA_KEY_OPERATION, operation);
        MessageSender messageSender = new MessageSender(context);
        messageSender.sendOrderedMessage(intent, null);
    }

    /**
     * Set Display Mode
     * 
     * @param context
     * @param cmd
     */
    public static void sendSetDisplayModeAction(Context context, int mode) {
        Intent intent = new Intent(UniCarNaviConstant.ACTION_SET_DISPLAY_MODE);
        intent.putExtra(UniCarNaviConstant.KEY_DISPLAY_MODE, mode);
        MessageSender messageSender = new MessageSender(context);
        messageSender.sendOrderedMessage(intent, null);
    }

    /**
     * 
     * @param context
     * @param ttsID
     */
    public static void sendOnTTSPlayEndAction(Context context, String ttsID) {
        Intent intent = new Intent(UniCarNaviConstant.ACTION_ON_TTS_PLAY_END);
        intent.putExtra(UniCarNaviConstant.CallbackConstant.KEY_CALLBACK_TTS_ID, ttsID);
        MessageSender messageSender = new MessageSender(context);
        messageSender.sendOrderedMessage(intent, null);

    }

    /**
     * 
     * @param context
     */
    public static void sendShowNaviUIAction(Context context) {
        Intent intent = new Intent(UniCarNaviConstant.ACTION_SHOW_UNICAR_NAVI_UI);
        MessageSender messageSender = new MessageSender(context);
        messageSender.sendOrderedMessage(intent, null);
    }

    // ---------------------Protocol to UniCarNaviService Util Begin--------------------

    /**
     * 
     * @param mode
     * @param fromLat
     * @param fromLng
     * @param fromCity
     * @param fromPoi
     * @param toLat
     * @param toLng
     * @param toCity
     * @param toName
     * @param toAddress
     * @param routePlan
     * @return
     */
    public static String getStartNaviJson(String mode, double fromLat, double fromLng,
            String fromCity, String fromPoi, double toLat, double toLng, String toCity,
            String toName, String toAddress, int routePlan) {
        JSONObject json = new JSONObject();
        try {
            json.put(UniCarNaviConstant.KEY_MODE, mode);
            json.put(UniCarNaviConstant.KEY_FROM_LAT, fromLat);
            json.put(UniCarNaviConstant.KEY_FROM_LNG, fromLng);
            json.put(UniCarNaviConstant.KEY_FROM_CITY, fromCity);
            json.put(UniCarNaviConstant.KEY_FROM_POI, fromPoi);

            json.put(UniCarNaviConstant.KEY_TO_LAT, toLat);
            json.put(UniCarNaviConstant.KEY_TO_LNG, toLng);
            json.put(UniCarNaviConstant.KEY_TO_CITY, toCity);
            json.put(UniCarNaviConstant.KEY_TO_NAME, toName);
            json.put(UniCarNaviConstant.KEY_TO_ADDRESS, toAddress);

            json.put(UniCarNaviConstant.KEY_ROUTE_PLAN, changeRoutePlan(routePlan));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    private static int changeRoutePlan(int routePlan) {
        switch (routePlan) {
            case 0:
                routePlan = UniCarNaviConstant.VALUE_ROUTE_PLAN_HIGHWAY;
                break;
            case 1:
                routePlan = UniCarNaviConstant.VALUE_ROUTE_PLAN_NO_HIGHWAY;
                break;
            case 2:
                routePlan = UniCarNaviConstant.VALUE_ROUTE_PLAN_ECAR_AVOID_TRAFFIC_JAM;
                break;
            case 3:
                routePlan = UniCarNaviConstant.VALUE_ROUTE_PLAN_ECAR_DIS_FIRST;
                break;
            case 4:
                routePlan = UniCarNaviConstant.VALUE_ROUTE_PLAN_ECAR_AVOID_TRAFFIC_JAM;
                break;
            case 5:
                routePlan = UniCarNaviConstant.VALUE_ROUTE_PLAN_HIGHWAY;
                break;
            case 6:
                routePlan = UniCarNaviConstant.VALUE_ROUTE_PLAN_ECAR_AVOID_TOLLS;
                break;
            case 7:
                routePlan = UniCarNaviConstant.VALUE_ROUTE_PLAN_EBUS_NO_SUBWAY;
                break;
            case 8:
                routePlan = UniCarNaviConstant.VALUE_ROUTE_PLAN_EBUS_WALK_FIRST;
                break;
            case 9:
                routePlan = UniCarNaviConstant.VALUE_ROUTE_PLAN_EBUS_TRANSFER_FIRST;
                break;
            case 10:
                routePlan = UniCarNaviConstant.VALUE_ROUTE_PLAN_SUGGESTED;
                break;
            default:
                routePlan = UniCarNaviConstant.VALUE_ROUTE_PLAN_ECAR_AVOID_TRAFFIC_JAM;
                break;
        }
        return routePlan;
    }


    /**
     * 
     * @param module: {@link UniCarNaviConstant#VALUE_CMD_MODULE_ROUTE}
     *        {@link UniCarNaviConstant#VALUE_CMD_MODULE_NAVIGATION}
     *        {@link UniCarNaviConstant#VALUE_CMD_MODULE_COMMON}
     * @param cmdName
     * @param dataJson
     * @return
     */
    private static String getOnControlCommandJson(String module, String cmdName, JSONObject dataJson) {
        JSONObject json = new JSONObject();
        try {
            json.put(UniCarNaviConstant.KEY_CMD_MODULE, module);
            json.put(UniCarNaviConstant.KEY_CMD_NAME, cmdName);
            if (null != dataJson) {
                json.put(UniCarNaviConstant.KEY_OBJ_DATA, dataJson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    /**
     * 
     * @date 2016-1-25
     * @param ttsID
     * @return
     */
    public static String getOnTTSPlayEndJson(String ttsID) {
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put(UniCarNaviConstant.CallbackConstant.KEY_CALLBACK_TTS_ID, ttsID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getOnControlCommandJson(UniCarNaviConstant.VALUE_CMD_MODULE_COMMON,
                UniCarNaviConstant.VALUE_CMD_NAME_ON_TTS_PLAY_END, dataObj);
    }

    /**
     * 
     * @return
     */
    public static String getBeginNavigationJson() {
        return getOnControlCommandJson(UniCarNaviConstant.VALUE_CMD_MODULE_ROUTE,
                UniCarNaviConstant.VALUE_CMD_NAME_BEGIN_NAVIGATION, null);
    }

    /**
     * 
     * @return
     */
    public static String getShowUniCarNaviJson() {
        return getOnControlCommandJson(UniCarNaviConstant.VALUE_CMD_MODULE_COMMON,
                UniCarNaviConstant.VALUE_CMD_NAME_SHOW_UNICAR_NAVI, null);
    }

    /**
     * 
     * @return
     */
    public static String getCancelJson() {
        return getOnControlCommandJson(UniCarNaviConstant.VALUE_CMD_MODULE_COMMON,
                UniCarNaviConstant.VALUE_CMD_NAME_CANCEL, null);
    }

    /**
     * 
     * @return
     */
    public static String getExitJson() {
        return getOnControlCommandJson(UniCarNaviConstant.VALUE_CMD_MODULE_COMMON,
                UniCarNaviConstant.VALUE_CMD_NAME_EXIT, null);
    }

    /**
     * get SetRoutePlan Json
     * 
     * @param routeplan {@link UniCarNaviConstant#VALUE_ROUTE_PLAN_AVOID_CONGESTION}
     *        {@link UniCarNaviConstant#VALUE_ROUTE_PLAN_NO_HIGHWAY}
     *        {@link UniCarNaviConstant#VALUE_ROUTE_PLAN_SAVE_MONEY}
     *        {@link UniCarNaviConstant#VALUE_ROUTE_PLAN_HIGHWAY}
     * @return
     */
    public static String getSetRoutePlanJson(int routeplan) {
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put(UniCarNaviConstant.DATA_KEY_ROUTE_PLAN, routeplan);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getOnControlCommandJson(UniCarNaviConstant.VALUE_CMD_MODULE_ROUTE,
                UniCarNaviConstant.VALUE_CMD_NAME_SET_ROUTE_PLAN, dataObj);
    }

    /**
     * 
     * @param operation
     * @return
     */
    public static String getSetDisplayModeJson(int displayMode) {
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put(UniCarNaviConstant.DATA_KEY_DISPLAY_MODE, displayMode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getOnControlCommandJson(UniCarNaviConstant.VALUE_CMD_MODULE_COMMON,
                UniCarNaviConstant.VALUE_CMD_NAME_SET_DISPLAY_MODE, dataObj);
    }

    /**
     * getNaviControlJson
     * 
     * @param OPERATION {@link UniCarNaviConstant#VALUE_OPERATION_NAVI_START}
     *        {@link UniCarNaviConstant#VALUE_OPERATION_EMULATE_NAVI_START}
     *        {@link UniCarNaviConstant#VALUE_OPERATION_EMULATE_NAVI_PAUSE}
     *        {@link UniCarNaviConstant#VALUE_OPERATION_EMULATE_NAVI_CONTINUE}
     * @return
     */
    public static String getNaviControlJson(int OPERATION) {
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put(UniCarNaviConstant.DATA_KEY_OPERATION, OPERATION);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getOnControlCommandJson(UniCarNaviConstant.VALUE_CMD_MODULE_NAVIGATION,
                UniCarNaviConstant.VALUE_CMD_NAME_CONTROL_NAVIGATION, dataObj);
    }

    /**
     * 
     * @param OPERATION: {@link UniCarNaviConstant#VALUE_OPERATION_ROAD_CONDITION_SHOW}
     *        {@link UniCarNaviConstant#VALUE_OPERATION_ROAD_CONDITION_CLOSE}
     * @return
     */
    public static String getRoadConditionControlJson(int OPERATION) {
        JSONObject dataObj = new JSONObject();
        try {
            dataObj.put(UniCarNaviConstant.DATA_KEY_OPERATION, OPERATION);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getOnControlCommandJson(UniCarNaviConstant.VALUE_CMD_MODULE_NAVIGATION,
                UniCarNaviConstant.VALUE_CMD_NAME_CONTROL_ROAD_CONDITION, dataObj);
    }


    /**
     * callback datObj
     * 
     * @param datObj
     * @return
     */
    public static String getCallbackTTS(JSONObject datObj) {
        String ttsText =
                JsonTool.getJsonValue(datObj,
                        UniCarNaviConstant.CallbackConstant.KEY_CALLBACK_TTS_TEXT);
        return ttsText;
    }

}
