package com.unisound.unicar.gui.preference;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.text.TextUtils;

import com.unisound.unicar.gui.R;
import com.unisound.unicar.gui.utils.JsonTool;
import com.unisound.unicar.gui.utils.Logger;

/**
 * User Preference Util
 * 
 * @author xiaodong
 * @date 20150714
 */
public class UserPerferenceUtil {

    private static final String TAG = UserPerferenceUtil.class.getSimpleName();

    private static final String SP_NAME = "unicar_user_settings";

    public static final String KEY_ENABLE_WAKEUP = "KEY_ENABLE_WAKEUP";
    public static final boolean VALUE_DEFAULT_WAKEUP = true;
    // debug switch
    public static final String KEY_LOGCAT_SWITCH = "KEY_LOGCAT_SWITCH";
    public static final boolean VALUE_DEFAULT_LOGCAT = false;

    // Map setting
    public static final String KEY_MAP = "KEY_MAP";
    /** Gao de */
    public static final int VALUE_MAP_AMAP = 1;
    public static final int VALUE_MAP_BAIDU = 2;
    public static final int VALUE_MAP_TUBA = 3;
    public static final int VALUE_MAP_DAODAOTONG = 4;

    // TTS speed setting
    public static final String KEY_TTS_SPEED = "KEY_TTS_SPEED";
    public static final int VALUE_TTS_SPEED_SLOWLY = -1;
    public static final int VALUE_TTS_SPEED_STANDARD = 0; // DEFAULT
    public static final int VALUE_TTS_SPEED_FAST = 1;

    // float mic setting
    public static final String KEY_ENABLE_FLOAT_MIC = "KEY_ENABLE_FLOAT_MIC";
    public static final boolean VALUE_ENABLE_FLOAT_MIC_DEFAULT = false;

    // oneShot setting
    public static final String KEY_ENABLE_ONESHOT = "KEY_ENABLE_ONESHOT";
    public static final boolean VALUE_ENABLE_ONESHOT_DEFAULT = false;

    public static final String KEY_VERSION_MODE = "KEY_VERSION_LEVEL";
    /** experience version: series asr & oneshot close */
    public static final int VALUE_VERSION_MODE_EXP = -1;
    /** standard version: oneshot close */
    public static final int VALUE_VERSION_MODE_STANDARD = 0; // DEFAULT
    /** high version: oneshot open */
    public static final int VALUE_VERSION_MODE_HIGH = 1;

    // TTS Timbre setting
    public static final String KEY_TTS_TIMBRE = "KEY_TTS_TIMBRE";
    public static final int VALUE_TTS_TIMBRE_STANDARD = 0; // DEFAULT
    public static final int VALUE_TTS_TIMBRE_SEXY = 1;
    public static final int VALUE_TTS_TIMBRE_AUTO = 2;

    // AEC setting
    public static final String KEY_ENABLE_AEC = "KEY_ENABLE_AEC";
    public static final boolean VALUE_ENABLE_AEC_DEFAULT = false;

    // SUB setting
    public static final String KEY_ENABLE_SUB = "KEY_ENABLE_SUB";
    public static final boolean VALUE_ENABLE_SUB_DEFAULT = true;

    // gps setting
    public static final String KEY_ENABLE_GPS = "KEY_ENABLE_GPS";
    public static final boolean VALUE_ENABLE_GPS_DEFAULT = true;

    private static final String KEY_INPUT_VIEW_X = "KEY_INPUT_VIEW_X";
    private static final String KEY_INPUT_VIEW_Y = "KEY_INPUT_VIEW_Y";

    // WakeUp word
    public static final String KEY_WAKEUP_WORDS = "KEY_WAKEUP_WORDS";

    // UUID
    public static final String KEY_UUID = "KEY_UUID";

    // Home & Company Location JSON
    public static final String KEY_HOME_LOCATION = "KEY_HOME_LOCATION";
    public static final String KEY_COMPANY_LOCATION = "KEY_COMPANY_LOCATION";

    public static final String KEY_APP_LIMIT_TIME = "KEY_APP_TIME_LIMIT";

