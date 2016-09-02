/**
 * Copyright (c) 2012-2016 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : NaviControlSession.java
 * @ProjectName : uniCarSolution
 * @PakageName : com.unisound.unicar.gui.session
 * @version : 1.0
 * @Author : Xiaodong.He
 * @CreateDate : 2016-1-21
 */
package com.unisound.unicar.gui.session;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import com.unisound.unicar.gui.application.CrashApplication;
import com.unisound.unicar.gui.preference.SessionPreference;
import com.unisound.unicar.gui.uninavi.UniCarNaviConstant;
import com.unisound.unicar.gui.uninavi.UniCarNaviUtil;
import com.unisound.unicar.gui.utils.JsonTool;
import com.unisound.unicar.gui.utils.Logger;

/**
 * 
 * @author xiaodong.he
 * @date 2016-1-21
 */
public class NaviControlSession extends BaseSession {

    private static final String TAG = NaviControlSession.class.getSimpleName();

    private static final String ACTION_BASE = "com.unisound.unicar.navi.action";

    /** Map big */
    public static final String ACTION_ZOOM_IN = ACTION_BASE + ".ZOOM_IN";
    /** Map small */
    public static final String ACTION_ZOOM_OUT = ACTION_BASE + ".ZOOM_OUT";
    // 预览全程
    public static final String ACTION_PRE_SHOW_ROUTE = ACTION_BASE + ".PRE_SHOW_ROUTE";
    // 继续导航
    public static final String ACTION_CONTINUE_NAVI = ACTION_BASE + ".CONTINUE_NAVI";
    // 车头向上，车头朝前
    public static final String ACTION_MODE_CAR_UP = ACTION_BASE + ".MODE_CAR_UP";
    // 正北朝上，地图正北
    public static final String ACTION_MODE_NORTH_UP = ACTION_BASE + ".MODE_NORTH_UP";
    // 模拟导航
    public static final String ACTION_SIMULATE_NAVI = ACTION_BASE + ".SIMULATE_NAVI";

    public final static int APP_STATE_MAP = 202;
    public final static int APP_STATE_NAVI = 203;
    public static final String KEY_APP_STATE = "app_state";


    private String mOperator;

    /**
     * @param context
     * @param sessionManagerHandler
     */
    public NaviControlSession(Context context, Handler sessionManagerHandler) {
        super(context, sessionManagerHandler);
    }

