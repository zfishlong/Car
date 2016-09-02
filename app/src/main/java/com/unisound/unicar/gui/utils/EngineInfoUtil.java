/**
 * Copyright (c) 2012-2016 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : EngineInfoUtil.java
 * @ProjectName : uniCarSolution
 * @PakageName : com.unisound.unicar.gui.utils
 * @version : 1.0
 * @Author : Xiaodong.He
 * @CreateDate : 2016-2-16
 */
package com.unisound.unicar.gui.utils;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.unisound.unicar.gui.R;
import com.unisound.unicar.gui.preference.SessionPreference;
import com.unisound.unicar.gui.preference.UserPerferenceUtil;

/**
 * @author xiaodong.he
 * 
 */
public class EngineInfoUtil {

    public static final String TAG = EngineInfoUtil.class.getSimpleName();

    public static boolean isASREngineDueDate = false;


    /**
     * updateGuiVersionByEngineInfo
     * 
     * @author xiaodong.he
     * @date 2016-01-11
     * @param context
     * @param infoJson
     */
    public static void updateGuiVersionByEngineInfo(Context context, JSONObject infoJson) {
        if (infoJson == null) {
            return;
        }
        try {
            int engineInfoType = infoJson.getInt(SessionPreference.KEY_ENGINE_INFO_ASRKEY);
            String dueDateStr = infoJson.getString(SessionPreference.KEY_ENGINE_INFO_ASRVALUE);
            // "2016-9-30"
            switch (engineInfoType) {
                case SessionPreference.VALUE_ENGINE_INFO_ASRKEY_NO_LIMIT:
                    GUIConfig.isTestVersion = false;
                    isASREngineDueDate = false;
                    break;
                case SessionPreference.VALUE_ENGINE_INFO_ASRKEY_TIME_LIMIT:
                    GUIConfig.isTestVersion = true;
                    checkDueDate(context, dueDateStr);
                    break;
                case SessionPreference.VALUE_ENGINE_INFO_ASRKEY_PACKAGE_LIMIT:
                    GUIConfig.isTestVersion = false;
                    isASREngineDueDate = false;
                    break;
                case SessionPreference.VALUE_ENGINE_INFO_ASRKEY_TIME_PACKAGE_LIMIT:
                    GUIConfig.isTestVersion = true;
                    checkDueDate(context, dueDateStr);
                    break;
                default:
                    break;
            }
            Logger.d(TAG, "updateGuiVersionByEngineInfo---engineType = " + engineInfoType
                    + "; dueDateStr:" + dueDateStr + "; isASREngineDueDate: " + isASREngineDueDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param context
     * @param data
     */
    private static void checkDueDate(Context context, String data) {
        Date dueDate = DateUtil.string2Date(data, "yyyy-MM-dd");
        isASREngineDueDate = DateUtil.isDueDate(dueDate);

        String dateStr =
                DateUtil.date2String(dueDate, context.getString(R.string.date_format_show));
        UserPerferenceUtil.setAppLimitTime(context, dateStr);
    }
}