    /**
     * 
     * @param context
     * @param listener
     */
    public static void registerOnSharedPreferenceChangeListener(Context context,
            OnSharedPreferenceChangeListener listener) {
        SharedPreferencesHelper.getInstance(context, SP_NAME)
                .registerOnSharedPreferenceChangeListener(listener);
    }

    /**
     * 
     * @param context
     * @param listener
     */
    public static void unregisterOnSharedPreferenceChangeListener(Context context,
            OnSharedPreferenceChangeListener listener) {
        SharedPreferencesHelper.getInstance(context, SP_NAME)
                .unregisterOnSharedPreferenceChangeListener(listener);
    }

    public static void setWakeupEnable(Context context, boolean isEnable) {
        Logger.d(TAG, "setWakeupEnable---isEnable = " + isEnable + "----Begin--");
        SharedPreferencesHelper.getInstance(context, SP_NAME).saveBooleanValue(KEY_ENABLE_WAKEUP,
                isEnable);
        Logger.d(TAG, "setWakeupEnable---isEnable = " + isEnable + "----End--");
    }

    public static boolean isWakeupEnable(Context context) {
        SharedPreferencesHelper sph = SharedPreferencesHelper.getInstance(context, SP_NAME);
        return sph.getBooleanValue(KEY_ENABLE_WAKEUP, VALUE_DEFAULT_WAKEUP);
    }

    public static void setMapChoose(Context context, int mapType) {
        boolean status =
                SharedPreferencesHelper.getInstance(context, SP_NAME)
                        .saveIntValue(KEY_MAP, mapType);
        Logger.d(TAG, "!--->setMapChoose--save status = " + status);
    }

    public static int getMapChoose(Context context) {
        SharedPreferencesHelper sph = SharedPreferencesHelper.getInstance(context, SP_NAME);
        return sph.getIntValue(KEY_MAP, VALUE_MAP_AMAP);
    }

    public static void setFloatMicEnable(Context context, boolean isEnable) {
        SharedPreferencesHelper.getInstance(context, SP_NAME).saveBooleanValue(
                KEY_ENABLE_FLOAT_MIC, isEnable);
    }

    public static boolean getFloatMicEnable(Context context) {
        SharedPreferencesHelper sph = SharedPreferencesHelper.getInstance(context, SP_NAME);
        return sph.getBooleanValue(KEY_ENABLE_FLOAT_MIC, VALUE_ENABLE_FLOAT_MIC_DEFAULT);
    }

    public static void setOneShotEnable(Context context, boolean isEnable) {
        SharedPreferencesHelper.getInstance(context, SP_NAME).saveBooleanValue(KEY_ENABLE_ONESHOT,
                isEnable);
    }

    public static boolean getOneShotEnable(Context context) {
        SharedPreferencesHelper sph = SharedPreferencesHelper.getInstance(context, SP_NAME);
        return sph.getBooleanValue(KEY_ENABLE_ONESHOT, VALUE_ENABLE_ONESHOT_DEFAULT);
    }

    /**
     * 
     * @param context
     * @param level: {@link UserPerferenceUtil#VALUE_VERSION_MODE_EXP} or
     *        {@link UserPerferenceUtil#VALUE_VERSION_MODE_STANDARD} or
     *        {@link UserPerferenceUtil#VALUE_VERSION_MODE_HIGH}
     */
    public static void setVersionMode(Context context, int level) {
        SharedPreferencesHelper.getInstance(context, SP_NAME).saveIntValue(KEY_VERSION_MODE, level);
    }

    /**
     * 
     * @param context
     * @return {@link UserPerferenceUtil#VALUE_VERSION_MODE_EXP} or
     *         {@link UserPerferenceUtil#VALUE_VERSION_MODE_STANDARD} or
     *         {@link UserPerferenceUtil#VALUE_VERSION_MODE_HIGH}
     */
    public static int getVersionMode(Context context) {
        SharedPreferencesHelper sph = SharedPreferencesHelper.getInstance(context, SP_NAME);
        return sph.getIntValue(KEY_VERSION_MODE, VALUE_VERSION_MODE_STANDARD);
    }

    /**
     * 
     * @param context
     * @param level: {@link UserPerferenceUtil#VALUE_TTS_TIMBRE_STANDARD} or
     *        {@link UserPerferenceUtil#VALUE_TTS_TIMBRE_SEXY}
     */
    public static void setTtsTimbre(Context context, int level) {
        SharedPreferencesHelper.getInstance(context, SP_NAME).saveIntValue(KEY_TTS_TIMBRE, level);
    }

