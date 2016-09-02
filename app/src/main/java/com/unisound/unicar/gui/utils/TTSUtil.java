/**
 * Copyright (c) 2012-2015 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : TTSUtil.java
 * @ProjectName : UniCarGUI_series_asr_1123
 * @PakageName : com.unisound.unicar.gui.utils
 * @version : 1.0
 * @Author : Xiaodong.He
 * @CreateDate : 2015-12-8
 */
package com.unisound.unicar.gui.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.unisound.unicar.gui.preference.SessionPreference;
import com.unisound.unicar.gui.ui.MessageReceiver;
import com.unisound.unicar.gui.ui.WindowService;

/**
 * TTSUtil
 * 
 * @author xiaodong.he
 * @date 2015-12-8
 */
public class TTSUtil {

    private static final String TAG = TTSUtil.class.getSimpleName();

    public static final String TTS_SETTING_MAP_BAIDU = "导航已切换为百度地图";
    public static final String TTS_SETTING_MAP_AMAP = "导航已切换为高德地图";
    public static final String TTS_SETTING_FLOAT_MIC_OPEN = "悬浮麦克风已开启";
    public static final String TTS_SETTING_FLOAT_MIC_CLOSE = "悬浮麦克风已关闭";

    public static final String TTS_SETTING_SUB_OPEN = "订阅早报已打开";
    public static final String TTS_SETTING_SUB_CLOSE = "订阅早报已关闭";

    public static final String TTS_SETTING_GPS_OPEN = "上传防盗信息已打开";
    public static final String TTS_SETTING_GPS_CLOSE = "上传防盗信息已关闭";

    /**
     * playTTS WakeUp
     * 
     * @param context
     * @param text
     */
    public static void playTTSWakeUp(Context context, String text) {
        if (context == null) {
            return;
        }
        playTTS(context, text, "", SessionPreference.PARAM_TTS_FROM_UNICARGUI,
                SessionPreference.PARAM_VALUE_TTS_END_WAKEUP);
    }

    /**
     * 
     * @param context
     * @param text
     * @param recognizerType
     */
    public static void playTTS(Context context, String text, String recognizerType) {
        if (context == null) {
            return;
        }
        playTTS(context, text, "", SessionPreference.PARAM_TTS_FROM_UNICARGUI, recognizerType);
    }

    /**
     * playTTS
     * 
     * @date 2016-1-25
     * 
     * @param context
     * @param text
     * @param ttsID
     * @param ttsFrom: {@link SessionPreference#SessionPreference#PARAM_TTS_FROM_UNICARGUI} 、
     *        {@link SessionPreference#SessionPreference#PARAM_TTS_FROM_UNICARNAVI}
     * @param recognizerType: {@link SessionPreference#PARAM_VALUE_TTS_END_WAKEUP}、
     *        {@link SessionPreference#PARAM_VALUE_TTS_END_RECOGNIZER}
     */
    public static void playTTS(Context context, String text, String ttsID, String ttsFrom,
            String recognizerType) {
        if (context == null) {
            return;
        }
        Logger.d(TAG, "playTTS: text:" + text + "; ttsID:" + ttsID + "; ttsFrom:" + ttsFrom
                + "; recognizerType: " + recognizerType);
        Intent intent = new Intent(MessageReceiver.ACTION_PLAY_TTS);
        intent.putExtra(MessageReceiver.KEY_EXTRA_TTS_TEXT, text);
        if (!TextUtils.isEmpty(ttsID)) {
            intent.putExtra(MessageReceiver.KEY_EXTRA_TTS_ID, ttsID);
        }
        intent.putExtra(MessageReceiver.KEY_EXTRA_TTS_FROM, ttsFrom);
        intent.putExtra(MessageReceiver.KEY_EXTRA_TTS_RECOGNIZER_TYPE, recognizerType);
        intent.setClass(context, WindowService.class);
        context.startService(intent);
    }
}
