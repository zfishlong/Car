/**
 * Copyright (c) 2012-2015 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : OTAActivity.java
 * @ProjectName : uniCarGUI
 * @PakageName : com.unisound.unicar.gui.ui
 * @version : 1.0
 * @Author : Xiaodong.He
 * @CreateDate : 2015-07-20
 */
package com.unisound.unicar.gui.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unisound.unicar.gui.R;
import com.unisound.unicar.gui.preference.UserPerferenceUtil;
import com.unisound.unicar.gui.utils.DeviceTool;
import com.unisound.unicar.gui.utils.GUIConfig;
import com.unisound.unicar.gui.utils.GuiSettingUpdateUtil;
import com.unisound.unicar.gui.utils.Logger;

/**
 * 
 * @author xiaodong
 * @date 20150720
 */
public class OTAActivity extends BaseActivity {

    private static final String TAG = OTAActivity.class.getSimpleName();

    private TextView tvVersion;
    private TextView tvNewestVersion;
    private LinearLayout llOTA;
    private CheckBox cbDebugBox;
    private Context mContext;
    private LinearLayout llDebug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_ota);
        ImageButton returnBtn = (ImageButton) findViewById(R.id.backBtn);
        returnBtn.setOnClickListener(mReturnListerner);
        llOTA = (LinearLayout) findViewById(R.id.bottomPannel);
        llOTA.setOnClickListener(mReturnListerner);
        tvVersion = (TextView) findViewById(R.id.tv_ota_app_version);
        tvVersion.setText(getString(R.string.setting_ota_text_version,
                DeviceTool.getAppVersionName(this)));
        // XD 20150921 added for test version
        ImageView testVersionMark = (ImageView) findViewById(R.id.iv_test_version_mark);
        testVersionMark.setVisibility(GUIConfig.isTestVersion ? View.VISIBLE : View.GONE);

        llDebug = (LinearLayout) findViewById(R.id.ll_debugLayout);
        cbDebugBox = (CheckBox) findViewById(R.id.cb_debugSwitch);
        if (!UserPerferenceUtil.getLogcatEnable(mContext)) {
            llDebug.setVisibility(View.GONE);
        } else {
            llDebug.setVisibility(View.VISIBLE);
            llDebug.bringToFront();
        }
        cbDebugBox.setChecked(UserPerferenceUtil.getLogcatEnable(mContext));
        cbDebugBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Logger.d(TAG, "cbDebugBox isChecked = " + isChecked);
                UserPerferenceUtil.setLogcatEnable(mContext, isChecked);
                GuiSettingUpdateUtil.sendLogcatConfigure(mContext);
                if (!isChecked) {
                    llDebug.setVisibility(View.GONE);
                }

            }
        });

        tvNewestVersion = (TextView) findViewById(R.id.tv_ota_newest_version);
        String dueDate = UserPerferenceUtil.getAppLimitTime(mContext);
        if (TextUtils.isEmpty(dueDate)) {
            tvNewestVersion.setText(mContext.getString(R.string.setting_ota_text_newest_version));
        } else {
            tvNewestVersion.setText(mContext.getString(R.string.setting_ota_text_app_duedate,
                    dueDate));
        }
    }

    private OnClickListener mReturnListerner = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backBtn:
                    onBackPressed();
                    break;
                case R.id.bottomPannel:
                    new HideClick().start();
                    if (sIsAlive > 5) {
                        Logger.d(TAG, "bottomPannel 5");
                        sIsAlive = 0;
                        if (!UserPerferenceUtil.getLogcatEnable(mContext)) {
                            llDebug.setVisibility(View.VISIBLE);
                            llDebug.bringToFront();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public static volatile int sIsAlive = 0;

    private class HideClick extends Thread {

        @Override
        public void run() {
            sIsAlive++;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (sIsAlive > 0) {
                sIsAlive--;
            }
            super.run();

        }
    }

    private boolean isNeedOTA() {
        boolean isNeedOTA = false;
        // TODO:
        Logger.d(TAG, "!--->---checkOTA()----------isNeedOTA = " + isNeedOTA);
        return isNeedOTA;
    }

    private void doOTA() {
        Logger.d(TAG, "!--->---doOTA()----------");
        // TODO:


    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (isNeedOTA()) {
            doOTA();
        }
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

}
