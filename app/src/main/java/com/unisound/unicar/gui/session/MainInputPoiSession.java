/**
 * Copyright (c) 2012-2016 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : MainInputPoiSession.java
 * @ProjectName : uniCarSolution
 * @PakageName : com.unisound.unicar.gui.session
 * @version : 1.0
 * @Author : Xiaodong.He
 * @CreateDate : 2016-01-28
 */
package com.unisound.unicar.gui.session;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;

import com.unisound.unicar.gui.preference.SessionPreference;
import com.unisound.unicar.gui.preference.UserPerferenceUtil;
import com.unisound.unicar.gui.ui.AddressFavoriteActivity;
import com.unisound.unicar.gui.utils.GuiProtocolUtil;
import com.unisound.unicar.gui.utils.GuiSettingUpdateUtil;
import com.unisound.unicar.gui.utils.Logger;
import com.unisound.unicar.gui.view.InputPoiPopWindow;
import com.unisound.unicar.gui.view.InputPoiPopWindow.OnInputPoiListener;
import com.unisound.unicar.gui.view.MainInputPoiView;
import com.unisound.unicar.gui.view.MainInputPoiView.IMainInputPoiViewListener;

/**
 * MainInputPoiSession
 * 
 * @author xiaodong.he
 * @date 2016-01-28
 */
public class MainInputPoiSession extends BaseSession {

    public static final String TAG = MainInputPoiSession.class.getSimpleName();

    private Context mContext;

    private boolean isRecordingControlStoped = false;

    private MainInputPoiView mMainInputPoiView;

    private InputPoiPopWindow mInputPoiPop;

    /**
     * @param context
     * @param sessionManagerHandler
     */
    public MainInputPoiSession(Context context, Handler sessionManagerHandler) {
        super(context, sessionManagerHandler);
        mContext = context;
    }

    @Override
    public void putProtocol(JSONObject jsonProtocol) {
        super.putProtocol(jsonProtocol);

        int mapType = UserPerferenceUtil.getMapChoose(mContext);
        Logger.d(TAG, "mapType = " + mapType);

        if (mMainInputPoiView == null) {
            mMainInputPoiView = new MainInputPoiView(mContext);
        }
        mMainInputPoiView.setMainInputPoiViewListener(mMainNaviViewListener);
        addAnswerView(mMainInputPoiView, true, Gravity.TOP);
    }

    private IMainInputPoiViewListener mMainNaviViewListener = new IMainInputPoiViewListener() {

        @Override
        public void onSearchInputClick() {
            Logger.d(TAG, "onSearchInputClick---isRecordingControlStoped = "
                    + isRecordingControlStoped);
            if (!isRecordingControlStoped) {
                sendRecordingControlEvent(false);
            }
            updateMicEnableStatus(false);

            showInputPoiPopWindow(mContext);
        }

        @Override
        public void onGoHomeClick() {
            Logger.d(TAG, "onGoHomeClick---");
            String homeLocation = UserPerferenceUtil.getHomeLocation(mContext);
            Logger.d(TAG, "homeLocation = " + homeLocation);
            if (TextUtils.isEmpty(homeLocation)) {
                showAddAddressActivity(GuiSettingUpdateUtil.VALUE_ADD_ADDRESS_TYPE_HOME);
            } else {
                onUiProtocal(SessionPreference.EVENT_NAME_GO_FAVORITE_ADDRESS,
                        GuiProtocolUtil.getGoFavoriteAddressProtocol(homeLocation,
                                SessionPreference.PARAM_ADDRESS_TYPE_HOME));
            }
        }

        @Override
        public void onGoCompanyClick() {
            Logger.d(TAG, "onGoCompanyClick---");
            String companyLocation = UserPerferenceUtil.getCompanyLocation(mContext);
            if (TextUtils.isEmpty(companyLocation)) {
                showAddAddressActivity(GuiSettingUpdateUtil.VALUE_ADD_ADDRESS_TYPE_COMPANY);
            } else {
                onUiProtocal(SessionPreference.EVENT_NAME_GO_FAVORITE_ADDRESS,
                        GuiProtocolUtil.getGoFavoriteAddressProtocol(companyLocation,
                                SessionPreference.PARAM_ADDRESS_TYPE_COMPANY));
            }
        }
    };

    /**
     * 
     * @param addType
     */
    private void showAddAddressActivity(int addType) {
        cancelSession(SessionPreference.PARAM_TYPE_NOT_PLAY_TTS,
                SessionPreference.PARAM_REASON_ADD_ADDRESS);
        if (mContext == null) {
            return;
        }
        Intent intent = new Intent(mContext, AddressFavoriteActivity.class);
        intent.putExtra(GuiSettingUpdateUtil.KEY_ADD_ADDRESS_TYPE, addType);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }


    /**
     * 
     * @param isStart
     */
    private void sendRecordingControlEvent(boolean isStart) {
        Logger.d(TAG, "sendRecordingControlEvent---isStart = " + isStart);
        sendRecordingControlEvent(isStart, SessionPreference.DOMAIN_ROUTE);
        isRecordingControlStoped = !isStart;
    }

    // -------------------------------------------

    /**
     * 
     * @author xiaodong.he
     * @date 2016-1-28
     * @param context
     */
    public void showInputPoiPopWindow(Context context) {
        mInputPoiPop = new InputPoiPopWindow(context);
        mInputPoiPop.setOnInputPoiListener(mOnInputPoiListener);
        mInputPoiPop.showPopWindow(mMainInputPoiView);
    }

    private OnInputPoiListener mOnInputPoiListener = new OnInputPoiListener() {

        @Override
        public void onOkClick(String location) {
            Logger.d(TAG, "onOkClick---location:" + location);
            isRecordingControlStoped = false;
            onUiProtocal(SessionPreference.EVENT_NAME_UPDATE_POI_KEYWORD,
                    GuiProtocolUtil.getChangeLocationParamProtocol(location));
            updateMicEnableStatus(true);

            if (null != mInputPoiPop) {
                mInputPoiPop.dismiss();
            }
            mMainInputPoiView.setToPoi(location);
        }

        @Override
        public void onCancelClick() {
            Logger.d(TAG, "onCancelClick-----");
            sendRecordingControlEvent(true);
            updateMicEnableStatus(true);
        }
    };



    @Override
    public void onTTSEnd() {
        super.onTTSEnd();
        Logger.d(TAG, "onTTSEnd---");

    }

    @Override
    public void release() {
        super.release();
        Logger.d(TAG, "release---");
        if (null != mMainInputPoiView) {
            mMainInputPoiView.release();
        }
    }

}
