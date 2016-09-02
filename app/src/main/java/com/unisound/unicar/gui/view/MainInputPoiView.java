/**
 * Copyright (c) 2012-2016 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : MainNaviView.java
 * @ProjectName : uniCarSolution_oneshot
 * @PakageName : com.unisound.unicar.gui.view
 * @version : 1.0
 * @Author : Xiaodong.He
 * @CreateDate : 2016-1-28
 */
package com.unisound.unicar.gui.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unisound.unicar.gui.R;
import com.unisound.unicar.gui.model.LocationInfo;
import com.unisound.unicar.gui.preference.UserPerferenceUtil;
import com.unisound.unicar.gui.ui.WindowService;
import com.unisound.unicar.gui.utils.Logger;

/**
 * MainInputPoiView
 * 
 * @author xiaodong.he
 * @date 2016-1-28
 */
public class MainInputPoiView extends LinearLayout implements ISessionView {

    private static final String TAG = MainInputPoiView.class.getSimpleName();

    private Context mContext;

    private LocationInfo mLocationInfo = null;

    private LinearLayout mInputToPoiLayout;
    private TextView mTvInputToPoi;

    private LinearLayout mGoHomeLayout;
    private TextView mTvHomeAddress;

    private LinearLayout mGoCompanyLayout;
    private TextView mTvCompanyAddress;

    private IMainInputPoiViewListener mMainInputPoiViewListener;

    public MainInputPoiView(Context context) {
        this(context, null);
        mContext = context;

        getCurrentLocation();
        initView();
    }

    /**
     * @param context
     * @param attrs
     */
    public MainInputPoiView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    private void getCurrentLocation() {
        mLocationInfo = WindowService.mLocationInfo;
    }

    private void initView() {
        View view =
                LayoutInflater.from(mContext).inflate(R.layout.layout_main_input_poi, this, true);
        mInputToPoiLayout = (LinearLayout) findViewById(R.id.ll_main_input_to_poi);
        mTvInputToPoi = (TextView) findViewById(R.id.tv_main_input_to_poi);
        mGoHomeLayout = (LinearLayout) findViewById(R.id.ll_btn_go_home);
        mTvHomeAddress = (TextView) findViewById(R.id.tv_home_address);
        mGoCompanyLayout = (LinearLayout) findViewById(R.id.ll_btn_go_company);
        mTvCompanyAddress = (TextView) findViewById(R.id.tv_company_address);

        mInputToPoiLayout.setOnClickListener(mOnClickListener);

        initButton();
    }

    private void initButton() {
        String homeAddress =
                UserPerferenceUtil.getAddressName(UserPerferenceUtil.getHomeLocation(mContext));
        if (TextUtils.isEmpty(homeAddress)) {
            mGoHomeLayout.setBackgroundResource(R.drawable.selector_btn_no_address);
        } else {
            mTvHomeAddress.setText(homeAddress);
            mGoHomeLayout.setBackgroundResource(R.drawable.selector_btn_session);
        }
        mGoHomeLayout.setOnClickListener(mOnClickListener);

        String compAddress =
                UserPerferenceUtil.getAddressName(UserPerferenceUtil.getCompanyLocation(mContext));
        if (TextUtils.isEmpty(compAddress)) {
            mGoCompanyLayout.setBackgroundResource(R.drawable.selector_btn_no_address);
        } else {
            mTvCompanyAddress.setText(compAddress);
            mGoCompanyLayout.setBackgroundResource(R.drawable.selector_btn_session);
        }
        mGoCompanyLayout.setOnClickListener(mOnClickListener);
    }


    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_main_input_to_poi:
                    if (null != mMainInputPoiViewListener) {
                        mMainInputPoiViewListener.onSearchInputClick();
                    }
                    break;
                case R.id.ll_btn_go_home:
                    if (null != mMainInputPoiViewListener) {
                        mMainInputPoiViewListener.onGoHomeClick();
                    }
                    break;
                case R.id.ll_btn_go_company:
                    if (null != mMainInputPoiViewListener) {
                        mMainInputPoiViewListener.onGoCompanyClick();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 
     * @param toPoi
     */
    public void setToPoi(String toPoi) {
        mTvInputToPoi.setText(toPoi);
    }

    /**
     * 
     * @param address
     */
    public void setHomeAddress(String address) {
        mTvHomeAddress.setText(address);
    }

    /**
     * 
     * @param address
     */
    public void setCompanyAddress(String address) {
        mTvCompanyAddress.setText(address);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.unisound.unicar.gui.view.ISessionView#isTemporary()
     */
    @Override
    public boolean isTemporary() {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.unisound.unicar.gui.view.ISessionView#release()
     */
    @Override
    public void release() {
        Logger.d(TAG, "release---");
    }


    public void setMainInputPoiViewListener(IMainInputPoiViewListener listener) {
        mMainInputPoiViewListener = listener;
    }

    public interface IMainInputPoiViewListener {

        public void onSearchInputClick();

        public void onGoHomeClick();

        public void onGoCompanyClick();

    }

}
