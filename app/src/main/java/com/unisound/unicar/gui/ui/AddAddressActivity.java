/**
 * Copyright (c) 2012-2016 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : AddAddressActivity.java
 * @ProjectName : uniCarSolution
 * @PakageName : com.unisound.unicar.gui.ui
 * @version : 1.0
 * @Author : Xiaodong.He
 * @CreateDate : 2016-1-27
 */
package com.unisound.unicar.gui.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.unisound.unicar.gui.R;
import com.unisound.unicar.gui.adapter.LocationAdapter;
import com.unisound.unicar.gui.data.operation.PoiDataModel;
import com.unisound.unicar.gui.location.interfaces.ILocationListener;
import com.unisound.unicar.gui.location.operation.LocationModelProxy;
import com.unisound.unicar.gui.model.LocationInfo;
import com.unisound.unicar.gui.model.PoiInfo;
import com.unisound.unicar.gui.preference.UserPerferenceUtil;
import com.unisound.unicar.gui.search.interfaces.IPoiListener;
import com.unisound.unicar.gui.search.operation.POISearchModelProxy;
import com.unisound.unicar.gui.utils.DataModelErrorUtil;
import com.unisound.unicar.gui.utils.DeviceTool;
import com.unisound.unicar.gui.utils.ErrorUtil;
import com.unisound.unicar.gui.utils.FunctionHelpUtil;
import com.unisound.unicar.gui.utils.GuiSettingUpdateUtil;
import com.unisound.unicar.gui.utils.JsonTool;
import com.unisound.unicar.gui.utils.Logger;
import com.unisound.unicar.gui.utils.Network;
import com.unisound.unicar.gui.utils.StringUtil;
import com.unisound.unicar.gui.view.SettingLoadingPopupWindow;
import com.unisound.unicar.gui.view.SettingLoadingPopupWindow.ISettingLoadingPopListener;

/**
 * Add Address Activity
 * 
 * @author xiaodong.he
 * @date 2016-1-27
 */
public class AddAddressActivity extends Activity {

    private static final String TAG = AddAddressActivity.class.getSimpleName();

    private Context mContext;

    private LinearLayout mAddAddressLayout;
    private EditText mEtInputText;
    private ImageView mBtnOk;

    private ListView mLvLocation;
    private ArrayList<LocationInfo> mLocationList;
    private LocationAdapter mLocationAdapter;

    private TextView mTvErrorMsg;

    private SettingLoadingPopupWindow mSearchingPop;

    private LocationInfo mlLocationInfo = null;
    private String toPoi = "";
    private String toCity = "";

    private final int RADIUS_DEFALUT = 1000;

    private POISearchModelProxy poiSearchModelProxy = null;

