package com.unisound.unicar.gui.session;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.unisound.unicar.gui.preference.SessionPreference;

/**
 * @author 作者 :tyz
 * @date 创建时间：2016-2-25 上午10:08:24
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class WechatSession extends BaseSession {
    private static final String TAG = WechatSession.class.getSimpleName();

    public WechatSession(Context context, Handler sessionManagerHandler) {
        super(context, sessionManagerHandler);
    }

    @Override
    public void putProtocol(JSONObject jsonProtocol) {
        // TODO Auto-generated method stub
        super.putProtocol(jsonProtocol);
        Log.d(TAG, "putProtocol = " + jsonProtocol);
        mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_SESSION_DONE);
    }

    @Override
    public void onTTSEnd() {
        super.onTTSEnd();
        Log.d(TAG, "onTTSEnd");
        mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_SESSION_DONE);
    }
}
