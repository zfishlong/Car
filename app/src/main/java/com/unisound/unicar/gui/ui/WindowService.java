/**
 * Copyright (c) 2012-2015 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 *
 * @FileName : WindowService.java
 * @ProjectName : uniCarGUI
 * @PakageName : com.unisound.unicar.gui.ti
 * @version : 1.0
 * @Author : Xiaodong.He
 * @CreateDate : 2015-6-9
 */
package com.unisound.unicar.gui.ui;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import cn.yunzhisheng.common.PinyinConverter;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.android.kwmusic.KWMusicService;
import com.google.gson.Gson;
import com.unisound.unicar.framework.service.IMessageRouterCallback;
import com.unisound.unicar.framework.service.IMessageRouterService;
import com.unisound.unicar.gui.R;
import com.unisound.unicar.gui.application.CrashApplication;
import com.unisound.unicar.gui.data.interfaces.IBaseListener;
import com.unisound.unicar.gui.data.operation.AppsDataModel;
import com.unisound.unicar.gui.data.operation.ContactDataModel;
import com.unisound.unicar.gui.data.operation.MediaDataModel;
import com.unisound.unicar.gui.fm.UniDriveFmGuiService;
import com.unisound.unicar.gui.fm.XmFmGuiService;
import com.unisound.unicar.gui.location.interfaces.ILocationListener;
import com.unisound.unicar.gui.location.operation.LocationModelProxy;
import com.unisound.unicar.gui.model.LocationInfo;
import com.unisound.unicar.gui.msg.ISystemCallTransitionListener;
import com.unisound.unicar.gui.msg.SystemCallTransition;
import com.unisound.unicar.gui.oem.RomCustomerProcessing;
import com.unisound.unicar.gui.preference.Constant;
import com.unisound.unicar.gui.preference.PrivatePreference;
import com.unisound.unicar.gui.preference.SessionPreference;
import com.unisound.unicar.gui.preference.UserPerferenceUtil;
import com.unisound.unicar.gui.pushbean.GpsInfo;
import com.unisound.unicar.gui.pushbean.PushModle;
import com.unisound.unicar.gui.pushserver.PushBlockQueue;
import com.unisound.unicar.gui.session.GUISessionManager;
import com.unisound.unicar.gui.uninavi.UniCarNaviConstant;
import com.unisound.unicar.gui.uninavi.UniCarNaviGuiService;
import com.unisound.unicar.gui.utils.AppManager;
import com.unisound.unicar.gui.utils.AudioFocusHelper;
import com.unisound.unicar.gui.utils.DeviceTool;
import com.unisound.unicar.gui.utils.EngineInfoUtil;
import com.unisound.unicar.gui.utils.ErrorUtil;
import com.unisound.unicar.gui.utils.GUIConfig;
import com.unisound.unicar.gui.utils.GuiProtocolUtil;
import com.unisound.unicar.gui.utils.GuiSettingUpdateUtil;
import com.unisound.unicar.gui.utils.JsonTool;
import com.unisound.unicar.gui.utils.Logger;
import com.unisound.unicar.gui.utils.PackageUtil;
import com.unisound.unicar.gui.utils.TTSEndManager;
import com.unisound.unicar.gui.utils.Util;
import com.unisound.unicar.gui.view.EditLocationPopWindow;
import com.unisound.unicar.gui.view.EditLocationPopWindow.OnEditLocationPopListener;
import com.unisound.unicar.gui.view.EditWakeupWordPopWindow;
import com.unisound.unicar.gui.view.EditWakeupWordPopWindow.OnEditWakeupwordPopListener;
import com.unisound.unicar.gui.view.FloatMicView;
import com.unisound.unicar.gui.view.MicrophoneControl;
import com.unisound.unicar.gui.view.MicrophoneControl.MicrophoneViewClickListener;
import com.unisound.unicar.gui.view.MicrophoneControlDoresoView;
import com.unisound.unicar.gui.view.PushView;
import com.unisound.unicar.gui.view.SessionContainer;
import com.unisound.unicar.gui.view.SessionLinearLayout;
import com.unisound.unicar.gui.view.SessionLinearLayout.DispatchKeyEventListener;
import com.unisound.unicar.gui.view.SessionLinearLayout.OnTouchEventListener;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint("InlinedApi")

public class WindowService extends Service {

    private static final String TAG = "WindowService";

    // XD added 20160229
    public static final String ACTION_START_WINDOWSERVICE = "com.unisound.unicar.gui.ui.START_WINDOWSERVICE";
    public static final String KEY_IS_COMPILE_FINISH = "isCompileFinish";
    public static final String ACTION_SET_WAKEUP = "com.unisound.unicar.gui.ACTION.SET_WAKEUP";
    public static final String ACTION_SET_TTSSPEED = "com.unisound.unicar.gui.ACTION.SET_TTSSPEED";
    public static final String ACTION_SET_GPS = "com.unisound.unicar.gui.ACTION.SET_GPS";
    public static final String ACTION_SET_ONESHOT = "com.unisound.unicar.gui.ACTION.SET_ONESHOT";
    public static final String ACTION_SET_LOGCAT = "com.unisound.unicar.gui.ACTION.SET_LOGCAT";
    public static final String ACTION_SET_VERSION_LEVEL = "com.unisound.unicar.gui.ACTION.SET_VERSION_LEVEL";
    public static final String ACTION_SET_TTS_TIMBRE = "com.unisound.unicar.gui.ACTION.SET_TTS_TIMBRE";
    public static final String ACTION_SET_AEC = "com.unisound.unicar.gui.ACTION.SET_AEC";
    public static final String KEY_SET_TTSSPEED = "SET_TTSSPEED";

    /* XD added 20160128 */
    public static final String ACTION_SET_FAVORITE_ADDRESS = "com.unisound.unicar.gui.ACTION.UPDATE_FAVORITE_ADDRESS";

    // XD added
    public static final String ACTION_START_REQUEST_MAKE_FINISHED = "com.unisound.unicar.gui.ACTION.REQUEST_MAKE_FINISHED";

    // XD added 2016-1-18
    public static final String ACTION_REQUEST_TTS_TIMBRE_LIST = "com.unisound.unicar.gui.ACTION.REQUEST_TTS_TIMBRE_LIST";

    // XD added 2016-1-21 for sub service call back event to VUI
    public static final String ACTION_SEND_PROTOCAL_EVENT = "com.unisound.unicar.gui.ACTION.SEND_PROTOCAL_EVENT";

    public static final String ACTION_CUSTOMFROMBT = "com.unisound.unicar.customFromBt";

    private static final String ACTION_MSG_MESSAGEROUTER_START = "com.unisound.unicar.messagerouter.start";


    public static final String ACTION_UPDATE_WAKEUP_WORD =
            "com.unisound.unicar.gui.ACTION.UPDATE_WAKEUP_WORD";
    public static final String EXTRA_KEY_WAKEUP_WORD = "WAKEUP_WORD";

    public static final String ACTION_UPDATE_LOCATION =
            "com.unisound.unicar.gui.ACTION.UPDATE_LOCATION";

    public static final String EXTRA_KEY_LOCATION = "LOCATION";

    public static final String EXTRA_KEY_DISMISS_FLOAT_WINDOW = "DISMISS_FLOAT_WINDOW";

    public static final int MSG_DISMISS_WELCOME_ACTIVITY = 1001;
    public static final int MSG_SET_STATE = 2001;
    public static final int MSG_ON_RECORDING_START = 2002;
    public static final int MSG_ON_RECORDING_STOP = 2003;
    public static final int MSG_ON_SESSION_PROTOCAL = 2004;
    public static final int MSG_IS_ASR_COMPILE_DONE = 2005;
    public static final int MSG_ON_CONTROL_WAKEUP_SUCCESS = 2006;
    public static final int MSG_ON_TTS_PLAY_END = 2007;
    public static final int MSG_ON_RECORDING_PREPARED = 2008;
    public static final int MSG_ON_RECORDING_RESULT = 2009;
    public static final int MSG_ON_RECORDING_EXCEPTION = 2010; // XD 20150807 added
    public static final int MSG_ON_RECOGNIZER_TIMEOUT = 2011;
    public static final int MSG_ON_CTT_CANCEL = 2012;
    public static final int MSG_ON_ONESHOT_RECOGNIZER_TIMEOUT = 2013;
    public static final int MSG_ON_START_RECORDING_FAKE_ANIMATION = 2014; // XD added 20151015
    public static final int MSG_ON_GET_WAKEUP_WORDS = 2015; // XD added 20151019
    public static final int MSG_ON_CLICK_MAIN_ACTION_BUTTON = 2016;// add tyz 20151020
    public static final int MSG_ON_UPDATE_WAKEUP_WORDS_STATUS = 2017;
    public static final int MSG_ON_RECORDING_IDLE = 2018;
    public static final int MSG_ON_GET_UUID = 2019;

    public static final int MSG_ON_SWITCH_TTS_DONE = 2020;
    public static final int MSG_ON_GET_TTS_MODEL_LIST = 2021;
    public static final int MSG_ON_COPY_TTS_MODEL_SUCCESS = 2022;
    public static final int MSG_ON_COPY_TTS_MODEL_FAIL = 2023;
    public static final int MSG_ON_ADDED_FAVORITE_ADDRESS = 2024;
    public static final int MSG_ON_ASR_ENGINE_DUEDATE = 2025;// XD added 20160216

    public static final int MSG_ON_SEND_PUSH_EVENT = 2026;

    public static final int MSG_ON_SHOW_BIND_INFO = 2027;

    /* < XD 20150813 added Begin */
    public static final int MSG_GUI_CANCEL_SESSION = 3100;
    public static final int MSG_GUI_CANCEL_WAITTING_SESSION = 3101;

    /* XD 20150813 added End > */
    public static final int MSG_ON_SPEAKER_SPEECH_DETECHED = 2028;// XD added 20160216

    public static final int MSG_ON_AEC_CANCEL = 12012;

    private static final String KEY_TTS_MODLE_NAME = "key_tts_modle_name";
    private static final String KEY_TTS_FAILCAUSE = "key_tts_failcause";

    private Point mWindowSize = new Point();

    private WindowManager mWindowManager;
    private SessionLinearLayout mViewRoot;
    private SessionContainer mSessionContainer;
    private MicrophoneControl mMicrophoneControl;
    private MicrophoneControlDoresoView mMicrophoneDoresoControl; // XD added 20150828

    private GUISessionManager mSessionManager = null;
    private WindowManager.LayoutParams mWindowParams = new WindowManager.LayoutParams();
    private Context mContext;
    private IMessageRouterService mMessageRouterService;
    private SystemCallTransition messageTransCenter = null;

    private List<String> mLauncherPackage;
    private FloatMicView mScreeFloatView;
    private boolean mPendingStartMicChecker;
    private boolean mEnableFloatMic = false;
    private Handler mHandler = new Handler();


    private boolean isASRCompile = false;

    /**
     * is Need Hide MicFloatView for some special case
     */
    private boolean isNeedHideMicFloatView = false;
    /* XD 20150805 added for Mic float View End > */

    /* < XD 20150828 added for Music Doreso Begin */
    /**
     * wake up success type SessionPreference.VALUE_SET_STATE_TYPE_WAKEUP_SUCCESS
     * SessionPreference.VALUE_SET_STATE_TYPE_WAKEUP_SUCCESS_DORESO
     */
    private int mWakeupSuccessType = SessionPreference.VALUE_SET_STATE_TYPE_WAKEUP_SUCCESS;

    public int getWakeupSuccessType() {
        return mWakeupSuccessType;
    }

    /* XD 20150828 added for Music Doreso End > */

    // xd added 20150707
    private static ArrayList<String> mOnlineSupportList = null;
    private static ArrayList<String> mOfflineSupportList = null;
    private String mSystemCallJson = "";

    private ContactDataModel contactDataModel = null;

    private MediaDataModel mediaDataModel = null;

    private AppsDataModel appsDataModel = null;

    private boolean isMainMark = false;

    private AudioFocusHelper mAudioFocusHelper = null;

    // Foreground Service
    private boolean mReflectFlg = false;
    private static final int NOTIFICATION_ID = 1; // 设置为前台service
    private static final Class<?>[] mSetForegroundSignature = new Class[]{boolean.class};
    private static final Class<?>[] mStartForegroundSignature = new Class[]{int.class,
            Notification.class};
    private static final Class<?>[] mStopForegroundSignature = new Class[]{boolean.class};

    private NotificationManager mNM;
    private Method mSetForeground;
    private Method mStartForeground;
    private Method mStopForeground;
    private Object[] mSetForegroundArgs = new Object[1];
    private Object[] mStartForegroundArgs = new Object[2];
    private Object[] mStopForegroundArgs = new Object[1];
    private long lastSessionActiveTime = 0;
    private final int IdleAfterSession = 5000;
    private final int IdleAfterCreatApp = 20000;
    private PushView pushView;
    private Toast mToast = null;

    public static final String PARAM_TTS_FROM_UNICARGUI = "UniCarGUI";
    public static final String PARAM_TTS_FROM_UNICARNAVI = "UniCarNavi";
    public static final String PARAM_TTS_FROM_UNIWECHAT = "UniCarWeChat";
    public String PARAM_TTS_FROM = "";


