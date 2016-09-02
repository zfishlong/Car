/**
 * Copyright (c) 2012-2015 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : WelcomeActivity.java
 * @ProjectName : uniCarGUI
 * @PakageName : com.unisound.unicar.gui.ui
 * @version : 1.0
 * @Author : Xiaodong.He
 * @CreateDate : 2015-06-17
 */
package com.unisound.unicar.gui.ui;

import java.io.File;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.unisound.unicar.gui.R;
import com.unisound.unicar.gui.application.CrashApplication;
import com.unisound.unicar.gui.preference.SharedPreference;
import com.unisound.unicar.gui.pushserver.PushMediaPlayer;
import com.unisound.unicar.gui.subscribebean.WelcomeData;
import com.unisound.unicar.gui.utils.DeviceTool;
import com.unisound.unicar.gui.utils.FileHelper;
import com.unisound.unicar.gui.utils.Logger;
import com.unisound.unicar.gui.utils.PackageUtil;
import com.unisound.unicar.gui.utils.Util;
import com.unisound.unicar.gui.view.GlideImageView;

/**
 *
 * @author xiaodong
 * @date 20150617
 */
public class WelcomeActivity extends Activity {

    private static final String TAG = "WelcomeActivity";

    public static final String ACTION_FINISH_WELCOMEACTIVITY =
            "com.unisound.unicar.gui.ACTION_FINISH_WELCOMEACTIVITY";

    private TextView tvVersion;
    private LinearLayout layout_nomal;
    private RelativeLayout layout_push;
    private WelcomeData welcomeData;
    private boolean canFinish = false;
    private boolean receiveFinish = false;
    private boolean showPushWelcome = false;
    private Button but_jump;
    private GlideImageView img_push;
    private PushMediaPlayer pushMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        pushMediaPlayer = new PushMediaPlayer(CrashApplication.getAppContext());
        tvVersion = (TextView) findViewById(R.id.tv_version);
        layout_nomal = (LinearLayout) findViewById(R.id.layout_nomal);
        layout_push = (RelativeLayout) findViewById(R.id.layout_push);
        img_push = (GlideImageView) findViewById(R.id.img_push);
        but_jump = (Button) findViewById(R.id.but_jump);


        tvVersion.setText("V" + DeviceTool.getAppVersionName(this));   //获取版本号

        IntentFilter filter = new IntentFilter(ACTION_FINISH_WELCOMEACTIVITY);  //注册广播接收者
        registerReceiver(mFinishReceiver, filter);
        Logger.d(TAG, "!--->onCreate()-------registerReceiver mFinishReceiver");


