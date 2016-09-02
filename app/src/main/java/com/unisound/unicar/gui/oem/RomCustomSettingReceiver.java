/**
 * Copyright (c) 2012-2015 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : RomCustomSettingReceiver.java
 * @ProjectName : UniCarGUI
 * @PakageName : com.unisound.unicar.gui.oem
 * @version : 1.0
 * @Author : Xiaodong.He
 * @CreateDate : 2015-12-2
 */
package com.unisound.unicar.gui.oem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.unisound.unicar.gui.preference.UserPerferenceUtil;
import com.unisound.unicar.gui.ui.WindowService;
import com.unisound.unicar.gui.utils.Logger;

/**
 * Rom update UniCarGUI setting
 * 
 * @author xiaodong.he
 * @date 2015-12-2
 */
public class RomCustomSettingReceiver extends BroadcastReceiver {

    private static final String TAG = RomCustomSettingReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (RomCustomConstant.ACTION_CUSTOM_SETTING_CONVERSATION_MODE.equals(action)) {
            int mode = intent.getIntExtra("key_conversation_mode", 0);
            updateConversionMode(context, mode);
        }
    }

    /**
     * update ConversionMode
     * 
     * @author xiaodong.he
     * @date 2015-12-2
     * @param mode
     */
    private void updateConversionMode(Context context, int mode) {
        int setValue = -100;
        switch (mode) {
            case RomCustomConstant.VALUE_CONVERSATION_MODE_EXP:
                setValue = UserPerferenceUtil.VALUE_VERSION_MODE_EXP;
                break;
            case RomCustomConstant.VALUE_CONVERSATION_MODE_STANDARD:
                setValue = UserPerferenceUtil.VALUE_VERSION_MODE_STANDARD;
                break;
            case RomCustomConstant.VALUE_CONVERSATION_MODE_HIGH:
                setValue = UserPerferenceUtil.VALUE_VERSION_MODE_HIGH;
                break;
            default:
                break;
        }
        Logger.d(TAG, "updateConversionMode---mode =" + mode + "; setValue = " + setValue);
        if (setValue != -100) {
            UserPerferenceUtil.setVersionMode(context, setValue);
            sendVersionLevelConfigure(context);
        }
    }

    /**
     * send VersionLevel Configure
     * 
     * @author xiaodong.he
     * @date 2015-12-2
     * @param context
     */
    private void sendVersionLevelConfigure(Context context) {
        sendConfigure(context, WindowService.ACTION_SET_VERSION_LEVEL);
    }

    /**
     * 
     * @author xiaodong.he
     * @date 2015-12-2
     * @param action
     */
    private void sendConfigure(Context context, String action) {
        Logger.d(TAG, "!--->---sendConfigure-----");
        Intent intent = new Intent(context, WindowService.class);
        intent.setAction(action);
        context.startService(intent);
    }
}