    private DispatchKeyEventListener mDispatchKeyEventListener = new DispatchKeyEventListener() {

        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {

            Logger.d(TAG, "!--->dispatchKeyEvent()---keyCode = " + event.getKeyCode() + "; action = " + event.getAction() + "; isMainMark = " + isMainMark);

            //按下了返回键
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                mSessionManager.cancelTalk();
                return true;
            } else {
                if (isMainMark) {
                    return mSessionContainer.dispatchKeyEvent(event);
                } else {
                    return mMicrophoneControl.dispatchKeyEvent(event);
                }

            }
        }
    };


    private OnTouchEventListener mOnTouchEventListener = new OnTouchEventListener() {

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            Logger.d(TAG, "!--->onTouchEvent()---Action = " + event.getAction());
            final int x = (int) event.getX();
            final int y = (int) event.getY();
            if ((event.getAction() == MotionEvent.ACTION_DOWN)
                    && ((x < 0) || (x >= mViewRoot.getWidth()) || (y < 0) || (y >= mViewRoot
                    .getHeight()))) {
                mSessionManager.cancelTalk();
                return true;
            } else if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                mSessionManager.cancelTalk();
                return true;
            }
            return false;
        }

    };


    //AIDL
    private final IWindowService.Stub mBinder = new IWindowService.Stub() {

        @Override
        public boolean guiIsIdle() throws RemoteException {
            boolean rootShow = isViewRootShow();
            if (TelephonyManager.CALL_STATE_IDLE != phoneStats) {
                return false;
            }
            Logger.d(TAG, "guiIsIdle PARAM_TTS_FROM : " + PARAM_TTS_FROM);

            if (PARAM_TTS_FROM_UNICARNAVI.equals(PARAM_TTS_FROM)
                    || PARAM_TTS_FROM_UNICARGUI.equals(PARAM_TTS_FROM)) {
                return false;
            }

            if (rootShow) {
                return false;
            } else {
                boolean pushShow = isPushShow();
                if (pushShow) return false;

                if (WelcomeActivity.isInWelcomeActivity()) return false;
                long now = System.currentTimeMillis();
                if ((now - CrashApplication.getAppStartTime()) <= IdleAfterCreatApp) return false;
                if ((now - lastSessionActiveTime) > IdleAfterSession) {
                    return true;
                } else {
                    Logger.d(TAG, "now = " + now + "lastSessionActiveTime = "
                            + lastSessionActiveTime);
                }
                return false;
            }
        }
    };


    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;                    //创建上下文

        //推送的处理
        PushBlockQueue.getInstance().setGuiCallBack(mBinder);

        //创建一个通知栏 --> 将进程提升到前台进程
        initServiceForeground();

        //隶属模块
        PrivatePreference.init(mContext);

        // 初始化拼音模块
        initPinyinConverter();

        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        //设置为第一次启动
        Constant.setFirstStart(this, true);

        //推送消息的处理
        PushBlockQueue.getInstance().setGuiHandler(mUIHandler);


        //开启欢迎界面
        startWelcomeActivity();

        mViewRoot = (SessionLinearLayout) View.inflate(this, R.layout.window_service_main, null);
        if (DeviceTool.getDeviceSDKVersion() > 13) {
            mViewRoot.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);// XD added
        } else {   //版本号小于13

        }

        findViews();

        //判断SD卡上空间是否充足  满足100M
        if (!DeviceTool.isAvailableSDcardSpace(GUIConfig.MIN_SIZE_SDCARD_AVAILABLE)) {
            dismissGUIMainActivity();   //不满足 -->发一个关闭activity的广播
            showNoSDCardSpaceView();    //显示对话框
            return;
        }


        //绑定线路服务
        bindMessageRouterService();


        //开启酷狗音乐的服务
        startKWMusicService();

        //检查喜马拉雅FM是否安装
        boolean isXmFmInstalled = DeviceTool.checkApkExist(mContext, GUIConfig.PACKAGE_NAME_XMLY_FM);

        if (isXmFmInstalled) {
            startXmFmGuiService();    //如果安装 开启喜马拉雅的服务
        } else {
            startUniDriveFmService(); //没安装 开启汽车自带的服务
        }

        startUniCarNaviService();   //开启导航的服务

        // init framework & show prepare view
        mSessionManager = new GUISessionManager(this, mSessionContainer, mMicrophoneControl);

        //显示的话筒
        mScreeFloatView = new FloatMicView(this);


        mLauncherPackage = PackageUtil.getLauncherPackages(this);

        initWindowParams();

        registerListener();  //初始化监听器  -->点击事件/事件分发/按键的事件


        registerReceiver();  //注册广播接收者 -->屏幕的事件-->截屏锁屏等事件


        mPendingStartMicChecker = true;

        updateEnableFloatMic();  //更新说话按钮

        startFloatMicChecker(0); //默认点击了一次


        //设置处理。。。
        messageTransCenter = new SystemCallTransition();
        messageTransCenter.setMessageTransListener(mMessageTransListener);


        //
        if (GUIConfig.isAllowGUIRequestAsDefaultSmsApp) {
            DeviceTool.changeGUIToDefaultSmsApp(mContext);
        }

        //注册发送消息成功的广播
        IntentFilter filter = new IntentFilter(GUIConfig.ACTION_SMS_SEND_SUCCESS);
        filter.addAction(GUIConfig.ACTION_SMS_SEND_FAIL);
        registerReceiver(mSmsSendStatusReceiver, filter);


        //获取大概的位置
        getGeneralGPS();

        //初始化音频
        initAudioFoucus();

        //设置唤醒的监听器
        IntentFilter wakeupfilter = new IntentFilter(GUIConfig.ACTION_WAKEUP_WORDS_UPDATE);
        registerReceiver(receiver, wakeupfilter);

    }


    //开启服务
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //观察服务是否打开
        boolean isUnicarServiceInstalled = DeviceTool.isUnicarServiceInstalled(getApplicationContext());

        //云服务是否打开
        if (!isUnicarServiceInstalled) {
            mSessionManager.showInitFailedView(getString(R.string.error_init_msg_vui_service_not_install));
        }

        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_START_WINDOWSERVICE.equals(action)) {
                boolean isCompile = intent.getBooleanExtra(KEY_IS_COMPILE_FINISH, false);
                if (!isCompile && isASRCompile) {
                    sendBroadcastForWechat();
                }

            } else if (MessageReceiver.ACTION_START_TALK.equals(action)) {

                //开始讲话
                startTalk();

            } else if (MessageReceiver.ACTION_MUSIC_DONE.equals(action)) {

                //音乐播放完毕
                sendMusicDone();

            } else if (ACTION_START_REQUEST_MAKE_FINISHED.equals(action)) {

                //请求完成
                requestIsASRCompileFinished();

            } else if (MessageReceiver.ACTION_START_CALL_OUT.equals(action)) {

                //请求电话
                requestCallOut();

            } else if (MessageReceiver.ACTION_START_NAVIGATION.equals(action)) {

                //请求导航
                requestNavication();
            } else if (MessageReceiver.ACTION_START_MUSIC.equals(action)) {

                //请求音乐
                requestMusic();
            } else if (MessageReceiver.ACTION_START_LOCAL_SEARCH.equals(action)) {

                //请求本地查询
                requestLocalSearch();

            } else if (ACTION_SET_WAKEUP.equals(action)) {
                if (UserPerferenceUtil.isWakeupEnable(mContext)) {
                    sendProtocolEvent(SessionPreference.EVENT_NAME_SWITCH_WAKEUP,
                            SessionPreference.EVENT_PROTOCAL_SWITCH_WAKEUP_START);
                } else {
                    sendProtocolEvent(SessionPreference.EVENT_NAME_SWITCH_WAKEUP,
                            SessionPreference.EVENT_PROTOCAL_SWITCH_WAKEUP_STOP);
                }
            } else if (ACTION_UPDATE_WAKEUP_WORD.equals(action)) {    //更新唤醒词

                String newText = intent.getStringExtra(EXTRA_KEY_WAKEUP_WORD);
                String oldWakeupWord = UserPerferenceUtil.getWakeupWord(mContext);
                sendUpdateWakeupWordEvent(oldWakeupWord, newText);

            } else if (ACTION_UPDATE_LOCATION.equals(action)) {      //请求位置

                String newLocation = intent.getStringExtra(EXTRA_KEY_LOCATION);
                sendkeywordEvent(SessionPreference.EVENT_NAME_UPDATE_POI_KEYWORD, newLocation);

            } else if (ACTION_SET_TTSSPEED.equals(action)) {
                int speed = UserPerferenceUtil.getTTSSpeed(mContext);
                if (UserPerferenceUtil.VALUE_TTS_SPEED_SLOWLY == speed) {
                    sendProtocolEvent(SessionPreference.EVENT_NAME_SWITCH_TTS_SPEED,
                            SessionPreference.EVENT_PROTOCAL_SWITCH_TTS_SPEED_SLOWLY);
                } else if (UserPerferenceUtil.VALUE_TTS_SPEED_STANDARD == speed) {
                    sendProtocolEvent(SessionPreference.EVENT_NAME_SWITCH_TTS_SPEED,
                            SessionPreference.EVENT_PROTOCAL_SWITCH_TTS_SPEED_STANDARD);
                } else if (UserPerferenceUtil.VALUE_TTS_SPEED_FAST == speed) {
                    sendProtocolEvent(SessionPreference.EVENT_NAME_SWITCH_TTS_SPEED,
                            SessionPreference.EVENT_PROTOCAL_SWITCH_TTS_SPEED_FAST);
                }
            } else if (ACTION_SET_GPS.equals(action)) {
                boolean gps = UserPerferenceUtil.getGpsEnable(mContext);
                if (gps) {
                    sendProtocolEvent(SessionPreference.EVENT_NAME_SWITCH_GPS,
                            SessionPreference.EVENT_PROTOCAL_SWITCH_GPS_TRUE);
                } else {
                    sendProtocolEvent(SessionPreference.EVENT_NAME_SWITCH_GPS,
                            SessionPreference.EVENT_PROTOCAL_SWITCH_GPS_FALSE);
                }
            } else if (MessageReceiver.ACTION_PLAY_TTS.equals(action)) {
                // XD added 20150914
                String tts = intent.getStringExtra(MessageReceiver.KEY_EXTRA_TTS_TEXT);
                String ttsID = intent.getStringExtra(MessageReceiver.KEY_EXTRA_TTS_ID);
                String ttsFrom = intent.getStringExtra(MessageReceiver.KEY_EXTRA_TTS_FROM);
                String recognizerType =
                        intent.getStringExtra(MessageReceiver.KEY_EXTRA_TTS_RECOGNIZER_TYPE);
                if (TextUtils.isEmpty(recognizerType)) {
                    recognizerType = SessionPreference.PARAM_VALUE_TTS_END_WAKEUP;
                }
                int callState = mSessionManager.getPhoneState();
                Logger.d(TAG, "!--->--ACTION_PLAY_TTS----tts =" + tts + "; ttsID = " + ttsID
                        + "; ttsFrom = " + ttsFrom + "; callState:" + callState);
                if (SessionPreference.PARAM_TTS_FROM_UNICARNAVI.equals(ttsFrom) && isViewRootShow()) {
                    Logger.w(TAG, "Window is showing, do not play UniCarNavi TTS:" + tts);
                } else if (SessionPreference.PARAM_TTS_FROM_UNICARNAVI.equals(ttsFrom)
                        && TelephonyManager.CALL_STATE_IDLE != callState) {
                    Logger.w(TAG, "On call state, do not play UniCarNavi TTS:" + tts);
                } else {
                    String protol =
                            GuiProtocolUtil.getPlayTTSEventParam(tts, ttsID, ttsFrom,
                                    recognizerType);
                    sendProtocolEvent(SessionPreference.EVENT_NAME_PLAY_TTS, protol);
                }
            } else if (ACTION_SET_ONESHOT.equals(action)) {
                if (GUIConfig.isSupportOneShotSetting) {
                    if (UserPerferenceUtil.getOneShotEnable(mContext)) {
                        sendProtocolEvent(SessionPreference.EVENT_NAME_SWITCH_ONESHOT,
                                SessionPreference.EVENT_PROTOCAL_SWITCH_ONESHOT_START);
                    } else {
                        sendProtocolEvent(SessionPreference.EVENT_NAME_SWITCH_ONESHOT,
                                SessionPreference.EVENT_PROTOCAL_SWITCH_ONESHOT_STOP);
                    }
                }
            } else if (ACTION_SET_VERSION_LEVEL.equals(action)) {
                // XD added 20151123
                String type = intent.getStringExtra(GuiSettingUpdateUtil.KEY_SETTING_TYPE);
                sendVersionLevel(type);
            } else if (ACTION_SET_TTS_TIMBRE.equals(action)) {
                // XD added 20160112
                String actionType = intent.getStringExtra(GuiSettingUpdateUtil.KEY_SETTING_TYPE);
                sendTtsTimbre(SessionPreference.PARAM_TYPE_PLAY_TTS, actionType);
            } else if (ACTION_SET_AEC.equals(action)) {
                if (GUIConfig.isSupportAECSetting) {
                    if (UserPerferenceUtil.getAECEnable(mContext)) {
                        sendProtocolEvent(SessionPreference.EVENT_NAME_SWITCH_AEC,
                                SessionPreference.EVENT_PROTOCAL_SWITCH_AEC_START);
                    } else {
                        sendProtocolEvent(SessionPreference.EVENT_NAME_SWITCH_AEC,
                                SessionPreference.EVENT_PROTOCAL_SWITCH_AEC_STOP);
                    }
                }
            } else if (ACTION_SET_LOGCAT.equals(action)) {
                sysLogcatConfig();
            } else if (ACTION_SET_FAVORITE_ADDRESS.equals(action)) {
                int setType = intent.getIntExtra(GuiSettingUpdateUtil.KEY_ADD_ADDRESS_TYPE, -1);
                sendFavoriteAddress(setType);
            } else if (ACTION_REQUEST_TTS_TIMBRE_LIST.equals(action)) {
                Logger.d(TAG, "!--->onStartCommand--ACTION_REQUEST_TTS_TIMBRE_LIST");
                sendEvent(SessionPreference.EVENT_NAME_REQUEST_TTS_MODEL_LIST);
            } else if (ACTION_SEND_PROTOCAL_EVENT.equals(action)) {
                String eventName = intent.getStringExtra(SessionPreference.KEY_BUNDLE_EVENT_NAME);
                String protocal = intent.getStringExtra(SessionPreference.KEY_BUNDLE_PROTOCAL);
                Logger.d(TAG, "!--->onStartCommand--SEND_PROTOCAL_EVENT eventName:" + eventName
                        + "; protocal: " + protocal);
                sendProtocolEvent(eventName, protocal);
            }
        }
        // XD 2015-12-23 added
        if (!DeviceTool.isAvailableSDcardSpace(GUIConfig.MIN_SIZE_SDCARD_AVAILABLE)) {
            Logger.e(TAG, "onStartCommand--no sdcard space left.");
            return START_NOT_STICKY;
        }
        return START_STICKY;
    }


    @SuppressLint("NewApi")
    private void findViews() {
        mSessionContainer = (SessionContainer) mViewRoot.findViewById(R.id.sessionContainer);
        mMicrophoneControl = (MicrophoneControl) mViewRoot.findViewById(R.id.microphoneControl);
        mMicrophoneControl.setMicrophoneViewClickListener(mMicrophoneViewClickListener);// add tyz
        // 20151012
        // XD added 20150828
        mMicrophoneDoresoControl =
                (MicrophoneControlDoresoView) mViewRoot.findViewById(R.id.microphoneControl_doreso);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void updateDisplaySize() {
        Display display = mWindowManager.getDefaultDisplay();
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
            mWindowSize.y = display.getHeight();
            mWindowSize.x = display.getWidth();
        } else {
            display.getSize(mWindowSize);
        }
        Logger.d(TAG, "!--->updateDisplaySize:x " + mWindowSize.x + ",y " + mWindowSize.y);
    }

    /**
     * 显示第一次进入
     */
    private void showWakeupStatusToast() {
        if (!Constant.isFirstStart(mContext)) {
            return;
        }
        Logger.d(TAG, "!--->showWakeupStatusToast----");
        if (UserPerferenceUtil.isWakeupEnable(mContext)) {
            String wakeupWord = UserPerferenceUtil.getWakeupWord(mContext);
            mToast = Toast.makeText(mContext, getString(R.string.toast_wakeup_open, wakeupWord), Toast.LENGTH_LONG);
        } else {
            mToast = Toast.makeText(mContext, R.string.toast_wakeup_closed, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    /**
     * start WelcomeActivity xd added 20150706
     */
    private void startWelcomeActivity() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void registerListener() {

        mViewRoot.setDispatchKeyEventListener(mDispatchKeyEventListener);
        mViewRoot.setOnTouchEventListener(mOnTouchEventListener);
        mScreeFloatView.setOnClickListener(mOnClickListener);
        UserPerferenceUtil.registerOnSharedPreferenceChangeListener(this, mPreferenceChangeListener);
    }

    private void unregisterListener() {
        mViewRoot.setDispatchKeyEventListener(null);
        /* < XD 20150805 added for float mic view Begin */
        if (null != mScreeFloatView) {
            mScreeFloatView.setOnClickListener(null);
        }
        UserPerferenceUtil.unregisterOnSharedPreferenceChangeListener(this,
                mPreferenceChangeListener);
        /* XD 20150805 added for float mic view End > */
    }


    @Override
    public IBinder onBind(Intent intent) {
        Logger.e(TAG, "Windows Service Bind");
        return mBinder;
    }

    private void resetWindowParamsFlags() {
        Logger.d(TAG, "!--->------resetWindowParamsFlags()------");
        mWindowParams.flags &=
                ~(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_IGNORE_CHEEK_PRESSES
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN // xiaodong
                        // added
                        // 20150611
                );
    }

    /**
     * 初始化布局
     */
    private void initWindowParams() {
        mWindowParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        mWindowParams.format = PixelFormat.RGBA_8888;
        resetWindowParamsFlags();
        mWindowParams.flags =
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        mWindowParams.gravity = Gravity.CENTER;
        updateDisplaySize();
    }


    /**
     * @param setType
     * @author xiaodong.he
     * @date 2016-01-28
     */
    private void sendFavoriteAddress(int setType) {
        Logger.d(TAG, "sendFavoriteAddress--setType:" + setType);
        switch (setType) {
            case GuiSettingUpdateUtil.VALUE_ADD_ADDRESS_TYPE_HOME:
                String locationJson = UserPerferenceUtil.getHomeLocation(mContext);
                sendProtocolEvent(SessionPreference.EVENT_NAME_SET_FAVORITE_ADDRESS,
                        GuiProtocolUtil.getSetFavoriteAddressProtocol(locationJson,
                                SessionPreference.PARAM_ADDRESS_TYPE_HOME));
                break;
            case GuiSettingUpdateUtil.VALUE_ADD_ADDRESS_TYPE_COMPANY:
                String compJson = UserPerferenceUtil.getCompanyLocation(mContext);
                sendProtocolEvent(SessionPreference.EVENT_NAME_SET_FAVORITE_ADDRESS,
                        GuiProtocolUtil.getSetFavoriteAddressProtocol(compJson,
                                SessionPreference.PARAM_ADDRESS_TYPE_COMPANY));
                break;
            default:
                break;
        }
    }

    /**
     * sendVersionLevel
     *
     * @param type
     * @author xiaodong.he
     * @date 2015-11-23
     */
    private void sendVersionLevel(String type) {
        if (!GUIConfig.isSupporVersionLevelSetting) {
            return;
        }
        int level = UserPerferenceUtil.getVersionMode(mContext);
        String levelName = SessionPreference.PARAM_VERSION_LEVEL_STANDRARD;
        switch (level) {
            case UserPerferenceUtil.VALUE_VERSION_MODE_EXP:
                levelName = SessionPreference.PARAM_VERSION_LEVEL_EXP;
                break;
            case UserPerferenceUtil.VALUE_VERSION_MODE_STANDARD:
                levelName = SessionPreference.PARAM_VERSION_LEVEL_STANDRARD;
                break;
            case UserPerferenceUtil.VALUE_VERSION_MODE_HIGH:
                levelName = SessionPreference.PARAM_VERSION_LEVEL_HIGH;
                break;
            default:
                break;
        }
        Logger.d(TAG, "sendVersionLevel--level = " + level + "; levelName = " + levelName
                + "; type = " + type);
        sendProtocolEvent(SessionPreference.EVENT_NAME_SWITCH_VERSION_LEVEL,
                GuiProtocolUtil.getSetVersionLevelProtocol(levelName, type));
    }

    /**
     * @param
     * @author xiaodong.he
     * @date 2016-01-12
     */
    private void sendTtsTimbre(String type, String actionType) {
        int timbre = UserPerferenceUtil.getTtsTimbre(mContext);
        String timbreType = SessionPreference.PARAM_TTS_TIMBRE_STARNAND;
        switch (timbre) {
            case UserPerferenceUtil.VALUE_TTS_TIMBRE_STANDARD:
                timbreType = SessionPreference.PARAM_TTS_TIMBRE_STARNAND;
                break;
            case UserPerferenceUtil.VALUE_TTS_TIMBRE_SEXY:
                timbreType = SessionPreference.PARAM_TTS_TIMBRE_SEXY;
                break;
            case UserPerferenceUtil.VALUE_TTS_TIMBRE_AUTO:
                timbreType = SessionPreference.PARAM_TTS_TIMBRE_AUTO;
                break;
            default:
                break;
        }
        Logger.d(TAG, "sendTtsTimbre--timbreType = " + timbreType + "; timbre = " + timbre
                + ";type : " + type);
        sendProtocolEvent(SessionPreference.EVENT_NAME_SWITCH_TTS_MODEL,
                GuiProtocolUtil.getSetTtsTimbreProtocol(timbreType, type, actionType));
    }

    /**
     * sync Configure to VUI
     */
    private void syncConfigure() {
        Logger.d(TAG, "syncConfigure----");
        sendTtsTimbre(SessionPreference.PARAM_TYPE_NOT_PLAY_TTS, null); // XD 20160112 added

        // send switch WakeUp
        if (UserPerferenceUtil.isWakeupEnable(mContext)) {
            sendProtocolEvent(SessionPreference.EVENT_NAME_SWITCH_WAKEUP,
                    SessionPreference.EVENT_PROTOCAL_SWITCH_WAKEUP_START);
        } else {
            sendProtocolEvent(SessionPreference.EVENT_NAME_SWITCH_WAKEUP,
                    SessionPreference.EVENT_PROTOCAL_SWITCH_WAKEUP_STOP);
        }

        // send switch TTSSpeed
        int speed = UserPerferenceUtil.getTTSSpeed(mContext);
        if (UserPerferenceUtil.VALUE_TTS_SPEED_SLOWLY == speed) {
            sendProtocolEvent(SessionPreference.EVENT_NAME_SWITCH_TTS_SPEED,
                    SessionPreference.EVENT_PROTOCAL_SWITCH_TTS_SPEED_SLOWLY);
        } else if (UserPerferenceUtil.VALUE_TTS_SPEED_STANDARD == speed) {
            sendProtocolEvent(SessionPreference.EVENT_NAME_SWITCH_TTS_SPEED,
                    SessionPreference.EVENT_PROTOCAL_SWITCH_TTS_SPEED_STANDARD);
        } else if (UserPerferenceUtil.VALUE_TTS_SPEED_FAST == speed) {
            sendProtocolEvent(SessionPreference.EVENT_NAME_SWITCH_TTS_SPEED,
                    SessionPreference.EVENT_PROTOCAL_SWITCH_TTS_SPEED_FAST);
        }

        if (GUIConfig.isSupportOneShotSetting) {
            // send switch OneShot
            if (UserPerferenceUtil.getOneShotEnable(mContext)) {
                sendProtocolEvent(SessionPreference.EVENT_NAME_SWITCH_ONESHOT,
                        SessionPreference.EVENT_PROTOCAL_SWITCH_ONESHOT_START);
            } else {
                sendProtocolEvent(SessionPreference.EVENT_NAME_SWITCH_ONESHOT,
                        SessionPreference.EVENT_PROTOCAL_SWITCH_ONESHOT_STOP);
            }
        }

        if (GUIConfig.isSupportAECSetting) {
            // send switch AEC
            if (UserPerferenceUtil.getAECEnable(mContext)) {
                sendProtocolEvent(SessionPreference.EVENT_NAME_SWITCH_AEC,
                        SessionPreference.EVENT_PROTOCAL_SWITCH_AEC_START);
            } else {
                sendProtocolEvent(SessionPreference.EVENT_NAME_SWITCH_AEC,
                        SessionPreference.EVENT_PROTOCAL_SWITCH_AEC_STOP);
            }
        }

        sendVersionLevel(SessionPreference.PARAM_VERSION_LEVEL_TYPE_IDEL); // XD added 20151123
        sysLogcatConfig();

        sendFavoriteAddress(GuiSettingUpdateUtil.VALUE_ADD_ADDRESS_TYPE_HOME);
        sendFavoriteAddress(GuiSettingUpdateUtil.VALUE_ADD_ADDRESS_TYPE_COMPANY);
    }

    private void startTalk() {
        mSessionManager.startTalk();


        mWakeupSuccessType = SessionPreference.VALUE_SET_STATE_TYPE_WAKEUP_SUCCESS;
        updateMicControlView(SessionPreference.VALUE_SET_STATE_TYPE_WAKEUP_SUCCESS);
    }


    public void onReset() {
        Logger.d(TAG, "onReset");
        dismiss();
    }

    private void show(View view) {
        if (view == null || view.isShown()) {
            return;
        }
        // 弹出框强制横屏
        if (mWindowParams.screenOrientation != ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
            mWindowParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
        }

        mWindowManager.addView(view, mWindowParams);
    }

    public void addPrepareView(View view) {
        if (view == null) {
            Logger.w(TAG, "addSessionView: view null,return!");
            return;
        }
        Logger.d(TAG, "!--->addPrepareView------");
        dismiss();
        WindowManager.LayoutParams WindowParams =
                new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        WindowParams.gravity = Gravity.CENTER;
        WindowParams.format = PixelFormat.RGBA_8888;
        if (view.isShown()) {
            mWindowManager.removeViewImmediate(view);
        }
        mWindowManager.addView(view, WindowParams);
    }

    public void addPushView(PushView view) {
        if (view == null) {
            return;
        }
        dismiss();
        Logger.d(TAG, "!--->addpushView------" + view);
        WindowManager.LayoutParams WindowParams =
                new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        WindowParams.format = PixelFormat.TRANSLUCENT;
        WindowParams.gravity = Gravity.TOP;
        WindowParams.flags =
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_FULLSCREEN;
        if (WindowParams.screenOrientation != ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
            WindowParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
        }
        mWindowManager.addView(view, WindowParams);
        pushView = view;
        Logger.d(TAG, "!--->pushView------" + pushView);
    }

    /**
     * @return
     * @author xiaodong.he
     * @date 2016-2-1
     */
    public boolean isViewRootShow() {
        if (mViewRoot != null && mViewRoot.isShown()) {
            Logger.d(TAG, "gui show");
            return true;
        } else {
            Logger.d(TAG, "gui not show");
            return false;
        }
    }

    public boolean isPushShow() {
        if (pushView != null && pushView.isShown()) {
            Logger.d(TAG, "push show");
            return true;
        } else {
            Logger.d(TAG, "push not show");
            return false;
        }
    }

    public void show() {
        if (mViewRoot.isShown()) {
            return;
        }
        if (pushView != null && pushView.isShown()) {
            pushView.cancel();
            return;
        }
        // RomSystemSetting.setMute(mContext);
        requestAudioFocus();
        Logger.d(TAG, "show");

        /* < xiaodong 20150805 added for Float Mic View Begin */
        stopFloatMicChecker();
        hideFloatView();
        mPendingStartMicChecker = true;
        /* xiaodong 20150805 added for Float Mic View End > */

        show(mViewRoot);

    }

    public void dismiss() {
        isMainMark = false;
        Logger.d(TAG, "dismiss");
        Logger.d(TAG, "pushView = " + pushView);
        if (mViewRoot.isShown() == false && pushView != null && pushView.isShown() == false) {
            return;
        }

        lastSessionActiveTime = System.currentTimeMillis();
        if (mViewRoot != null && mViewRoot.isShown()) {
            mWindowManager.removeViewImmediate(mViewRoot);
        }

        if (pushView != null && pushView.isShown()) {
            Logger.d(TAG, "dismiss pushView");
            mWindowManager.removeViewImmediate(pushView);
            pushView = null;
        }

        /* < xiaodong 20150805 added for Float Mic View Begin */
        if (mPendingStartMicChecker) {
            mPendingStartMicChecker = false;
            updateEnableFloatMic();
            startFloatMicChecker(0);
        }
        // RomSystemSetting.setUnMute(mContext);
        abandonAudioFocus();
        /* xiaodong 20150805 added for Float Mic View End > */
    }

    public void dimissView(View view) {
        if (!view.isShown()) {
            return;
        }
        Logger.d(TAG, "dimissView---prepare view dismiss");
        mWindowManager.removeViewImmediate(view);

        /* < xiaodong 20150805 added for Float Mic View Begin */
        if (mPendingStartMicChecker) {
            mPendingStartMicChecker = false;
            updateEnableFloatMic();
            startFloatMicChecker(0);
        }
        /* xiaodong 20150805 added for Float Mic View End > */
    }


    public Point getWindowSize() {
        return mWindowSize;
    }

    public void addView(View view, int windowWidth, int windowHeight) {
        Logger.d(TAG, "!--->addView:windowWidth " + windowWidth + ",windowHeight " + windowHeight);
        mWindowParams.width = windowWidth;
        mWindowParams.height = windowHeight;
        addView(view);
    }

    public void addView(View view) {
        Logger.d(TAG, "addView");
        dismiss();
        mWindowParams.gravity = Gravity.CENTER;
        mWindowParams.x = 0;
        mWindowParams.y = 0;
        resetWindowParamsFlags();
        mWindowParams.flags =
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        if (view == null || !canTextInput(view)) {
            mWindowParams.flags |= WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
        }

        if ((mWindowParams.softInputMode & WindowManager.LayoutParams.SOFT_INPUT_IS_FORWARD_NAVIGATION) == 0) {
            mWindowParams.softInputMode |=
                    WindowManager.LayoutParams.SOFT_INPUT_IS_FORWARD_NAVIGATION;
        }

        show(view);
    }

    static boolean canTextInput(View v) {
        if (v.onCheckIsTextEditor()) {
            return true;
        }

        if (!(v instanceof ViewGroup)) {
            return false;
        }

        ViewGroup vg = (ViewGroup) v;
        int i = vg.getChildCount();
        while (i > 0) {
            i--;
            v = vg.getChildAt(i);
            if (canTextInput(v)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Logger.d(TAG, "onLowMemory");
    }

    @Override
    public void onDestroy() {
        Logger.d(TAG, "onDestroy");
        super.onDestroy();
        dismiss();
        unregisterListener();
        unBindService(); // xd added 20150706
        unregisterReceiver(mSmsSendStatusReceiver);// xd added 20150709

        /* < xiaodong 20150805 added for Float Mic View Begin */
        unregisterReceiver();
        stopKWMusicService();
        stopFloatMicChecker();
        mLauncherPackage.clear();
        mLauncherPackage = null;
        /* xiaodong 20150805 added for Float Mic View End > */
        mLocateTime = 0;
        mSessionManager.onDestroy();
        // PhoneStateReceiver.release();
        mSessionManager = null;
        mWindowParams = null;
        mWindowSize = null;
        if (contactDataModel != null) {
            contactDataModel.release();
        }

        if (mediaDataModel != null) {
            mediaDataModel.release();
        }

        if (appsDataModel != null) {
            appsDataModel.release();
        }

        abandonAudioFocus();
        stopForegroundCompat(NOTIFICATION_ID);
    }

    public static Intent getExplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }
        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;

        Logger.d(TAG, "getExplicitIntent packagename = " + packageName + ";  classname="
                + className);

        ComponentName component = new ComponentName(packageName, className);
        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);
        // Set the component to be explicit
        explicitIntent.setComponent(component);
        return explicitIntent;
    }


    //绑定路线服务
    public void bindMessageRouterService() {
        try {
            Logger.d(TAG, "!--->bindMessageRouterService");

            Intent intent = new Intent("com.unisound.unicar.messagerouter.start");
            // for android 5.0 and above
            if (getExplicitIntent(mContext, intent) != null) {
                intent = getExplicitIntent(mContext, intent);
            }
            mContext.startService(intent);
            mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void rebindMessageRouterService() {
        if (mMessageRouterService == null) {
            Logger.d(TAG, "rebindMessageRouterService");
            bindMessageRouterService();
        }
    }

    private void startKWMusicService() {
        Intent intentMusic = new Intent(mContext, KWMusicService.class);
        mContext.startService(intentMusic);
    }

    private void stopKWMusicService() {
        Intent intentMusic = new Intent(mContext, KWMusicService.class);
        mContext.stopService(intentMusic);
    }

    /**
     * start UniCarNaviGuiService
     *
     * @author xiaodong.he
     * @date 20150923
     */
    private void startUniCarNaviService() {
        Intent intent = new Intent(mContext, UniCarNaviGuiService.class);
        intent.setAction(UniCarNaviConstant.ACTION_START_UNICARNAVI_GUI_SERVICE);
        mContext.startService(intent);
    }


    /**
     * startUniDriveFmService xiaodong 20150923 added
     */
    private void startUniDriveFmService() {
        Logger.d(TAG, "startUniDriveFmService from window service");
        Intent intent = new Intent(mContext, UniDriveFmGuiService.class);
        intent.setAction(MessageReceiver.ACTION_START_UNIDRIVE_FM);
        mContext.startService(intent);
    }

    /**
     * startXmFmGuiService xiaodong 20150923 added
     */
    private void startXmFmGuiService() {
        Logger.d(TAG, "XmFmGuiService from window service");
        Intent intent = new Intent(mContext, XmFmGuiService.class);
        intent.setAction(MessageReceiver.ACTION_START_XM_FM);
        mContext.startService(intent);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mMessageRouterService = IMessageRouterService.Stub.asInterface(service);
            try {
                Logger.d(TAG, "!--->onServiceConnected()-----------");
                mMessageRouterService.registerCallback(mCallback);
                registSystemCall();
                // if(isNeedRequestIsASRCompileFinished){
                requestIsASRCompileFinished();
                sendRequestWakeupWordsEvent();
                sendRequestUuidEvent();// XD
                // 2015-11-22
                // added
                syncConfigure();
                // bind MessageRouterService成功后开始定位
                getGeneralGPS();
                // }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Logger.d(TAG, "onServiceDisconnected");
            mMessageRouterService = null;
            rebindMessageRouterService();
        }
    };

    private IMessageRouterCallback mCallback = new IMessageRouterCallback.Stub() {

        @Override
        public void onCallBack(String callBackJson) throws RemoteException {
            Logger.d(TAG, "!--->onCallBack : " + callBackJson);
            onCallBackFunction(callBackJson);
            mSystemCallJson = callBackJson;
            checkIfNeedSendRespImmediately(callBackJson);
        }
    };

    private void onCallBackFunction(String callBackJson) {
        messageTransCenter.handlerSystemCall(callBackJson);
    }

    /**
     * check If Need Send Response Immediately
     *
     * @param callBackJson
     */
    private void checkIfNeedSendRespImmediately(String callBackJson) {
        Logger.d(TAG, "!--->checkIfNeedSendRespImmediately----");
        JSONObject obj = JsonTool.parseToJSONObject(callBackJson);
        if (obj != null) {
            JSONObject jobj = JsonTool.parseToJSONObject(callBackJson);
            JSONObject data = JsonTool.getJSONObject(jobj, SessionPreference.KEY_DATA);
            String functionName =
                    JsonTool.getJsonValue(data, SessionPreference.KEY_FUNCTION_NAME, "");
            JSONObject dataParams = JsonTool.getJSONObject(data, SessionPreference.KEY_PARAMS);

            if (SessionPreference.VALUE_FUNCTION_NAME_ON_TALK_PROTOCOL.equals(functionName)) {
                JSONObject typeObj = JsonTool.getJSONObject(dataParams, SessionPreference.KEY_TYPE);
                String type = JsonTool.getJsonValue(typeObj, SessionPreference.KEY_TYPE);
                String originType =
                        JsonTool.getJsonValue(typeObj, SessionPreference.KEY_ORIGIN_TYPE);
                if (dataParams != null
                        && SessionPreference.VALUE_TYPE_WAITING.equals(type)
                        && (SessionPreference.DOMAIN_ROUTE.equals(originType) || SessionPreference.DOMAIN_NEARBY_SEARCH
                        .equals(originType))) {
                    return;
                } else {
                    sendResponse("");
                }
            } else {
                sendResponse("");
            }
        }
    }

    /**
     * TODO:if type in "data" is "CALL" need response to VUI XD modify 20150807
     */

    public void sendResponse(String params) {
        sendResponse(params, "SUCCESS");
    }

    /**
     * @param params
     * @param state
     */
    public void sendResponse(String params, String state) {
        Logger.d(TAG, "sendResponse params : " + params + "; state : " + state);
        Logger.d(TAG, "sendResponse mSystemCallJson : " + mSystemCallJson);
        JSONObject jobj = JsonTool.parseToJSONObject(mSystemCallJson);
        JSONObject data = JsonTool.getJSONObject(jobj, SessionPreference.KEY_DATA);
        String callName = JsonTool.getJsonValue(data, SessionPreference.KEY_FUNCTION_NAME);
        String callID = JsonTool.getJsonValue(jobj, SessionPreference.KEY_CALL_ID);
        String param = "";
        String domain = "";
        if (params != null && params.length() > 0) {
            param = params;
            // add tyz 0714
            JSONObject params_ = JsonTool.getJSONObject(data, "params");
            JSONObject type = JsonTool.getJSONObject(params_, "type");
            domain = JsonTool.getJsonValue(type, "origin_type");
            Logger.d(TAG, "sendResponse domain : " + domain);
            // add tyz 0714
        } else {
            JSONObject dataParams = JsonTool.getJSONObject(data, SessionPreference.KEY_PARAMS);
            if (dataParams != null) {
                param = dataParams.toString();
            }
        }

        messageTransCenter.sendResponse(callID, callName, state, domain, param);
    }

    public void unBindService() {
        try {
            if (mContext != null) {
                Logger.d(TAG, "!--->unBindService");
                mContext.unbindService(mConnection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopBindService() {
        Logger.d(TAG, "stopBindService");
        Intent intent = new Intent(ACTION_MSG_MESSAGEROUTER_START);
        // for android 5.0 and above
        if (getExplicitIntent(mContext, intent) != null) {
            intent = getExplicitIntent(mContext, intent);
        }
        mContext.stopService(intent);
    }

    private void stopWindowService() {
        Logger.d(TAG, "stopWindowService");
        Intent intent = new Intent(this, WindowService.class);
        mContext.stopService(intent);
    }

    /**
     * register System Call to VUI service 0 setState 1 onRecordingStart 2 onRecordingStop 3
     * isASRCompileDone
     */
    private void registSystemCall() {
        // String systemCall =
        // "{\"type\":\"REG\",\"data\":{\"version\":\"v3.0\",\"moduleName\":\"GUI\",\"callNameList\":[{\"callName\":\"setState\"},{\"callName\":\"isASRCompileDone\"},{\"callName\":\"onRecordingStart\"},{\"callName\":\"onRecordingStop\"}]}}";
        sendMsg(GuiProtocolUtil.getRegisterSystemCallProtocol(mContext));
    }

    /**
     * send message to framework service
     *
     * @param msgJson
     */
    private void sendMsg(final String msgJson) {
        //重新绑定
        rebindMessageRouterService();

        if (mMessageRouterService != null) {
            try {
                mMessageRouterService.sendMessage(msgJson);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * when Click mic icon, send "PTT" event to start record when GUI is run in background
     */
    public void sendPTT() {
        Logger.d(TAG, "sendPTT");

        sendEvent(SessionPreference.EVENT_NAME_PTT);
    }


    //音乐播放完毕
    public void sendMusicDone() {
        sendEvent(SessionPreference.EVENT_MUSIC_DONE);
    }

    /* < XD 20150828 added for Music Doreso Begin */

    /**
     * sendPTTDoreso: "PTT_DORESO"
     */
    public void sendPTTDoreso() {
        Logger.d(TAG, "EVENT_NAME_PTT_DORESO");
        sendEvent(SessionPreference.EVENT_NAME_PTT_DORESO);
    }

    /**
     * send PTT By WakeupSuccess Type
     */
    public void sendPTTByWakeupSuccessType() {
        Logger.d(TAG, "!--->sendPTTByWakeupSuccessType--mWakeupSuccessType = " + mWakeupSuccessType);
        if (SessionPreference.VALUE_SET_STATE_TYPE_WAKEUP_SUCCESS == mWakeupSuccessType) {
            sendPTT();
        } else if (SessionPreference.VALUE_SET_STATE_TYPE_WAKEUP_SUCCESS_DORESO == mWakeupSuccessType) {
            sendPTTDoreso();
        }
    }

    /* XD 20150828 added for Music Doreso End > */

    /**
     * XD added 2015-12-22
     */
    public void sendRequestUuidEvent() {
        sendEvent(SessionPreference.EVENT_NAME_REQUEST_UUID);
    }

    /**
     * @param paramJson
     * @author xiaodong.he
     * @modifyDate : 2016-2-1
     */
    public void sendCancelEvent(String paramJson) {
        Logger.d(TAG, "sendCancelEvent--paramJson:" + paramJson);
        sendProtocolEvent(SessionPreference.EVENT_NAME_CTT, paramJson);
    }

    public void sendStartLocalSearchNaviEvent() {
        Logger.d(TAG, "sendStartLocalSearchNaviEvent");
        sendEvent(SessionPreference.EVENT_NAME_LOCALSERACH_NAVI);
    }

    public void sendStartLocalSearchCallEvent() {
        Logger.d(TAG, "sendStartLocalSearchCallEvent");
        sendEvent(SessionPreference.EVENT_NAME_LOCALSERACH_CALL);
    }

    /**
     * called on mUIHandler MSG_GUI_CANCEL_WAITTING_SESSION XD 20150813 modify
     */
    private void sendWaittingCancelEvent() {
        Logger.d(TAG, "sendWaittingCancelEvent");
        sendEvent(SessionPreference.EVENT_NAME_WAITTING_CANCEL);
    }

    public void sendResetWakeupWordEvent() {
        sendEvent(SessionPreference.EVENT_NAME_RESET_WAKEUP_WORD);
    }

    public void sendStopWakeupWordEvent() {
        sendEvent(SessionPreference.EVENT_NAME_STOP_WAKEUP);
    }

    public void sendRequestWakeupWordsEvent() {
        sendEvent(SessionPreference.EVENT_NAME_REQUEST_WAKEUP_WORDS);
    }

    /**
     * @param oldWakeupWord
     * @param newWakeupWord
     */
    public void sendUpdateWakeupWordEvent(String oldWakeupWord, String newWakeupWord) {
        sendProtocolEvent(SessionPreference.EVENT_NAME_UPDATE_WAKEUP_WORD, "{\"oldWakeupWord\":"
                + "\"" + oldWakeupWord + "\" , \"newWakeupWord\":" + "\"" + newWakeupWord + "\" }");
    }

    //发送事件
    private void sendEvent(String eventName) {
        String eventMsg =
                "{\"type\":\"EVENT\",\"data\":{\"moduleName\":\"GUI\",\"eventName\":" + eventName
                        + "}}";
        Logger.d(TAG, "-sendEvent- eventMsg : " + eventMsg);
        sendMsg(eventMsg);
    }

    /**
     * TYZ addded 20151013 XD modify 20151023
     *
     * @param eventName
     * @param keyWord
     */
    private void sendkeywordEvent(String eventName, String keyWord) {
        // String eventMsg =
        // "{\"type\":\"EVENT\",\"data\":{\"moduleName\":\"GUI\",\"eventName\":" + eventName
        // + ",\"keyword\":" + keyWord + "}}";
        // Logger.d(TAG, "-sendEvent- eventMsg : " + eventMsg);
        // sendMsg(eventMsg);
        sendProtocolEvent(eventName, GuiProtocolUtil.getChangeLocationParamProtocol(keyWord));
    }

    /**
     * xd added 20150702
     *
     * @param eventName : ON_CONFIRM_OK, ON_CONFIRM_CANCEL, SELECT_ITEM
     * @param protol
     */
    public void sendProtocolEvent(String eventName, String protol) {
        Logger.d(TAG, "sendProtocolEvent()----eventName =" + eventName + "; protol = " + protol);
        if (SessionPreference.EVENT_NAME_ON_CONFIRM_TIME_UP.equals(eventName)) {
            protol = SessionPreference.EVENT_PROTOCAL_ON_CONFIRM_TIME_UP;
        }
        String eventMsg =
                "{\"type\":\"EVENT\",\"data\":{\"moduleName\":\"GUI\",\"eventName\":\"" + eventName
                        + "\"},\"param\":" + protol + "}";
        // {"type":"EVENT","data":{"moduleName":"GUI","eventName":"ON_CONFIRM_OK"},"param":{"service":"cn.yunzhisheng.setting","semantic":{"intent":{"confirm":"OK"}},"code":"SETTING_EXEC"}}
        // {"type":"EVENT","data":{"moduleName":"GUI","eventName":"ON_CONFIRM_CANCEL"},"param":{"service":"cn.yunzhisheng.setting","semantic":{"intent":{"confirm":"CANCEL"}},"code":"SETTING_EXEC"}}
        // {"type":"EVENT","data":{"moduleName":"GUI","eventName":"ON_CONFIRM_TIME_UP"},"param":{"service":"cn.yunzhisheng.setting","semantic":{"intent":{"confirm":"TIME_UP"}},"code":"SETTING_EXEC"}}
        // {"type":"EVENT","data":{"moduleName":"GUI","eventName":"SMS_CONTENT_RETYPE"},"param":{"service":"cn.yunzhisheng.setting","semantic":{"intent":{"confirm":"SMS_CONTENT_RETYPE"}},"code":"SETTING_EXEC"}}
        Logger.d(TAG, "!--->sendProtol()----eventMsg = " + eventMsg);
        sendMsg(eventMsg);
    }

    private void sendPushEvent(PushModle pushModle) {
        String title = "";
        String content = "content";
        String eventName = SessionPreference.EVENT_NAME_INCOMING_PUSH_MESSAGE;
        String protocal =
                "{\"service\":\"cn.yunzhisheng.setting\",\"semantic\":{\"intent\":{\"confirm\":\"INCOMING_PUSH_MESSAGE\"}},\"code\":\"SETTING_EXEC\",\"number\":\""
                        + title + "\",\"content\":\"" + content + "\",\"rc\":0}";
        try {
            JSONObject jsonObject = new JSONObject(protocal);
            jsonObject.put("content", new Gson().toJson(pushModle));
            protocal = jsonObject.toString();
        } catch (Exception e) {
        }
        Logger.d(TAG, "!--->sendPushEvent-eventName=" + eventName + "; protocal : " + protocal);
        sendProtocolEvent(eventName, protocal);
    }

    private void sendReceivedSmsEvent(String number, String content) {
        String eventName = SessionPreference.EVENT_NAME_INCOMING_SMS;
        // "{\"service\":\"cn.yunzhisheng.setting\",\"semantic\":{\"intent\":{\"confirm\":\"INCOMING_CALL\"}},\"code\":\"SETTING_EXEC\",\"type\":\"10086\"}"
        String protocal =
                "{\"service\":\"cn.yunzhisheng.setting\",\"semantic\":{\"intent\":{\"confirm\":\"INCOMING_SMS\"}},\"code\":\"SETTING_EXEC\",\"number\":\""
                        + number + "\",\"content\":\"" + content + "\",\"rc\":0}";
        Logger.d(TAG, "!--->sendReceivedSmsEvent-eventName=" + eventName + "; protocal : "
                + protocal);
        sendProtocolEvent(eventName, protocal);
    }

    /**
     * IS_ASR_COMPILE_FINISH  语音识别是否完成
     */
    public void requestIsASRCompileFinished() {
        if (mMessageRouterService == null) {
            rebindMessageRouterService();     //重新绑定处理
            return;
        }
        String eventMsg ="{\"type\":\"EVENT\",\"data\":{\"moduleName\":\"GUI\",\"eventName\":\"IS_ASR_COMPILE_FINISH\"}}";
        sendMsg(eventMsg);
    }



    /**
     * requestSupportDomainList xd added 20150702
     */
    public void requestSupportDomainList() {
        Logger.d(TAG, "!--->requestSupportDomainList()----");
        String eventMsg =
                "{\"type\":\"EVENT\",\"data\":{\"moduleName\":\"GUI\",\"eventName\":\"GET_SUPPORT_DOMAIN_LIST\"}}";
        sendMsg(eventMsg);
    }

    /**
     * 请求打出电话
     */
    public void requestCallOut() {
        String eventMsg =
                "{\"type\":\"EVENT\",\"data\":{\"moduleName\":\"GUI\",\"eventName\":\"MAIN_ACTION\"},\"param\":{\"type\":\"call\"}}";
        sendMsg(eventMsg);
    }

    /**
     * 请求导航
     */
    public void requestNavication() {
        isMainMark = true;
        String eventMsg ="{\"type\":\"EVENT\",\"data\":{\"moduleName\":\"GUI\",\"eventName\":\"MAIN_ACTION\"},\"param\":{\"type\":\"navi\"}}";
        sendMsg(eventMsg);
    }

    /**
     * 请求音乐
     */
    public void requestMusic() {
        String eventMsg =
                "{\"type\":\"EVENT\",\"data\":{\"moduleName\":\"GUI\",\"eventName\":\"MAIN_ACTION\"},\"param\":{\"type\":\"music\"}}";
        sendMsg(eventMsg);
    }

    /**
     * 请求本地查找
     */
    public void requestLocalSearch() {
        isMainMark = true;
        String eventMsg =
                "{\"type\":\"EVENT\",\"data\":{\"moduleName\":\"GUI\",\"eventName\":\"MAIN_ACTION\"},\"param\":{\"type\":\"localsearch\"}}";
        sendMsg(eventMsg);
    }

    /**
     * write Contacts Info
     */
    private void writeContactsInfo() {
        if (PackageUtil.isRunningForeground(WindowService.this)) {
            mSessionManager.showInitView(true);
        }
        if (!DeviceTool.isSdCardExist()) {
            Logger.e(TAG, "!--->No SDCard mounted!");
            mSessionManager.showInitFailedView(mContext
                    .getString(R.string.error_init_msg_no_sdcard));
            // Toast.makeText(mContext, mContext.getString(R.string.error_init_msg_no_sdcard),
            // Toast.LENGTH_LONG).show();
            return;
        }
        /* XD added 20150706 end > */
        contactDataModel = new ContactDataModel(mContext);
        contactDataModel.setDataModelListener(mDataModelListener);
        contactDataModel.init();
    }

    /**
     * write Medias Info
     */
    private void writeMediasInfo() {
        Logger.d(TAG, "!--->writeMediasInfo()-----");
        mediaDataModel = new MediaDataModel(mContext);
        mediaDataModel.setDataModelListener(mDataModelListener);
        mediaDataModel.init();
    }

    // add by tyz 0709 write apps info
    protected void writeAppsInfo() {
        // TODO Auto-generated method stub
        Logger.d(TAG, "!--->writeAppsInfo()-----");
        appsDataModel = new AppsDataModel(mContext);
        appsDataModel.setDataModelListener(mDataModelListener);
        appsDataModel.init();
    }

    IBaseListener mDataModelListener = new IBaseListener() {
        @Override
        public void onDataDone(int type) {
            switch (type) {
                case SessionPreference.SAVE_CONTACT_DATA_DONE:
                    sendDataDoneEvent(SessionPreference.EVENT_NAME_SAVE_CONTACTS_DONE);
                    // XD added 2015-12-7
                    mContext.sendBroadcast(new Intent(GUIConfig.ACTION_ON_CONTACT_DATA_DONE));
                    break;
                case SessionPreference.SAVE_MEDIA_DATA_DONE:
                    sendDataDoneEvent(SessionPreference.EVENT_NAME_SAVE_MEDIAS_DONE);
                    break;
                case SessionPreference.SAVE_APPS_DATA_DONE:
                    sendDataDoneEvent(SessionPreference.EVENT_NAME_SAVE_APPS_DONE);
                    break;
                case SessionPreference.SAVE_UPDATE_CONTACT_DATA_DONE:
                    sendDataDoneEvent(SessionPreference.EVENT_NAME_SAVE_UPDATE_CONTACTS_DONE);
                    break;
                case SessionPreference.SAVE_UPDATE_MEDIA_DATA_DONE:
                    sendDataDoneEvent(SessionPreference.EVENT_NAME_SAVE_UPDATE_MEDIAS_DONE);
                    break;
                case SessionPreference.SAVE_UPDATE_APPS_DATA_DONE:
                    sendDataDoneEvent(SessionPreference.EVENT_NAME_SAVE_UPDATE_APPS_DONE);
                    break;
            }
        }
    };

    private void sendDataDoneEvent(String eventName) {
        JSONObject objcData = new JSONObject();
        try {
            objcData.put("type", "EVENT");
            JSONObject data = new JSONObject();
            data.put("moduleName", "GUI");
            data.put("eventName", eventName);
            objcData.put("data", data);
            sendMsg(objcData.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ISystemCallTransitionListener mMessageTransListener =
            new ISystemCallTransitionListener() {

                @Override
                public void setState(int type) {
                    Logger.d(TAG, "!--->setState()----type = " + type);
                    Message msg = new Message();
                    msg.what = MSG_SET_STATE;
                    msg.obj = type;
                    mUIHandler.sendMessage(msg);
                }

                @Override
                public void onEngineInfo(JSONObject infoJson) {
                    Logger.d(TAG, "!--->---onEngineInfo---" + infoJson);
                    EngineInfoUtil.updateGuiVersionByEngineInfo(mContext, infoJson);
                    boolean isASRDueDate = EngineInfoUtil.isASREngineDueDate;
                    if (isASRDueDate) {
                        Message msg = new Message();
                        msg.what = MSG_ON_ASR_ENGINE_DUEDATE;
                        mUIHandler.sendMessage(msg);
                    }
                }

                @Override
                public void onTalkRecordingPrepared() {
                    Logger.d(TAG, "!--->---onTalkRecordingPrepared---");
                    Message msg = new Message();
                    msg.what = MSG_ON_RECORDING_PREPARED;
                    mUIHandler.sendMessage(msg);
                    // mSessionManager.onTalkRecordingStart();
                }

                @Override
                public void onTalkRecordingException() {
                    mUIHandler.sendEmptyMessage(MSG_ON_RECORDING_EXCEPTION);
                }

                @Override
                public void onTalkRecordingStart() {
                    Logger.d(TAG, "!--->---onTalkRecordingStart---");
                    Message msg = new Message();
                    msg.what = MSG_ON_RECORDING_START;
                    mUIHandler.sendMessage(msg);
                    // mSessionManager.onTalkRecordingStart();
                }

                @Override
                public void onTalkRecordingStop() {
                    Logger.d(TAG, "!--->---onTalkRecordingStop---");
                    Message msg = new Message();
                    msg.what = MSG_ON_RECORDING_STOP;
                    mUIHandler.sendMessage(msg);
                    // mSessionManager.onTalkRecordingStop();
                }

                @Override
                public void onTalkResult(String result) {
                    Logger.d(TAG, "!--->onTalkResult---result = " + result);
                    Message msg = new Message();
                    msg.what = MSG_ON_RECORDING_RESULT;
                    msg.obj = result;
                    mUIHandler.sendMessage(msg);
                }

                @Override
                public void onSessionProtocal(String protocol) {
                    Message msg = new Message();
                    msg.what = MSG_ON_SESSION_PROTOCAL;
                    msg.obj = protocol;
                    mUIHandler.sendMessage(msg);
                    // mSessionManager.onSessionProtocal(protocol);
                }

                @Override
                public void onSendMsg(String msg) {
                    sendMsg(msg);
                }

                @Override
                public void onPlayEnd(String ttsID) {
                    Message msg = new Message();
                    msg.what = MSG_ON_TTS_PLAY_END;
                    msg.obj = ttsID;
                    mUIHandler.sendMessage(msg);
                    // mSessionManager.onPlayEnd();
                }

                @Override
                public void onUpdateVolume(int volume) {
                    // xd added 20150714
                    // Logger.d(TAG, "!--->onUpdateVolume---volume = "+volume);
                    if (SessionPreference.VALUE_SET_STATE_TYPE_WAKEUP_SUCCESS == mWakeupSuccessType) {
                        mMicrophoneControl.setVoiceLevel(volume);
                    } else if (SessionPreference.VALUE_SET_STATE_TYPE_WAKEUP_SUCCESS_DORESO == mWakeupSuccessType) {
                        mMicrophoneDoresoControl.setVoiceLevel(volume);
                    }
                }

                @Override
                public void onRecognizerTimeout() {
                    Message msg = new Message();
                    msg.what = MSG_ON_RECOGNIZER_TIMEOUT;
                    mUIHandler.sendMessage(msg);
                }

                @Override
                public void onCTTCancel() {
                    Message msg = new Message();
                    msg.what = MSG_ON_CTT_CANCEL;
                    mUIHandler.sendMessage(msg);
                }

                @Override
                public void onOneShotRecognizerTimeout() {
                    Logger.d(TAG, "!--->---onOneShotRecognizerTimeout---");
                    Message msg = new Message();
                    msg.what = MSG_ON_ONESHOT_RECOGNIZER_TIMEOUT;
                    mUIHandler.sendMessage(msg);
                }

                @Override
                public void onStartFakeAnimation() {
                    Logger.d(TAG, "!--->onStartFakeAnimation---");
                    Message msg = new Message();
                    msg.what = MSG_ON_START_RECORDING_FAKE_ANIMATION;
                    mUIHandler.sendMessage(msg);
                }

                @Override
                public void onGetWakeupWords(String wakeupWords) {
                    Logger.d(TAG, "!--->onGetWakeupWords---");
                    Message msg = new Message();
                    msg.what = MSG_ON_GET_WAKEUP_WORDS;
                    msg.obj = wakeupWords;
                    mUIHandler.sendMessage(msg);
                }

                public void onClickMainActionButton(int style) {// add tyz 20151020 do mian action
                    // branch
                    // TODO Auto-generated method stub
                    Logger.d(TAG, "!--->onClickMainActionButton()----style = " + style);
                    Message msg = new Message();
                    msg.what = MSG_ON_CLICK_MAIN_ACTION_BUTTON;
                    msg.obj = style;
                    mUIHandler.sendMessage(msg);
                }

                @Override
                public void onControlWakeupSuccess(String wakeupWord) {
                    // 唤醒成功
                    Logger.d(TAG, "!--->onControlWakeupSuccess wakeupWord : " + wakeupWord);
                    Message msg = new Message();
                    msg.what = MSG_ON_CONTROL_WAKEUP_SUCCESS;
                    msg.obj = wakeupWord;
                    mUIHandler.sendMessage(msg);
                }

                @Override
                public void onUpdateWakeupWordsStatus(String status) {
                    Logger.d(TAG, "!--->onUpdateWakeupWordsStatus status : " + status);
                    Message msg = new Message();
                    msg.what = MSG_ON_UPDATE_WAKEUP_WORDS_STATUS;
                    msg.obj = status;
                    mUIHandler.sendMessage(msg);
                }

                @Override
                public void onTalkRecodingIdle() {
                    Logger.d(TAG, "!--->onTalkRecodingIdle");
                    Message msg = new Message();
                    msg.what = MSG_ON_RECORDING_IDLE;
                    mUIHandler.sendMessage(msg);
                }

                @Override
                public void onGetUuid(String uuid) {
                    Logger.d(TAG, "!--->onGetUuid---uuid = " + uuid);
                    Message msg = new Message();
                    msg.what = MSG_ON_GET_UUID;
                    msg.obj = uuid;
                    mUIHandler.sendMessage(msg);
                }

                @Override
                public void onSwitchTTS(String ttsTimbre) {
                    Logger.w(TAG, "onSwitchTTS---ttsTimbre = " + ttsTimbre);
                    Message msg = new Message();
                    msg.what = MSG_ON_SWITCH_TTS_DONE;
                    msg.obj = ttsTimbre;
                    mUIHandler.sendMessage(msg);
                }

                @Override
                public void onGetTTSModelList(String json) {
                    // TODO Auto-generated method stub
                    Logger.d(TAG, "onTTSModelList--json = " + json);
                    Message msg = new Message();
                    msg.what = MSG_ON_GET_TTS_MODEL_LIST;
                    msg.obj = json;
                    mUIHandler.sendMessage(msg);
                }

                @Override
                public void onCopyTTSModelSuccess(String modleName) {
                    Logger.d(TAG, "onCopyTTSModelSuccess--modleName:" + modleName);
                    Message msg = new Message();
                    msg.what = MSG_ON_COPY_TTS_MODEL_SUCCESS;
                    msg.obj = modleName;
                    mUIHandler.sendMessage(msg);
                }

                @Override
                public void onCopyTTSModelFail(String modleName, String failcause) {
                    Logger.d(TAG, "onCopyTTSModelFail--modleName:" + modleName + "; failcause:"
                            + failcause);
                    Message msg = new Message();
                    msg.what = MSG_ON_COPY_TTS_MODEL_FAIL;
                    Bundle b = new Bundle();
                    b.putString(KEY_TTS_MODLE_NAME, modleName);
                    b.putString(KEY_TTS_FAILCAUSE, failcause);
                    msg.setData(b);
                    mUIHandler.sendMessage(msg);
                }

                @Override
                public void onAddedFavoriteAddress(String locationType, String locationInfoJson) {
                    Message msg = new Message();
                    msg.what = MSG_ON_ADDED_FAVORITE_ADDRESS;
                    Bundle b = new Bundle();
                    b.putString(GuiSettingUpdateUtil.KEY_ADD_ADDRESS_TYPE, locationType);
                    b.putString(GuiSettingUpdateUtil.KEY_ADD_ADDRESS_LOCATION, locationInfoJson);
                    msg.setData(b);
                    mUIHandler.sendMessage(msg);
                }

                private long lastReceived = 0;

                @Override
                public void onPushMessageReceive(PushModle pushModle) {
                    long now = System.currentTimeMillis();
                    if (now - lastReceived < 3000) {
                        lastReceived = now;
                        return;
                    }
                    PushBlockQueue.getInstance().addQueue(pushModle);
                }

                @Override
                public void onShowBindInfo(String info) {
                    Logger.d(TAG, "onShowBindInfo  info :" + info);
                    Message msg = new Message();
                    msg.what = MSG_ON_SHOW_BIND_INFO;
                    msg.obj = info;
                    mUIHandler.sendMessage(msg);
                }

                @Override
                public void onSpeakerSpeechDetected() {
                    mUIHandler.sendEmptyMessage(MSG_ON_SPEAKER_SPEECH_DETECHED);
                }

                @Override
                public void onAecCancel() {
                    Log.e(TAG, "aec cancel");
                    Message msg = new Message();
                    msg.what = MSG_ON_AEC_CANCEL;
                    mUIHandler.sendMessage(msg);

                }

                @Override
                public void getCurrentTTSType(String ttsType) {
                    // 当push在前台的时候过来导航的tts的时候需要将push的dimiss
                    judgeIsDismissPushView(PARAM_TTS_FROM);
                    PARAM_TTS_FROM = ttsType;
                }
            };

    private void judgeIsDismissPushView(String ttsType) {
        Logger.d(TAG, "judgeIsDismissPushView ttsType : " + ttsType);
        if (isPushShow()) {
            if (PARAM_TTS_FROM_UNICARNAVI.equals(ttsType)) {
                mSessionManager.onUiProtocal(SessionPreference.EVENT_NAME_INCOMING_PUSH_MESSAGE,
                        SessionPreference.EVENT_PROTOCAL_ON_CONFIRM_CANCEL_PUSH_MESSAGE_AUTO);
            }
        }
    }

    /**
     * XD 20150827 added
     *
     * @param type
     */
    private void updateMicControlView(int type) {
        Logger.d(TAG, "!--->updateMicControlView---type = " + type);
        if (SessionPreference.VALUE_SET_STATE_TYPE_WAKEUP_SUCCESS == type) {
            mMicrophoneControl.setVisibility(View.VISIBLE);
            mMicrophoneDoresoControl.setVisibility(View.GONE);
            mSessionManager.setMicrophoneControl(mMicrophoneControl); // XD 20150827 added
        } else if (SessionPreference.VALUE_SET_STATE_TYPE_WAKEUP_SUCCESS_DORESO == type) {
            mMicrophoneControl.setVisibility(View.GONE);
            mMicrophoneDoresoControl.setVisibility(View.VISIBLE);
            mSessionManager.setMicrophoneControl(mMicrophoneDoresoControl); // XD 20150827 added
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mUIHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_DISMISS_WELCOME_ACTIVITY:
                    sendDismissWelcomeActivityBroadcast();
                    break;
                case MSG_SET_STATE:
                    int type = (Integer) msg.obj;
                    switch (type) {
                        case SessionPreference.VALUE_SET_STATE_TYPE_WAKEUP_INIT_DONE:
                            Logger.d(TAG, "!--->VALUE_SET_STATE_TYPE_WAKEUP_INIT_DONE");
                            // 唤醒已经打开 已经初始化完成，可以说命令词了
                            mSessionManager.onWakeUpInitDone();
                            break;
                        case SessionPreference.VALUE_SET_STATE_TYPE_WAKEUP_SUCCESS:
                            Logger.d(TAG, "!--->VALUE_SET_STATE_TYPE_WAKEUP_SUCCESS");
                            mWakeupSuccessType =
                                    SessionPreference.VALUE_SET_STATE_TYPE_WAKEUP_SUCCESS;
                            updateMicControlView(mWakeupSuccessType);
                            mSessionManager.onNormalWakeUpSuccess();
                            break;
                        case SessionPreference.VALUE_SET_STATE_TYPE_WAKEUP_SUCCESS_DORESO:
                            // 我想哼歌 唤醒成功
                            Logger.d(TAG, "!--->VALUE_SET_STATE_TYPE_WAKEUP_SUCCESS_DORESO");
                            mWakeupSuccessType =
                                    SessionPreference.VALUE_SET_STATE_TYPE_WAKEUP_SUCCESS_DORESO;
                            updateMicControlView(mWakeupSuccessType);
                            mSessionManager.onWakeUpSuccessDoreso();
                            break;
                        case SessionPreference.VALUE_SET_STATE_TYPE_ASR_COMPILE_FINISH:
                            Logger.d(TAG, "!--->VALUE_SET_STATE_TYPE_ASR_COMPILE_FINISH");
                            // 语法编译成功后通知wechat去bind vui
                            sendBroadcastForWechat();
                            isASRCompile = true;
                            // 语法数据编译完成
                            mSessionManager.onTalkDataDone();
                            showWakeupStatusToast(); // XD 20150819 added
                            Constant.setFirstStart(mContext, false); // first start has finished.
                            requestSupportDomainList(); // xd added 20150702
                            // 在第一次数据编译完成后，再监听数据变化 //LP added 20151104
                            if (contactDataModel != null) {
                                contactDataModel.registerObserver();
                            }

                            if (appsDataModel != null) {
                                appsDataModel.registerReceiver();
                            }
                            break;
                        case SessionPreference.VALUE_SET_STATE_TYPE_WRITE_CONTACT_INFO:
                            Logger.d(TAG, "!--->VALUE_SET_STATE_TYPE_WRITE_CONTACT_INFO----");
                            writeContactsInfo();
                            break;
                        case SessionPreference.VALUE_SET_STATE_TYPE_WRITE_MEDIA_INFO:
                            writeMediasInfo();
                            break;
                        case SessionPreference.VALUE_SET_STATE_TYPE_WRITE_APPS_INFO:
                            writeAppsInfo();
                            break;
                        case SessionPreference.VALUE_SET_STATE_TYPE_GET_SUPPORT_DOMAIN_LIST:
                            Logger.d(TAG, "!--->VALUE_SET_STATE_TYPE_GET_SUPPORT_DOMAIN_LIST----");
                            // TODO:
                            String supportListProtocol = "";
                            onGetSupportListProtocol(supportListProtocol);
                            break;
                        default:
                            break;
                    }
                    break;
                case MSG_ON_CONTROL_WAKEUP_SUCCESS:
                    String wakeupWord = (String) msg.obj;
                    mSessionManager.onControlWakeUpSuccess(wakeupWord);
                    break;
                case MSG_ON_RECORDING_PREPARED:
                    Logger.d(TAG, "!--->MSG_ON_RECORDING_PREPARED----");
                    getGeneralGPS();
                    mSessionManager.onTalkRecordingPrepared();
                    requestAudioFocus(); // XD
                    // added
                    // 20160119
                    break;
                case MSG_ON_RECORDING_EXCEPTION:
                    Logger.d(TAG, "!--->MSG_ON_RECORDING_EXCEPTION-----");
                    mSessionManager.onTalkRecordingException();
                    break;
                case MSG_ON_RECORDING_START:
                    Logger.d(TAG, "!--->MSG_RECORDING_START----");
                    mSessionManager.onTalkRecordingStart();
                    break;
                case MSG_ON_RECORDING_STOP:
                    Logger.d(TAG, "!--->MSG_ON_RECORDING_STOP----");
                    // 正在识别
                    mSessionManager.onTalkRecordingStop();
                    break;
                case MSG_ON_RECORDING_RESULT:
                    String result = (String) msg.obj;
                    /* < XD 20150906 modify for ASR partial result Begin */
                    Logger.d(TAG, "!--->MSG_ON_RECORDING_RESULT----result = " + result);
                    // {"result":"打电话","resultType":"full"}
                    JSONObject resultObj = JsonTool.parseToJSONObject(result);
                    String resultType = JsonTool.getJsonValue(resultObj, "resultType", "full"); // "partial"
                    String text = JsonTool.getJsonValue(resultObj, "result");
                    String recognizerGender = JsonTool.getJsonValue(resultObj, "recognizerGender");
                    /*
                     * if (!TextUtils.isEmpty(recognizerGender)) { if
                     * ("male".equals(recognizerGender)) { UserPerferenceUtil.setTtsTimbre(mContext,
                     * UserPerferenceUtil.VALUE_TTS_TIMBRE_STANDARD);
                     * sendTtsTimbre(SessionPreference.PARAM_TYPE_NOT_PLAY_TTS_GENDER); } else if
                     * ("female".equals(recognizerGender)) {
                     * UserPerferenceUtil.setTtsTimbre(mContext,
                     * UserPerferenceUtil.VALUE_TTS_TIMBRE_SEXY);
                     * sendTtsTimbre(SessionPreference.PARAM_TYPE_NOT_PLAY_TTS_GENDER); } }
                     */
                    boolean isPartial = false;
                    if ("partial".equals(resultType) || "change".equals(resultType)) {
                        isPartial = true;
                    }
                    mSessionManager.onTalkRecordingResult(text, isPartial);
                    /* XD 20150906 modify for ASR partial result End > */
                    break;
                case MSG_ON_SESSION_PROTOCAL:
                    Logger.d(TAG, "!--->MSG_ON_SESSION_PROTOCAL----");
                    String sessionProtocol = (String) msg.obj;
                    mSessionManager.onSessionProtocal(sessionProtocol);
                    break;
                case MSG_ON_TTS_PLAY_END:
                    String ttsID = (String) msg.obj;
                    Logger.d(TAG, "!--->MSG_ON_TTS_PLAY_END----ttsID:" + ttsID);
                    if (TextUtils.isEmpty(ttsID) && null != mSessionManager) {
                        mSessionManager.onTTSPlayEnd();
                    } else {
                        TTSEndManager.onTTSPlayEnd(mContext, ttsID);
                    }
                    break;
                case MSG_ON_RECOGNIZER_TIMEOUT:
                    Logger.d(TAG, "!--->MSG_ON_RECOGNIZER_TIMEOUT----");
                    mSessionManager.onRecognizerTimeout();
                    break;
                case MSG_GUI_CANCEL_SESSION:
                    String param = (String) msg.obj;
                    Logger.d(TAG, "!--->MSG_GUI_CANCEL_SESSION---param:" + param);
                    sendCancelEvent(param);
                    // mSessionManager.dismissWindowService(); //DELET do this on onCTTCancel()
                    // system call
                    break;
                case MSG_GUI_CANCEL_WAITTING_SESSION:
                    Logger.d(TAG, "!--->MSG_GUI_CANCEL_WAITTING_SESSION--sendWaittingCancelEvent--");
                    sendWaittingCancelEvent();
                    break;
                case MSG_ON_CTT_CANCEL:
                    Logger.d(TAG, "!--->MSG_ON_CTT_CANCEL----");
                    mSessionManager.onCTTCancel();
                    break;
                case MSG_ON_AEC_CANCEL:
                    Logger.d(TAG, "!--->MSG_ON_CTT_CANCEL----");
                    mSessionManager.cancelTalk();
                    break;
                case MSG_ON_ONESHOT_RECOGNIZER_TIMEOUT:
                    Logger.d(TAG, "!--->MSG_ON_ONESHOT_RECOGNIZER_TIMEOUT----");
                    mSessionManager.onOneShotRecognizerTimeOut();
                    break;
                case MSG_ON_START_RECORDING_FAKE_ANIMATION:
                    Logger.d(TAG, "!--->MSG_ON_START_RECORDING_FAKE_ANIMATION----");
                    mSessionManager.onStartRecordingFakeAnimation();
                    break;
                case MSG_ON_GET_WAKEUP_WORDS:
                    String wakeupWords = (String) msg.obj;
                    Logger.d(TAG, "!--->MSG_ON_GET_WAKEUP_WORDS----wakeupWords = " + wakeupWords);
                    UserPerferenceUtil.setWakeupWords(mContext, wakeupWords);
                    break;
                case MSG_ON_CLICK_MAIN_ACTION_BUTTON:
                    int style = (Integer) msg.obj;
                    Logger.d(TAG, "!--->MSG_ON_CLICK_MAIN_ACTION_BUTTON--- style-" + style);
                    switch (style) {
                        case 23:// call

                            break;
                        case 24:// navi

                            break;
                        case 25:// music

                            break;
                        case 26:// localsearch

                            break;

                        default:
                            break;
                    }
                    break;
                case MSG_ON_UPDATE_WAKEUP_WORDS_STATUS:
                    String status = (String) msg.obj;
                    Logger.d(TAG, "!--->MSG_ON_UPDATE_WAKEUP_WORDS_STATUS status : " + status);
                    Intent intent = new Intent(GUIConfig.ACTION_WAKEUP_WORDS_UPDATE);
                    sendBroadcast(intent);
                    if ("SUCCESS".equals(status)) {
                        Logger.d(TAG, "pop.dismiss 1803");
                        sendBroadcast(intent);
                    }
                    mSessionManager.onCTTCancel();
                    break;
                case MSG_ON_RECORDING_IDLE:
                    Logger.d(TAG, "!--->MSG_ON_RECORDING_IDLE");
                    mSessionManager.onTalkRecordingIdle();
                    break;
                case MSG_ON_GET_UUID:
                    String uuid = (String) msg.obj;
                    Logger.d(TAG, "!--->MSG_ON_GET_UUID----uuid = " + uuid);
                    UserPerferenceUtil.setUuid(mContext, uuid);
                    break;
                case MSG_ON_SWITCH_TTS_DONE:
                    String ttsTimbre = (String) msg.obj;
                    Logger.d(TAG, "!--->MSG_ON_SWITCH_TTS_DONE----ttsTimbre = " + ttsTimbre);
                    GuiSettingUpdateUtil.notifySwitchTtsTimbreDone(mContext, ttsTimbre);
                    break;
                case MSG_ON_GET_TTS_MODEL_LIST:
                    String json = (String) msg.obj;
                    Logger.d(TAG, "!--->MSG_ON_GET_TTS_MODEL_LIST--ListJson = " + json);
                    GuiSettingUpdateUtil.notifyTtsTimbreList(mContext, json.toString());
                    break;
                case MSG_ON_COPY_TTS_MODEL_SUCCESS:
                    String moduleName = (String) msg.obj;
                    Logger.d(TAG, "MSG_ON_COPY_TTS_MODEL_SUCCESS--moduleName = " + moduleName);
                    // TODO: do nothing
                    break;
                case MSG_ON_COPY_TTS_MODEL_FAIL:
                    String modleName = msg.getData().getString(KEY_TTS_MODLE_NAME);
                    String failcause = msg.getData().getString(KEY_TTS_FAILCAUSE);
                    Logger.d(TAG, "MSG_ON_COPY_TTS_MODEL_FAIL--modleName = " + modleName
                            + "; failcause = " + failcause);
                    if (SessionPreference.PARAM_TTS_TIMBRE_STARNAND.equals(modleName)) {
                        // first init TTS model fail
                        if (SessionPreference.VALUE_TTS_FAILCAUSE_UNAVAILABLE_MEMORY
                                .equals(failcause)) {
                            showNoInternalMemoryView();
                        } else if (SessionPreference.VALUE_TTS_FAILCAUSE_UNAVAILABLE_FILE
                                .equals(failcause)) {
                            showCopyTTSModleFileErrorView();
                        }
                    } else {
                        // NOTIFY SettingViewPagerActivity show error view
                        GuiSettingUpdateUtil
                                .notifyCopyTtsTimbreFail(mContext, modleName, failcause);
                    }
                    break;
                case MSG_ON_ADDED_FAVORITE_ADDRESS:
                    String locationType =
                            msg.getData().getString(GuiSettingUpdateUtil.KEY_ADD_ADDRESS_TYPE);
                    String locationJson =
                            msg.getData().getString(GuiSettingUpdateUtil.KEY_ADD_ADDRESS_LOCATION);
                    saveFavoriteAddress(locationType, locationJson);
                    break;
                case MSG_ON_ASR_ENGINE_DUEDATE:
                    showDueDateView();
                    break;
                case MSG_ON_SEND_PUSH_EVENT:
                    Logger.d(TAG, "!--->MSG_ON_SEND_PUSH_EVENT----");
                    PushModle pushModle = (PushModle) msg.obj;
                    sendPushEvent(pushModle);
                    // sendReceivedSmsEvent("10001", "测试短信");
                    break;
                case MSG_ON_SHOW_BIND_INFO:
                    // 显示用户信息
                    String Info = (String) msg.obj;
                    Logger.d(TAG, "MSG_ON_SHOW_BIND_INFO  Info : " + Info);
                    showBindSuccessInfo(Info);
                    break;
                case MSG_ON_SPEAKER_SPEECH_DETECHED:
                    mSessionManager.isNeedToCancelDownTime();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    /**
     * @param locationType
     * @param locationJson
     */
    private void saveFavoriteAddress(String locationType, String locationJson) {
        if (SessionPreference.PARAM_ADDRESS_TYPE_HOME.equals(locationType)) {
            UserPerferenceUtil.setHomeLocation(mContext, locationJson);
        } else if (SessionPreference.PARAM_ADDRESS_TYPE_COMPANY.equals(locationType)) {
            UserPerferenceUtil.setCompanyLocation(mContext, locationJson);
        }
    }

    // 显示用户信息
    private void showBindSuccessInfo(String info) {
        Intent intent = new Intent();
        intent.setAction(GUIMainActivity.SHOW_SUCCESS_INFO);
        intent.putExtra("infoContent", info);
        sendBroadcast(intent);

    }

    /**
     * Sms Send Status Receiver xd added 20150709
     */
    private BroadcastReceiver mSmsSendStatusReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.d(TAG, "!--->mSmsSendStatusReceiver Action:" + intent.getAction());
            if (GUIConfig.ACTION_SMS_SEND_SUCCESS.equals(intent.getAction())) {
                mSessionManager.showSmsSendStatusView(GUIConfig.SMS_STATUS_SEND_SUCCESS);
            } else if (GUIConfig.ACTION_SMS_SEND_FAIL.equals(intent.getAction())) {
                mSessionManager.showSmsSendStatusView(GUIConfig.SMS_STATUS_SEND_FAIL);
            }
        }

    };

    /**
     * TODO: xd added 20150702
     *
     * @param supportListProtocol
     */
    private void onGetSupportListProtocol(String supportListProtocol) {
        Logger.d(TAG, "!--->onGetSupportListProtocol()--supportListProtocol = "
                + supportListProtocol);
        // mOnlineSupportList = null;
        // mOfflineSupportList = null;
    }

    /**
     * xd added 20150702
     *
     * @param hasNetWork
     * @return
     */
    public static ArrayList<String> getSupportList(boolean hasNetWork) {
        Logger.d(TAG, "!--->getSupportList----hasNetWork=" + hasNetWork);
        if (null == mOnlineSupportList && null == mOfflineSupportList) {
            testAddSupportList();
        }

        if (hasNetWork) {
            return mOnlineSupportList;
        } else {
            return mOfflineSupportList;
        }
    }

    /**
     * test function for Support List
     */
    private static void testAddSupportList() {
        mOnlineSupportList = new ArrayList<String>();
        mOnlineSupportList.add(SessionPreference.DOMAIN_CALL);
        if (GUIConfig.isShowSMSFunctionHelp) {
            mOnlineSupportList.add(SessionPreference.DOMAIN_SMS);
        }
        mOnlineSupportList.add(SessionPreference.DOMAIN_MUSIC);
        mOnlineSupportList.add(SessionPreference.DOMAIN_AUDIO);
        mOnlineSupportList.add(SessionPreference.DOMAIN_WECHAT);
        mOnlineSupportList.add(SessionPreference.DOMAIN_ROUTE);
        mOnlineSupportList.add(SessionPreference.DOMAIN_WEATHER);
        mOnlineSupportList.add(SessionPreference.DOMAIN_SETTING);
        mOnlineSupportList.add(SessionPreference.DOMAIN_STOCK);
        mOnlineSupportList.add(SessionPreference.DOMAIN_LOCAL);
        mOnlineSupportList.add(SessionPreference.DOMAIN_TRAFFIC);
        mOnlineSupportList.add(SessionPreference.DOMAIN_LIMIT);
        mOnlineSupportList.add(SessionPreference.DOMAIN_BROADCAST);

        mOfflineSupportList = new ArrayList<String>();
        mOfflineSupportList.add(SessionPreference.DOMAIN_CALL);
        mOfflineSupportList.add(SessionPreference.DOMAIN_MUSIC);
        mOfflineSupportList.add(SessionPreference.DOMAIN_BROADCAST);
        mOfflineSupportList.add(SessionPreference.DOMAIN_SETTING);
    }

    private long mLocateTime = 0;
    public static LocationInfo mLocationInfo = null;
    private long getGPSCacheTime = 20000;

    /**
     * @return
     */
    public static LocationInfo getLocationInfo() {
        return mLocationInfo;
    }

    private void getGeneralGPS() {
        Logger.d(TAG, "getGeneralGPS start mLocateTime : " + mLocateTime);
        final long now = System.currentTimeMillis();
        if (now - mLocateTime >= getGPSCacheTime || mLocateTime == 0) {
            Logger.d(TAG, "getGeneralGPS in!!!");
            LocationModelProxy locationModel = LocationModelProxy.getInstance(mContext);
            locationModel.setLocationListener(new ILocationListener() {
                @Override
                public void onLocationResult(List<LocationInfo> infos, ErrorUtil code) {

                }

                @Override
                public void onLocationChanged(LocationInfo locationInfo, ErrorUtil errorUtil) {
                    if (locationInfo != null) {
                        Logger.d(TAG, "getGeneralGPS end Latitude : " + locationInfo.getLatitude()
                                + ";Longitude : " + locationInfo.getLongitude() + ";city : "
                                + locationInfo.getCity() + "; info: " + locationInfo);
                        mLocateTime = now;
                        mLocationInfo = locationInfo;
                        if (mLocationInfo != null) {
                            if (!TextUtils.isEmpty(mLocationInfo.getCity())) {
                                sendGpsInfo(mLocationInfo);
                            } else {
                                getCityByGeoPoint(mLocationInfo.getLatitude(),
                                        mLocationInfo.getLongitude());
                            }
                        }
                    }
                }
            });
            locationModel.startLocate();
        }

        if (mLocationInfo != null) {
            if (!TextUtils.isEmpty(mLocationInfo.getCity())) {
                sendGpsInfo(mLocationInfo);
            } else {
                getCityByGeoPoint(mLocationInfo.getLatitude(), mLocationInfo.getLongitude());
            }
        }
    }

    private void sendGpsInfo(LocationInfo locationInfo) {
        GpsInfo gpsInfo = new GpsInfo();
        gpsInfo.setMapType(GpsInfo.MapGaoDe);
        gpsInfo.setArea(locationInfo.getDistrict());
        gpsInfo.setCity(mLocationInfo.getCity());
        gpsInfo.setGpsInfo(mLocationInfo.getLatitude() + "," + mLocationInfo.getLongitude());
        sendProtocolEvent(SessionPreference.EVENT_NAME_GENERAL_GPS, new Gson().toJson(gpsInfo));
    }

    /**
     * 通过经纬度获得城市
     *
     * @param lat
     * @param lang
     */
    private void getCityByGeoPoint(final double lat, final double lang) {
        GeocodeSearch geocoderSearch = new GeocodeSearch(this);
        // latLonPoint参数表示一个Latlng，第二参数表示范围多少米，GeocodeSearch.AMAP表示是国测局坐标系还是GPS原生坐标系
        LatLonPoint point = new LatLonPoint(lat, lang);
        RegeocodeQuery query = new RegeocodeQuery(point, 200, GeocodeSearch.AMAP);
        geocoderSearch.setOnGeocodeSearchListener(new OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult result, int code) {
                if (code == 0) {
                    if (result != null && result.getRegeocodeAddress() != null) {
                        String cityString =
                                TextUtils.isEmpty(result.getRegeocodeAddress().getCity()) ? result
                                        .getRegeocodeAddress().getProvince() : result
                                        .getRegeocodeAddress().getCity();
                        sendProtocolEvent(SessionPreference.EVENT_NAME_GENERAL_GPS, "{\"gpsInfo\":"
                                + "\"" + lat + "," + lang + "\",\"city\":" + cityString + "}");
                    }
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult arg0, int arg1) {

            }
        });
        geocoderSearch.getFromLocationAsyn(query);
    }


    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == mScreeFloatView.getFloatMicInstance()) {
                startTalk();     //开始通话
            }
        }
    };

    private OnSharedPreferenceChangeListener mPreferenceChangeListener =
            new OnSharedPreferenceChangeListener() {

                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                      String key) {
                    Logger.d(TAG, "!--->onSharedPreferenceChanged: key " + key);
                    if (UserPerferenceUtil.KEY_ENABLE_FLOAT_MIC.equals(key)) {
                        updateEnableFloatMic();
                        if (mEnableFloatMic) {
                            startFloatMicChecker(0);
                        }
                    }
                }
            };

    /**
     * mScreenReceiver
     */
    private BroadcastReceiver mScreenReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.d(TAG, "!--->mScreenReceiver--onReceive:intent " + intent);
            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                updateEnableFloatMic();
                startFloatMicChecker(0);
                mSessionManager.onResume();
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                stopFloatMicChecker();
                mSessionManager.onPause();
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                mSessionManager.onResume();
            } else if (ACTION_CUSTOMFROMBT.equals(action)) {
                sendEventObject(SessionPreference.EVENT_NAME_SAVE_CONTACTS_DONE);
            } else if (MessageReceiver.ACTION_START_DORMANT.equals(action)) {
                unBindService();
                stopWindowService();
                stopBindService();
                AppManager.getAppManager().finishAllActivity();
            }
            // else if (ACTION_UPDATE_WAKEUP_WORD.equals(action)) {
            // Logger.d(TAG, "!--->mScreenReceiver--ACTION_UPDATE_WAKEUP_WORD");
            // String newText = intent.getStringExtra(EXTRA_KEY_WAKEUP_WORD);
            // String oldWakeupWord = UserPerferenceUtil.getWakeupWord(mContext);
            // sendUpdateWakeupWordEvent(oldWakeupWord, newText);
            // }
        }

    };

    private void sendEventObject(String eventName) {
        JSONObject appJsonObject = new JSONObject();
        try {
            appJsonObject.put("type", "EVENT");
            JSONObject data = new JSONObject();
            data.put("moduleName", "GUI");
            data.put("eventName", eventName);
            appJsonObject.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendMsg(appJsonObject.toString());
    }

    /**
     * register mScreenReceiver XD added 20150805
     */
    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);

        filter.addAction(MessageReceiver.ACTION_START_DORMANT);
        filter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);
        filter.addAction(ACTION_CUSTOMFROMBT);
        registerReceiver(mScreenReceiver, filter);
    }

    /**
     * unregister mScreenReceiver XD added 20150805
     */
    private void unregisterReceiver() {
        unregisterReceiver(mScreenReceiver);
    }

    private void startFloatMicChecker(long delay) {
        mHandler.postDelayed(mRunnable, delay);
    }

    private void stopFloatMicChecker() {
        Logger.d(TAG, "stopFloatMicChecker");
        setFloatMicEnable(false);
        mHandler.removeCallbacks(mRunnable);
    }


    //运行一个任务
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mEnableFloatMic) {
                if (PackageUtil.isNeedHideFloatView(WindowService.this) || isNeedHideMicFloatView) {
                    hideFloatView();
                } else {
                    showFloatView();
                }
                startFloatMicChecker(800);
            } else {
                hideFloatView();
            }
        }
    };

    /**
     * setNeedHideMicFloatView for some special case XD added 20150811
     *
     * @param isNeedHide
     */
    public void setNeedHideMicFloatView(boolean isNeedHide) {
        Logger.d(TAG, "!--->setNeedHideMicFloatView---isNeedHide = " + isNeedHide);
        isNeedHideMicFloatView = isNeedHide;
    }

    public void showFloatView() {
        if (!mScreeFloatView.isShown()) {
            Logger.d(TAG, "!--->showFloatView---");
            mScreeFloatView.show();
        }
    }

    public void hideFloatView() {
        if (mScreeFloatView.isShown()) {
            Logger.d(TAG, "!--->hideFloatView---");
            mScreeFloatView.hide();
        }
    }

    private void updateEnableFloatMic() {
        Logger.d(
                TAG,
                "updateEnableFloatMic---isShowFloatMicView = "
                        + UserPerferenceUtil.getFloatMicEnable(this));
        setFloatMicEnable(UserPerferenceUtil.getFloatMicEnable(this));
    }

    private void setFloatMicEnable(boolean enable) {
        Logger.d(TAG, "setFloatMicEnable: enable " + enable);
        mEnableFloatMic = enable;
    }

    /* xiaodong 20150805 added for Mic float View End > */

    /* < xiaodong 20150813 added for cancel button click Begin */

    /**
     * onCancelTalk play TTS
     *
     * @author xiaodong.he
     * @modifyDate : 2016-2-1
     */
    public void onCancelTalk() {
        // mUIHandler.sendEmptyMessage(MSG_GUI_CANCEL_SESSION);
        // mUIHandler.removeMessages(MSG_ON_RECORDING_PREPARED);
        onCancelTalk(true, "");
    }

    /**
     * @param isPlayTTS
     * @param reason
     * @author xiaodong.he
     * @date 2016-2-1
     */
    public void onCancelTalk(boolean isPlayTTS, String reason) {
        String param = "";
        if (isPlayTTS) {
            param = GuiProtocolUtil.getCTTProtocol(SessionPreference.PARAM_TYPE_PLAY_TTS, "");
        } else {
            param =
                    GuiProtocolUtil.getCTTProtocol(SessionPreference.PARAM_TYPE_NOT_PLAY_TTS,
                            reason);
        }
        if (isViewRootShow()) {
            Message cancelMsg = new Message();
            cancelMsg.what = MSG_GUI_CANCEL_SESSION;
            cancelMsg.obj = param;
            mUIHandler.sendMessage(cancelMsg);
        }
        mUIHandler.removeMessages(MSG_ON_RECORDING_PREPARED);
    }

    /**
     * XD 20150813 added
     */
    public void onWaittingSessionCancel() {
        mUIHandler.sendEmptyMessage(MSG_GUI_CANCEL_WAITTING_SESSION);
    }

    /* xiaodong 20150813 added for for cancel button click End > */


    /**
     * Add default Location 20150909 从assets/config.mg中读取位置信息，若没有配置，缺省使用上海公司位置信息
     * 应jason要求,一定要加一个缺省的位置信息，规避第一次定位就失败的情况。
     */
    private void setDefaultLocation() {
        JSONObject mLocationJsonObject =
                JsonTool.parseToJSONObject(PrivatePreference.mDeufaultLocation);
        mLocationInfo = new LocationInfo();
        mLocationInfo.setCityCode(JsonTool.getJsonValue(mLocationJsonObject, "cityCode", "021"));
        mLocationInfo.setLatitude(JsonTool.getJsonValue(mLocationJsonObject, "lat", 31.177598));
        mLocationInfo.setLongitude(JsonTool.getJsonValue(mLocationJsonObject, "lng", 121.401098));
        mLocationInfo.setType(JsonTool.getJsonValue(mLocationJsonObject, "type", 5));
        mLocationInfo.setAddress(JsonTool.getJsonValue(mLocationJsonObject, "address",
                "上海市 徐汇区 钦州北路 靠近电信恒联"));
        mLocationInfo.setDistrict(JsonTool.getJsonValue(mLocationJsonObject, "destrict", "徐汇区"));
        mLocationInfo.setCity(JsonTool.getJsonValue(mLocationJsonObject, "city", "上海"));
    }

    public void playTTs(String ttsContent, int type) {
        switch (type) {
            case RomCustomerProcessing.TTS_END_WAKEUP_START:
                sendProtocolEvent(SessionPreference.EVENT_NAME_PLAY_TTS,
                        GuiProtocolUtil.getPlayTTSEventParam(ttsContent, "WAKEUP"));
                break;
            case RomCustomerProcessing.TTS_END_RECOGNIER_START:
                sendProtocolEvent(SessionPreference.EVENT_NAME_PLAY_TTS,
                        GuiProtocolUtil.getPlayTTSEventParam(ttsContent, "RECOGNIZER"));
                break;
            default:
                break;
        }
    }

    /* < xiaodong.he 20151015 added for location change Begin */

    private MicrophoneViewClickListener mMicrophoneViewClickListener =
            new MicrophoneViewClickListener() {

                @Override
                public void onWakeupWordClick() {
                    Logger.d(TAG, "onWakeupWordClick----");
                    // showChangeTextPopWindow(POP_TYPE_EDIT_WAKEUP_WORD);
                    showEditWakeupwordPopWindow(mContext);
                }

                @Override
                public void onChangeVersionModeClick() {
                    Logger.d(TAG, "onChangeVersionModeClick----");
                    mSessionManager.releaseCurrentSessionOnly();
                }
            };

    private EditWakeupWordPopWindow pop;

    /**
     * show Edit Wakeupword PopWindow
     *
     * @param context
     * @author xiaodong.he
     * @date 2015-11-01
     */
    private void showEditWakeupwordPopWindow(Context context) {
        Logger.d(TAG, "showEditWakeupwordPopWindow----");
        sendProtocolEvent(SessionPreference.EVENT_NAME_UPDATE_WAKEUP_WORD_TIMEOUT_SWITCH,
                GuiProtocolUtil.getTimeOutParamProtocol(SessionPreference.EVENT_PARAM_SWITCH_CLOSE));
        pop = new EditWakeupWordPopWindow(context);
        pop.setOnEditWakeupwordPopListener(mOnEditWakeupwordPopListener);
        pop.showPopWindow(mSessionContainer);
    }

    /**
     * mOnEditWakeupwordPopListener
     *
     * @author xiaodong.he
     * @date 2015-11-01
     */
    private OnEditWakeupwordPopListener mOnEditWakeupwordPopListener =
            new OnEditWakeupwordPopListener() {
                @Override
                public void onOkClick(String wakeupWord) {
                    Logger.d(TAG, "mOnEditWakeupwordPopListener-onOkClick-wakeupWord = "
                            + wakeupWord);
                    String oldWakeupWord = UserPerferenceUtil.getWakeupWord(mContext);
                    sendUpdateWakeupWordEvent(oldWakeupWord, wakeupWord);
                }

                @Override
                public void onCancelClick() {
                    Logger.d(TAG, "mOnEditWakeupwordPopListener - onCancelClick");
                    sendProtocolEvent(
                            SessionPreference.EVENT_NAME_UPDATE_WAKEUP_WORD_TIMEOUT_SWITCH,
                            GuiProtocolUtil
                                    .getTimeOutParamProtocol(SessionPreference.EVENT_PARAM_SWITCH_OPEN));
                }
            };

    /**
     * show Edit Location PopWindow
     *
     * @param context
     * @param toPoi
     * @author xiaodong.he
     * @date 2015-11-18
     */
    public void showEditLocationPopWindow(Context context, String toPoi) {
        Logger.d(TAG, "showEditLocationPopWindow----toPoi = " + toPoi);
        sendProtocolEvent(SessionPreference.EVENT_NAME_UPDATE_POI_TIMEOUT_SWITCH,
                GuiProtocolUtil.getTimeOutParamProtocol(SessionPreference.EVENT_PARAM_SWITCH_CLOSE));
        EditLocationPopWindow pop = new EditLocationPopWindow(context);
        pop.setDefaultLocation(toPoi);// XD added 2015-12-14
        pop.setOnEditWakeupwordPopListener(mOnEditLocationPopListener);
        pop.showPopWindow(mSessionContainer);
    }

    /**
     * mOnEditLocationPopListener
     *
     * @author xiaodong.he
     * @date 2015-11-18
     */
    private OnEditLocationPopListener mOnEditLocationPopListener = new OnEditLocationPopListener() {
        @Override
        public void onOkClick(String location) {
            Logger.d(TAG, "mOnEditLocationPopListener-onOkClick-location = " + location);
            if (!TextUtils.isEmpty(location)) {
                sendkeywordEvent(SessionPreference.EVENT_NAME_UPDATE_POI_KEYWORD, location);
            }
        }

        @Override
        public void onCancelClick() {
            Logger.d(TAG, "mOnEditLocationPopListener-onCancelClick");
            sendProtocolEvent(SessionPreference.EVENT_NAME_UPDATE_POI_TIMEOUT_SWITCH,
                    SessionPreference.EVENT_PARAM_SWITCH_OPEN);
        }
    };


    //初始化声音改变是的监听器
    private void initAudioFoucus() {
        mAudioFocusHelper = new AudioFocusHelper(mContext);
    }


    public void requestAudioFocus() {
        Logger.d(TAG, "requestAudioFocus");
        if (mAudioFocusHelper != null) {
            mAudioFocusHelper.requestAudioFocus();
        }
    }

    public void abandonAudioFocus() {
        Logger.d(TAG, "abandonAudioFocus");
        if (mAudioFocusHelper != null) {
            mAudioFocusHelper.abandonAudioFocus();
        }
    }

    @SuppressWarnings("deprecation")
    private void initServiceForeground() {
        try {
            mStartForeground =
                    WindowService.class.getMethod("startForeground", mStartForegroundSignature);
            mStopForeground =
                    WindowService.class.getMethod("stopForeground", mStopForegroundSignature);
        } catch (NoSuchMethodException e) {
            mStartForeground = mStopForeground = null;
        }

        try {
            mSetForeground = getClass().getMethod("setForeground", mSetForegroundSignature);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(
                    "OS doesn't have Service.startForeground OR Service.setForeground!");
        }

        Notification.Builder builder = new Notification.Builder(mContext);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle("uniDrive");
        builder.setContentText("uniDrive Service");
        /* < XD modify 20151218 Begin */
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
        } else {
            notification = builder.getNotification();
            ;
        }
        /* XD modify 20151218 End > */
        PendingIntent pendingintent =
                PendingIntent.getActivity(this, 0, new Intent(this, GUIMainActivity.class), 0);
        notification.setLatestEventInfo(this, "uniCarSolution", "uniDrive Service", pendingintent);

        startForegroundCompat(NOTIFICATION_ID, notification);
    }


    void invokeMethod(Method method, Object[] args) {
        try {
            method.invoke(this, args);
        } catch (InvocationTargetException e) {
            // Should not happen.
            Logger.e(TAG, "Unable to invoke method : " + e);
        } catch (IllegalAccessException e) {
            // Should not happen.
            Logger.e(TAG, "Unable to invoke method : " + e);
        }
    }

    /**
     * startForeground
     */
    private void startForegroundCompat(int id, Notification notification) {
        if (mReflectFlg) {
            if (mStartForeground != null) {
                mStartForegroundArgs[0] = Integer.valueOf(id);
                mStartForegroundArgs[1] = notification;
                invokeMethod(mStartForeground, mStartForegroundArgs);
                return;
            }

            mSetForegroundArgs[0] = Boolean.TRUE;
            invokeMethod(mSetForeground, mSetForegroundArgs);
            mNM.notify(id, notification);
        } else {
            if (VERSION.SDK_INT >= 5) {
                startForeground(id, notification);
            } else {
                mSetForegroundArgs[0] = Boolean.TRUE;
                invokeMethod(mSetForeground, mSetForegroundArgs);
                mNM.notify(id, notification);
            }
        }
    }

    /**
     * stopForeground
     */
    private void stopForegroundCompat(int id) {
        if (mReflectFlg) {
            if (mStopForeground != null) {
                mStopForegroundArgs[0] = Boolean.TRUE;
                invokeMethod(mStopForeground, mStopForegroundArgs);
                return;
            }
            mNM.cancel(id);
            mSetForegroundArgs[0] = Boolean.FALSE;
            invokeMethod(mSetForeground, mSetForegroundArgs);
        } else {
            if (VERSION.SDK_INT >= 5) {
                stopForeground(true);
            } else {
                mNM.cancel(id);
                mSetForegroundArgs[0] = Boolean.FALSE;
                invokeMethod(mSetForeground, mSetForegroundArgs);
            }
        }
    }


    //转换拼音
    private void initPinyinConverter() {
        // 由于采用自定义的获取联系人方式，并没有初始化拼音模块，所以在服务创建时初始化好
        // 规避在收到同步完联系人广播后再去初始化，避免耗时导致生成cop文件中全拼字段异常
        try {
            Logger.d(TAG, "initPinyinConverter");
            PinyinConverter.init(mContext.getAssets().open("un2py.mg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* < XD 20151201 added for no SDCard space left Begin */

    /**
     * dismiss WelcomeActivity
     *
     * @author xiaodong.he
     * @date 2015-12-1
     */
    private void dismissWelcomeActivity() {
        String topActivity = PackageUtil.getTopActivityName(this);
        Logger.d(TAG, "dismissWelcomeActivity----topActivity = " + topActivity);
        if (WelcomeActivity.class.getName().equals(topActivity)) {
            sendDismissWelcomeActivityBroadcast();
        } else {
            Logger.w(TAG,
                    "dismissWelcomeActivity---WelcomeActivity is not show, dismiss it 3s delay.");
            mUIHandler.sendEmptyMessageDelayed(MSG_DISMISS_WELCOME_ACTIVITY, 3000);
        }
    }

    /**
     *
     */
    public void sendDismissWelcomeActivityBroadcast() {
        Logger.d(TAG, "sendDismissWelcomeActivityBroadcast----");
        this.sendBroadcast(new Intent(WelcomeActivity.ACTION_FINISH_WELCOMEACTIVITY));
    }

    /**
     * dismiss GUIMainActivity
     *
     * @author xiaodong.he
     * @date 2015-12-1
     */
    private void dismissGUIMainActivity() {
        Logger.d(TAG, "!--->disGUIMainActivity()----------");
        mContext.sendBroadcast(new Intent(GUIMainActivity.ACTION_FINISH_GUIMAINACTIVITY));
    }

    /**
     * @author xiaodong.he
     * @date 2016-2-16
     */
    private void showDueDateView() {
        Logger.d(TAG, "showDueDateView---");
        showInitNoSpaceErrorView(getString(R.string.error_title_due_date),
                getString(R.string.error_msg_due_date));
    }

    /**
     * show init No SDCard Space View
     */
    private void showNoSDCardSpaceView() {
        showInitNoSpaceErrorView(
                getString(R.string.error_title_sdcard_space_insufficient),
                getString(R.string.error_msg_sdcard_space_insufficient,
                        GUIConfig.MIN_SIZE_SDCARD_AVAILABLE));
    }

    /**
     * show init No Internal Memory View
     */
    private void showNoInternalMemoryView() {
        showInitNoSpaceErrorView(
                mContext.getString(R.string.error_title_internal_memory_insufficient),
                mContext.getString(R.string.error_msg_internal_memory_insufficient,
                        GUIConfig.MIN_SIZE_INTERNAL_MEMORY_AVAILABLE));
    }

    /**
     * showCopyTTSModleFileErrorView
     */
    private void showCopyTTSModleFileErrorView() {
        Logger.d(TAG, "showCopyTTSModleFileErrorView");
        showInitNoSpaceErrorView(mContext.getString(R.string.error_title_write_tts_fail),
                mContext.getString(R.string.error_msg_write_tts_fail));
    }

    /**
     * show init No Space Error View
     *
     * @author xiaodong.he
     * @date 2015-12-1
     */
    private void showInitNoSpaceErrorView(String title, String content) {
        Logger.d(TAG, "showInitNoSpaceErrorView--title = " + title + "; content:" + content);
        mSessionContainer.removeAllSessionViews();
        mMicrophoneControl.setVisibility(View.GONE);

        final View view = View.inflate(mContext, R.layout.pop_setting_help, null);
        if (DeviceTool.getDeviceSDKVersion() > 13) {
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        TextView btnQuit = (TextView) view.findViewById(R.id.tv_i_know);
        btnQuit.setText(R.string.quit);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_pop_title);
        tvTitle.setText(title);

        TextView tvContent = (TextView) view.findViewById(R.id.tv_pop_content);
        tvContent.setText(content);

        btnQuit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d(TAG, "showInitNoSpaceErrorView--onQuit--click");
                if (null != mToast) {
                    mToast.cancel();
                    mToast = null;
                }
                dimissView(view);
                dismissGUIMainActivity();
                dismissWelcomeActivity();
                stopWindowService();
                // AppManager.getAppManager().finishAllActivity();
                System.exit(0);
            }
        });
        addPrepareView(view);
    }

    /* XD 20151201 added for no SDCard space left End > */

    private void sysLogcatConfig() {
        if (UserPerferenceUtil.getLogcatEnable(mContext)) {
            sendProtocolEvent(SessionPreference.EVENT_NAME_SWITCH_LOGCAT,
                    SessionPreference.EVENT_PROTOCAL_SWITCH_LOGCAT_OPEN);
            if (!CrashApplication.mLogcatHelper.isRunning()) {
                CrashApplication.mLogcatHelper.start();
            }
        } else {
            sendProtocolEvent(SessionPreference.EVENT_NAME_SWITCH_LOGCAT,
                    SessionPreference.EVENT_PROTOCAL_SWITCH_LOGCAT_CLOSE);
            if (CrashApplication.mLogcatHelper.isRunning()) {
                CrashApplication.mLogcatHelper.stop();
            }
        }
    }

    // 关闭popWindow
    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (pop != null) {
                pop.dismiss();
            }

        }
    };

    private void sendBroadcastForWechat() {
        Logger.d(TAG,
                "sendBroadcastForWechat action = com.unisound.unicar.gui.intent.action.COMPILE_FINISH");
        Intent intent = new Intent("com.unisound.unicar.gui.intent.action.COMPILE_FINISH");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(intent);
    }

    private static final String START_WECHAT_ACTIVITY =
            "com.unisound.unicar.gui.ui.START_WECHAT_ACTIVITY";
    private static final String WECHAT_PACKAGE_NAME = "com.unisound.unicar.wechat";
    private static final String WECHAT_CLASS_NAME = "com.unisound.unicar.wechat.ui.WelcomeActivity";

    public void startWechatActivity() {
        Logger.d(TAG, "startWechatActivity");
        boolean isWechatExist = DeviceTool.checkApkExist(mContext, WECHAT_PACKAGE_NAME);
        if (!Util.isUniCarGUIStarted(mContext, WECHAT_PACKAGE_NAME) && isWechatExist) {
            playTTs(getResources().getString(R.string.tts_wechat_no_running),
                    RomCustomerProcessing.TTS_END_WAKEUP_START);
            startModle();
        } else if (!isWechatExist) {
            playTTs(getResources().getString(R.string.tts_wechat_no_install),
                    RomCustomerProcessing.TTS_END_WAKEUP_START);
        } else if (isWechatExist) {
            playTTs(getResources().getString(R.string.tts_wechat_no_running),
                    RomCustomerProcessing.TTS_END_WAKEUP_START);
            startModle();
        }

    }

    private void startModle() {
        sendEvent(SessionPreference.EVENT_NAME_WECHAT_NOT_LOGIN);
        Logger.d(TAG, "startWechatActivity  start");
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ComponentName cn = new ComponentName(WECHAT_PACKAGE_NAME, WECHAT_CLASS_NAME);
        intent.setComponent(cn);
        startActivity(intent);
    }

    private int phoneStats = TelephonyManager.CALL_STATE_IDLE;

    public void setPhoneStatus(int type) {
        phoneStats = type;
    }
}