        String welcomeCache = SharedPreference.getInstance().getData(SharedPreference.WelcomeData);
        if (TextUtils.isEmpty(welcomeCache)) {
            layout_nomal.setVisibility(View.VISIBLE);
            layout_push.setVisibility(View.GONE);
        } else {
            //使用的是json
            welcomeData = new Gson().fromJson(welcomeCache, WelcomeData.class);
            if (welcomeData.needShow()) {
                showPushWelcome();
            } else {
                layout_nomal.setVisibility(View.VISIBLE);
                layout_push.setVisibility(View.GONE);
            }
        }
        // startWindowService(); //xd delete 20150706
    }

    @SuppressLint("NewApi")
    private void showPushWelcome() {
        showPushWelcome = true;
        layout_nomal.setVisibility(View.GONE);
        layout_push.setVisibility(View.VISIBLE);

        String imgUrl = welcomeData.getImgUrl();
        String imgName = Util.stringToMD5(imgUrl) + imgUrl.substring(imgUrl.lastIndexOf("."));
        String imgPath = Util.getWelcomeCachePath(CrashApplication.getAppContext());
        File imgFile = new File(imgPath, imgName);
        if (imgFile.exists()) {
            img_push.setImageFile(imgFile);
        }

        but_jump.setEnabled(false);
        but_jump.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                WelcomeActivity.this.finish();
            }
        });
        String catchPath = null;
        File file = null;
        if (!TextUtils.isEmpty(welcomeData.getMusicUrl())) {
            String musicUrl = welcomeData.getMusicUrl();
            String name =
                    Util.stringToMD5(musicUrl) + musicUrl.substring(musicUrl.lastIndexOf("."));
            catchPath = Util.getWelcomeCachePath(CrashApplication.getAppContext());
            file = new File(catchPath, name);
        }
        if (file == null || (!file.exists())) {
            final int seconds = 2;
            ValueAnimator valueAnimator = ValueAnimator.ofInt(seconds);
            valueAnimator.setDuration(seconds * 1000);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (Integer) animation.getAnimatedValue();
                    int restSeconds = seconds - value;
                    but_jump.setText(getString(R.string.jump) + "(" + restSeconds + ")");
                    if (restSeconds == 0) {
                        if (receiveFinish) {
                            WelcomeActivity.this.finish();
                        } else {
                            canFinish = true;
                        }
                    }
                }
            });
            valueAnimator.start();
        } else {
            final int seconds = FileHelper.getWavTimeLong(file.getAbsolutePath());
            ValueAnimator valueAnimator = ValueAnimator.ofInt(seconds);
            valueAnimator.setDuration(seconds * 1000);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (Integer) animation.getAnimatedValue();
                    int restSeconds = seconds - value;
                    but_jump.setText(getString(R.string.jump) + "(" + restSeconds + ")");
                    if (restSeconds == 0) {
                        if (receiveFinish) {
                            WelcomeActivity.this.finish();
                        } else {
                            canFinish = true;
                        }
                    }
                }
            });
            valueAnimator.start();
            pushMediaPlayer.play(file.getPath(), null);
        }
    }

    /**
     * 
     */
    private BroadcastReceiver mFinishReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Logger.d(TAG, "!--->mFinishReceiver action = " + action);
            if (ACTION_FINISH_WELCOMEACTIVITY.equals(action)) {
                // startMainActivity(); //xd delete 20150706
                if (showPushWelcome) {
                    Logger.d(TAG, "showPushWelcome true canFinish =  " + canFinish);
                    if (canFinish) {
                        WelcomeActivity.this.finish();
                        return;
                    }
                    but_jump.setEnabled(true);
                    receiveFinish = true;
                } else {
                    WelcomeActivity.this.finish();
                }
            }
        }
    };

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

//    private void startMainActivity() {
//        Logger.d(TAG, "!--->---startMainActivity()-----");
//        Intent intent = new Intent(this, GUIMainActivity.class);
//        intent.setAction(WindowService.ACTION_START_REQUEST_MAKE_FINISHED);
//        startActivity(intent);
//    }
//
//    private void startWindowService() {
//        Logger.d(TAG, "!--->---startWindowService()-----");
//        Intent i = new Intent(this, WindowService.class);
//        i.setAction(WindowService.ACTION_START_REQUEST_MAKE_FINISHED);
//        startService(i);
//    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        Logger.d(TAG, "onResume------");
        if (isNeedOTA()) {
            doOTA();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        Logger.d(TAG, "onPause---");
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        sendMusicDone();
        Logger.d(TAG, "onDestroy---");
        unregisterReceiver(mFinishReceiver);
        if (pushMediaPlayer != null) {
            pushMediaPlayer.release();
        }
    }

    private void sendMusicDone() {
        Logger.d(TAG, "!--->musicDone----");
        Intent intent = new Intent(this, WindowService.class);
        intent.setAction(MessageReceiver.ACTION_MUSIC_DONE);
        startService(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // BACK
            if (receiveFinish) {
                WelcomeActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static boolean isInWelcomeActivity() {
        String topActivityClassName =
                PackageUtil.getTopActivityName(CrashApplication.getAppContext());
        Logger.d(TAG, "activityName = " + topActivityClassName);
        Logger.d(TAG, "myName = " + WelcomeActivity.class.getName());
        if (topActivityClassName != null
                && topActivityClassName.equals(WelcomeActivity.class.getName())) {
            return true;
        } else {
            return false;
        }
    }
}
