/**
 * Copyright (c) 2012-2015 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : SettingsViewPagerActivity.java
 * @ProjectName : uniCarGui
 * @PakageName : com.unisound.unicar.gui.ui
 * @version : 1.4
 * @Author : Xiaodong.He
 * @CreateDate : 2015-06-10
 * @modifyDate : 2016-01-20
 */
package com.unisound.unicar.gui.ui;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.unisound.unicar.gui.R;
import com.unisound.unicar.gui.preference.SessionPreference;
import com.unisound.unicar.gui.preference.UserPerferenceUtil;
import com.unisound.unicar.gui.utils.FunctionHelpUtil;
import com.unisound.unicar.gui.utils.GUIConfig;
import com.unisound.unicar.gui.utils.GuiSettingUpdateUtil;
import com.unisound.unicar.gui.utils.JsonTool;
import com.unisound.unicar.gui.utils.Logger;
import com.unisound.unicar.gui.utils.Network;
import com.unisound.unicar.gui.utils.TTSUtil;
import com.unisound.unicar.gui.view.EditWakeupWordPopWindow;
import com.unisound.unicar.gui.view.ImageViewWithText;
import com.unisound.unicar.gui.view.SettingHelpPopupWindow;
import com.unisound.unicar.gui.view.SettingHelpPopupWindow.ISettingHelpPopListener;
import com.unisound.unicar.gui.view.SettingLoadingPopupWindow;
import com.unisound.unicar.gui.view.SettingLoadingPopupWindow.ISettingLoadingPopListener;

/**
 * 
 * @author xiaodong
 * @date 20150610
 */
@SuppressLint("InflateParams")
public class SettingsViewPagerActivity extends BaseActivity {

    private static final String TAG = SettingsViewPagerActivity.class.getSimpleName();

    private Context mContext;

    private ArrayList<View> mViewList = new ArrayList<View>();
    private LayoutInflater mLayoutInflater;
    private ViewGroup indicatorViewGroup;

    private ImageView mImageView;
    private ImageView[] mImageViews;
    private ViewPager mViewPager;

    // --page-1
    // Wake up
    private CheckBox mCBWakeup;
    private TextView mTvWakeupStatusClose;
    private TextView mTvWakeupStatusOpen;
    private ImageView mIvEditWakeupword;

    // Version Conversation Mode
    private RadioGroup mRgVersionMode;
    private RadioButton mRbVersionModeExp;
    private RadioButton mRbVersionModeStandrard;
    private RadioButton mRbVersionModeHigh;
    private TextView mTvVersionModeStatus;

    // Sub
    private TextView mTxtSubStatus;
    private CheckBox mCbSub;

    // 汽车防盗
    private TextView mTxtGpsStatus;
    private CheckBox mCbGps;

    // Timbre
    private RadioGroup mRgSettingTtsTimbre;
    private RadioButton mRbTtsTimbreStandard;
    private RadioButton mRbTtsTimbreSexy;
    private RadioButton mRbTtsTimbreAuto;

    private TextView mTvTtsTimbreStatus;

    private int mLastTtsTimbre = UserPerferenceUtil.VALUE_TTS_TIMBRE_STANDARD;

    // --page-2
    // Map
    private RadioGroup mRgMapChoose;
    private RadioButton mRbMapGaode;
    private RadioButton mRbMapBaidu;
    private RadioButton mBbMapMore;
    private TextView mTvMapChooseStatus;

    // TTS
    private RadioGroup mRgTTSSpeed;
    private RadioButton mRbTTSSlowly;
    private RadioButton mRbTTSStandard;
    private RadioButton mRbTTSFast;
    private TextView mTvTTSSpeedStatus;

    // FloatMic
    private CheckBox mCBFloatMic;
    private TextView mTvFloatMicStatus;

    // AEC
    private LinearLayout mSettingAECLayout;
    private CheckBox mCBAEC;
    private TextView mTvAECStatus;

    // page2 extra layout
    // private LinearLayout mPage2ExtraLayout;

    // --page-3
    private ImageViewWithText mIvtSettingAddrFavorite;
    private ImageViewWithText mIvtOta;
    private ImageViewWithText mIvtAboutUs;

    private SettingHelpPopupWindow mSettingHelpPop;
    private SettingLoadingPopupWindow mSettingLoadingPop;

