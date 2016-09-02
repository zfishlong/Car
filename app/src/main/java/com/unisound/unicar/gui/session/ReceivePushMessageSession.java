package com.unisound.unicar.gui.session;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.gson.Gson;
import com.unisound.unicar.gui.preference.SessionPreference;
import com.unisound.unicar.gui.pushbean.PushModle;
import com.unisound.unicar.gui.pushbean.PushType;
import com.unisound.unicar.gui.pushserver.PushDb;
import com.unisound.unicar.gui.utils.JsonTool;
import com.unisound.unicar.gui.utils.Logger;
import com.unisound.unicar.gui.view.ReceivePushMessageView;
import com.unisound.unicar.gui.view.ReceivePushMessageView.IReceivePushMessageViewListener;

public class ReceivePushMessageSession extends ContactSelectBaseSession {

    public static final String TAG = ReceivePushMessageSession.class.getSimpleName();

    private final int delayDismiss = 10000;
    private PushModle pushModle;
    private ReceivePushMessageView mReceivePushMessageView;
    private String SCHEDULE_TYPE = SessionPreference.VALUE_SCHEDULE_TYPE_INCOMING_PUSH_MESSAGE;

    public ReceivePushMessageSession(Context context, Handler handle) {
        super(context, handle);
        Logger.d(TAG, "!--->----ReceiveSmsSession()------");
    }

    public void putProtocol(JSONObject jsonProtocol) {
        super.putProtocol(jsonProtocol);
        JSONObject data = JsonTool.getJSONObject(jsonProtocol, SessionPreference.KEY_DATA);
        String content = JsonTool.getJsonValue(data, SessionPreference.KEY_CONTENT);
        pushModle = new Gson().fromJson(content.toString(), PushModle.class);
        if (null == mReceivePushMessageView && pushModle != null) {
            mReceivePushMessageView =
                    new ReceivePushMessageView(mContext, pushModle, SCHEDULE_TYPE);
            mReceivePushMessageView.setListener(mReceivePushMessageViewListener);
        }
        addPushView(mReceivePushMessageView);
        if (pushModle != null && pushModle.getPushBaseInfo().getPushType() == PushType.ShowMessage) {
            if (pushModle != null) {
                pushModle.setRead(true);
                PushDb.upPushModel(pushModle);
            }
        } else {
            delayDismissHandler.sendEmptyMessageDelayed(0, delayDismiss);
        }
    }

    private IReceivePushMessageViewListener mReceivePushMessageViewListener =
            new IReceivePushMessageViewListener() {
                @Override
                public void onConfirm() {
                    String protocal =
                            SessionPreference.EVENT_PROTOCAL_ON_CONFIRM_OK.replace("播放", pushModle
                                    .getPushAction().getPushCommand().getConfirms().get(0));
                    onUiProtocal(SessionPreference.EVENT_NAME_INCOMING_PUSH_MESSAGE, protocal);
                    if (pushModle != null) {
                        pushModle.setRead(true);
                        PushDb.upPushModel(pushModle);
                    }
                }

                @Override
                public void onCancel() {
                    onUiProtocal(SessionPreference.EVENT_NAME_INCOMING_PUSH_MESSAGE,
                            SessionPreference.EVENT_PROTOCAL_ON_CONFIRM_CANCEL_PUSH_MESSAGE);
                    mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_SESSION_DONE);
                    if (pushModle != null) {
                        pushModle.setRead(true);
                        PushDb.upPushModel(pushModle);
                    }
                }
            };



    @Override
    public void onTTSEnd() {
        Logger.d(TAG, "!--->onTTSEnd");
        super.onTTSEnd();
        if (pushModle != null) {
            if (!pushModle.needStartWakeUp()) {
                mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_SESSION_DONE);
            }
        }

    }

    @Override
    public void release() {
        Logger.d(TAG, "!--->release");
        super.release();
        delayDismissHandler.removeMessages(0);
    }

    private Handler delayDismissHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            onUiProtocal(SessionPreference.EVENT_NAME_INCOMING_PUSH_MESSAGE,
                    SessionPreference.EVENT_PROTOCAL_ON_CONFIRM_CANCEL_PUSH_MESSAGE_AUTO);
            mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_SESSION_DONE);
            if (pushModle != null) {
                pushModle.setRead(false);
                PushDb.upPushModel(pushModle);
            }
        }
    };
}