    private int mAddAddressType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_address);
        mContext = getApplicationContext();

        initView();

        Intent intent = getIntent();
        if (null != intent) {
            mAddAddressType = intent.getIntExtra(GuiSettingUpdateUtil.KEY_ADD_ADDRESS_TYPE, 0);
            Logger.d(TAG, "onCreate---AddAddressType: " + mAddAddressType);
        }

        initData();
    }

    private void initView() {
        mAddAddressLayout = (LinearLayout) findViewById(R.id.ll_add_address);
        mTvErrorMsg = (TextView) findViewById(R.id.tv_search_poi_error_msg);
        mLvLocation = (ListView) findViewById(R.id.lv_address_location);

        mEtInputText = (EditText) findViewById(R.id.et_add_address);
        mEtInputText.setHint(R.string.default_hint_added_address);

        mEtInputText.setFocusable(true);
        mBtnOk = (ImageView) findViewById(R.id.iv_add_address_ok);
        ImageView btnCancel = (ImageView) findViewById(R.id.iv_add_address_cancel);
        mBtnOk.setOnClickListener(mOnClickListener);
        mBtnOk.setEnabled(false);
        btnCancel.setOnClickListener(mOnClickListener);

        mEtInputText.addTextChangedListener(mEditTextWatcher);
        showKeyboard();
    }

    /**
     * 
     */
    private void showKeyboard() {
        // show Input Keypad
        mEtInputText.requestFocus();
        mEtInputText.setSelection(mEtInputText.getText().length());
        mEtInputText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (null != mEtInputText) {
                        mEtInputText.clearFocus();
                    }
                }
                return false;
            }
        });
        DeviceTool.showEditTextKeyboard(mEtInputText, true);
    }

    /**
     * showConfirmOkButton
     * 
     * @param isShow
     */
    private void showConfirmOkButton(boolean isShow) {
        Logger.d(TAG, "showConfirmOkButton--isShow = " + isShow);
        if (null == mBtnOk) {
            mBtnOk = (ImageView) findViewById(R.id.iv_add_address_ok);
        }
        mBtnOk.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    private void initData() {
        String location = "";
        switch (mAddAddressType) {
            case GuiSettingUpdateUtil.VALUE_ADD_ADDRESS_TYPE_HOME:
                String homeLocation = UserPerferenceUtil.getHomeLocation(mContext);
                location = UserPerferenceUtil.getAddressName(homeLocation);
                break;
            case GuiSettingUpdateUtil.VALUE_ADD_ADDRESS_TYPE_COMPANY:
                String companyLocation = UserPerferenceUtil.getCompanyLocation(mContext);
                location = UserPerferenceUtil.getAddressName(companyLocation);
                break;
            default:
                break;
        }
        if (null != mEtInputText && !TextUtils.isEmpty(location)) {
            mEtInputText.setText(location);
            mEtInputText.setSelection(mEtInputText.getText().length());
        }
    }


    /**
     * set default location
     * 
     * @author xiaodong.he
     * @date 2015-12-14
     * 
     * @param location
     */
    public void setDefaultLocation(String location) {
        if (!TextUtils.isEmpty(location) && mEtInputText != null) {
            mEtInputText.setText(location);
        }
    }

    private TextWatcher mEditTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Logger.d(TAG, "mEditTextWatcher--onTextChanged--s=" + s + "; count = " + count
                    + "; start = " + start + "; before = " + before);
            if (null == mBtnOk) {
                return;
            }
            showConfirmOkButton(true);
            if (TextUtils.isEmpty(s)) {
                mBtnOk.setEnabled(false);
            } else {
                mBtnOk.setEnabled(true);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Logger.d(TAG, "mEditTextWatcher--beforeTextChanged--s=" + s + "; count = " + count
                    + "; start = " + start + "; after = " + after);
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_add_address_ok:
                    Logger.d(TAG, "onClick--OK--");
                    if (null != mEtInputText) {
                        String text = mEtInputText.getText().toString();
                        text = StringUtil.clearSpecialCharacter(text);
                        mEtInputText.setText(text);
                        mEtInputText.setSelection(mEtInputText.getText().length());
                        Logger.d(TAG, "onClick--OK--newText = " + text);
                        if (TextUtils.isEmpty(text)) {
                            return;
                        }
                        searchPoi(text);
                    }
                    DeviceTool.showEditTextKeyboard(mEtInputText, false);
                    break;
                case R.id.iv_add_address_cancel:
                    Logger.d(TAG, "onClick--Cancel--");
                    AddAddressActivity.this.finish();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 
     * @param isKeyword
     */
    private void changeInputTextStyle(boolean isKeyword) {
        if (null == mEtInputText) {
            return;
        }
        String text = mEtInputText.getText().toString();
        Logger.d(TAG, "changeInputTextStyle--text:" + text + "; isKeyword = " + isKeyword);
        if (isKeyword) {
            mEtInputText.setText(FunctionHelpUtil.addDoubleQuotationMarks(text));
            mEtInputText.setTextColor(mContext.getResources().getColor(R.color.color_wakeup_word));
        } else {
            mEtInputText.setText(FunctionHelpUtil.removeDoubleQuotationMarks(text));
            mEtInputText.setTextColor(mContext.getResources().getColor(R.color.grey_white));
        }
    }


    /**
     * show Searching PopWindow
     * 
     * @param context
     */
    private void showSearchingPopWindow(Context context) {
        Logger.d(TAG, "showSearchingPopWindow");
        if (null != mSearchingPop && mSearchingPop.isShowing()) {
            mSearchingPop.dismiss();
        }
        mSearchingPop = new SettingLoadingPopupWindow(context);
        mSearchingPop.setTitle(R.string.searching);
        mSearchingPop.setSettingLoadingPopListener(mSearchingPopListener);
        mSearchingPop.showPopWindowCenter(mAddAddressLayout);
    }

    private ISettingLoadingPopListener mSearchingPopListener = new ISettingLoadingPopListener() {
        @Override
        public void onCancelClick() {
            Logger.d(TAG, "onCancelClick--cancel search");
            changeInputTextStyle(false);
            showKeyboard();
        }
    };

    /**
     * 
     */
    private void dismissSearchingPopWindow() {
        if (null != mSearchingPop) {
            mSearchingPop.dismiss();
            mSearchingPop = null;
        }
    }

    /**
     * 
     * @param poi
     */
    private void searchPoi(String poi) {
        Logger.d(TAG, "searchPoi--poi: " + poi);
        toPoi = poi;
        if (!Network.isNetworkConnected(mContext)) {
            showErrorMsg(mContext.getString(R.string.error_msg_search_poi_no_network));
            return;
        }
        showErrorMsg("");
        dismissLocationListView();
        if (!TextUtils.isEmpty(poi)) {
            showSearchingPopWindow(mContext);
        }
        changeInputTextStyle(true);

        if (WindowService.mLocationInfo != null) {
            this.mlLocationInfo = WindowService.mLocationInfo;
            toCity = mlLocationInfo.getCity();
            startPoiSearch(mlLocationInfo, toPoi, toCity);
        } else {
            startLocation(mContext);
        }
    }

    /**
     * start Location
     * 
     * @param context
     */
    private void startLocation(Context context) {
        Logger.d(TAG, "startLocation---");
        LocationModelProxy locationModel = LocationModelProxy.getInstance(context);
        locationModel.setLocationListener(mLocationListener);
        locationModel.startLocate();
    }

    /**
     * Location Listener
     */
    private ILocationListener mLocationListener = new ILocationListener() {

        @Override
        public void onLocationResult(List<LocationInfo> infos, ErrorUtil code) {

        }

        @Override
        public void onLocationChanged(LocationInfo info, ErrorUtil errorUtil) {
            Logger.d(TAG, "onLocationChanged :　info : " + info);
            mlLocationInfo = info;
            if (errorUtil != null) {
                Logger.d(TAG, "Location fail");
            } else {
                // location success
                if (mlLocationInfo != null) {
                    toCity = info.getCity();
                    startPoiSearch(mlLocationInfo, toPoi, toCity);
                }
            }
        }
    };

    /**
     * start POI Search
     * 
     * @param locationInfo
     * @param toPoi
     * @param toCity
     */
    private void startPoiSearch(LocationInfo locationInfo, String toPoi, String toCity) {
        Logger.d(TAG, "startPoiSearch---locationInfo: " + locationInfo + "; toPoi: " + toPoi
                + "; toCity: " + toCity);
        poiSearchModelProxy = POISearchModelProxy.getInstance(mContext);
        PoiDataModel poiDataModel = convert2PoiDataModel(locationInfo, toPoi, toCity);
        poiSearchModelProxy.startSearch(poiDataModel, mPoiListener);
    }

    /**
     * POI search Listener
     */
    private IPoiListener mPoiListener = new IPoiListener() {
        @Override
        public void onPoiSearchResult(List<PoiInfo> infos, ErrorUtil errorUtil) {
            dismissSearchingPopWindow();
            if (errorUtil != null) {
                Logger.d(TAG, "onPoiSearchResult--search POI fail. " + errorUtil.toString());
                if (DataModelErrorUtil.SEARCH_POI_TIMEOUT == errorUtil.code) {
                    showErrorMsg(mContext.getString(R.string.error_msg_search_poi_timeout));
                } else {
                    showErrorMsg(mContext.getString(R.string.error_msg_search_poi_no_result));
                }
            } else {
                Logger.d(TAG, "onPoiSearchResult---search POI success.");
                showConfirmOkButton(false);

                if (infos != null) {
                    mLocationList = new ArrayList<LocationInfo>();
                    for (PoiInfo poiInfo : infos) {
                        LocationInfo locationInfo = poiInfo.getLocationInfo();
                        if (locationInfo == null) {
                            locationInfo = new LocationInfo();
                        }
                        locationInfo.setName(poiInfo.getName());
                        mLocationList.add(locationInfo);
                    }
                    Logger.d(TAG, "onPoiSearchResult--locationInfos size : " + mLocationList.size()
                            + "; locationInfos: " + JsonTool.toJson(mLocationList));
                    showLocationListView(mContext);
                }
            }
        }
    };

    /**
     * 
     * @param msg
     */
    private void showErrorMsg(String msg) {
        if (null != mLvLocation && mLvLocation.getVisibility() != View.VISIBLE) {
            mLvLocation.setVisibility(View.GONE);
        }
        if (null != mTvErrorMsg) {
            mTvErrorMsg.setText(msg);
            if (TextUtils.isEmpty(msg)) {
                mTvErrorMsg.setVisibility(View.GONE);
            } else {
                mTvErrorMsg.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 
     * @param context
     * @param locationInfos
     */
    private void showLocationListView(Context context) {
        if (null != mTvErrorMsg) {
            mTvErrorMsg.setText("");
            mTvErrorMsg.setVisibility(View.GONE);
        }
        if (null == mLocationList) {
            Logger.w(TAG, "showLocationListView---mLocationList is null");
            return;
        }
        mLocationAdapter = new LocationAdapter(context, mLocationList);
        if (null == mLvLocation) {
            mLvLocation = (ListView) findViewById(R.id.lv_address_location);
        }
        mLvLocation.setAdapter(mLocationAdapter);
        mLvLocation.setOnItemClickListener(mOnItemClickListener);
        mLvLocation.setVisibility(View.VISIBLE);
    }

    /**
     * 
     */
    private void dismissLocationListView() {
        if (null != mLocationList) {
            mLocationList.clear();
            if (null != mLocationAdapter) {
                mLocationAdapter.notifyDataSetChanged();
            }
        }
        if (mLvLocation != null & mLvLocation.getVisibility() != View.GONE) {
            mLvLocation.setVisibility(View.GONE);
        }
    }

    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Logger.d(TAG, "onItemClick---position = " + position);
            if (null != mLocationList) {
                LocationInfo info = mLocationList.get(position);
                saveLocationData(info.toString());
            }
            AddAddressActivity.this.finish();
        }
    };

    /**
     * 
     * @param locationJson
     */
    private void saveLocationData(String locationJson) {
        Logger.d(TAG, "saveLocationData--locationJson:" + locationJson + "; Type = "
                + mAddAddressType);
        switch (mAddAddressType) {
            case GuiSettingUpdateUtil.VALUE_ADD_ADDRESS_TYPE_HOME:
                UserPerferenceUtil.setHomeLocation(mContext, locationJson);
                break;
            case GuiSettingUpdateUtil.VALUE_ADD_ADDRESS_TYPE_COMPANY:
                UserPerferenceUtil.setCompanyLocation(mContext, locationJson);
                break;
            case GuiSettingUpdateUtil.VALUE_ADD_ADDRESS_TYPE_OTHER:
                break;
            default:
                break;
        }
        GuiSettingUpdateUtil.sendSetFavoriteAddressConfigure(mContext, mAddAddressType);
    }

    private PoiDataModel convert2PoiDataModel(LocationInfo mlLocationInfo, String toPoi,
            String toCity) {
        PoiDataModel poiDataModelTemp = new PoiDataModel();

        poiDataModelTemp.setLatitude(mlLocationInfo.getLatitude());
        poiDataModelTemp.setLontitude(mlLocationInfo.getLongitude());
        poiDataModelTemp.setCity(toCity);
        poiDataModelTemp.setPoi(toPoi);
        poiDataModelTemp.setCategory("");
        poiDataModelTemp.setRadius(RADIUS_DEFALUT);

        return poiDataModelTemp;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.d(TAG, "onDestroy---");
    }

}
