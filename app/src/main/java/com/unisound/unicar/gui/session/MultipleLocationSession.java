/**
 * Copyright (c) 2012-2014 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * 
 * @FileName : MultipleLocationSession.java
 * @ProjectName : uniCarSolution
 * @PakageName : com.unisound.unicar.gui.session
 * @Author : Alieen
 * @CreateDate : 2015-07-08
 */
package com.unisound.unicar.gui.session;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.unisound.unicar.gui.R;
import com.unisound.unicar.gui.model.LocationInfo;
import com.unisound.unicar.gui.preference.SessionPreference;
import com.unisound.unicar.gui.utils.GuiProtocolUtil;
import com.unisound.unicar.gui.utils.JsonTool;
import com.unisound.unicar.gui.utils.Logger;
import com.unisound.unicar.gui.view.PickLocationView;
import com.unisound.unicar.gui.view.PickLocationView.PickLocationViewListener;

/**
 * @Module : 隶属模块名
 * @Comments : 导航到“XXX”
 * @Author : Alieen
 * @CreateDate : 2015-07-08
 * @ModifiedBy : Alieen
 * @ModifiedDate: 2015-07-08
 * @Modified: 2015-07-08: 实现基本功能
 */
public class MultipleLocationSession extends SelectCommonSession {

    public static final String TAG = "MultipleLocationSession";

    // private Handler mSessionManagerHandler;

    private PickLocationView mPickLocationView;

    private String locationToPoi;

    private boolean isClickMore = false;

    private int mCurrentPage = 0;
    private int mTotalPage = 0;

    private PickLocationViewListener mPickLocationViewListener = new PickLocationViewListener() {

        @Override
        public void onEditLocationClick() {
            Logger.d(TAG, "onEditLocationClick-----locationToPoi = " + locationToPoi);
            if (null != mSessionManagerHandler) {
                Message msg = new Message();
                msg.what = SessionPreference.MESSAGE_SHOW_EDIT_LOCATION_POP;
                msg.obj = locationToPoi;
                mSessionManagerHandler.sendMessage(msg);
            }
        }

        @Override
        public void onLoadmoreClick() {
            Logger.d(TAG, "onLoadmoreClick--");
            onUiProtocal(SessionPreference.EVENT_NAME_ONCLICK_LOAD_MORE,
                    GuiProtocolUtil.getLoadMoreParamProtocol(SessionPreference.DOMAIN_ROUTE));
        }
    };

    private ArrayList<LocationInfo> mLocationInfos = new ArrayList<LocationInfo>();
    private String ttsAnswer = "";

    public MultipleLocationSession(Context context, Handler sessionManagerHandler) {
        super(context, sessionManagerHandler);

    }

    public void putProtocol(JSONObject jsonProtocol) {
        super.putProtocol(jsonProtocol);
        Logger.d(TAG, "putProtocol--jsonProtocol = " + jsonProtocol.toString());
        Logger.d(TAG, "isClickMore =" + isClickMore);
        if (!isClickMore) {
            locationToPoi = JsonTool.getJsonValue(mDataObject, "locationToPoi");
            Logger.d(TAG, "putProtocol---locationToPoi = " + locationToPoi);
            mCurrentPage = JsonTool.getJsonValue(mDataObject, "current_page", -1);
            mTotalPage = JsonTool.getJsonValue(mDataObject, "total_page", -1);
            Logger.d(TAG, "mCurrentPage : " + mCurrentPage + " ; mTotalPage :" + mTotalPage);

            JSONArray dataArray =
                    JsonTool.getJsonArray(mDataObject, SessionPreference.KEY_LOCATION);
            if (dataArray != null) {
                mAnswer = mContext.getString(R.string.say_number_choose);
                addSessionAnswerText(mAnswer);
                mTtsText = ttsAnswer;
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject item = JsonTool.getJSONObject(dataArray, i);
                    LocationInfo info = new LocationInfo();
                    info.setName(JsonTool.getJsonValue(item, "name"));
                    info.setAddress(JsonTool.getJsonValue(item, "address"));
                    info.setType(JsonTool.getJsonValue(item, "type", -1));
                    info.setCity(JsonTool.getJsonValue(item, "city"));
                    info.setProvider(JsonTool.getJsonValue(item, "provider"));
                    info.setLatitude(JsonTool.getJsonValue(item, "lat", 0d));
                    info.setLongitude(JsonTool.getJsonValue(item, "lng", 0d));
                    mLocationInfos.add(info);
                    mDataItemProtocalList.add(JsonTool.getJsonValue(item,
                            SessionPreference.KEY_TO_SELECT));
                }
                Logger.d(TAG, "!--->mDataItemProtocalList size = " + mDataItemProtocalList.size());
                if (mPickLocationView == null) {
                    mPickLocationView = new PickLocationView(mContext);
                    mPickLocationView.initView(mLocationInfos, locationToPoi, mTotalPage,
                            mCurrentPage);
                    mPickLocationView.setPickListener(mPickViewListener);
                    mPickLocationView.setPickLocationViewListener(mPickLocationViewListener);
                }
                addSessionView(mPickLocationView, false);
            }
        }
    }

    public void loadMoreItem() {
        isClickMore = true;
        mPickLocationView.addMoreView();
    }

    @Override
    public void release() {
        super.release();
        Logger.d(TAG, "!--->release----");
        if (mPickLocationView != null) {
            mPickLocationView.setPickListener(null);
            mPickLocationView = null;
        }
        mLocationInfos.clear();
        mLocationInfos = null;
        isClickMore = false;
    }
}