    /**
     * 
     * @param context
     * @return {@link UserPerferenceUtil#VALUE_TTS_TIMBRE_STANDARD} or
     *         {@link UserPerferenceUtil#VALUE_TTS_TIMBRE_SEXY}
     */
    public static int getTtsTimbre(Context context) {
        SharedPreferencesHelper sph = SharedPreferencesHelper.getInstance(context, SP_NAME);
        return sph.getIntValue(KEY_TTS_TIMBRE, VALUE_TTS_TIMBRE_STANDARD);
    }

    public static void setAECEnable(Context context, boolean isEnable) {
        SharedPreferencesHelper.getInstance(context, SP_NAME).saveBooleanValue(KEY_ENABLE_AEC,
                isEnable);
    }

    public static boolean getAECEnable(Context context) {
        SharedPreferencesHelper sph = SharedPreferencesHelper.getInstance(context, SP_NAME);
        return sph.getBooleanValue(KEY_ENABLE_AEC, VALUE_ENABLE_AEC_DEFAULT);
    }

    public static boolean getSubEnable(Context context) {
        SharedPreferencesHelper sph = SharedPreferencesHelper.getInstance(context, SP_NAME);
        return sph.getBooleanValue(KEY_ENABLE_SUB, VALUE_ENABLE_SUB_DEFAULT);
    }

    public static void setSubEnable(Context context, boolean isEnable) {
        SharedPreferencesHelper.getInstance(context, SP_NAME).saveBooleanValue(KEY_ENABLE_SUB,
                isEnable);
    }

    public static boolean getGpsEnable(Context context) {
        SharedPreferencesHelper sph = SharedPreferencesHelper.getInstance(context, SP_NAME);
        return sph.getBooleanValue(KEY_ENABLE_SUB, VALUE_ENABLE_SUB_DEFAULT);
    }

    public static void setGpsEnable(Context context, boolean isEnable) {
        SharedPreferencesHelper.getInstance(context, SP_NAME).saveBooleanValue(KEY_ENABLE_SUB,
                isEnable);
    }

    public static void setTTSSpeed(Context context, int speed) {
        SharedPreferencesHelper.getInstance(context, SP_NAME).saveIntValue(KEY_TTS_SPEED, speed);
    }

    public static int getTTSSpeed(Context context) {
        SharedPreferencesHelper sph = SharedPreferencesHelper.getInstance(context, SP_NAME);
        return sph.getIntValue(KEY_TTS_SPEED, VALUE_TTS_SPEED_STANDARD);
    }

    public static void setInputViewX(Context context, int x) {
        SharedPreferencesHelper.getInstance(context, SP_NAME).saveIntValue(KEY_INPUT_VIEW_X, x);
    }

    public static int getInputViewX(Context context, int defaultX) {
        SharedPreferencesHelper sph = SharedPreferencesHelper.getInstance(context, SP_NAME);
        return sph.getIntValue(KEY_INPUT_VIEW_X, defaultX);
    }

    public static void setInputViewY(Context context, int y) {
        SharedPreferencesHelper.getInstance(context, SP_NAME).saveIntValue(KEY_INPUT_VIEW_Y, y);
    }

    public static int getInputViewY(Context context, int defaultY) {
        SharedPreferencesHelper sph = SharedPreferencesHelper.getInstance(context, SP_NAME);
        return sph.getIntValue(KEY_INPUT_VIEW_Y, defaultY);
    }

    public static void setWakeupWords(Context context, String wakeupWords) {
        Logger.d(TAG, "setWakeupWords---wakeupWords:" + wakeupWords);
        SharedPreferencesHelper.getInstance(context, SP_NAME).saveStringValue(KEY_WAKEUP_WORDS,
                wakeupWords);
    }

    /**
     * get WakeUp Words
     * 
     * @param context
     * @return
     */
    public static String getWakeupWords(Context context) {
        SharedPreferencesHelper sph = SharedPreferencesHelper.getInstance(context, SP_NAME);
        String wakeupWords =
                sph.getStringValue(KEY_WAKEUP_WORDS,
                        context.getString(R.string.wakeup_word_default));
        return wakeupWords;
    }

