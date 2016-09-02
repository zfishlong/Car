package com.unisound.unicar.gui.pushbean;

public class PushBaseInfo {

    private String notificationTitle; // 通知标题 用于显示
    private String notificationContent;// 通知内容 用于显示
    private String notificationIcon;// 通知图标 用于显示
    private String tts;// 通知过来时播报的TTS内容 如收到一条体育新闻,播放还是取消 或者纯通知的 XX路拥堵,请提前绕行
    private PushType pushType; // 通知类型 见push type注释

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationContent() {
        return notificationContent;
    }

    public void setNotificationContent(String notificationContent) {
        this.notificationContent = notificationContent;
    }

    public String getNotificationIcon() {
        return notificationIcon;
    }

    public void setNotificationIcon(String notificationIcon) {
        this.notificationIcon = notificationIcon;
    }

    public String getTts() {
        return tts;
    }

    public void setTts(String tts) {
        this.tts = tts;
    }

    public PushType getPushType() {
        return pushType;
    }

    public void setPushType(PushType pushType) {
        this.pushType = pushType;
    }
}
