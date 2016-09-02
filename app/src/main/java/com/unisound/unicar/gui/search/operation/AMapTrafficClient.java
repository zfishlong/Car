package com.unisound.unicar.gui.search.operation;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.unisound.unicar.gui.R;
import com.unisound.unicar.gui.location.interfaces.ILocationListener;
import com.unisound.unicar.gui.location.operation.LocationModelProxy;
import com.unisound.unicar.gui.model.LocationInfo;
import com.unisound.unicar.gui.ui.AmapTrafficShowActivity;
import com.unisound.unicar.gui.ui.WindowService;
import com.unisound.unicar.gui.utils.ErrorUtil;
import com.unisound.unicar.gui.utils.Logger;

/**
 * 实时路况工具类
 * 
 * @author Ch
 * 
 */
public class AMapTrafficClient extends BaseAsyncSearchClient {

    public static final String TAG = AMapTrafficClient.class.getSimpleName();

    private GeoCoder bdSearch;// 百度坐标
    private Context mContext;
    private LocationModelProxy locationModel = null;
    private GeocodeSearch geocoderSearch;// 地理编码
    private IGaoDeSearchListener mIGaoDeSearchListener; // 地理编码监听
    private String mRoad = "";// 道路名称信息
    private ITrafficListener mITrafficListener;// 回调

    private String bdCity = "";
    private String bdRoad = "";

    public AMapTrafficClient(Context context) {
        super(context);
        this.mContext = context;
        geocoderSearch = new GeocodeSearch(mContext);
        mIGaoDeSearchListener = new IGaoDeSearchListener();
        geocoderSearch.setOnGeocodeSearchListener(mIGaoDeSearchListener);

        bdSearch = GeoCoder.newInstance();
        bdSearch.setOnGetGeoCodeResultListener(bdListener);
    }

    /**
     * 设置监听
     * 
     * @param mlistener
     */
    public void setMlistener(ITrafficListener mlistener) {
        this.mITrafficListener = mlistener;
    }


    /**
     * 思路：封装三个方法 1.获取城市和道路信息 2.地理编码 3.拉起实时路况 4.定义一个接口 ||回调正在操作中提示onProcess(String)
     * ||回调失败信息onError(String) ||回调流程结束信息onSessionDone())
     * 
     */
    public interface ITrafficListener {
        void onProcess(String process);// 流程进行中

        void onError(String error);// 流程失败

        void onSessionDone();// sessionDone
    }

    /**
     * 开始流程 判断并确定city和road 信息
     * 
     * @param city
     * @param road
     * @author Ch
     */
    public void startTraffic(String city, String road) {
        if (city == null || city.equals("") || city.equalsIgnoreCase("CURRENT_CITY")
                || city.equalsIgnoreCase("附近")) {
            if (WindowService.mLocationInfo != null) {
                LocationInfo info = WindowService.mLocationInfo;
                city = info.getCity();
                road = info.getAddress();
                if (mITrafficListener != null) {
                    String process = "正在帮你查找" + road + "的路况";
                    mITrafficListener.onProcess(process);
                }
                // 1.开始获取地理编码信息
                if (!TextUtils.isEmpty(city) && !TextUtils.isEmpty(road)) {
                    getLonStart(road, city);// 根据城市和道路名 ->获取经纬度信息
                } else {
                    if (mITrafficListener != null) {
                        String error = "获取定位信息失败,请检查网络和地图的key";
                        mITrafficListener.onError(error);
                    }
                }
            } else {
                locationModel = LocationModelProxy.getInstance(mContext);
                locationModel.setLocationListener(new ILocationListener() {

                    @Override
                    public void onLocationResult(List<LocationInfo> infos, ErrorUtil code) {}

                    @Override
                    public void onLocationChanged(LocationInfo info, ErrorUtil errorUtil) {
                        if (info != null) {
                            String mcity = info.getCity();
                            String mroad = info.getAddress();
                            if (mITrafficListener != null) {
                                String process = "正在帮你查找" + mroad + "的路况";
                                mITrafficListener.onProcess(process);
                            }
                            // 1.开始获取地理编码信息
                            if (!TextUtils.isEmpty(mcity) && !TextUtils.isEmpty(mroad)) {
                                getLonStart(mroad, mcity);// 根据城市和道路名 ->获取经纬度信息
                            } else {
                                if (mITrafficListener != null) {
                                    String error = "获取定位信息失败,请检查网络和地图的key";
                                    mITrafficListener.onError(error);
                                }
                            }
                        }
                    }
                });
                locationModel.startLocate();
            }
        } else {
            if (mITrafficListener != null) {
                String process = "正在帮你查找" + road + "的路况";
                mITrafficListener.onProcess(process);
            }
            // 1.开始获取地理编码信息
            if (!TextUtils.isEmpty(city) && !TextUtils.isEmpty(road)) {
                getLonStart(road, city);// 根据城市和道路名 ->获取经纬度信息
            } else {
                if (mITrafficListener != null) {
                    String error = "获取定位信息失败,请检查网络和地图的key";
                    mITrafficListener.onError(error);
                }
            }
        }
    }

