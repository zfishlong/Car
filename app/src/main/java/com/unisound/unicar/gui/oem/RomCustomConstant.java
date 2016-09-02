/**
 * Copyright (c) 2012-2015 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : RomCustomConstant.java
 * @ProjectName : UniCarGUI_series_asr_1123
 * @PakageName : com.unisound.unicar.gui.oem
 * @version : 1.0
 * @Author : Xiaodong.He
 * @CreateDate : 2015-12-2
 */
package com.unisound.unicar.gui.oem;

/**
 * CustomerConstant
 * 
 * @author xiaodong.he
 * @date 2015-12-2
 * 
 */
public interface RomCustomConstant {

    public static final String INTENT_BASE_ACTION = "com.unisound.intent.action.";

    /**
     * update contact. This is old action, need modify.
     */
    public static final String ACTION_CUSTOM_UPDATE_CONTACT =
            "cn.yunzhisheng.intent.action.custom.order.contact";

    /**
     * update conversation mode setting
     */
    public static final String ACTION_CUSTOM_SETTING_CONVERSATION_MODE = INTENT_BASE_ACTION
            + "custom.SETTING_CONVERSATION_MODE";

    public static final String KEY_CONVERSATION_MODE = "key_conversation_mode";

    public static final int VALUE_CONVERSATION_MODE_EXP = -1;

    public static final int VALUE_CONVERSATION_MODE_STANDARD = 0;

    public static final int VALUE_CONVERSATION_MODE_HIGH = 1;

}
