/**
 * Copyright (c) 2012-2016 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : UniCarNaviGuiService.java
 * @ProjectName : uniCarSolution
 * @PakageName : com.unisound.unicar.gui.uninavi
 * @version : 1.0
 * @Author : Xiaodong.He
 * @CreateDate : 2016-1-6
 */
package com.unisound.unicar.gui.uninavi;

import org.json.JSONObject;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.unisound.unicar.gui.R;
import com.unisound.unicar.gui.preference.SessionPreference;
import com.unisound.unicar.gui.utils.DeviceTool;
import com.unisound.unicar.gui.utils.GUIConfig;
import com.unisound.unicar.gui.utils.JsonTool;
import com.unisound.unicar.gui.utils.Logger;
import com.unisound.unicar.gui.utils.TTSUtil;
import com.unisound.unicar.navi.aidl.IUniCarNaviCallback;
import com.unisound.unicar.navi.aidl.IUniCarNaviService;

/**
 * UniCarNaviGuiService
 * 
 * @author xiaodong.he
 * @date 2016-1-6
 */
public class UniCarNaviGuiService extends Service {

    private static final String TAG = UniCarNaviGuiService.class.getSimpleName();

    private Context mContext;

    private IUniCarNaviService mUniCarNaviService;

    /** is Need ReDo Cmd before service connected success */
    private boolean isNeedReDoCmd = false;
    private Intent mReDoCmdIntent = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d(TAG, "!--->onCreate");
        mContext = getApplicationContext();

        bindUniCarNaviService();
        registReceiver();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Service#onBind(android.content.Intent)
     */
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Logger.d(TAG, "!--->onStartCommand: intent " + intent);
        boolean isUniCarNaviInstalled =
                DeviceTool.checkApkExist(mContext, GUIConfig.PACKAGE_NAME_UNICAR_NAVI);
        if (!isUniCarNaviInstalled) {
            Logger.w(TAG, "!--->onStartCommand---UniCarNavi.apk not Installed return;");
            return START_NOT_STICKY;
        }
        // doOnReceivedCmd(intent);
        return START_STICKY;
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mUniCarNaviService = IUniCarNaviService.Stub.asInterface(service);
            try {
                Logger.d(TAG, "!--->onServiceConnected---isNeedReDoCmd = " + isNeedReDoCmd);
                mUniCarNaviService.registerCallback(mCallback);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (isNeedReDoCmd && null != mReDoCmdIntent) {
                doReceivedCmd(mReDoCmdIntent);
                mReDoCmdIntent = null;
                isNeedReDoCmd = false;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Logger.w(TAG, "!--->onServiceDisconnected...");
            mUniCarNaviService = null;
            rebindUniCarNaviService();
        }
    };

    private IUniCarNaviCallback mCallback = new IUniCarNaviCallback.Stub() {

        @Override
        public void onCallBack(String callBackJson) throws RemoteException {
            Logger.d(TAG, "onCallBack---callBackJson:" + callBackJson);
            if (TextUtils.isEmpty(callBackJson)) {
                return;
            }
            doOnCallback(callBackJson);
        }
    };

