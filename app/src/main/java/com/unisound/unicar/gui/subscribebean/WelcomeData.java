package com.unisound.unicar.gui.subscribebean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WelcomeData {

    private String imgUrl;          // 图片URL
    private String musicUrl;        // 音频URL
    private String startDate;       // 显示起始日期 yyyy-MM-dd 格式
    private String endDate;         // 显示结束日期 yyyy-MM-dd 格式
    private int displayTime;        // 单词显示时间 单位秒
    private boolean dismissAfterMusic;//是否播放完音频后离开页面 如果为true 且musicUrl 不为空 则displayTime 无效
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(int displayTime) {
        this.displayTime = displayTime;
    }

    public boolean isDismissAfterMusic() {
        return dismissAfterMusic;
    }

    public void setDismissAfterMusic(boolean dismissAfterMusic) {
        this.dismissAfterMusic = dismissAfterMusic;
    }

    public boolean needShow() {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date dateStart = df.parse(startDate);
            Date dateEnd = df.parse(endDate);
            String nowStr = df.format(new Date());
            Date now = df.parse(nowStr);
            if (now.compareTo(dateStart) >= 0 && now.compareTo(dateEnd) <= 0) {
                return true;
            }
        } catch (Exception e) {}
        return false;
    }
}
