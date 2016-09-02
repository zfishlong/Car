package com.unisound.unicar.gui.pushbean;

public class PushLocation {

    // {"address":"1号线;9号线;11号线","name":"徐家汇(地铁站)","cityCode":"021","province":"上海市","lng":121.436837,"lat":31.195338,"city":"上海"}
    private double lng;// 经度 action 为 ActionNavi时有效
    private double lat;// 纬度 action 为 ActionNavi时有效
    private String address;
    private String name;
    private String cityCode;
    private String province;
    private String city;

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