    public void putProtocol(JSONObject jsonProtocol) {
        super.putProtocol(jsonProtocol);

        // XD 2016-2-4 add
        boolean isNeedSessionDone = true;

        mOperator = JsonTool.getJsonValue(mDataObject, SessionPreference.KEY_OPERATOR, "");
        Logger.d(TAG, "putProtocol---mOperator = " + mOperator);
        if (TextUtils.isEmpty(mOperator)) {
            return;
        }
        if (SessionPreference.VALUE_OPERATOR_ROUTE_AVOID_CONGESTION.equals(mOperator)) {
            UniCarNaviUtil.sendControlRoutePlan(mContext,
                    UniCarNaviConstant.VALUE_ROUTE_PLAN_ECAR_AVOID_TRAFFIC_JAM);
        } else if (SessionPreference.VALUE_OPERATOR_ROUTE_HIGHWAY.equals(mOperator)) {
            UniCarNaviUtil.sendControlRoutePlan(mContext,
                    UniCarNaviConstant.VALUE_ROUTE_PLAN_HIGHWAY);
        } else if (SessionPreference.VALUE_OPERATOR_ROUTE_NO_HIGHWAY.equals(mOperator)) {
            UniCarNaviUtil.sendControlRoutePlan(mContext,
                    UniCarNaviConstant.VALUE_ROUTE_PLAN_NO_HIGHWAY);
        } else if (SessionPreference.VALUE_OPERATOR_ROUTE_SAVE_MONEY.equals(mOperator)) {
            UniCarNaviUtil.sendControlRoutePlan(mContext,
                    UniCarNaviConstant.VALUE_ROUTE_PLAN_ECAR_DIS_FIRST);
        } else if (SessionPreference.VALUE_OPERATOR_START_NAVIGATION.equals(mOperator)) {
            UniCarNaviUtil.sendBeginNavigationAction(mContext);
        } else if (SessionPreference.VALUE_OPERATOR_ACT_NAVMODE_DEMO.equals(mOperator)) {
            UniCarNaviUtil.sendControlNaviAction(mContext,
                    UniCarNaviConstant.VALUE_OPERATION_EMULATE_NAVI_START);
        } else if (SessionPreference.VALUE_OPERATOR_ACT_NAV_PAUSE.equals(mOperator)) {
            UniCarNaviUtil.sendControlNaviAction(mContext,
                    UniCarNaviConstant.VALUE_OPERATION_EMULATE_NAVI_PAUSE);
        } else if (SessionPreference.VALUE_OPERATOR_ACT_NAV_CONTINUE.equals(mOperator)) {
            UniCarNaviUtil.sendControlNaviAction(mContext,
                    UniCarNaviConstant.VALUE_OPERATION_EMULATE_NAVI_CONTINUE);
        } else if (SessionPreference.VALUE_OPERATOR_ACT_NAVMODE_NIGHT.equals(mOperator)) {
            UniCarNaviUtil.sendSetDisplayModeAction(mContext,
                    UniCarNaviConstant.VALUE_DISPLAY_MODE_NIGHT);
        } else if (SessionPreference.VALUE_OPERATOR_ACT_NAVMODE_STANDARD.equals(mOperator)) {
            UniCarNaviUtil.sendSetDisplayModeAction(mContext,
                    UniCarNaviConstant.VALUE_DISPLAY_MODE_STANDARD);
        } else if (SessionPreference.VALUE_OPERATOR_ACT_NAV_SHOWTRAFFIC.equals(mOperator)) {
            UniCarNaviUtil.sendControlNaviAction(mContext,
                    UniCarNaviConstant.VALUE_OPERATION_ROAD_CONDITION_SHOW);
        } else if (SessionPreference.VALUE_OPERATOR_ACT_NAV_CLOSETRAFFIC.equals(mOperator)) {
            UniCarNaviUtil.sendControlNaviAction(mContext,
                    UniCarNaviConstant.VALUE_OPERATION_ROAD_CONDITION_CLOSE);
        } else if (SessionPreference.VALUE_OPERATOR_ACT_NAV_CLOSE.equals(mOperator)) {
            String type = JsonTool.getJsonValue(mDataObject, SessionPreference.KEY_TYPE, "");
            // XD 2016-2-4 modify
            boolean isPlayTTS = true;
            if (SessionPreference.PARAM_TYPE_NOT_PLAY_TTS.equals(type)) {
                isNeedSessionDone = false;
                isPlayTTS = false;
            }
            Logger.d(TAG, "ACT_NAV_CLOSE---isPlayTTS: " + isPlayTTS);
            UniCarNaviUtil.sendCloseNaviAction(mContext, isPlayTTS);
        } else if (SessionPreference.VALUE_OPERATOR_MAP_TO_NARROW.equals(mOperator)) {
            sendBrodcastForNavi(ACTION_ZOOM_OUT, APP_STATE_MAP);
        } else if (SessionPreference.VALUE_OPERATOR_MAP_TO_ZOOM.equals(mOperator)) {
            sendBrodcastForNavi(ACTION_ZOOM_IN, APP_STATE_MAP);
        } else if (SessionPreference.VALUE_OPERATOR_PREVIEW_THE_WHOLE.equals(mOperator)) {
            sendBrodcastForNavi(ACTION_PRE_SHOW_ROUTE, APP_STATE_NAVI);
        } else if (SessionPreference.VALUE_OPERATOR_CONTINUE_NAVI.equals(mOperator)) {
            sendBrodcastForNavi(ACTION_CONTINUE_NAVI, APP_STATE_NAVI);
        } else if (SessionPreference.VALUE_OPERATOR_MODE_CAR_UP.equals(mOperator)) {
            sendBrodcastForNavi(ACTION_MODE_CAR_UP, APP_STATE_NAVI);
        } else if (SessionPreference.VALUE_OPERATOR_MODE_NORTH_UP.equals(mOperator)) {
            sendBrodcastForNavi(ACTION_MODE_NORTH_UP, APP_STATE_NAVI);
        } else if (SessionPreference.VALUE_OPERATOR_MODE_SIMULATE_NAVI.equals(mOperator)) {
            sendBrodcastForNavi(ACTION_SIMULATE_NAVI, APP_STATE_NAVI);
        }


        if (isNeedSessionDone) {
            mSessionManagerHandler.sendEmptyMessageDelayed(SessionPreference.MESSAGE_SESSION_DONE,
                    300);
        } else {
            mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_SESSION_RELEASE_ONLY);
        }
    }

    private void sendBrodcastForNavi(String action, int type) {
        Logger.d(TAG, "sendBrodcastForNavi action:" + action + "; type :" + type);
        Intent intent = new Intent(action);
        intent.putExtra(KEY_APP_STATE, type);
        CrashApplication.getAppContext().sendBroadcast(intent);
    }

    @Override
    public void onTTSEnd() {
        Logger.d(TAG, "onTTSEnd---");
        super.onTTSEnd();

    }

}