    /**
     * 地理编码
     * 
     * @param road
     * @param city
     */
    private void getLonStart(String road, String city) {
        mRoad = road;
        // add by ch 2015/11/23 处理road不带城市名 个别地址经纬度信息不正确
        if (!road.contains(city.substring(0, city.length() - 1))) {
            road = city + road;
        }

        bdCity = city;
        bdRoad = road;

        GeocodeQuery query = new GeocodeQuery(road, city);// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
        setTimeOutOpt();
        geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
    }

    OnGetGeoCoderResultListener bdListener = new OnGetGeoCoderResultListener() {

        @Override
        public void onGetGeoCodeResult(GeoCodeResult result) {
            Logger.d(TAG, "!--->getLonStart----" + " 调百度地理编码 result= " + result);
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                // 没有找到检索结果
                if (mITrafficListener != null) {
                    mITrafficListener.onError("无相应结果");
                }
            }
            // 获取反向地理编码结果
            String address = result.getAddress();
            double lat = result.getLocation().latitude;
            double lon = result.getLocation().longitude;

            // if (PackageUtil.isAppInstalled(mContext, "com.autonavi.minimap")) {
            showTrafficInGaodeMap(address, lat, lon);
            // } else {
            // if (mITrafficListener != null) {
            // mITrafficListener.onError("未安装高德地图");
            // }
            // }
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
            // TODO Auto-generated method stub

        }
    };

    /**
     * 高德地理编码回调监听
     * 
     * @author Ch
     * 
     */
    private class IGaoDeSearchListener implements OnGeocodeSearchListener {

        @Override
        public void onGeocodeSearched(GeocodeResult result, int rCode) {
            onSearchResultReach();
            // 地理编码 中文地名—> 经纬度
            String onResultNotifyText = "";
            if (rCode == 0) {
                if (result != null && result.getGeocodeAddressList() != null
                        && result.getGeocodeAddressList().size() > 0) {
                    GeocodeAddress address = result.getGeocodeAddressList().get(0);
                    // XD 20160104 modify
                    if (null != address && null != address.getLatLonPoint()) {
                        LatLonPoint latLonPoint = address.getLatLonPoint();
                        Logger.d(TAG, "onGeocodeSearched---latLonPoint = " + latLonPoint);
                        double lat = latLonPoint.getLatitude();
                        double lon = latLonPoint.getLongitude();
                        /*
                         * 2.拉去路况信息 2.1判断是否安装了高德地图
                         */
                        // if (PackageUtil.isAppInstalled(mContext, "com.autonavi.minimap")) {
                        showTrafficInGaodeMap(mRoad, lat, lon);
                        // } else {
                        // onResultNotifyText = "还没有安装高德地图哦！";
                        // }
                    } else {
                        onResultNotifyText = mContext.getString(R.string.traffic_no_info_find);
                        Logger.w(TAG, "onGeocodeSearched---no traffic info.");
                    }
                } else {
                    Logger.d(TAG, "!--->getLonStart----" + " 调百度地理编码 road/city= " + bdRoad + "/"
                            + bdCity);
                    // onResultNotifyText = "无相应结果";
                    bdSearch.geocode(new GeoCodeOption().city(bdCity).address(bdRoad));
                }
            } else if (rCode == 27) {
                onResultNotifyText = "网络不给力";
            } else if (rCode == 32) {
                onResultNotifyText = "高德key无效";
            } else {
                onResultNotifyText = "其他错误";
            }
            // TODO toast提示下结果 (过滤出成功的case)
            if (!TextUtils.isEmpty(onResultNotifyText)) {
                if (mITrafficListener != null) {
                    mITrafficListener.onError(onResultNotifyText);
                }
            }
        }

        @Override
        public void onRegeocodeSearched(RegeocodeResult arg0, int arg1) {
            // 反地理编码 经纬度->中文地名
        }
    }

    /**
     * 显示路况
     * 
     * @param road 道路名称
     * @param lat 经度
     * @param lon 纬度
     */
    private void showTrafficInGaodeMap(String road, double lat, double lon) {
        // data=androidamap://showTraffic?poiname=方恒国际中心&lat=39.92&lon=116.46
        // &level=14&dev=1&sourceApplication=softname
        // 回调onSessionDone()
        if (mITrafficListener != null) {
            mITrafficListener.onSessionDone();
        }
        // String data =
        // "androidamap://showTraffic?poiname=" + road + "&lat=" + lat + "&lon=" + lon
        // + "&level=" + 14 + "&dev=" + 0 + "&sourceApplication=uniCarSolution";
        // Intent intent = new Intent("android.intent.action.VIEW");
        // intent.addCategory("android.intent.category.DEFAULT");
        // intent.setPackage("com.autonavi.minimap");
        // intent.setData(Uri.parse(data.trim()));
        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // mContext.startActivity(intent);
        /* 采用mapView显示 */
        Intent intent = new Intent(mContext, AmapTrafficShowActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("lat", lat);
        intent.putExtra("lng", lon);
        intent.putExtra("road", road);
        mContext.startActivity(intent);
    }

    /**
     * 设置超时
     * 
     * @param timeout 超时提示信息
     */
    private void setTimeOutOpt() {
        Logger.d(TAG, "!--->setTimeOutOpt----");
        requestSearch(new Runnable() {
            @Override
            public void run() {
                if (mITrafficListener != null) {
                    mITrafficListener.onSessionDone();
                }
            }
        });
    }
}
