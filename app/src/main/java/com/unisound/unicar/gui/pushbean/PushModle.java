package com.unisound.unicar.gui.pushbean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import android.text.TextUtils;


public class PushModle {


    private PushBaseInfo pushBaseInfo; // 通知基础信息 所有字段不能为空

    private PushSetting pushSetting; // 后台设置 pushType为 BackSettingMessage有效

    private PushAction pushAction;

    private boolean read = false;

    private String id;

    private String time;

    public PushModle() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        setId(UUID.randomUUID().toString());
        setTime(df.format(new Date()));
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public PushBaseInfo getPushBaseInfo() {
        return pushBaseInfo;
    }

    public void setPushBaseInfo(PushBaseInfo pushBaseInfo) {
        this.pushBaseInfo = pushBaseInfo;
    }

    public PushSetting getPushSetting() {
        return pushSetting;
    }

    public void setPushSetting(PushSetting pushSetting) {
        this.pushSetting = pushSetting;
    }

    public PushAction getPushAction() {
        return pushAction;
    }

    public void setPushAction(PushAction pushAction) {
        this.pushAction = pushAction;
    }


    public boolean doneAfterTTS() {
        try {
            if (this.getPushBaseInfo().getPushType() == PushType.ConfirmMessage
                    && this.getPushAction().getAction() == PushAction.ActionPlayTTS) {
                return true;
            }
        } catch (Exception e) {}
        return false;
    }

    public boolean needPlayMusic() {
        try {
            if (this.getPushBaseInfo().getPushType() == PushType.ConfirmMessage
                    && this.getPushAction().getAction() == PushAction.ActionPlayMusic
                    && TextUtils.isEmpty(this.getPushAction().getActionMusic()) == false) {
                return true;
            }
        } catch (Exception e) {}
        return false;
    }

    public boolean needNavi() {
        try {
            if (this.getPushBaseInfo().getPushType() == PushType.ConfirmMessage
                    && this.getPushAction().getAction() == PushAction.ActionNavi
                    && this.getPushAction().getActionLocation() != null) {
                return true;
            }
        } catch (Exception e) {}
        return false;
    }

    public boolean needJump() {
        try {
            if (this.getPushBaseInfo().getPushType() == PushType.ConfirmMessage
                    && this.getPushAction().getAction() == PushAction.ActionJump
                    && this.getPushAction().getClassName() != null) {
                return true;
            }
        } catch (Exception e) {}
        return false;
    }

    public boolean needStartWakeUp() {
        try {
            if (getPushBaseInfo().getPushType() == PushType.ConfirmMessage) {
                return true;
            }
        } catch (Exception e) {}
        return false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

}
