/**
 * Copyright (c) 2012-2016 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : AddressFavoriteActivity.java
 * @ProjectName : uniCarSolution
 * @PakageName : com.unisound.unicar.gui.ui
 * @version : 1.1
 * @Author : Xiaodong.He
 * @CreateDate : 2016-01-26
 */
package com.unisound.unicar.gui.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.unisound.unicar.gui.R;
import com.unisound.unicar.gui.preference.UserPerferenceUtil;
import com.unisound.unicar.gui.utils.GuiSettingUpdateUtil;
import com.unisound.unicar.gui.utils.Logger;
import com.unisound.unicar.gui.utils.Network;

/**
 * AddressFavoriteActivity
 * 
 * @author xiaodong.he
 * @date 2016-01-26
 */
public class AddressFavoriteActivity extends BaseActivity {

    private static final String TAG = AddressFavoriteActivity.class.getSimpleName();

    private Context mContext;

    private TextView mTvHomeAddress;
    private ImageView mIvSettingGoHome;

    private TextView mTvCompanyAddress;
    private ImageView mIvSettingGoCompany;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_favorite);

        mContext = getApplicationContext();

        ImageButton returnBtn = (ImageButton) findViewById(R.id.backBtn);
        returnBtn.setOnClickListener(mReturnListerner);

        findView();

        checkAddAddressNow(getIntent());

        registReceiver();
    }

    private void findView() {
        mTvHomeAddress = (TextView) findViewById(R.id.tv_setting_home_address);
        mIvSettingGoHome = (ImageView) findViewById(R.id.iv_setting_go_home);

        mTvCompanyAddress = (TextView) findViewById(R.id.tv_setting_company_address);
        mIvSettingGoCompany = (ImageView) findViewById(R.id.iv_setting_go_company);

        mIvSettingGoHome.setOnClickListener(mOnClickListener);
        mIvSettingGoCompany.setOnClickListener(mOnClickListener);
    }


    private OnClickListener mReturnListerner = new OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_setting_go_home:
                    showAddedAddressActivity(GuiSettingUpdateUtil.VALUE_ADD_ADDRESS_TYPE_HOME);
                    break;
                case R.id.iv_setting_go_company:
                    showAddedAddressActivity(GuiSettingUpdateUtil.VALUE_ADD_ADDRESS_TYPE_COMPANY);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 
     * @param intent
     */
    private void checkAddAddressNow(Intent intent) {
        if (null == intent) {
            return;
        }
        int addType = intent.getIntExtra(GuiSettingUpdateUtil.KEY_ADD_ADDRESS_TYPE, -1);
        if (addType != -1) {
            showAddedAddressActivity(addType);
        }
    }

    /**
     * 
     * @param addType
     */
    private void showAddedAddressActivity(int addType) {
        Logger.d(TAG, "showAddedAddressActivity--addType = " + addType);
        Intent intent = new Intent(AddressFavoriteActivity.this, AddAddressActivity.class);
        intent.putExtra(GuiSettingUpdateUtil.KEY_ADD_ADDRESS_TYPE, addType);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initLocationView();
    }

    private void initLocationView() {
        String homeLocation = UserPerferenceUtil.getHomeLocation(mContext);
        String companyLocation = UserPerferenceUtil.getCompanyLocation(mContext);

        Logger.d(TAG, "initLocationView--homeLocation = " + homeLocation + "; companyLocation = "
                + companyLocation);


        if (TextUtils.isEmpty(homeLocation)) {
            mIvSettingGoHome.setBackgroundResource(R.drawable.selector_btn_add_favorite);
        } else {
            mTvHomeAddress.setText(UserPerferenceUtil.getAddressName(homeLocation));
            mIvSettingGoHome.setBackgroundResource(R.drawable.selector_btn_edit_favorite);
        }

        if (TextUtils.isEmpty(companyLocation)) {
            mIvSettingGoCompany.setBackgroundResource(R.drawable.selector_btn_add_favorite);
        } else {
            mTvCompanyAddress.setText(UserPerferenceUtil.getAddressName(companyLocation));
            mIvSettingGoCompany.setBackgroundResource(R.drawable.selector_btn_edit_favorite);
        }

        updateUIByNetwork();
    }

    /**
     * 
     */
    private void updateUIByNetwork() {
        boolean isNetWorkEnable = Network.isNetworkConnected(mContext);
        if (isNetWorkEnable) {
            mIvSettingGoHome.setEnabled(true);
            mIvSettingGoCompany.setEnabled(true);
        } else {
            mIvSettingGoHome.setEnabled(false);
            mIvSettingGoCompany.setEnabled(false);
        }
    }

    /**
     * 
     * @author xiaodong.he
     * @date 2016-2-1
     */
    private void registReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, filter);
    }


    /**
     * unRegistReceiver
     * 
     * @author xiaodong.he
     * @date 2016-2-1
     */
    private void unRegistReceiver() {
        unregisterReceiver(mReceiver);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
                updateUIByNetwork();
            }
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        unRegistReceiver();
    };

}