    /**
     * 
     * @param callBackJson
     */
    private void doOnCallback(String callBackJson) {
        JSONObject json = JsonTool.parseToJSONObject(callBackJson);
        String callbackCmd =
                JsonTool.getJsonValue(json, UniCarNaviConstant.CallbackConstant.KEY_CALLBACK_CMD);
        Logger.d(TAG, "doOnCallback---callbackCmd = " + callbackCmd);
        JSONObject datObj =
                JsonTool.getJSONObject(json,
                        UniCarNaviConstant.CallbackConstant.KEY_CALLBACK_DATA_OBJ);
        if (UniCarNaviConstant.CallbackConstant.VALUE_CALLBACK_CMD_PLAY_TTS.equals(callbackCmd)) {
            // String ttsText = UniCarNaviUtil.getCallbackTTS(datObj);
            String ttsText =
                    JsonTool.getJsonValue(datObj,
                            UniCarNaviConstant.CallbackConstant.KEY_CALLBACK_TTS_TEXT);
            String ttsID =
                    JsonTool.getJsonValue(datObj,
                            UniCarNaviConstant.CallbackConstant.KEY_CALLBACK_TTS_ID);
            String ttsFrom =
                    JsonTool.getJsonValue(datObj,
                            UniCarNaviConstant.CallbackConstant.KEY_CALLBACK_TTS_FROM);
            Logger.d(TAG, "doOnCallback---ttsText = " + ttsText + "; ttsID = " + ttsID
                    + "; ttsFrom = " + ttsFrom);
            TTSUtil.playTTS(mContext, ttsText, ttsID, SessionPreference.PARAM_TTS_FROM_UNICARNAVI,
                    SessionPreference.PARAM_VALUE_TTS_END_WAKEUP);
        } else if (UniCarNaviConstant.CallbackConstant.VALUE_CALLBACK_CMD_ROUTE_STARTED
                .equals(callbackCmd)) {
            Logger.d(TAG, "doOnCallback---CALLBACK_MSG_ROUTE_STARTED");
            UniCarNaviUtil.setUniCarNaviStatus(UniCarNaviConstant.CallbackConstant.APP_STATE_MAP);
            UniCarNaviUtil.sendRouteStartedEvent(mContext);
        } else if (UniCarNaviConstant.CallbackConstant.VALUE_CALLBACK_CMD_ROUTE_QUIT
                .equals(callbackCmd)) {
            Logger.d(TAG, "doOnCallback---CALLBACK_CMD_ROUTE_QUIT");
            UniCarNaviUtil.setUniCarNaviStatus(UniCarNaviConstant.CallbackConstant.APP_STATE_IDEL);
            UniCarNaviUtil.sendRouteQuitEvent(mContext);
        } else if (UniCarNaviConstant.CallbackConstant.VALUE_CALLBACK_CMD_NAVI_STARTED
                .equals(callbackCmd)) {
            Logger.d(TAG, "doOnCallback---CALLBACK_MSG_NAVI_STARTED");
            UniCarNaviUtil.setUniCarNaviStatus(UniCarNaviConstant.CallbackConstant.APP_STATE_NAVI);
            UniCarNaviUtil.sendNaviStartedEvent(mContext);
        }
    }

    public void bindUniCarNaviService() {
        try {
            Logger.d(TAG, "!--->bindUniCarNaviService");
            Intent intent = new Intent(UniCarNaviConstant.ACTION_START_UNICARNAVI_SERVICE);
            intent.setPackage("com.unisound.unicar.navi");
            mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            mContext.startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void rebindUniCarNaviService() {
        if (mUniCarNaviService == null) {
            Logger.d(TAG, "rebindUniCarNaviService");
            bindUniCarNaviService();
        }
    }

    /**
     * 
     */
    private BroadcastReceiver mCmdReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            doReceivedCmd(intent);
        }
    };

