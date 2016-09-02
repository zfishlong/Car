package com.unisound.unicar.gui.subscribebean;


public class SubscribeMessage {
    private WelcomeData welcomeData;// 欢迎页面数据信息
    private WeatherInfo weatherInfo;// 天气信息
    private LimitDrive limitDriveInfo;// 限行信息
    private String ProcessStatus;

    public String getProcessStatus() {
        return ProcessStatus;
    }

    public void setProcessStatus(String processStatus) {
        ProcessStatus = processStatus;
    }

    public WelcomeData getWelcomeData() {
        return welcomeData;
    }

    public void setWelcomeData(WelcomeData welcomeData) {
        this.welcomeData = welcomeData;
    }

    public LimitDrive getLimitDriveInfo() {
        return limitDriveInfo;
    }

    public void setLimitDriveInfo(LimitDrive limitDrive) {
        this.limitDriveInfo = limitDrive;
    }

    public WeatherInfo getWeatherInfo() {
        return weatherInfo;
    }

    public void setWeatherInfo(WeatherInfo weatherInfo) {
        this.weatherInfo = weatherInfo;
    }

    public String getTTS() {
        String tts = "";
        if (weatherInfo != null) {
            tts = "今日天气:" + weatherInfo.getWeather() + "洗车指数:" + weatherInfo.getCarWash();
        }
        if (limitDriveInfo != null) {
            tts = tts + limitDriveInfo.getInfo();
        }
        return tts;
    }
}
