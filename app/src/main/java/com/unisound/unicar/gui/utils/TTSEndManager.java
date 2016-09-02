/**
 * Copyright (c) 2012-2016 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : TTSEndManager.java
 * @ProjectName : uniCarSolution_oneshot
 * @PakageName : com.unisound.unicar.gui.utils
 * @version : 1.0
 * @Author : Xiaodong.He
 * @CreateDate : 2016-1-25
 */
package com.unisound.unicar.gui.utils;

import android.content.Context;
import android.text.TextUtils;

import com.unisound.unicar.gui.uninavi.UniCarNaviConstant;
import com.unisound.unicar.gui.uninavi.UniCarNaviUtil;

/**
 * TTS End Manager
 * 
 * @author xiaodong.he
 * @date 2016-1-25
 */
public class TTSEndManager {

    private static final String TAG = TTSEndManager.class.getSimpleName();

    /**
     * 
     * @param context
     * @param ttsID
     */
    public static void onTTSPlayEnd(Context context, String ttsID) {
        if (null == context || TextUtils.isEmpty(ttsID)) {
            return;
        }
        Logger.d(TAG, "onTTSEnd---ttsID:" + ttsID);
        if (UniCarNaviConstant.CallbackConstant.VALUE_TTS_ID_SELECT_ROUTE_PLAN_AUTO.equals(ttsID)) {
            Logger.d(TAG, "uniCarNavi SELECT_ROUTE_PLAN_AUTO tts play end.");
            UniCarNaviUtil.sendOnTTSPlayEndAction(context, ttsID);
        } else if (UniCarNaviConstant.CallbackConstant.VALUE_TTS_ID_SELECT_ROUTE_PLAN.equals(ttsID)) {
            UniCarNaviUtil.sendOnTTSPlayEndAction(context, ttsID);
        }

    }

}