    /**
     * get first WakeUp Word
     * 
     * @param context
     * @return
     */
    public static String getWakeupWord(Context context) {
        String wakeupWord = context.getString(R.string.wakeup_word_default);
        String wakeupWords = getWakeupWords(context);
        if (!TextUtils.isEmpty(wakeupWords)) {
            String[] wakeupWordArray = wakeupWords.split("#");
            if (wakeupWordArray.length > 0) {
                wakeupWord = wakeupWordArray[0].trim();
            }
        }
        return wakeupWord;
    }

    /**
     * debug
     * 
     * @param context
     * @param isEnable
     */

    public static void setLogcatEnable(Context context, boolean isEnable) {
        SharedPreferencesHelper.getInstance(context, SP_NAME).saveBooleanValue(KEY_LOGCAT_SWITCH,
                isEnable);
    }

    public static boolean getLogcatEnable(Context context) {
        SharedPreferencesHelper sph = SharedPreferencesHelper.getInstance(context, SP_NAME);
        return sph.getBooleanValue(KEY_LOGCAT_SWITCH, VALUE_DEFAULT_LOGCAT);
    }

    /**
     * 
     * @param context
     * @param uuid
     */
    public static void setUuid(Context context, String uuid) {
        Logger.d(TAG, "setUuid---uuid:" + uuid);
        SharedPreferencesHelper.getInstance(context, SP_NAME).saveStringValue(KEY_UUID, uuid);
    }

    /**
     * 
     * @param context
     * @return
     */
    public static String getUuid(Context context) {
        SharedPreferencesHelper sph = SharedPreferencesHelper.getInstance(context, SP_NAME);
        return sph.getStringValue(KEY_UUID, "");
    }

    /**
     * 
     * @param context
     * @param date
     */
    public static void setAppLimitTime(Context context, String date) {
        Logger.d("setAppLimitTime---date:" + date);
        SharedPreferencesHelper.getInstance(context, SP_NAME).saveStringValue(KEY_APP_LIMIT_TIME,
                date);
    }

    /**
     * 
     * @param context
     * @return
     */
    public static String getAppLimitTime(Context context) {
        SharedPreferencesHelper sph = SharedPreferencesHelper.getInstance(context, SP_NAME);
        return sph.getStringValue(KEY_APP_LIMIT_TIME, "");
    }

    /**
     * 
     * @param context
     * @param locationJson
     */
    public static void setHomeLocation(Context context, String locationJson) {
        Logger.d(TAG, "setHomeLocation--- locationJson:" + locationJson);
        SharedPreferencesHelper.getInstance(context, SP_NAME).saveStringValue(KEY_HOME_LOCATION,
                locationJson);
    }

    /**
     * 
     * @param context
     * @return locationJson / ""
     */
    public static String getHomeLocation(Context context) {
        SharedPreferencesHelper sph = SharedPreferencesHelper.getInstance(context, SP_NAME);
        return sph.getStringValue(KEY_HOME_LOCATION, "");
    }


    /**
     * 
     * @param context
     * @param locationJson
     */
    public static void setCompanyLocation(Context context, String locationJson) {
        Logger.d(TAG, "setCompanyLocation--- locationJson:" + locationJson);
        SharedPreferencesHelper.getInstance(context, SP_NAME).saveStringValue(KEY_COMPANY_LOCATION,
                locationJson);
    }

    /**
     * 
     * @param context
     * @return locationJson / ""
     */
    public static String getCompanyLocation(Context context) {
        SharedPreferencesHelper sph = SharedPreferencesHelper.getInstance(context, SP_NAME);
        return sph.getStringValue(KEY_COMPANY_LOCATION, "");
    }

    /**
     * 
     * @param locationInfoJson
     * @return
     */
    public static String getAddressName(String locationInfoJson) {
        String addressName = "";
        if (TextUtils.isEmpty(locationInfoJson)) {
            return "";
        }
        JSONObject locationJsonObj = JsonTool.parseToJSONObject(locationInfoJson);
        try {
            addressName = locationJsonObj.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return addressName;
    }



}
