/**
 * Copyright (c) 2012-2015 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : GUIConfig.java
 * @ProjectName : uniCarGUI
 * @PakageName : com.unisound.unicar.gui.fm
 * @version : 1.0
 * @Author : Xiaodong.He
 * @CreateDate : 2015-7-20
 */
package com.unisound.unicar.gui.utils;

/**
 * GUI Function & UI display & App package configuration
 * 
 * @author xiaodong.He
 * 
 */
public class GUIConfig {

    /** is Test Version Configuration */
    public static boolean isTestVersion = true;

    public static final int TIME_AUTO_SHOW_HELP_TEXT = 3000;// 3s

    public static final int TIME_DELAY_AUTO_CONFIRM = 5000;// 5s

    public static final int TIME_DELAY_DISMISS_VIEW_ON_TTS_END = 5000;// 5s

    public static final long TIME_MIC_VOLUME_UPDATE = 90; //

    /**
     * available SDCard Size Limit for voice system, unit: MB
     */
    public static final int MIN_SIZE_SDCARD_AVAILABLE = 100;

    /**
     * available Internal Memory Size Limit for TTS system, unit: MB
     */
    public static final int MIN_SIZE_INTERNAL_MEMORY_AVAILABLE = 20;

    public static final int MAX_EXAMPLE_CONTACT_NUM = 10;


    public static final String ACTION_ON_CONTACT_DATA_DONE =
            "com.unisound.unicar.gui.ACTION_ON_CONTACT_DATA_DONE";

    // Function help
    public static final String ACTION_FUNCTION_HELP = "com.unisound.unicar.ACTION_FUNCTION_HELP";
    public static final String KEY_FUNCTION_HELP_TITLE = "FUNCTION_HELP_TITLE";
    public static final String KEY_FUNCTION_HELP_TYPE = "FUNCTION_HELP_TYPE";

    public static final int VALUE_FUNCTION_HELP_TYPE_BLUETOOTH = 1;
    public static final int VALUE_FUNCTION_HELP_TYPE_NAVIGATION = 2;
    public static final int VALUE_FUNCTION_HELP_TYPE_MUSIC = 3;
    public static final int VALUE_FUNCTION_HELP_TYPE_SETTING = 4;
    public static final int VALUE_FUNCTION_HELP_TYPE_WECHAT = 5;
    public static final int VALUE_FUNCTION_HELP_TYPE_NET_RADIO = 6;
    public static final int VALUE_FUNCTION_HELP_TYPE_WEATHER = 7;
    public static final int VALUE_FUNCTION_HELP_TYPE_STOCK = 8;
    public static final int VALUE_FUNCTION_HELP_TYPE_LOCAL_SEARCH = 9;
    public static final int VALUE_FUNCTION_HELP_TYPE_TRAFFIC = 10;
    public static final int VALUE_FUNCTION_HELP_TYPE_LIMIT = 11;
    public static final int VALUE_FUNCTION_HELP_TYPE_RADIO = 12;

    // SMS
    public static final String ACTION_SMS_SEND_SUCCESS =
            "com.unisound.unicar.ACTION_SMS_SEND_SUCCESS";
    public static final String ACTION_SMS_SEND_FAIL = "com.unisound.unicar.ACTION_SMS_SEND_FAIL";

    public static final int SMS_STATUS_SENDING = 0;
    public static final int SMS_STATUS_SEND_SUCCESS = 1;
    public static final int SMS_STATUS_SEND_FAIL = 2;

    // Setting activity
    public static final int ACTIVITY_REQUEST_CODE_CHOOSE_MAP = 1001;
    public static final int ACTIVITY_RESULT_CODE_SETTING_MAP_FINISH = 2001;

    // Device Configuration
    /**
     * Device Configuration :
     * 
     * @value true : StatusBar is visible on Screen Top
     * @value false : StatusBar is not visible on Screen Top (LS Device)
     */
    public static final boolean isStatusBarOnScreenTop = true;


    // UI display configuration
    /** GUI Configuration: is Show ASR Record Result */
    public static final boolean isShowASRRecordResult = true;

    /** GUI Configuration: is Show Change VersionMode button on Microphone control view */
    public static final boolean isShowChangeVersionModeBtn = true;

    /** GUI Configuration: is Show Main page Button Random Help Text */
    public static final boolean isShowMainButtonRandomHelpText = false;

    /** GUI Configuration: is Show Microphone Button when on Recognize status */
    public static final boolean isShowRecognizeMicBtn = false;

    /** GUI Configuration: is Show SMS Function Help */
    public static final boolean isShowSMSFunctionHelp = false;

    /** GUI Configuration: is Show SMS send Confirm Timer */
    public static final boolean isShowSmsConfirmTimer = false;

    /** GUI Configuration: is Show incoming SMS Reply button */
    public static final boolean isShowSmsReplyButton = false;


    // UI function configuration
    /** GUI Configuration: is allow UniCarGUI request as Android 4.4 default SMS App */
    public static final boolean isAllowGUIRequestAsDefaultSmsApp = false;

    /** GUI Configuration: is support more map setting */
    public static final boolean isSupportMoreMapSetting = false;

    /** GUI Configuration: is support Version Level setting */
    public static final boolean isSupporVersionLevelSetting = true;

    /** GUI Configuration: is support ONESHOT setting, replaced by isSupporVersionLevelSetting */
    public static final boolean isSupportOneShotSetting = true;

    /** GUI Configuration: is support AEC setting */
    public static final boolean isSupportAECSetting = true;

    /** GUI Configuration: is support updateWakeup word setting */
    public static final boolean isSupportUpdateWakeupWordSetting = true;

    /**
     * XD 20150811 added for bluetooth call LandSem:
     * "com.ls.bluetooth.bluetoothphone.DialpadActivity"
     */
    public static final String ACTIVITY_NAME_BLUETOOTH_CALL =
            "com.ls.bluetooth.bluetoothphone.DialpadActivity";

    /** XD 2016-2-4 added */
    public static final String ACTIVITY_NAME_UNICARNAVI_ROUTE =
            "com.unisound.unicar.navi.ui.RouteShowActivity";

    /** UniDrive VUI package name */
    public static final String PACKAGE_NAME_UNICAR_VUI = "com.unisound.unicar.framework";

    public static final String PACKAGE_NAME_KUWO_MUSIC = "cn.kuwo.kwmusiccar";

    /** UniCarNavi package name */
    public static final String PACKAGE_NAME_UNICAR_NAVI = "com.unisound.unicar.navi";

    /** UniCarFM package name */
    public static final String PACKAGE_NAME_UNICAR_FM = "com.unisound.unicar.fm";

    /** XMLY FM package name */
    public static final String PACKAGE_NAME_XMLY_FM = "com.ximalaya.ting.android.car";
    /** XMLY FM Main Activity */
    public static final String ACTIVITY_NAME_XMLY_FM_MAIN =
            "com.ximalaya.ting.android.car.activity.WelcomeActivity";

    public static final String ACTION_WAKEUP_WORDS_UPDATE =
            "com.unisound.unicar.ACTION_WAKEUP_WORDS_UPDATE";
}