    /**
     * 
     * @param context
     * @param intent
     */
    private void doReceivedCmd(Intent intent) {
        if (null == intent) {
            Logger.w(TAG, "doReceivedCmd--intent is null");
            return;
        }
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            for (String key : bundle.keySet()) {
                Logger.d(TAG, "doReceivedCmd--key : " + key + ",value : " + bundle.get(key));
            }
        }
        String action = intent.getAction();
        Logger.d(TAG, "doReceivedCmd--action : " + action);
        if (mUniCarNaviService == null) {
            Logger.w(TAG, "mReceiver--UniCarNaviService is null, begin rebind.");
            isNeedReDoCmd = true;
            mReDoCmdIntent = intent;
            bindUniCarNaviService();
            return;
        }
        if (UniCarNaviConstant.ACTION_START_UNICARNAVI.equals(action)) {
            String paramJson = intent.getStringExtra(UniCarNaviConstant.KEY_NAVI_PARAMS);
            Logger.d(TAG, "doReceivedCmd-START_UNICARNAVI-paramJson : " + paramJson);
            startNavi(paramJson);
        } else if (UniCarNaviConstant.ACTION_ON_TTS_PLAY_END.equals(action)) {
            String ttsID =
                    intent.getStringExtra(UniCarNaviConstant.CallbackConstant.KEY_CALLBACK_TTS_ID);
            Logger.d(TAG, "doReceivedCmd-ON_TTS_PLAY_END---ttsID:" + ttsID);
            onControlCommand(UniCarNaviUtil.getOnTTSPlayEndJson(ttsID));
        } else if (UniCarNaviConstant.ACTION_BEGIN_NAVIGAION.equals(action)) {
            onControlCommand(UniCarNaviUtil.getBeginNavigationJson());
        } else if (UniCarNaviConstant.ACTION_CANCEL.equals(action)) {
            onControlCommand(UniCarNaviUtil.getCancelJson());
            UniCarNaviUtil.setUniCarNaviStatus(UniCarNaviConstant.CallbackConstant.APP_STATE_IDEL);
        } else if (UniCarNaviConstant.ACTION_EXIT.equals(action)) {
            boolean isPlayTTS = intent.getBooleanExtra(UniCarNaviConstant.KEY_IS_PLAY_TTS, true);
            onControlCommand(UniCarNaviUtil.getExitJson());
            if (isPlayTTS) {
                TTSUtil.playTTSWakeUp(mContext, mContext.getString(R.string.tts_exit_navi));
            }
            UniCarNaviUtil.setUniCarNaviStatus(UniCarNaviConstant.CallbackConstant.APP_STATE_IDEL);
        } else if (UniCarNaviConstant.ACTION_SET_ROUTE_PLAN.equals(action)) {
            int routeplan =
                    intent.getIntExtra(UniCarNaviConstant.KEY_ROUTE_PLAN,
                            UniCarNaviConstant.VALUE_ROUTE_PLAN_ECAR_AVOID_TRAFFIC_JAM);
            onControlCommand(UniCarNaviUtil.getSetRoutePlanJson(routeplan));
        } else if (UniCarNaviConstant.ACTION_SET_DISPLAY_MODE.equals(action)) {
            int displayMode = intent.getIntExtra(UniCarNaviConstant.KEY_DISPLAY_MODE, 0);
            Logger.d(TAG, "doReceivedCmd-SET_DISPLAY_MODE-displayMode : " + displayMode);
            onControlCommand(UniCarNaviUtil.getSetDisplayModeJson(displayMode));
        } else if (UniCarNaviConstant.ACTION_CONTROL_UNICARNAVI.equals(action)) {
            if (UniCarNaviConstant.CallbackConstant.APP_STATE_NAVI == UniCarNaviUtil
                    .getUniCarNaviStatus()) {
                int operation = intent.getIntExtra(UniCarNaviConstant.DATA_KEY_OPERATION, 0);
                Logger.d(TAG, "doReceivedCmd-CONTROL_UNICARNAVI-operation : " + operation);
                onControlCommand(UniCarNaviUtil.getNaviControlJson(operation));
            } else {
                TTSUtil.playTTSWakeUp(mContext,
                        mContext.getResources().getString(R.string.tts_not_support_cmd));
            }

        } else if (UniCarNaviConstant.ACTION_CONTROL_ROAD_CONDITION_SHOW.equals(action)) {
            int operation = intent.getIntExtra(UniCarNaviConstant.DATA_KEY_OPERATION, 0);
            Logger.d(TAG, "doReceivedCmd-CONTROL_ROAD_CONDITION_SHOW-operation : " + operation);
            onControlCommand(UniCarNaviUtil.getRoadConditionControlJson(operation));
        } else if (UniCarNaviConstant.ACTION_SHOW_UNICAR_NAVI_UI.equals(action)) {
            Logger.d(TAG, "doReceivedCmd-SHOW_UNICAR_NAVI");
            onControlCommand(UniCarNaviUtil.getShowUniCarNaviJson());
        }
    }

    /**
     * 
     * @param paramJson
     */
    private void startNavi(String paramJson) {
        try {
            mUniCarNaviService.startNavi(paramJson);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param json
     */
    private void onControlCommand(String json) {
        Logger.d(TAG, "onControlCommand---json: " + json);
        try {
            mUniCarNaviService.onControlCommand(json);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void registReceiver() {
        IntentFilter commandFilter = new IntentFilter();
        commandFilter.addAction(UniCarNaviConstant.ACTION_ON_TTS_PLAY_END);
        commandFilter.addAction(UniCarNaviConstant.ACTION_START_UNICARNAVI);
        commandFilter.addAction(UniCarNaviConstant.ACTION_BEGIN_NAVIGAION);
        commandFilter.addAction(UniCarNaviConstant.ACTION_CANCEL);
        commandFilter.addAction(UniCarNaviConstant.ACTION_EXIT);
        commandFilter.addAction(UniCarNaviConstant.ACTION_SET_ROUTE_PLAN);
        commandFilter.addAction(UniCarNaviConstant.ACTION_SET_DISPLAY_MODE);
        commandFilter.addAction(UniCarNaviConstant.ACTION_CONTROL_UNICARNAVI);
        commandFilter.addAction(UniCarNaviConstant.ACTION_CONTROL_ROAD_CONDITION_SHOW);
        commandFilter.addAction(UniCarNaviConstant.ACTION_SHOW_UNICAR_NAVI_UI);
        mContext.registerReceiver(mCmdReceiver, commandFilter);
    }

    private void unregistReceiver() {
        try {
            mContext.unregisterReceiver(mCmdReceiver);
        } catch (Exception e) {
            Logger.printStackTrace(e);
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Logger.d(TAG, "!--->onUnbind");
        // TODO Auto-generated method stub
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Logger.d(TAG, "!--->onRebind");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d(TAG, "!--->onDestroy");
        unregistReceiver();
        mReDoCmdIntent = null;
    }

}
