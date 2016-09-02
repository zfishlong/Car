/**
 * Copyright (c) 2012-2016 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : PoiChatShowSession.java
 * @ProjectName : uniCarSolution
 * @PakageName : com.unisound.unicar.gui.session
 * @version : 1.0
 * @Author : Xiaodong.He
 * @CreateDate : 2016-3-21
 */
package com.unisound.unicar.gui.session;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.unisound.unicar.gui.R;
import com.unisound.unicar.gui.chat.ChatObject;
import com.unisound.unicar.gui.model.LocationInfo;
import com.unisound.unicar.gui.preference.SessionPreference;
import com.unisound.unicar.gui.preference.UserPerferenceUtil;
import com.unisound.unicar.gui.ui.WindowService;
import com.unisound.unicar.gui.utils.JsonTool;
import com.unisound.unicar.gui.utils.Logger;
import com.unisound.unicar.gui.utils.TTSUtil;
import com.unisound.unicar.gui.view.ChatView;

/**
 * 
 * @author Xiaodong.He
 * @date 2016-3-21
 */
public class PoiChatShowSession extends BaseSession {

    private static final String TAG = "PCSS";

    private Context mContext;
    private ChatView mChatView = null;
    private static PoiChatShowSession mPoiChatSession;

    /** all ChatObject List */
    private List<ChatObject> mAllChatObjList = new ArrayList<ChatObject>();

    /** new ChatObject List */
    // private List<ChatObject> mNewChatObjList = new ArrayList<ChatObject>();

    public static PoiChatShowSession getInstance(Context context, Handler sessionManagerHandler) {
        if (mPoiChatSession == null) {
            mPoiChatSession = new PoiChatShowSession(context, sessionManagerHandler);
        }
        return mPoiChatSession;
    }

    private PoiChatShowSession(Context context, Handler sessionManagerHandler) {
        super(context, sessionManagerHandler);
        mContext = context;
        mSessionManagerHandler = sessionManagerHandler;
        // getExistChatData();
    }

    @Override
    public void putProtocol(JSONObject jsonProtocol) {
        super.putProtocol(jsonProtocol);
        Logger.d(TAG, "ChatUiSession putProtocol jsonProtocol=" + jsonProtocol);
        updateChatObjectList(jsonProtocol);

        if (mChatView == null) {
            mChatView = new ChatView(mContext, mAllChatObjList);
        } else {
            mChatView.notifyDataChanged();
        }

        addAnswerView(mChatView);
    }

    // private void getExistChatData() {
    // mAllChatObjList.clear();
    // mNewChatObjList.clear();
    // mAllChatObjList = ChatDataBaseUtil.getExistChatDataFromDB(mContext);
    // }

    /**
     * update ChatObject List from protocol
     * 
     * @param objc
     */
    private void updateChatObjectList(JSONObject objc) {
        JSONObject obj = JsonTool.getJSONObject(objc, SessionPreference.KEY_DATA);
        String text = JsonTool.getJsonValue(obj, SessionPreference.KEY_TEXT);

        String tts = "";
        String answer = "";
        LocationInfo locationInfo = WindowService.getLocationInfo();
        if (null != locationInfo) {
            answer = locationInfo.getAddress();
            tts = mContext.getString(R.string.tts_current_poi, answer);
        } else {
            Logger.w(TAG, "LocationInfo is null");
            tts = mContext.getString(R.string.tts_get_poi_failed);
        }

        TTSUtil.playTTSWakeUp(mContext, tts);

        Logger.d(TAG, "!--->updateChatObjectList----text =" + text + "  answer =" + answer);
        if (!TextUtils.isEmpty(text) && !TextUtils.isEmpty(answer)) {
            // mNewChatObjList.add(new ChatObject(text, answer));
            mAllChatObjList.add(new ChatObject(text, answer));
        }

    }

    @Override
    public void release() {
        Logger.d(TAG, "!--->release-----");
        if (null != mAllChatObjList) {
            mAllChatObjList.clear();
        }
        // mNewChatObjList.clear();
        super.release();
        mPoiChatSession = null;
    }

    @Override
    public void onTTSEnd() {
        // TODO Auto-generated method stub
        super.onTTSEnd();
        if (UserPerferenceUtil.VALUE_VERSION_MODE_EXP == UserPerferenceUtil
                .getVersionMode(mContext)) {
            mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_SESSION_RELEASE_ONLY);
        } else {
            mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_SESSION_DONE);
        }
    }

}
