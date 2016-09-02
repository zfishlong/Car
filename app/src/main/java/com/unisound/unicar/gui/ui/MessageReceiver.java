package com.unisound.unicar.gui.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.unisound.unicar.gui.fm.UniDriveFmGuiService;
import com.unisound.unicar.gui.fm.XmFmGuiService;
import com.unisound.unicar.gui.utils.Logger;

/**
 * 
 * @author xiaodong
 * 
 */
public class MessageReceiver extends BroadcastReceiver {

    private static final String TAG = "MessageReceiver";

    public static final String ACTION_START_TALK = "com.unisound.intent.action.START_TALK";

    public static final String ACTION_MUSIC_DONE = "com.unisound.intent.action.MUSIC_DONE";
    // public static final String ACTION_STOP_TALK = "com.unisound.intent.action.STOP_TALK";
    // public static final String ACTION_CANCEL_TALK = "com.unisound.intent.action.CANCEL_TALK";

    public static final String ACTION_COMPILE_GRAMMER =
            "com.unisound.intent.action.COMPILE_GRAMMER";

    public static final String ACTION_START_CALL_OUT = "com.unisound.intent.action.MAIN_CALL_OUT";
    public static final String ACTION_START_NAVIGATION =
            "com.unisound.intent.action.MAIN_NAVIGATION";
    public static final String ACTION_START_MUSIC = "com.unisound.intent.action.MAIN_MUSIC";
    public static final String ACTION_START_LOCAL_SEARCH =
            "com.unisound.intent.action.MAIN_LOCAL_SEARCH";

    public static final String ACTION_PLAY_TTS = "com.unisound.intent.action.PLAY_TTS";
    public static final String KEY_EXTRA_TTS_TEXT = "TTS_TEXT";
    public static final String KEY_EXTRA_TTS_ID = "TTS_ID";
    public static final String KEY_EXTRA_TTS_FROM = "TTS_FROM";
    public static final String KEY_EXTRA_TTS_RECOGNIZER_TYPE = "RECOGNIZER_TYPE";

    /** XD added 20150923 for uniDriveFm */
    public static final String ACTION_START_UNIDRIVE_FM =
            "com.unisound.intent.action.START_UNIDRIVE_FM";
    public static final String ACTION_START_XM_FM = "com.unisound.intent.action.START_XM_FM";

    public static final String KEY_EXTRA_FM_SEARCH_TYPE = "FM_SEARCH_TYPE";
    public static final String KEY_EXTRA_FM_ARTIST = "FM_ARITST";
    public static final String KEY_EXTRA_FM_CATEGORY = "FM_CATEGORY";
    public static final String KEY_EXTRA_FM_KEYWORD = "FM_KEYWORD";
    public static final String KEY_EXTRA_FM_EPISODE = "FM_EPISODE";

    // 开始休眠
    public static final String ACTION_START_DORMANT = "com.unisound.intent.action.START_DORMANT";


    // 停止休眠
    public static final String ACTION_STOP_DORMANT = "com.unisound.intent.action.STOP_DORMANT";



    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.d(TAG, "onReceive:intent " + intent);
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            intent.setClass(context, WindowService.class);
            context.startService(intent);
        } else if (ACTION_START_TALK.equals(action)) {
            intent.setClass(context, WindowService.class);
            intent.setAction(MessageReceiver.ACTION_START_TALK);
            context.startService(intent);
        } else if (ACTION_PLAY_TTS.equals(action)) {
            // XD added 20150914
            intent.setClass(context, WindowService.class);
            intent.setAction(MessageReceiver.ACTION_PLAY_TTS);
            context.startService(intent);
        } else if (ACTION_START_UNIDRIVE_FM.equals(action)) {
            intent.setClass(context, UniDriveFmGuiService.class);
            intent.setAction(ACTION_START_UNIDRIVE_FM);
            context.startService(intent);
        } else if (ACTION_START_XM_FM.equals(action)) {
            intent.setClass(context, XmFmGuiService.class);
            intent.setAction(ACTION_START_XM_FM);
            context.startService(intent);
        } else if (ACTION_STOP_DORMANT.equals(action)) {
            intent.setClass(context, WindowService.class);
            context.startService(intent);
        }
    }
}
