package com.unisound.unicar.gui.pushbean;



public class GpsInfo {

    public static final int MapGaoDe = 1;
    public static final int MapBaidu = 2;
    private int mapType;
    private String gpsInfo;
    private String city;
    private String area;

    public int getMapType() {
        return mapType;
    }

    public void setMapType(int mapType) {
        this.mapType = mapType;
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

    public String getGpsInfo() {
        return gpsInfo;
    }

    public void setGpsInfo(String gpsInfo) {
        this.gpsInfo = gpsInfo;
    }

}
