/**
 * Copyright (c) 2012-2015 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : GuiSettingUpdateUtil.java
 * @ProjectName : UniCarGUI_series_asr_1123
 * @PakageName : com.unisound.unicar.gui.utils
 * @version : 1.0
 * @Author : Xiaodong.He
 * @CreateDate : 2015-12-2
 */
package com.unisound.unicar.gui.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.unisound.unicar.gui.preference.SessionPreference;
import com.unisound.unicar.gui.ui.WindowService;

/**
 * GUI Setting Update Util
 * 
 * @author xiaodong.he
 * @date 2015-12-2
 */
public class GuiSettingUpdateUtil {

    private static final String TAG = GuiSettingUpdateUtil.class.getSimpleName();

    public static final String ACTION_UPDATE_VERSION_MODE =
            "com.unisound.unicar.intent.action.UPDATE_VERSION_MODE";

    /**
     * @date 2016-1-18
     */
    public static final String ACTION_ON_GET_TTS_TIMBRE_LIST =
            "com.unisound.unicar.intent.action.ON_GET_TTS_TIMBRE_LIST";

    /**
     * ON_SWITCH_TTS_TIMBRE_DONE: after received onSwitchTTS SystemCall
     * 
     * @date 20160112
     */
    public static final String ACTION_ON_SWITCH_TTS_TIMBRE_DONE =
            "com.unisound.unicar.intent.action.ON_SWITCH_TTS_TIMBRE_DONE";

    /**
     * @date 2016-1-18
     */
    public static final String ACTION_ON_COPY_TTS_TIMBRE_FAIL =
            "com.unisound.unicar.intent.action.ON_COPY_TTS_TIMBRE_FAIL";

    public static final String KEY_SETTING_TYPE = "setting_type";

    /**
     * @values: {@link SessionPreference# VALUE_TTS_MODEL_LIST_ACTION_REQUEST}
     *          {@link SessionPreference# VALUE_TTS_MODEL_LIST_ACTION_UPDATE}
     */
    public static final String KEY_TTS_TIMBRE_LIST_ACTION = "tts_timbre_list_action";
    public static final String KEY_TTS_TIMBRE_LIST = "tts_timbre_list";
    public static final String KEY_TTS_TIMBRE_NAME = "tts_timbre_name";
    public static final String KEY_TTS_FAILCAUSE = "tts_failcause";


    // XD 2016-1-28 added
    public static final String KEY_ADD_ADDRESS_TYPE = "key_add_address_type";
    public static final int VALUE_ADD_ADDRESS_TYPE_HOME = 1;
    public static final int VALUE_ADD_ADDRESS_TYPE_COMPANY = 2;
    public static final int VALUE_ADD_ADDRESS_TYPE_OTHER = 3;

    public static final String KEY_ADD_ADDRESS_LOCATION = "key_add_address_location";

    public static void sendWakeupConfigure(Context context) {
        Logger.d(TAG, "sendWakeupConfigure---");
        sendConfigure(context, WindowService.ACTION_SET_WAKEUP, "");
    }

    public static void sendTTSSpeedConfigure(Context context) {
        sendConfigure(context, WindowService.ACTION_SET_TTSSPEED, "");
    }

    public static void sendOneShotConfigure(Context context) {
        sendConfigure(context, WindowService.ACTION_SET_ONESHOT, "");
    }

    public static void sendGpsConfigure(Context context) {
        sendConfigure(context, WindowService.ACTION_SET_GPS, "");
    }

    /**
     * 
     * @param context
     * @param type {@link SessionPreference#PARAM_VERSION_LEVEL_TYPE_IDEL}
     *        {@link SessionPreference#PARAM_VERSION_LEVEL_TYPE_RECORDING}
     */
    public static void sendVersionLevelConfigure(Context context, String type) {
        sendConfigure(context, WindowService.ACTION_SET_VERSION_LEVEL, type);
    }

    /**
     * 
     * @date 2016-1-12
     * @param context
     */
    public static void sendTTSTimbreConfigure(Context context, String type) {
        sendConfigure(context, WindowService.ACTION_SET_TTS_TIMBRE, type);
    }

    public static void sendAECConfigure(Context context) {
        sendConfigure(context, WindowService.ACTION_SET_AEC, "");
    }

    public static void sendLogcatConfigure(Context context) {
        Logger.d(TAG, "sendLogcatConfigure");
        sendConfigure(context, WindowService.ACTION_SET_LOGCAT, "");
    }

    /**
     * 
     * @param context
     * @param addressType
     */
    public static void sendSetFavoriteAddressConfigure(Context context, int addressType) {
        Intent intent = new Intent(context, WindowService.class);
        intent.setAction(WindowService.ACTION_SET_FAVORITE_ADDRESS);
        intent.putExtra(KEY_ADD_ADDRESS_TYPE, addressType);
        context.startService(intent);
    }

    private static void sendConfigure(Context context, String action, String type) {
        if (context == null) {
            Logger.w(TAG, "context is null");
            return;
        }
        Logger.d(TAG, "!--->---sendConfigure-----action = " + action);
        Intent intent = new Intent(context, WindowService.class);
        intent.setAction(action);
        if (!TextUtils.isEmpty(type)) {
            intent.putExtra(KEY_SETTING_TYPE, type);
        }
        context.startService(intent);
    }

    /**
     * 
     * @author xiaodong.he
     * @date 2016-1-18
     * @param context
     */
    public static void sendRequestTtsTimbreList(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(WindowService.ACTION_REQUEST_TTS_TIMBRE_LIST);
        intent.setClass(context, WindowService.class);
        context.startService(intent);
    }


    /**
     * 
     * @param context
     * @param action
     * @param value
     */
    private static void notifySettingActivity(Context context, String action, String key,
            String value) {
        if (context == null) {
            return;
        }
        Logger.d(TAG, "notifySettingActivity-- value = " + value);
        Intent intent = new Intent(action);
        if (!TextUtils.isEmpty(value)) {
            intent.putExtra(key, value);
        }
        context.sendBroadcast(intent);
    }

    /**
     * 
     * @date 2016-1-18
     * @author xiaodong.he
     * @param context
     * @param json
     */
    public static void notifyTtsTimbreList(Context context, String json) {
        notifySettingActivity(context, ACTION_ON_GET_TTS_TIMBRE_LIST, KEY_TTS_TIMBRE_LIST, json);
    }

    /**
     * 
     * @date 2016-1-12
     * @author xiaodong.he
     * @param context
     * @param ttsTimbre
     */
    public static void notifySwitchTtsTimbreDone(Context context, String ttsTimbre) {
        Logger.d(TAG, "notifySwitchTtsTimbreDone---ttsTimbre = " + ttsTimbre);
        notifySettingActivity(context, ACTION_ON_SWITCH_TTS_TIMBRE_DONE, KEY_TTS_TIMBRE_NAME,
                ttsTimbre);
    }

    /**
     * 
     * @date 2016-1-18
     * @author xiaodong.he
     * @param context
     * @param ttsTimbre
     * @param failcause
     */
    public static void notifyCopyTtsTimbreFail(Context context, String ttsTimbre, String failcause) {
        if (context == null) {
            return;
        }
        Logger.d(TAG, "notifyCopyTtsTimbreFail-- ttsTimbre = " + ttsTimbre + "; failcause = "
                + failcause);
        Intent intent = new Intent(ACTION_ON_COPY_TTS_TIMBRE_FAIL);
        intent.putExtra(KEY_TTS_TIMBRE_NAME, ttsTimbre);
        intent.putExtra(KEY_TTS_FAILCAUSE, failcause);
        context.sendBroadcast(intent);
    }

}
