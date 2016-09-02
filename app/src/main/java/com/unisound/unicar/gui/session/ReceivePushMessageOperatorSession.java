package com.unisound.unicar.gui.session;

import java.io.File;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.unisound.unicar.gui.application.CrashApplication;
import com.unisound.unicar.gui.preference.SessionPreference;
import com.unisound.unicar.gui.pushbean.PushAction;
import com.unisound.unicar.gui.pushbean.PushModle;
import com.unisound.unicar.gui.pushserver.IMediaPlayerStateListener;
import com.unisound.unicar.gui.pushserver.PlayerState;
import com.unisound.unicar.gui.pushserver.PushDb;
import com.unisound.unicar.gui.pushserver.PushMediaPlayer;
import com.unisound.unicar.gui.utils.GuiProtocolUtil;
import com.unisound.unicar.gui.utils.JsonTool;
import com.unisound.unicar.gui.utils.Logger;
import com.unisound.unicar.gui.utils.TTSUtil;
import com.unisound.unicar.gui.utils.Util;
import com.unisound.unicar.gui.view.ReceivePushMessageOperatorView;
import com.unisound.unicar.gui.view.ReceivePushMessageView.IReceivePushMessageViewListener;

public class ReceivePushMessageOperatorSession extends CommBaseSession {

    public static final String TAG = ReceivePushMessageOperatorSession.class.getSimpleName();
    private PushModle pushModle;
    private String operator;
    private PushMediaPlayer pushMediaPlayer = new PushMediaPlayer(CrashApplication.getAppContext());

    private ReceivePushMessageOperatorView mReceivePushMessageOperatorView;
    private String SCHEDULE_TYPE =
            SessionPreference.VALUE_SCHEDULE_TYPE_INCOMING_PUSH_MESSAGE_OPERATOR;

    public ReceivePushMessageOperatorSession(Context context, Handler handle) {
        super(context, handle);
        Logger.d(TAG, "!--->----ReceiveSmsOperatorSession()------");
    }

    public void putProtocol(JSONObject jsonProtocol) {
        super.putProtocol(jsonProtocol);
        Logger.d(TAG, "!--->--putProtocol()--jsonProtocol = " + jsonProtocol);
        Logger.d(TAG, "!--->--putProtocol()--mDataObject = " + mDataObject);
        operator = JsonTool.getJsonValue(mDataObject, "operator", "");
        Logger.d(TAG, "!--->putProtocol:---operator = " + operator);
        String content = JsonTool.getJsonValue(mDataObject, "content");// null
        try {
            if (!TextUtils.isEmpty(content)) {
                pushModle = new Gson().fromJson(content, PushModle.class);
            }
        } catch (Exception e) {
            return;
        }
        if (SessionPreference.VALUE_OPERATOR_INCOMING_PUSH_MESSAGE_CANCEL.equals(operator)) {
            mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_SESSION_DONE);
        } else if (SessionPreference.VALUE_OPERATOR_INCOMING_PUSH_MESSAGE_CONFIRM.equals(operator)) {
            if (pushModle != null) {
                pushModle.setRead(true);
                PushDb.upPushModel(pushModle);
                if (pushModle.getPushAction().getAction() != PushAction.ActionNavi
                        && pushModle.getPushAction().getAction() != PushAction.ActionJump) {
                    if (null == mReceivePushMessageOperatorView) {
                        mReceivePushMessageOperatorView =
                                new ReceivePushMessageOperatorView(mContext, pushModle,
                                        SCHEDULE_TYPE);
                        mReceivePushMessageOperatorView
                                .setListener(mReceivePushMessageViewListener);
                    }
                    addPushView(mReceivePushMessageOperatorView);
                }
                if (pushModle != null && pushModle.needJump()) {
                    String className = pushModle.getPushAction().getClassName();
                    try {
                        Class<?> jumpClass = Class.forName(className);
                        Intent it =
                                new Intent().setClass(CrashApplication.getAppContext(), jumpClass);
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        CrashApplication.getAppContext().startActivity(it);
                    } catch (Exception e) {
                        TTSUtil.playTTSWakeUp(mContext, "暂不支持该功能,请升级后再尝试!");
                    }
                }
            }
        }
    }

    private IReceivePushMessageViewListener mReceivePushMessageViewListener =
            new IReceivePushMessageViewListener() {
                @Override
                public void onConfirm() {
                    onUiProtocal(SessionPreference.EVENT_NAME_INCOMING_PUSH_MESSAGE,
                            SessionPreference.EVENT_PROTOCAL_ON_CONFIRM_OK);
                }

                @Override
                public void onCancel() {
                    onUiProtocal(SessionPreference.EVENT_NAME_INCOMING_PUSH_MESSAGE,
                            SessionPreference.EVENT_PROTOCAL_ON_CONFIRM_CANCEL_PUSH_MESSAGE);
                    mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_SESSION_DONE);
                }
            };

    @Override
    public void onTTSEnd() {
        super.onTTSEnd();
        if (pushModle != null && pushModle.doneAfterTTS()) {
            mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_SESSION_DONE);
        }
        if (SessionPreference.VALUE_OPERATOR_INCOMING_PUSH_MESSAGE_CONFIRM.equals(operator)) {
            if (pushModle != null && pushModle.needPlayMusic()) {
                String musicUrl = pushModle.getPushAction().getActionMusic();
                String name =
                        Util.stringToMD5(musicUrl) + musicUrl.substring(musicUrl.lastIndexOf("."));
                String catchPath = Util.getWelcomeCachePath(CrashApplication.getAppContext());
                File file = new File(catchPath, name);
                if (file.exists()) {
                    pushMediaPlayer.play(file.getPath(), new IMediaPlayerStateListener() {
                        @Override
                        public void onPlayerState(int state) {
                            Logger.e(TAG, "play state" + state);
                            Logger.d("playVoiceMessage onPlayerState:" + state);
                            switch (state) {
                                case PlayerState.MPS_COMPLETE:
                                    mSessionManagerHandler
                                            .sendEmptyMessage(SessionPreference.MESSAGE_SESSION_DONE);
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                } else {
                    mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_SESSION_DONE);
                }
            }
        }
        if (pushModle != null && pushModle.needNavi()) {
            String location = new Gson().toJson(pushModle.getPushAction().getActionLocation());
            onUiProtocal(SessionPreference.EVENT_NAME_GO_FAVORITE_ADDRESS,
                    GuiProtocolUtil.getGoFavoriteAddressProtocol(location,
                            SessionPreference.PARAM_ADDRESS_TYPE_NAVI));
        }
    }

    @Override
    public void release() {
        Logger.d(TAG, "!--->release");
        super.release();
        if (pushMediaPlayer != null) {
            pushMediaPlayer.release();
        }
    }
}
