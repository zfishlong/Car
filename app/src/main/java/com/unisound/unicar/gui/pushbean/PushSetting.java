package com.unisound.unicar.gui.pushbean;

public class PushSetting {
    private int setting; // type 为 BackSettingMessage 时有效 具体定义 等需要支持的详细后台设置功能清单再定义
    private String settingPara; // 设置的参数 具体定义 等需要支持的详细后台设置功能清单再定义

    public int getSetting() {
        return setting;
    }

    public void setSetting(int setting) {
        this.setting = setting;
    }

    public String getSettingPara() {
        return settingPara;
    }

    public void setSettingPara(String settingPara) {
        this.settingPara = settingPara;
    }
}