    /**
     * XD added 2016-1-18
     */
    private JSONArray mTtsTimbreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.d(TAG, "!--->onCreate()----");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_settings_view_pager);
        mLayoutInflater = getLayoutInflater();
        mContext = getApplicationContext();

        TextView tvTopTitle = (TextView) findViewById(R.id.tv_top_title);
        tvTopTitle.setText(R.string.common_control);
        ImageButton returnBtn = (ImageButton) findViewById(R.id.backBtn);
        returnBtn.setOnClickListener(mReturnListerner);

        // 添加layout
        mViewList.add(mLayoutInflater.inflate(R.layout.pager_settings_layout_1, null));
        mViewList.add(mLayoutInflater.inflate(R.layout.pager_settings_layout_2, null));
        mViewList.add(mLayoutInflater.inflate(R.layout.pager_settings_layout_3, null));
        mViewList.add(mLayoutInflater.inflate(R.layout.pager_settings_layout_4, null));

        // initViewPager();

        UserPerferenceUtil.registerOnSharedPreferenceChangeListener(mContext,
                mPreferenceChangeListener);
        registReceiver();

        IntentFilter wakeupfilter = new IntentFilter(GUIConfig.ACTION_WAKEUP_WORDS_UPDATE);
        registerReceiver(receiver, wakeupfilter);

        // XD 2016-1-18 added
        GuiSettingUpdateUtil.sendRequestTtsTimbreList(mContext);
    }

    private OnClickListener mReturnListerner = new OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Logger.d(TAG, "onResume");
        initMapUIStatus();

        initViewPager();

    }

    private void initViewPager() {
        mImageViews = new ImageView[mViewList.size()];

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(pagerAdapter);

        indicatorViewGroup = (LinearLayout) findViewById(R.id.viewGroup);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_dot_normal);
        indicatorViewGroup.removeAllViews();
        for (int i = 0; i < mViewList.size(); i++) {
            mImageView = new ImageView(SettingsViewPagerActivity.this);
            mImageView.setLayoutParams(new LayoutParams(bitmap.getWidth(), bitmap.getHeight()));
            mImageView.setPadding(0, 20, 0, 20);

            if (i == 0) {
                mImageView.setBackgroundResource(R.drawable.icon_dot_selected);
            } else {
                mImageView.setBackgroundResource(R.drawable.icon_dot_normal);
            }
            mImageViews[i] = mImageView;
            indicatorViewGroup.addView(mImageViews[i]);
        }
        bitmap.recycle();
        mViewPager.setOnPageChangeListener(mPageChangeLinstener);
    }

    private OnPageChangeListener mPageChangeLinstener = new OnPageChangeListener() {
        @Override
        public void onPageSelected(int arg0) {
            for (int i = 0; i < mImageViews.length; i++) {
                if (i == arg0) {
                    mImageViews[i].setBackgroundResource(R.drawable.icon_dot_selected);
                } else {
                    mImageViews[i].setBackgroundResource(R.drawable.icon_dot_normal);
                }
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {}

        @Override
        public void onPageScrollStateChanged(int arg0) {
            switch (arg0) {
                case ViewPager.SCROLL_STATE_IDLE:

                    break;
                case ViewPager.SCROLL_STATE_DRAGGING:

                    break;
                default:
                    break;
            }
        }
    };

    PagerAdapter pagerAdapter = new PagerAdapter() {

        @Override
        public int getCount() {
            return mViewList.size();
        }

        @Override
        public Object instantiateItem(View container, int position) {
            /* < XiaoDong 20150721 added begin */
            int childCount = ((ViewPager) container).getChildCount();
            if (mViewList == null) {
                Logger.e(TAG, "!--->PagerAdapter instantiateItem error, mViewList is null.");
                return null;
            }
            if (null != mViewList && mViewList.size() < position) {
                Logger.e(TAG,
                        "!--->PagerAdapter instantiateItem error, return null. ViewList size = "
                                + mViewList.size() + "; position = " + position);
                return null;
            }
            Logger.d(TAG, "!--->PagerAdapter--position:" + position + "; childCount:" + childCount
                    + "; mViewList.size:" + mViewList.size());
            /* XiaoDong 20150721 added End > */
            switch (position) {
                case 0:
                    ((ViewPager) container).addView(mViewList.get(position), 0);
                    // Wake up setting
                    mCBWakeup = (CheckBox) findViewById(R.id.cb_wakeup);
                    mTvWakeupStatusClose = (TextView) findViewById(R.id.tv_status_wakeup_close);
                    mTvWakeupStatusOpen = (TextView) findViewById(R.id.tv_status_wakeup_open);
                    mIvEditWakeupword = (ImageView) findViewById(R.id.iv_setting_edit_wakeupword);
                    initWakeupUIStatus();
                    mCBWakeup.setOnCheckedChangeListener(mCbListener);

                    // Version level setting
                    mTvVersionModeStatus = (TextView) findViewById(R.id.tv_status_oneshot);
                    mRgVersionMode = (RadioGroup) findViewById(R.id.rg_setting_version_mode);
                    mRbVersionModeExp = (RadioButton) findViewById(R.id.rBtn_version_mode_1);
                    mRbVersionModeStandrard = (RadioButton) findViewById(R.id.rBtn_version_mode_2);
                    mRbVersionModeHigh = (RadioButton) findViewById(R.id.rBtn_version_mode_3);

                    // TTS Timbre
                    mRgSettingTtsTimbre = (RadioGroup) findViewById(R.id.rg_setting_tts_timbre);
                    mRbTtsTimbreStandard =
                            (RadioButton) findViewById(R.id.rBtn_tts_timbre_standard);
                    mRbTtsTimbreSexy = (RadioButton) findViewById(R.id.rBtn_tts_timbre_sexy);
                    mRbTtsTimbreAuto = (RadioButton) findViewById(R.id.rBtn_tts_timbre_auto);
                    mTvTtsTimbreStatus = (TextView) findViewById(R.id.tv_status_timbre_choose);

                    // update UI Status
                    initWakeupUIStatus();
                    initVersionLevelUIStatus();
                    initTtsTimbreUIStatus();

                    // set listener
                    mCBWakeup.setOnCheckedChangeListener(mCbListener);
                    mRgVersionMode.setOnCheckedChangeListener(mRgCheckedChangeListener);
                    mRgSettingTtsTimbre.setOnCheckedChangeListener(mRgCheckedChangeListener);
                    break;

                case 1:
                    /* < XiaoDong 20150721 added Begin */
                    if (childCount == 0) {
                        Logger.w(TAG, "!--->position is 1 but childCount is 0");
                        ((ViewPager) container).addView(mViewList.get(position), 0);
                    }
                    /* XiaoDong 20150721 added End > */
                    ((ViewPager) container).addView(mViewList.get(position), 1);

                    // Map Choose setting
                    mRgMapChoose = (RadioGroup) findViewById(R.id.rg_setting_map_choose);
                    mRbMapGaode = (RadioButton) findViewById(R.id.rBtn_map_gaode);
                    mRbMapBaidu = (RadioButton) findViewById(R.id.rBtn_map_baidu);
                    mBbMapMore = (RadioButton) findViewById(R.id.rBtn_map_more);
                    mBbMapMore.setVisibility(GUIConfig.isSupportMoreMapSetting
                            ? View.VISIBLE
                            : View.GONE);
                    mTvMapChooseStatus = (TextView) findViewById(R.id.tv_status_map_choose);
                    initMapUIStatus();
                    mRgMapChoose.setOnCheckedChangeListener(mRgCheckedChangeListener);
                    mBbMapMore.setOnClickListener(mOnClickListener);

                    // AEC setting
                    mSettingAECLayout = (LinearLayout) findViewById(R.id.ll_setting_aec);
                    mCBAEC = (CheckBox) findViewById(R.id.cb_aec);
                    mTvAECStatus = (TextView) findViewById(R.id.tv_status_aec);
                    initAECSettingUIStatus();
                    // set listener
                    mCBAEC.setOnCheckedChangeListener(mCbListener);

                    // TTS setting
                    mRgTTSSpeed = (RadioGroup) findViewById(R.id.rg_setting_tts_speed);
                    mRbTTSSlowly = (RadioButton) findViewById(R.id.rBtn_tts_slowly);
                    mRbTTSStandard = (RadioButton) findViewById(R.id.rBtn_tts_standard);
                    mRbTTSFast = (RadioButton) findViewById(R.id.rBtn_tts_fast);
                    mTvTTSSpeedStatus = (TextView) findViewById(R.id.tv_status_tts_play);
                    initTtsSpeedUIStatus();
                    mRgTTSSpeed.setOnCheckedChangeListener(mRgCheckedChangeListener);


                    break;
                case 2:
                    ((ViewPager) container).addView(mViewList.get(position));

                    // Float mic setting
                    mCBFloatMic = (CheckBox) findViewById(R.id.cb_float_mic);
                    mTvFloatMicStatus = (TextView) findViewById(R.id.tv_status_float_mic);
                    initFloatMicSettingUIStatus();
                    mCBFloatMic.setOnCheckedChangeListener(mCbListener);

                    // 汽车防盗
                    mTxtGpsStatus = (TextView) findViewById(R.id.tv_status_gps);
                    mCbGps = (CheckBox) findViewById(R.id.cb_gps);
                    initGpsStatus();
                    mCbGps.setOnCheckedChangeListener(mCbListener);

                    mCbSub = (CheckBox) findViewById(R.id.cb_sub);
                    mTxtSubStatus = (TextView) findViewById(R.id.tv_status_sub);
                    initSubStatus();
                    mCbSub.setOnCheckedChangeListener(mCbListener);
                    break;
                case 3:
                    if (childCount == 0) {
                        Logger.w(TAG, "!--->position is 2 but childCount is 0");
                        ((ViewPager) container).addView(mViewList.get(position), 0);
                        ((ViewPager) container).addView(mViewList.get(position), 1);
                    } else if (childCount == 1) {
                        Logger.w(TAG, "!--->position is 2 but childCount is 1");
                        ((ViewPager) container).addView(mViewList.get(position), 1);
                    }
                    ((ViewPager) container).addView(mViewList.get(position), 2);

                    mIvtSettingAddrFavorite =
                            (ImageViewWithText) findViewById(R.id.ivt_setting_addr_favorite);
                    mIvtOta = (ImageViewWithText) findViewById(R.id.ivt_setting_ota);
                    mIvtAboutUs = (ImageViewWithText) findViewById(R.id.ivt_setting_about_us);

                    mIvtSettingAddrFavorite.setOnClickListener(mOnClickListener);
                    mIvtOta.setOnClickListener(mOnClickListener);
                    mIvtAboutUs.setOnClickListener(mOnClickListener);
                    break;
            }

            return mViewList.get(position);
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            Logger.d(TAG, "destroyItem-----position = " + position);
            if (mViewList != null && mViewList.size() > 0 && mViewList.size() >= position) {
                ((ViewPager) container).removeView(mViewList.get(position));
            }
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    };

    private void initWakeupUIStatus() {
        boolean isWakeUp = UserPerferenceUtil.isWakeupEnable(mContext);
        Logger.d(TAG, "!--->initWakeupUIStatus---isWakeUp = " + isWakeUp);
        updateWakeupwordTextView(isWakeUp, UserPerferenceUtil.getWakeupWord(mContext));
        mCBWakeup.setChecked(isWakeUp);
    }

    private void initTtsSpeedUIStatus() {
        int ttsSpeed = UserPerferenceUtil.getTTSSpeed(mContext);
        Logger.d(TAG, "!--->initTtsSpeedUIStatus---ttsSpeed = " + ttsSpeed);
        if (UserPerferenceUtil.VALUE_TTS_SPEED_SLOWLY == ttsSpeed) {
            mTvTTSSpeedStatus.setText(R.string.setting_tts_speed_slowly);
            mRbTTSSlowly.setChecked(true);
        } else if (UserPerferenceUtil.VALUE_TTS_SPEED_STANDARD == ttsSpeed) {
            mTvTTSSpeedStatus.setText(R.string.setting_tts_speed_standard);
            mRbTTSStandard.setChecked(true);
        } else if (UserPerferenceUtil.VALUE_TTS_SPEED_FAST == ttsSpeed) {
            mTvTTSSpeedStatus.setText(R.string.setting_tts_speed_fast);
            mRbTTSFast.setChecked(true);
        }
    }

    /**
     * initTtsTimbreUIStatus
     * 
     * @author xiaodong.he
     * @date 2016-1-12
     */
    private void initTtsTimbreUIStatus() {
        int ttsTimbre = UserPerferenceUtil.getTtsTimbre(mContext);
        Logger.d(TAG, "!--->initTtsTimbreUIStatus---ttsTimbre = " + ttsTimbre);
        if (UserPerferenceUtil.VALUE_TTS_TIMBRE_STANDARD == ttsTimbre) {
            mTvTtsTimbreStatus.setText(R.string.setting_tts_timbre_standard);
            mRbTtsTimbreStandard.setChecked(true);
        } else if (UserPerferenceUtil.VALUE_TTS_TIMBRE_SEXY == ttsTimbre) {
            mTvTtsTimbreStatus.setText(R.string.setting_tts_timbre_sexy);
            mRbTtsTimbreSexy.setChecked(true);
        } else if (UserPerferenceUtil.VALUE_TTS_TIMBRE_AUTO == ttsTimbre) {
            mTvTtsTimbreStatus.setText(R.string.setting_tts_timbre_auto);
            mRbTtsTimbreAuto.setChecked(true);
        }


    }

    private void initFloatMicSettingUIStatus() {
        boolean isEnableFloatMic = UserPerferenceUtil.getFloatMicEnable(mContext);
        Logger.d(TAG, "!--->initFloatMicSettingUIStatus---isEnableFloatMic = " + isEnableFloatMic);
        mTvFloatMicStatus.setText(isEnableFloatMic
                ? R.string.setting_float_mic_open
                : R.string.setting_float_mic_closed);
        mCBFloatMic.setChecked(isEnableFloatMic);
    }

    /**
     * initVersionLevelUIStatus
     * 
     * @author xiaodong.he
     * @date 2015-11-23
     */
    private void initVersionLevelUIStatus() {
        int versionLevel = UserPerferenceUtil.getVersionMode(mContext);
        Logger.d(TAG, "!--->initVersionLevelUIStatus---level = " + versionLevel);
        switch (versionLevel) {
            case UserPerferenceUtil.VALUE_VERSION_MODE_EXP:
                mTvVersionModeStatus.setText(R.string.setting_version_mode_exp);
                mRbVersionModeExp.setChecked(true);
                break;
            case UserPerferenceUtil.VALUE_VERSION_MODE_STANDARD:
                mTvVersionModeStatus.setText(R.string.setting_version_mode_standard);
                mRbVersionModeStandrard.setChecked(true);
                break;
            case UserPerferenceUtil.VALUE_VERSION_MODE_HIGH:
                mTvVersionModeStatus.setText(R.string.setting_version_mode_high);
                mRbVersionModeHigh.setChecked(true);
                break;
            default:
                break;
        }
    }

    private void initSubStatus() {
        boolean subEnable = UserPerferenceUtil.getSubEnable(mContext);
        mCbSub.setChecked(subEnable);
        mTxtSubStatus.setText(subEnable
                ? R.string.setting_float_mic_open
                : R.string.setting_float_mic_closed);
    }


    private void initGpsStatus() {
        boolean gpsEnable = UserPerferenceUtil.getGpsEnable(mContext);
        mCbGps.setChecked(gpsEnable);
        mTxtGpsStatus.setText(gpsEnable
                ? R.string.setting_float_mic_open
                : R.string.setting_float_mic_closed);
    }

    private void initAECSettingUIStatus() {
        if (GUIConfig.isSupportAECSetting) {
            mSettingAECLayout.setVisibility(View.VISIBLE);
            boolean isEnableAEC = UserPerferenceUtil.getAECEnable(mContext);
            Logger.d(TAG, "!--->isEnableAEC : " + isEnableAEC);
            mTvAECStatus.setText(isEnableAEC
                    ? R.string.setting_float_mic_open
                    : R.string.setting_float_mic_closed);
            mCBAEC.setChecked(isEnableAEC);
        } else {
            mSettingAECLayout.setVisibility(View.GONE);
        }
    }



    /**
	 * 
	 */
    private android.widget.CompoundButton.OnCheckedChangeListener mCbListener =
            new android.widget.CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    switch (buttonView.getId()) {
                        case R.id.cb_wakeup:
                            Logger.d(TAG, "onCheckedChanged---cb_wakeup--isChecked = " + isChecked);
                            if (isChecked) {
                                updateWakeupwordTextView(true,
                                        UserPerferenceUtil.getWakeupWord(mContext));
                                showHelpTextPopWindow(mContext, R.string.setting_title_wakeup,
                                        R.string.setting_help_text_wakeup,
                                        SettingHelpPopupWindow.TYPE_SWITCH_WAKE_UP);
                            } else {
                                updateWakeupwordTextView(false, "");
                            }
                            UserPerferenceUtil.setWakeupEnable(mContext, isChecked);
                            Logger.d(TAG, "onCheckedChanged--cb_wakeup-----End.");
                            break;
                        case R.id.cb_float_mic:
                            Logger.d(TAG, "!--->cb_float_mic isChecked = " + isChecked);
                            if (isChecked) {
                                mTvFloatMicStatus.setText(R.string.setting_float_mic_open);

                                showHelpTextPopWindow(mContext, R.string.setting_title_float_mic,
                                        R.string.setting_help_text_float_mic,
                                        SettingHelpPopupWindow.TYPE_SWITCH_FLOAT_MIC);
                                TTSUtil.playTTSWakeUp(mContext, TTSUtil.TTS_SETTING_FLOAT_MIC_OPEN);
                            } else {
                                mTvFloatMicStatus.setText(R.string.setting_float_mic_closed);
                                TTSUtil.playTTSWakeUp(mContext, TTSUtil.TTS_SETTING_FLOAT_MIC_CLOSE);
                            }
                            UserPerferenceUtil.setFloatMicEnable(mContext, isChecked);
                            break;
                        case R.id.cb_oneshot:
                            Logger.d(TAG, "!--->cb_oneshot isChecked = " + isChecked);
                            if (isChecked) {
                                mTvVersionModeStatus.setText(R.string.setting_float_mic_open);

                                String helpText =
                                        getString(R.string.setting_help_text_oneshot,
                                                UserPerferenceUtil.getWakeupWord(mContext));
                                showHelpTextPopWindow(mContext,
                                        getString(R.string.setting_title_oneshot), helpText,
                                        SettingHelpPopupWindow.TYPE_SWITCH_ONESHOT);
                            } else {
                                mTvVersionModeStatus.setText(R.string.setting_float_mic_closed);
                            }
                            UserPerferenceUtil.setOneShotEnable(mContext, isChecked);
                            GuiSettingUpdateUtil.sendOneShotConfigure(mContext);
                            break;
                        case R.id.cb_aec:
                            Logger.d(TAG, "!--->cb_aec isChecked = " + isChecked);
                            if (isChecked) {
                                mTvAECStatus.setText(R.string.setting_float_mic_open);

                                showHelpTextPopWindow(mContext, R.string.setting_title_aec,
                                        R.string.setting_help_text_aec,
                                        SettingHelpPopupWindow.TYPE_SWITCH_AEC);
                            } else {
                                mTvAECStatus.setText(R.string.setting_float_mic_closed);
                            }
                            UserPerferenceUtil.setAECEnable(mContext, isChecked);
                            GuiSettingUpdateUtil.sendAECConfigure(mContext);
                            break;
                        case R.id.cb_sub:
                            Logger.d(TAG, "!--->cb_sub isChecked = " + isChecked);
                            if (isChecked) {
                                mTxtSubStatus.setText(R.string.setting_float_mic_open);
                                TTSUtil.playTTSWakeUp(mContext, TTSUtil.TTS_SETTING_SUB_OPEN);
                            } else {
                                mTxtSubStatus.setText(R.string.setting_float_mic_closed);
                                TTSUtil.playTTSWakeUp(mContext, TTSUtil.TTS_SETTING_SUB_CLOSE);
                            }
                            UserPerferenceUtil.setSubEnable(mContext, isChecked);
                            break;
                        case R.id.cb_gps:
                            Logger.d(TAG, "!--->cb_gps isChecked = " + isChecked);
                            if (isChecked) {
                                mTxtGpsStatus.setText(R.string.setting_float_mic_open);
                                TTSUtil.playTTSWakeUp(mContext, TTSUtil.TTS_SETTING_GPS_OPEN);
                            } else {
                                mTxtGpsStatus.setText(R.string.setting_float_mic_closed);
                                TTSUtil.playTTSWakeUp(mContext, TTSUtil.TTS_SETTING_GPS_CLOSE);
                            }
                            UserPerferenceUtil.setGpsEnable(mContext, isChecked);
                            GuiSettingUpdateUtil.sendGpsConfigure(mContext);
                            break;
                        default:
                            break;
                    }
                }
            };

    /**
	 * 
	 */
    private android.widget.RadioGroup.OnCheckedChangeListener mRgCheckedChangeListener =
            new android.widget.RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    Logger.d(TAG,
                            "!--->---mRgCheckedChangeListener---onCheckedChanged----checkedId = "
                                    + checkedId);
                    switch (checkedId) {
                        case R.id.rBtn_map_gaode:
                            Logger.d(TAG, "!--->map gaode---");
                            mTvMapChooseStatus.setText(R.string.setting_map_choose_gaode);
                            UserPerferenceUtil.setMapChoose(mContext,
                                    UserPerferenceUtil.VALUE_MAP_AMAP);
                            TTSUtil.playTTSWakeUp(mContext, TTSUtil.TTS_SETTING_MAP_AMAP);
                            break;
                        case R.id.rBtn_map_baidu:
                            Logger.d(TAG, "!--->map baidu----");
                            mTvMapChooseStatus.setText(R.string.setting_map_choose_baidu);
                            UserPerferenceUtil.setMapChoose(mContext,
                                    UserPerferenceUtil.VALUE_MAP_BAIDU);
                            TTSUtil.playTTSWakeUp(mContext, TTSUtil.TTS_SETTING_MAP_BAIDU);
                            break;
                        case R.id.rBtn_map_more:
                            Logger.d(TAG,
                                    "!--->mRgCheckedChangeListener---rBtn_map_more--do nothing--");
                            break;
                        case R.id.rBtn_tts_slowly:
                            Logger.d(TAG, "!--->tts slowly----");
                            mTvTTSSpeedStatus.setText(R.string.setting_tts_speed_slowly);
                            UserPerferenceUtil.setTTSSpeed(mContext,
                                    UserPerferenceUtil.VALUE_TTS_SPEED_SLOWLY);
                            GuiSettingUpdateUtil.sendTTSSpeedConfigure(mContext);
                            break;
                        case R.id.rBtn_tts_standard:
                            Logger.d(TAG, "!--->tts standard----");
                            mTvTTSSpeedStatus.setText(R.string.setting_tts_speed_standard);
                            UserPerferenceUtil.setTTSSpeed(mContext,
                                    UserPerferenceUtil.VALUE_TTS_SPEED_STANDARD);
                            GuiSettingUpdateUtil.sendTTSSpeedConfigure(mContext);
                            break;
                        case R.id.rBtn_tts_fast:
                            Logger.d(TAG, "!--->tts fast----");
                            mTvTTSSpeedStatus.setText(R.string.setting_tts_speed_fast);
                            UserPerferenceUtil.setTTSSpeed(mContext,
                                    UserPerferenceUtil.VALUE_TTS_SPEED_FAST);
                            GuiSettingUpdateUtil.sendTTSSpeedConfigure(mContext);
                            break;
                        case R.id.rBtn_version_mode_1:
                            Logger.d(TAG, "!--->version mode 1----");
                            UserPerferenceUtil.setVersionMode(mContext,
                                    UserPerferenceUtil.VALUE_VERSION_MODE_EXP);
                            mTvVersionModeStatus.setText(R.string.setting_version_mode_exp);
                            GuiSettingUpdateUtil.sendVersionLevelConfigure(mContext,
                                    SessionPreference.PARAM_VERSION_LEVEL_TYPE_IDEL);
                            showHelpTextPopWindow(mContext,
                                    R.string.setting_help_title_version_mode_1,
                                    R.string.setting_help_text_version_mode_1,
                                    SettingHelpPopupWindow.TYPE_SWITCH_VERSION_MODE);
                            break;
                        case R.id.rBtn_version_mode_2:
                            Logger.d(TAG, "!--->version mode 2----");
                            UserPerferenceUtil.setVersionMode(mContext,
                                    UserPerferenceUtil.VALUE_VERSION_MODE_STANDARD);
                            mTvVersionModeStatus.setText(R.string.setting_version_mode_standard);
                            GuiSettingUpdateUtil.sendVersionLevelConfigure(mContext,
                                    SessionPreference.PARAM_VERSION_LEVEL_TYPE_IDEL);
                            showHelpTextPopWindow(mContext,
                                    R.string.setting_help_title_version_mode_2,
                                    R.string.setting_help_text_version_mode_2,
                                    SettingHelpPopupWindow.TYPE_SWITCH_VERSION_MODE);
                            break;
                        case R.id.rBtn_version_mode_3:
                            Logger.d(TAG, "!--->version mode 3----");
                            UserPerferenceUtil.setVersionMode(mContext,
                                    UserPerferenceUtil.VALUE_VERSION_MODE_HIGH);
                            mTvVersionModeStatus.setText(R.string.setting_version_mode_high);
                            GuiSettingUpdateUtil.sendVersionLevelConfigure(mContext,
                                    SessionPreference.PARAM_VERSION_LEVEL_TYPE_IDEL);
                            String helpText =
                                    getString(R.string.setting_help_text_version_mode_3,
                                            UserPerferenceUtil.getWakeupWord(mContext));
                            showHelpTextPopWindow(mContext,
                                    getString(R.string.setting_help_title_version_mode_3),
                                    helpText, SettingHelpPopupWindow.TYPE_SWITCH_VERSION_MODE);
                            break;
                        case R.id.rBtn_tts_timbre_standard:
                            Logger.d(TAG, "!--->click tts_timbre_standard----");
                            mLastTtsTimbre = UserPerferenceUtil.getTtsTimbre(mContext);
                            if (isNeedShowLoadingTtsTimbreView(SessionPreference.PARAM_TTS_TIMBRE_STARNAND)) {
                                showSettingLoadingPopWindow(mContext);
                            }
                            UserPerferenceUtil.setTtsTimbre(mContext,
                                    UserPerferenceUtil.VALUE_TTS_TIMBRE_STANDARD);
                            mTvTtsTimbreStatus.setText(R.string.setting_tts_timbre_standard);
                            GuiSettingUpdateUtil.sendTTSTimbreConfigure(mContext,
                                    SessionPreference.SWITCH_TTS_MODLE_HAND);
                            break;
                        case R.id.rBtn_tts_timbre_sexy:
                            Logger.d(TAG, "!--->click tts_timbre_sexy----");
                            mLastTtsTimbre = UserPerferenceUtil.getTtsTimbre(mContext);
                            if (isNeedShowLoadingTtsTimbreView(SessionPreference.PARAM_TTS_TIMBRE_SEXY)) {
                                showSettingLoadingPopWindow(mContext);
                            }
                            UserPerferenceUtil.setTtsTimbre(mContext,
                                    UserPerferenceUtil.VALUE_TTS_TIMBRE_SEXY);
                            mTvTtsTimbreStatus.setText(R.string.setting_tts_timbre_sexy);
                            GuiSettingUpdateUtil.sendTTSTimbreConfigure(mContext,
                                    SessionPreference.SWITCH_TTS_MODLE_HAND);
                            break;
                        case R.id.rBtn_tts_timbre_auto:
                            Logger.d(TAG, "!--->click tts_auto----");
                            mLastTtsTimbre = UserPerferenceUtil.getTtsTimbre(mContext);
                            /*
                             * if
                             * (isNeedShowLoadingTtsTimbreView(SessionPreference.PARAM_TTS_TIMBRE_SEXY
                             * )) { showSettingLoadingPopWindow(mContext); }
                             */
                            UserPerferenceUtil.setTtsTimbre(mContext,
                                    UserPerferenceUtil.VALUE_TTS_TIMBRE_AUTO);
                            mTvTtsTimbreStatus.setText(R.string.setting_tts_timbre_auto);
                            GuiSettingUpdateUtil.sendTTSTimbreConfigure(mContext,
                                    SessionPreference.SWITCH_TTS_MODLE_HAND);
                            break;
                        default:
                            break;
                    }
                }
            };

    private ISettingLoadingPopListener mSettingLoadingPopListener =
            new ISettingLoadingPopListener() {
                @Override
                public void onCancelClick() {
                    Logger.d(TAG, "onCancelClick--cancel switch");
                    UserPerferenceUtil.setTtsTimbre(mContext, mLastTtsTimbre);
                    initTtsTimbreUIStatus();
                }
            };

    private OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Logger.d(TAG, "!--->onClick = " + v.getId());
            switch (v.getId()) {
                case R.id.ivt_setting_addr_favorite:
                    Intent addrFavIntent =
                            new Intent(SettingsViewPagerActivity.this,
                                    AddressFavoriteActivity.class);
                    startActivity(addrFavIntent);
                    break;
                case R.id.ivt_setting_ota:
                    Logger.d(TAG, "!--->---click OTA----");
                    Intent OTAIntent =
                            new Intent(SettingsViewPagerActivity.this, OTAActivity.class);
                    startActivity(OTAIntent);
                    break;
                case R.id.ivt_setting_about_us:
                    Logger.d(TAG, "!--->---click About us----");
                    Intent intent = new Intent(SettingsViewPagerActivity.this, AboutActivity.class);
                    startActivity(intent);
                    break;
                case R.id.rBtn_map_more:
                    Logger.d(TAG, "!--->---mOnClickListener--click rBtn_map_more----");
                    Intent intentMore =
                            new Intent(SettingsViewPagerActivity.this,
                                    SettingMapViewPagerActivity.class);
                    startActivityForResult(intentMore, GUIConfig.ACTIVITY_REQUEST_CODE_CHOOSE_MAP);
                    break;
                case R.id.tv_status_wakeup_open:
                    Logger.d(TAG, "!--->---click tv_status_wakeup----");
                    showChangeTextPopWindow(mContext);
                    break;
                case R.id.iv_setting_edit_wakeupword:
                    Logger.d(TAG, "!--->---click edit_wakeupword----");
                    showChangeTextPopWindow(mContext);
                    break;
                default:
                    break;
            }
        }
    };

    private void initMapUIStatus() {
        int mapType = UserPerferenceUtil.getMapChoose(mContext);
        Logger.d(TAG, "!--->initMapUIStatus---mapType = " + mapType);
        if (null == mTvMapChooseStatus) {
            Logger.d(TAG, "!--->mTvMapChooseStatus is null, No need update Map UI Status.");
            return;
        }

        switch (mapType) {
            case UserPerferenceUtil.VALUE_MAP_AMAP:
                mTvMapChooseStatus.setText(R.string.setting_map_choose_gaode);
                mRbMapGaode.setChecked(true);
                break;
            case UserPerferenceUtil.VALUE_MAP_BAIDU:
                mTvMapChooseStatus.setText(R.string.setting_map_choose_baidu);
                mRbMapBaidu.setChecked(true);
                break;
            case UserPerferenceUtil.VALUE_MAP_TUBA:
                mTvMapChooseStatus.setText(R.string.setting_map_choose_tuba);
                mBbMapMore.setChecked(true);
                break;
            case UserPerferenceUtil.VALUE_MAP_DAODAOTONG:
                mTvMapChooseStatus.setText(R.string.setting_map_choose_daodaotong);
                mBbMapMore.setChecked(true);
                break;
            default:
                break;
        }

    }

    private EditWakeupWordPopWindow pop;

    /* < xiaodong.he 20151028 added for wakeUp word change Begin */
    /**
     * show Edit Wakeupword PopWindow
     * 
     * @author xiaodong.he
     * @date 2015-11-01
     * @param context
     */
    private void showChangeTextPopWindow(Context context) {
        Logger.d(TAG, "showEditWakeupwordPopWindow----");
        pop = new EditWakeupWordPopWindow(context);
        pop.showPopWindow(mViewPager);
    }

    private OnSharedPreferenceChangeListener mPreferenceChangeListener =
            new OnSharedPreferenceChangeListener() {

                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                        String key) {
                    Logger.d(TAG, "!--->onSharedPreferenceChanged: key " + key);
                    if (UserPerferenceUtil.KEY_WAKEUP_WORDS.equals(key)) {
                        String wakeupWord = UserPerferenceUtil.getWakeupWord(mContext);
                        boolean isWakeUpOpen = UserPerferenceUtil.isWakeupEnable(mContext);
                        if (isWakeUpOpen) {
                            updateWakeupwordTextView(isWakeUpOpen, wakeupWord);
                        }
                        // if (null != mTvOneshotTelpText) {
                        // mTvOneshotTelpText.setText(getString(
                        // R.string.setting_title_oneshot_hint, wakeupWord));
                        // }
                    } else if (UserPerferenceUtil.KEY_ENABLE_WAKEUP.equals(key)) {
                        // XD added 20151029
                        GuiSettingUpdateUtil.sendWakeupConfigure(mContext);
                    } else if (UserPerferenceUtil.KEY_TTS_TIMBRE.equals(key)) {
                        initTtsTimbreUIStatus();
                    } else if (UserPerferenceUtil.KEY_VERSION_MODE.equals(key)) {
                        initVersionLevelUIStatus();
                    }
                }
            };

    /**
     * 
     * @param isWakeupOpen
     * @param wakeupWord: if WakeUp is Open show wakeupWord
     */
    private void updateWakeupwordTextView(boolean isWakeupOpen, String wakeupWord) {
        if (null == mTvWakeupStatusOpen) {
            return;
        }
        boolean isConnected = Network.isNetworkConnected(mContext);
        Logger.d(TAG, "updateWakeupwordTextView--isWakeupOpen = " + isWakeupOpen + "; showText = "
                + wakeupWord + "; isConnected = " + isConnected);
        if (isWakeupOpen) {
            wakeupWord = FunctionHelpUtil.addDoubleQuotationMarks(wakeupWord);
            mTvWakeupStatusOpen.setText(wakeupWord);

            mTvWakeupStatusClose.setVisibility(View.GONE);
            mTvWakeupStatusOpen.setVisibility(View.VISIBLE);
            if (GUIConfig.isSupportUpdateWakeupWordSetting && isConnected) {
                // mTvWakeupStatusOpen.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
                // mTvWakeupStatusOpen.getPaint().setAntiAlias(true);
                mTvWakeupStatusOpen.setOnClickListener(mOnClickListener);
                mIvEditWakeupword.setOnClickListener(mOnClickListener);
                mIvEditWakeupword.setVisibility(View.VISIBLE);
            } else {
                mTvWakeupStatusOpen.setOnClickListener(null);
                mIvEditWakeupword.setOnClickListener(null);
                mIvEditWakeupword.setVisibility(View.GONE);
            }
        } else {
            mTvWakeupStatusOpen.setVisibility(View.GONE);
            mIvEditWakeupword.setVisibility(View.GONE);
            mTvWakeupStatusClose.setVisibility(View.VISIBLE);
            mTvWakeupStatusClose.setText(R.string.setting_wakeup_status_closed);
        }
    }

    /* xiaodong.he 20151028 added for wakeUp word change End > */

    /**
     * showHelpTextPopWindow
     * 
     * @author xiaodong.he
     * @date 2015-11-10
     * @param context
     * @param titleRes
     * @param contentRes
     * @param type
     */
    private void showHelpTextPopWindow(Context context, String titleRes, String contentRes, int type) {
        showHelpTextPopWindow(context, titleRes, contentRes, type, null);
    }

    /**
     * showHelpTextPopWindow
     * 
     * @author xiaodong.he
     * @date 2015-11-10
     * @param context
     * @param titleRes
     * @param contentRes
     * @param type
     */
    private void showHelpTextPopWindow(Context context, int titleRes, int contentRes, int type) {
        showHelpTextPopWindow(context, context.getResources().getString(titleRes), context
                .getResources().getString(contentRes), type, null);
    }

    /**
     * showHelpTextPopWindow
     * 
     * @author xiaodong.he
     * @date 2016-01-18
     * 
     * @param context
     * @param titleRes
     * @param contentRes
     * @param type
     * @param listener
     */
    private void showHelpTextPopWindow(Context context, String titleRes, String contentRes,
            int type, ISettingHelpPopListener listener) {
        Logger.d(TAG, "showHelpTextPopWindow-----type = " + type);
        if (null != mSettingLoadingPop) {
            Logger.d(TAG, "showHelpTextPopWindow dismiss LoadingPop");
            mSettingLoadingPop.dismiss();
        }
        if (mSettingHelpPop != null && mSettingHelpPop.isShowing()) {
            mSettingHelpPop.dismiss();
        }
        mSettingHelpPop = new SettingHelpPopupWindow(context);
        mSettingHelpPop.setTitle(titleRes);
        mSettingHelpPop.setContent(contentRes);
        mSettingHelpPop.showPopWindow(mViewPager);

        mSettingHelpPop.setType(type);
        mSettingHelpPop.setSettingHelpPopListener(listener);
    }


    /**
     * showSettingLoadingPopWindow
     * 
     * @author xiaodong.he
     * @date 2016-10-12
     * @param context
     */
    private void showSettingLoadingPopWindow(Context context) {
        Logger.d(TAG, "showSettingLoadingPopWindow");
        if (null != mSettingLoadingPop && mSettingLoadingPop.isShowing()) {
            mSettingLoadingPop.dismiss();
        }
        mSettingLoadingPop = new SettingLoadingPopupWindow(context);
        mSettingLoadingPop.setSettingLoadingPopListener(mSettingLoadingPopListener);
        mSettingLoadingPop.showPopWindow(mViewPager);
    }

    /**
     * 
     */
    private void dismissSettingLoadingPopWindow() {
        if (null != mSettingLoadingPop) {
            Logger.d(TAG, "dismissSettingLoadingPopWindow");
            mSettingLoadingPop.dismiss();
            mSettingLoadingPop = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d(TAG, "!--->onActivityResult-----resultCode = " + resultCode);
        if (resultCode == GUIConfig.ACTIVITY_RESULT_CODE_SETTING_MAP_FINISH) {
            // initMapUIStatus(); //do it onResume()
        }
    }

    /**
     * registReceiver
     * 
     * @author xiaodong.he
     * @date 2015-12-3
     */
    private void registReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(GuiSettingUpdateUtil.ACTION_UPDATE_VERSION_MODE);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(GuiSettingUpdateUtil.ACTION_ON_GET_TTS_TIMBRE_LIST);
        filter.addAction(GuiSettingUpdateUtil.ACTION_ON_SWITCH_TTS_TIMBRE_DONE);
        filter.addAction(GuiSettingUpdateUtil.ACTION_ON_COPY_TTS_TIMBRE_FAIL);
        registerReceiver(mReceiver, filter);

    }

    /**
     * unRegistReceiver
     * 
     * @author xiaodong.he
     * @date 2015-12-3
     */
    private void unRegistReceiver() {
        unregisterReceiver(mReceiver);
        unregisterReceiver(receiver);
    }

    /**
     * 
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (GuiSettingUpdateUtil.ACTION_UPDATE_VERSION_MODE.equals(action)) {
                Logger.d(TAG, "mReceiver--ACTION_UPDATE_VERSION_MODE");
                if (null != mRgVersionMode) {
                    mRgVersionMode.setOnCheckedChangeListener(null);
                    initVersionLevelUIStatus();
                    mRgVersionMode.setOnCheckedChangeListener(mRgCheckedChangeListener);
                }
            } else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
                boolean isConnected = Network.isNetworkConnected(mContext);
                boolean isWakeUpOpen = UserPerferenceUtil.isWakeupEnable(mContext);
                Logger.d(TAG, "!--->mReceiver--CONNECTIVITY_ACTION--isConnected:" + isConnected
                        + "; isWakeUpOpen = " + isWakeUpOpen);
                if (isWakeUpOpen) {
                    updateWakeupwordTextView(isWakeUpOpen,
                            UserPerferenceUtil.getWakeupWord(mContext));
                }
            } else if (GuiSettingUpdateUtil.ACTION_ON_GET_TTS_TIMBRE_LIST.equals(action)) {
                String json = intent.getStringExtra(GuiSettingUpdateUtil.KEY_TTS_TIMBRE_LIST);
                Logger.d(TAG, "mReceiver--GET_TTS_TIMBRE_LIST--json:" + json);
                JSONObject typeObj = JsonTool.parseToJSONObject(json);
                onGetTTSTimbreList(typeObj);
            } else if (GuiSettingUpdateUtil.ACTION_ON_SWITCH_TTS_TIMBRE_DONE.equals(action)) {
                String ttsTimBre = intent.getStringExtra(GuiSettingUpdateUtil.KEY_TTS_TIMBRE_NAME);
                Logger.d(TAG, "mReceiver--ON_SWITCH_TTS_TIMBRE_DONE--ttsTimbre:" + ttsTimBre);
                dismissSettingLoadingPopWindow();
                // GuiSettingUpdateUtil.sendRequestTtsTimbreList(mContext);
            } else if (GuiSettingUpdateUtil.ACTION_ON_COPY_TTS_TIMBRE_FAIL.equals(action)) {
                String ttsTimbre = intent.getStringExtra(GuiSettingUpdateUtil.KEY_TTS_TIMBRE_NAME);
                String failcause = intent.getStringExtra(GuiSettingUpdateUtil.KEY_TTS_FAILCAUSE);
                Logger.w(TAG, "mReceiver--ACTION_ON_COPY_TTS_MODEL_FAIL--ttsTimbre:" + ttsTimbre
                        + "; failcause:" + failcause);
                onCopyTTSTimbreFail(ttsTimbre, failcause);
            }
        }
    };

    /**
     * 
     * @date 2016-1-18
     * @author xiaodong.he
     * @param typeObj
     */
    private void onGetTTSTimbreList(JSONObject typeObj) {
        if (null == typeObj) {
            return;
        }
        String act = JsonTool.getJsonValue(typeObj, SessionPreference.KEY_TTS_MODEL_LIST_ACTION);
        mTtsTimbreList = JsonTool.getJsonArray(typeObj, SessionPreference.KEY_TTS_MODEL_LIST);
        if (SessionPreference.VALUE_TTS_MODEL_LIST_ACTION_UPDATE.equals(act)) {
            String updateModel =
                    JsonTool.getJsonValue(typeObj, SessionPreference.KEY_TTS_UPDATE_MODEL_NAME);
            Logger.d(TAG, "mReceiver--GET_TTS_TIMBRE_LIST--updateModel:" + updateModel);
            // TODO:
        } else if (SessionPreference.VALUE_TTS_MODEL_LIST_ACTION_REQUEST.equals(act)) {}
    }

    /**
     * 
     * @date 2016-1-18
     * @author xiaodong.he
     * @param ttsTimbre
     * @param failcause
     */
    private void onCopyTTSTimbreFail(String ttsTimbre, String failcause) {
        if (SessionPreference.VALUE_TTS_FAILCAUSE_UNAVAILABLE_SDCARD.equals(failcause)
                || SessionPreference.VALUE_TTS_FAILCAUSE_UNMOUNT_SDCARD.equals(failcause)) {
            Logger.w(TAG, "can't switch TTS for internal sdcard space is insufficient");
            showHelpTextPopWindow(
                    mContext,
                    getString(R.string.error_title_internal_memory_insufficient),
                    getString(R.string.error_msg_sdcard_space_insufficient_switch_tts,
                            GUIConfig.MIN_SIZE_INTERNAL_MEMORY_AVAILABLE),
                    SettingHelpPopupWindow.TYPE_SWITCH_TTS_TIMBRE, mSettingHelpPopListener);
        } else if (SessionPreference.VALUE_TTS_FAILCAUSE_UNAVAILABLE_FILE.equals(failcause)) {
            showHelpTextPopWindow(mContext, getString(R.string.error_title_write_tts_fail),
                    getString(R.string.error_msg_write_tts_fail),
                    SettingHelpPopupWindow.TYPE_SWITCH_TTS_TIMBRE, mSettingHelpPopListener);
        }
    }

    /**
     * 
     * @date 2016-1-18
     * @author xiaodong.he
     */
    private ISettingHelpPopListener mSettingHelpPopListener = new ISettingHelpPopListener() {

        @Override
        public void onIKonwClick() {
            doOnIKonwClick();
        }

        @Override
        public void onBackClick() {
            Logger.d(TAG, "onBackClick...");
            doOnIKonwClick();
        }

    };

    private void doOnIKonwClick() {
        int type = -1;
        if (mSettingHelpPop != null) {
            type = mSettingHelpPop.getType();
        }
        Logger.d(TAG, "doOnIKonwClick--pop type:" + type);
        if (type == SettingHelpPopupWindow.TYPE_SWITCH_TTS_TIMBRE) {
            UserPerferenceUtil.setTtsTimbre(mContext, mLastTtsTimbre);
            initTtsTimbreUIStatus();
        }
    }


    /**
     * is Need Show Loading TTS Timbre(model) View
     * 
     * @author xiaodong.he
     * @date 2016-1-18
     * 
     * @param ttsModelName
     * @return
     */
    private boolean isNeedShowLoadingTtsTimbreView(String ttsModelName) {
        if (TextUtils.isEmpty(ttsModelName)) {
            return false;
        }
        if (null == mTtsTimbreList) {
            Logger.w(TAG, "isNeedShowLoadingTtsTimbreView---mTtsTimbreList is null.");
            GuiSettingUpdateUtil.sendRequestTtsTimbreList(mContext);
            return false;
        }
        for (int i = 0; i < mTtsTimbreList.length(); i++) {
            try {
                JSONObject item = (JSONObject) mTtsTimbreList.get(i);
                String modelName = item.getString(SessionPreference.KEY_TTS_MODEL_NAME);
                int isEnable = item.getInt(SessionPreference.KEY_TTS_MODEL_IS_ENABLE);
                // int isLocal = item.getInt("isLocal");
                if (ttsModelName.equals(modelName) && isEnable == 0) {
                    Logger.d(TAG, "isNeedShowLoadingTtsTimbreView--true--modelName = " + modelName);
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.d(TAG, "onPause---");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.d(TAG, "onDestroy---");
        UserPerferenceUtil.unregisterOnSharedPreferenceChangeListener(mContext,
                mPreferenceChangeListener);
        unRegistReceiver();
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (pop != null) {
                pop.dismiss();
            }
        }

    };

}
