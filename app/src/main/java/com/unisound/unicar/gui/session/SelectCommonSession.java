/**
 * Copyright (c) 2012-2014 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * 
 * @FileName : MutipleLocationSession.java
 * @ProjectName : vui_car_assistant
 * @PakageName : cn.yunzhisheng.vui.assistant.session
 * @Author : Brant
 * @CreateDate : 2014-10-29
 */
package com.unisound.unicar.gui.session;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.unisound.unicar.gui.preference.SessionPreference;
import com.unisound.unicar.gui.utils.Logger;
import com.unisound.unicar.gui.view.PickBaseView.IPickListener;

/**
 * @Module : 隶属模块名
 * @Comments : 描述
 * @Author : Brant
 * @CreateDate : 2014-10-29
 * @ModifiedBy : xiaodong
 * @ModifiedDate: 2015-7-2
 * @Modified: 2014-10-29: 实现基本功能
 */
public class SelectCommonSession extends CommBaseSession {
    public static final String TAG = "SelectCommonSession";
    protected ArrayList<String> mDataItemProtocalList = new ArrayList<String>();
    private boolean canBeClick = true;
    protected IPickListener mPickViewListener = new IPickListener() {
        @Override
        public void onItemPicked(int position) {
            Logger.d(TAG, "!--->onItemPicked()----position = " + position);
            if (canBeClick) {
                if (mDataItemProtocalList != null && mDataItemProtocalList.size() > 0) {
                    /* < BUG-2792 XD 20150722 modify Begin */
                    Logger.d(TAG, "!--->onItemPicked()----position = " + position
                            + "; mDataItemProtocalList" + mDataItemProtocalList.size());
                    if (mDataItemProtocalList.size() > position) {
                        String selectedItem = mDataItemProtocalList.get(position);
                        Logger.d(TAG, "!--->onItemPicked()----selectedItem = " + selectedItem);
                        onUiProtocal(SessionPreference.EVENT_NAME_SELECT_ITEM, selectedItem);
                    } else {
                        Logger.e(TAG, "!--->Error: position >= mDataItemProtocalList size");
                    }
                    /* BUG-2792 XD 20150722 modify End > */
                }
                canBeClick = false;
            }
        }

        @Override
        public void onPickCancel() {
            // onUiProtocal("");
            Logger.d(TAG, "onItemPicked");
        }

        @Override
        public void onNext() {
            Logger.d(TAG, "onNext");
            onUiProtocal(SessionPreference.EVENT_NAME_CLICK_NEXT);
        }

        @Override
        public void onPre() {
            Logger.d(TAG, "onPre");
            onUiProtocal(SessionPreference.EVENT_NAME_CLICK_PRE);
        }
    };

    public SelectCommonSession(Context context, Handler sessionManagerHandler) {
        super(context, sessionManagerHandler);
    }

    @Override
    public void release() {
        super.release();
        Logger.d(TAG, "!--->SelectCommonSession--release----");
        mDataItemProtocalList.clear();
        mDataItemProtocalList = null;
        mPickViewListener = null;
        canBeClick = true;
    }

    private void onUiProtocal(String eventName) {
        Logger.d(TAG, "onUiProtocal eventName : " + eventName);
        Message msg = new Message();
        msg.what = SessionPreference.MESSAGE_UI_OPERATE_PROTOCAL;

        Bundle bundle = new Bundle();
        bundle.putString(SessionPreference.KEY_BUNDLE_EVENT_NAME, eventName);
        msg.obj = bundle;

        mSessionManagerHandler.sendMessage(msg);
    }

}
