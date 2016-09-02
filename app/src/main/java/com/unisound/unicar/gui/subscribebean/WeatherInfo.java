package com.unisound.unicar.gui.subscribebean;


public class WeatherInfo {
    // 晴 多云 阴 雾 扬沙 浮尘 沙尘暴 强沙尘暴 冻雨 阵雨 雷阵雨 雷阵雨伴有冰雹
    // 雨夹雪 小雨 中雨 大雨 暴雨 大暴雨 特大暴雨 阵雪 小雪 中雪 大雪 暴雪 霾
    public static final int sunny = 1; // 晴
    public static final int cloudy = 2; // 多云
    public static final int overcast = 3; // 阴
    public static final int foggy = 4; // 雾

    public static final int dustblow = 5; // 扬沙
    public static final int dust = 6; // 浮尘
    public static final int sandstorm = 7; // 沙尘暴
    public static final int strong_sandstorm = 8; // 强沙尘暴

    public static final int icerain = 9; // 冻雨
    public static final int shower = 10; // 阵雨
    public static final int thunder_rain = 11; // 雷阵雨
    public static final int hail = 12; // 雷阵雨伴有冰雹

    public static final int sleety = 13; // 雨夹雪
    public static final int light_rain = 14; // 小雨
    public static final int moderate_rain = 15; // 中雨
    public static final int heavy_rain = 16; // 大雨

    public static final int rainstorm = 17; // 暴雨
    public static final int big_rainstorm = 18; // 大暴雨
    public static final int super_rainstorm = 19; // 特大暴雨
    public static final int snow_shower = 20; // 阵雪

    public static final int light_snow = 21; // 小雪
    public static final int moderate_snow = 22; // 中雪
    public static final int heavy_snow_big = 23; // 大雪
    public static final int blizzard = 24; // 暴雪

    public static final int haze = 25; // 霾

    public static final int other = 0; // 其他

    private String weather;// 如晴
    private int lowTemp;// 如 10 单位摄氏度
    private int highTemp;// 如 20 单位摄氏度
    private String carWash;// 如 适宜
    private String city;// 如上海
    private String area;// 如 徐汇区 若暂时无法精确到区 可不填
    private int weatherType; // 目前26种 没定义的就是OTHER

    public int getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(int weatherType) {
        this.weatherType = weatherType;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public int getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(int lowTemp) {
        this.lowTemp = lowTemp;
    }

    public int getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(int highTemp) {
        this.highTemp = highTemp;
    }

    public String getCarWash() {
        return carWash;
    }

    public void setCarWash(String carWash) {
        this.carWash = carWash;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }


}
