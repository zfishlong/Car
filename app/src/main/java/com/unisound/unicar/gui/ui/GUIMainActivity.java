/**
 * Copyright (c) 2012-2015 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 *
 * @FileName : GUIMainActivity.java
 * @ProjectName : uniCarGUI
 * @PakageName : com.unisound.unicar.gui.ui
 * @version : 2.0
 * @Author : Xiaodong.He
 * @CreateDate : 2015-06-08
 * @ModifyDate : 2015-12-04
 */
package com.unisound.unicar.gui.ui;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unisound.unicar.gui.R;
import com.unisound.unicar.gui.model.TrackInfo;
import com.unisound.unicar.gui.preference.CommandPreference;
import com.unisound.unicar.gui.preference.UserPerferenceUtil;
import com.unisound.unicar.gui.pushserver.PushDb;
import com.unisound.unicar.gui.uninavi.UniCarNaviUtil;
import com.unisound.unicar.gui.utils.AutoHelpTextUpdateUtil;
import com.unisound.unicar.gui.utils.ContactsUtil;
import com.unisound.unicar.gui.utils.DeviceTool;
import com.unisound.unicar.gui.utils.FunctionHelpUtil;
import com.unisound.unicar.gui.utils.GUIConfig;
import com.unisound.unicar.gui.utils.JsonTool;
import com.unisound.unicar.gui.utils.Logger;
import com.unisound.unicar.gui.utils.Network;
import com.unisound.unicar.gui.view.AutoTextView;
import com.unisound.unicar.gui.view.CircleImageView;
import com.unisound.unicar.gui.view.EditWakeupWordPopWindow;
import com.unisound.unicar.gui.view.MainMenuPopupWindow;
import com.unisound.unicar.gui.view.MainMenuPopupWindow.MainMenuClickListener;

/**
 * GUIMainActivity for UI3
 *
 * @author xiaodong.he
 * @date 2015-12-04
 */
@SuppressLint("NewApi")
public class GUIMainActivity extends Activity implements OnClickListener {

    private final static String TAG = GUIMainActivity.class.getSimpleName();

    private Context mContext;

    private ImageView startSpeak;

    /**
     * Help Text Layout wake up close
     */
    private LinearLayout mLayoutHelpTextAuto;
    private AutoTextView mAutoTextView;

    /**
     * Help Text Layout wake up open
     */
    private LinearLayout mLayoutHelpTextWakeupOpen;
    private TextView mTvWakeupHelpText;
    private ImageView mIvEditWakeupword;

    private LinearLayout mMainIconsLayout;
    private CircleImageView mIvConnectMobie;

    private LayoutInflater mLayoutInflater;

    //ManiActivty关闭广播
    public static final String ACTION_FINISH_GUIMAINACTIVITY =
            "com.unisound.unicar.gui.ACTION_FINISH_GUIMAINACTIVITY";

    public static final int MSG_UPDATE_AUTO_TEXT_VIEW = 1001;
    /* show AutoHelpTextView delay XD added 20151030 */
    public static final int MSG_UPDATE_MAIN_TOP_HELP_TEXT = 1002;

    public static final int MSG_UPDATE_MAIN_CALL_HELP_TEXT = 1003;

    private static final int TIME_UPDATE_MAIN_TOP_HELP_TEXT_DELAY = 100;


    private ArrayList<String> mHelpTextList = new ArrayList<String>();
    private AutoHelpTextUpdateUtil mHelpTextUpdateMgr = null;

    private ImageView mIvMenuMore;
    private ImageView mIvPushNoRead;
    private TextView mTvHelpTextCall;
    private TextView mTvHelpTextPoi;
    private TextView mTvHelpTextMusic;
    private TextView mTvHelpTextLocalSearch;
    private IntentFilter mPsuhFilter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.activity_main);
        mLayoutInflater = getLayoutInflater();
        mContext = getApplicationContext();                 //获取整个应用的上下文

        initMainHeadView();                                   //初始化各种点击事件

        initView();

        setVolumeControlStream(AudioManager.STREAM_MUSIC);         //音量控制

        startWindowService();                   //开启了一个服务   most Important


        //注册界面销毁的广播
        IntentFilter filter = new IntentFilter(ACTION_FINISH_GUIMAINACTIVITY);
        filter.addAction(GUIConfig.ACTION_ON_CONTACT_DATA_DONE);
        registerReceiver(mFinishReceiver, filter);

        //监听网络的事件-->连接上网络的事件
        IntentFilter networkFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkStateReceiver, networkFilter);

        //注册临时文件发生改变时的广播
        UserPerferenceUtil.registerOnSharedPreferenceChangeListener(mContext, mPreferenceChangeListener);

        //注册唤醒的广播
        IntentFilter wakeupfilter = new IntentFilter(GUIConfig.ACTION_WAKEUP_WORDS_UPDATE);
        registerReceiver(receiver, wakeupfilter);

        //注册显示信息的广播
        registShowInfoBroadcast();

        //显示通知的广播
        registPushReceiver();


    }


    /**
     * 初始化头部
     */
    private void initMainHeadView() {
        startSpeak = (ImageView) findViewById(R.id.startSpeak);
        startSpeak.setOnClickListener(this);

        mLayoutHelpTextAuto = (LinearLayout) findViewById(R.id.ll_help_text_auto);
        mAutoTextView = (AutoTextView) findViewById(R.id.tvAutoText);
        mLayoutHelpTextWakeupOpen = (LinearLayout) findViewById(R.id.ll_help_text_wakeup_open);
        mTvWakeupHelpText = (TextView) findViewById(R.id.tvWakeupHelpText);
        mIvEditWakeupword = (ImageView) findViewById(R.id.iv_edit_wakeupword);
        mIvConnectMobie = (CircleImageView) findViewById(R.id.iv_connect_mobie);

        boolean isConnected = Network.isNetworkConnected(mContext);     //判断网络是否连接


        if (GUIConfig.isSupportUpdateWakeupWordSetting && isConnected) {   //是否支持修改唤醒词
            mTvWakeupHelpText.setOnClickListener(mOnClickListener);
            mIvEditWakeupword.setOnClickListener(mOnClickListener);
            mIvEditWakeupword.setVisibility(View.VISIBLE);
        } else {
            mIvEditWakeupword.setVisibility(View.GONE);
        }

        mIvConnectMobie.setOnClickListener(mOnClickListener);
    }


    //初始化各种View  设置。
    private void initView() {
        mIvMenuMore = (ImageView) findViewById(R.id.iv_main_menu_more);
        mIvPushNoRead = (ImageView) findViewById(R.id.push_no_read_icon);
        mMainIconsLayout = (LinearLayout) findViewById(R.id.layout_main_icons);

        FrameLayout navigationLayout = (FrameLayout) findViewById(R.id.main_layout_navigation);     //导航
        FrameLayout callLayout = (FrameLayout) findViewById(R.id.main_layout_call);                 //打电话
        FrameLayout musicLayout = (FrameLayout) findViewById(R.id.main_layout_music);               //音乐
        FrameLayout localsearchLayout = (FrameLayout) findViewById(R.id.main_layout_localsearch);   //查找周边

        int btnWith = DeviceTool.getScreenHight(mContext) / 2;     //按钮占屏幕的一半

        navigationLayout.setLayoutParams(new android.widget.LinearLayout.LayoutParams(btnWith,
                btnWith));
        callLayout.setLayoutParams(new android.widget.LinearLayout.LayoutParams(btnWith, btnWith));
        musicLayout.setLayoutParams(new android.widget.LinearLayout.LayoutParams(btnWith, btnWith));
        localsearchLayout.setLayoutParams(new android.widget.LinearLayout.LayoutParams(btnWith,
                btnWith));


        mIvMenuMore.setOnClickListener(mOnClickListener);                     //菜单点击事件
        callLayout.setOnClickListener(mOnClickListener);                      //打电话的点击事件
        navigationLayout.setOnClickListener(mOnClickListener);                //导航的点击事件
        musicLayout.setOnClickListener(mOnClickListener);                     //音乐的点击事件
        localsearchLayout.setOnClickListener(mOnClickListener);               //查找周边的点击事件


        mTvHelpTextCall = (TextView) findViewById(R.id.tv_help_text_call);    //里面显示的文字
        mTvHelpTextPoi = (TextView) findViewById(R.id.tv_help_text_poi);
        mTvHelpTextMusic = (TextView) findViewById(R.id.tv_help_text_music);
        mTvHelpTextLocalSearch = (TextView) findViewById(R.id.tv_help_text_local_search);


        boolean isOnline = Network.isNetworkConnected(mContext);              //根据是否有网络显示不同的文字
        updateDomainButtonHelpText(isOnline);
    }


    //推送接受器
    private BroadcastReceiver pushReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(PushDb.PushDbUpdate)) {
                refershPushNoRead();                    //刷新有未读消息
            }
        }
    };


    //注册通知信息
    private void registPushReceiver() {
        if (mPsuhFilter == null) {
            mPsuhFilter = new IntentFilter();
            mPsuhFilter.addAction(PushDb.PushDbUpdate);
            this.registerReceiver(pushReceiver, mPsuhFilter);
        }
    }

    //取消通知监听
    private void unregistPushReceiver() {
        this.unregisterReceiver(pushReceiver);
    }

    //刷新有未读提示
    private void refershPushNoRead() {
        if (PushDb.hasNoRead()) {
            mIvPushNoRead.setVisibility(View.VISIBLE);
        } else {
            mIvPushNoRead.setVisibility(View.GONE);
        }
    }

    //开启一个服务
    private void startWindowService() {
        Intent i = new Intent(this, WindowService.class);
        i.setAction(WindowService.ACTION_START_REQUEST_MAKE_FINISHED);
        startService(i);
    }

    @Override
    public void onClick(View v) {
        int key = v.getId();
        switch (key) {
            case R.id.startSpeak:           //点击话筒时的事件
                startTalk();
                break;
            default:
                break;
        }
    }


    //开启 一个谈话
    private void startTalk() {
        Intent intent = new Intent(this, WindowService.class);
        intent.setAction(MessageReceiver.ACTION_START_TALK);
        startService(intent);
    }


    //开始打电话
    private void startCallOut() {
        Intent intent = new Intent(this, WindowService.class);
        intent.setAction(MessageReceiver.ACTION_START_CALL_OUT);
        startService(intent);
    }


    //开始导航
    private void startNavigation() {
        Intent intent = new Intent(this, WindowService.class);
        intent.setAction(MessageReceiver.ACTION_START_NAVIGATION);
        startService(intent);
    }

    //开启音乐
    private void startMusic() {
        Intent intent = new Intent(this, WindowService.class);
        intent.setAction(MessageReceiver.ACTION_START_MUSIC);
        startService(intent);
    }

    //开启酷我音乐
    private void startKWMusic() {
        MessageSender messageSender = new MessageSender(mContext);
        Intent musicIntent = new Intent(CommandPreference.ACTION_MUSIC_DATA);
        Bundle bundle = new Bundle();
        TrackInfo track = new TrackInfo();
        bundle.putParcelable("track", track);
        musicIntent.putExtras(bundle);
        messageSender.sendOrderedMessage(musicIntent, null);
    }


    //开始定位查找
    private void startLocalSearch() {
        Intent intent = new Intent(this, WindowService.class);
        intent.setAction(MessageReceiver.ACTION_START_LOCAL_SEARCH);
        startService(intent);
    }

    //编辑唤醒词的对话框
    private EditWakeupWordPopWindow pop;

    //显示一个唤醒词的对话框
    private void showEditWakeupwordPopWindow(Context context) {
        pop = new EditWakeupWordPopWindow(context);
        pop.showPopWindow(mMainIconsLayout);
    }

    //点击事件的处理
    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvWakeupHelpText:                 //修改唤醒词时 显示修改唤醒词的对话框
                    showEditWakeupwordPopWindow(mContext);
                    break;
                case R.id.iv_edit_wakeupword:               //修改唤醒词的点击事件
                    showEditWakeupwordPopWindow(mContext);
                    break;
                case R.id.iv_connect_mobie:                 //连接汽车
                    Intent connectIntent = new Intent(GUIMainActivity.this, MobileConnectionActivity.class);
                    startActivity(connectIntent);
                    break;
                case R.id.main_layout_call:                 //打电话
                    startCallOut();
                    break;
                case R.id.main_layout_music:                // 播放音乐
                    //startMusic();                         // 打开系统的可以播放音乐的程序
                    startKWMusic();                         //打开酷我音乐
                    break;
                case R.id.main_layout_navigation:           //导航
                    onClickNavi();
                    break;
                case R.id.main_layout_localsearch:          //周边查找
                    startLocalSearch();
                    break;
                case R.id.iv_main_menu_more:                //显示菜单
                    showMenu();
                    break;

                default:
                    break;

            }
        }
    };

    //点击导航按钮
    private void onClickNavi() {
        if (UniCarNaviUtil.isUniCarNaviEnable(mContext)) {  //汽车导航是否可用
            UniCarNaviUtil.sendShowNaviUIAction(mContext);  //开启导航
        } else {
            startNavigation();      //不可用则开启-->汽车导航
        }
    }


    //显示菜单
    private void showMenu() {
        MainMenuPopupWindow mMenuPop = new MainMenuPopupWindow(mContext);    //显示一个popupWindow
        mMenuPop.setMainMenuClickListener(mMainMenuClickListener);
        mMenuPop.showPopWindow(mIvMenuMore);
    }


    /**
     * 主界面菜单的点击事件
     */
    private MainMenuClickListener mMainMenuClickListener = new MainMenuClickListener() {

        @Override
        public void onAllFunctionClick() {                                  //全部功能的点击事件
            Intent functionIntent = new Intent(GUIMainActivity.this, AllFunctionsActivity.class);
            startActivity(functionIntent);
        }

        @Override
        public void onSetingClick() {                                       //设置界面的点击事件
            Intent settingIntent = new Intent(GUIMainActivity.this, SettingsViewPagerActivity.class);
            startActivity(settingIntent);
        }


        @Override
        public void onPushClick() {                                            //通知按钮的点击事件
            Intent settingIntent = new Intent(GUIMainActivity.this, PushListActivity.class);
            startActivity(settingIntent);
        }


    };


    //GUIMainActivity 销毁时的方法
    BroadcastReceiver mFinishReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (ACTION_FINISH_GUIMAINACTIVITY.equals(action)) {                 //销毁
                GUIMainActivity.this.finish();

            } else if (GUIConfig.ACTION_ON_CONTACT_DATA_DONE.equals(action)) {   //没有数据
                boolean isNetworkConnected = Network.isNetworkConnected(mContext);
                if (isNetworkConnected) {
                    updateMainCallHelpText(true);
                }

            }
        }
    };

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            this.finish();  //返回
        }
        return super.dispatchKeyEvent(event);
    }


    //界面获得焦点时的事件
    @Override
    protected void onResume() {
        super.onResume();
        if (mContext == null) {
            mContext = getApplicationContext();
        }
        updateMainTopHelpText();
        refershPushNoRead();
    }


    /* < xiaodong.he 20151019 added for Help Text Begin */
    private OnSharedPreferenceChangeListener mPreferenceChangeListener = new OnSharedPreferenceChangeListener() {

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            Logger.d(TAG, "!--->onSharedPreferenceChanged: key " + key);
            if (UserPerferenceUtil.KEY_ENABLE_WAKEUP.equals(key)) {

            } else if (UserPerferenceUtil.KEY_WAKEUP_WORDS.equals(key)) {
                boolean isWakeUpOpen = UserPerferenceUtil.isWakeupEnable(mContext);
                if (isWakeUpOpen && null != mTvWakeupHelpText) {
                    mTvWakeupHelpText.setText(FunctionHelpUtil
                            .addDoubleQuotationMarks(UserPerferenceUtil
                                    .getWakeupWord(mContext)));
                }
            }
        }
    };

    /**
     * @param mContext
     */
    private void updateMainTopHelpText() {
        boolean isWakeUpOpen = UserPerferenceUtil.isWakeupEnable(mContext);
        Logger.d(TAG, "updateMainTopHelpText--isWakeUpOpen = " + isWakeUpOpen);
        if (isWakeUpOpen) {
            mLayoutHelpTextWakeupOpen.setVisibility(View.VISIBLE);
            mLayoutHelpTextAuto.setVisibility(View.GONE);
            releaseAuotHelpTextUpdateMgr();

            mTvWakeupHelpText.setText(FunctionHelpUtil.addDoubleQuotationMarks(UserPerferenceUtil
                    .getWakeupWord(mContext)));

            boolean isConnected = Network.isNetworkConnected(mContext);// 20151215
            if (GUIConfig.isSupportUpdateWakeupWordSetting && isConnected) {
                mIvEditWakeupword.setVisibility(View.VISIBLE);
            } else {
                mIvEditWakeupword.setVisibility(View.GONE);
            }
        } else {
            showAutoHelpTextView();
        }
    }

    //显示帮助文本
    private void showAutoHelpTextView() {
        mLayoutHelpTextWakeupOpen.setVisibility(View.GONE);
        mLayoutHelpTextAuto.setVisibility(View.VISIBLE);
        updateAutoHelpText(Network.isNetworkConnected(mContext));
    }

    /**
     * update Domain Button Help Text
     *
     * @param isOnline
     */
    private void updateDomainButtonHelpText(boolean isOnline) {
        updateMainCallHelpText(isOnline, mContactSyncListener);
        if (null != mTvHelpTextPoi) {
            mTvHelpTextPoi.setText(FunctionHelpUtil.getMainPageRouteHelpText(mContext, isOnline));
        }
        if (null != mTvHelpTextMusic) {
            mTvHelpTextMusic.setText(FunctionHelpUtil.getMainPageMusicHelpText(mContext, isOnline));
        }
        if (null != mTvHelpTextLocalSearch) {
            mTvHelpTextLocalSearch.setText(FunctionHelpUtil.getMainPageLocalSearchHelpText(
                    mContext, isOnline));
        }
    }


    private void updateMainCallHelpText(boolean isOnline) {
        updateMainCallHelpText(isOnline, null);
    }


    private void updateMainCallHelpText(boolean isOnline, ContactsUtil.ContactAsyncListener listener) {
        if (null != mTvHelpTextCall) {
            String helpText =
                    FunctionHelpUtil.getMainPageCallTelpText(mContext, isOnline, listener);
            Logger.d(TAG, "updateMainCallHelpText---helpText = " + helpText + "; listener = "
                    + listener);
            mTvHelpTextCall.setText(helpText);
        }
    }



    /**
     * mContactSyncListener
     * 联系人-->同步
     */
    private ContactsUtil.ContactAsyncListener mContactSyncListener = new ContactsUtil.ContactAsyncListener() {

        @Override
        public void onQueryCompleted(ConcurrentHashMap<Integer, String> contactMap) {
            if (contactMap != null && contactMap.size() > 0) {
                mUIHandler.sendEmptyMessage(MSG_UPDATE_MAIN_CALL_HELP_TEXT);
            }
        }

        @Override
        public void onQueryError() {
        }
    };


    private Handler mUIHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_AUTO_TEXT_VIEW:
                    String text = (String) msg.obj;
                    if (null != mAutoTextView) {
                        mAutoTextView.setText(text);
                    }
                    break;
                case MSG_UPDATE_MAIN_TOP_HELP_TEXT:
                    updateMainTopHelpText();
                    break;

                case MSG_UPDATE_MAIN_CALL_HELP_TEXT:
                    boolean isNetworkContected = Network.isNetworkConnected(mContext);

                    if (isNetworkContected) {
                        updateMainCallHelpText(true);
                    }
                    break;
                default:
                    break;
            }
        }

        ;
    };


    /**
     * update Help Text
     *
     * @param isConnected
     */
    private void updateAutoHelpText(boolean isConnected) {
        mHelpTextList = FunctionHelpUtil.getMainPageHelpTextList(mContext, isConnected);
        Logger.d(TAG, "!--->updateHelpText---isConnected = " + isConnected
                + "; mHelpTextList size = " + mHelpTextList.size());
        if (null == mHelpTextUpdateMgr) {
            mHelpTextUpdateMgr = new AutoHelpTextUpdateUtil(mHelpTextList, mUIHandler);
        } else {
            Logger.d(TAG, "!--->mUpdateHelpTextThread interrupt.");
            mHelpTextUpdateMgr.stop();
            mHelpTextUpdateMgr.setHelpTextList(mHelpTextList);
        }
        mHelpTextUpdateMgr.start();
    }

    /**
     * 监听网络的接收器
     */
    private BroadcastReceiver mNetworkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                boolean isConnected = Network.isNetworkConnected(mContext);
                boolean isWakeUpOpen = UserPerferenceUtil.isWakeupEnable(mContext);      //唤醒功能是否打开

                Logger.d(TAG, "!--->mNetworkStateReceiver--onReceive--isConnected:" + isConnected + "; isWakeUpOpen = " + isWakeUpOpen);

                if (!isWakeUpOpen) {
                    updateAutoHelpText(isConnected);
                }
                updateDomainButtonHelpText(isConnected);    // 更新控制按钮的文字
                updateEditWakeupWordView(isConnected);      // 更新编辑唤醒词的对话框的文字
            }
        }
    };

    /* XiaodDong 20150721 added for Auto Help Text End */

    /**
     * updateEditWakeupWordView
     *
     * @param isConnected
     * @author xiaodong.he
     * @date 2015-12-14
     */
    private void updateEditWakeupWordView(boolean isConnected) {
        if (null == mIvEditWakeupword || null == mTvWakeupHelpText) {
            return;
        }
        if (!GUIConfig.isSupportUpdateWakeupWordSetting) {
            return;
        }
        if (isConnected) {
            mIvEditWakeupword.setVisibility(View.VISIBLE);
            mIvEditWakeupword.setOnClickListener(mOnClickListener);
            mTvWakeupHelpText.setOnClickListener(mOnClickListener);
        } else {
            mIvEditWakeupword.setVisibility(View.GONE);
            mIvEditWakeupword.setOnClickListener(null);

            mTvWakeupHelpText.setOnClickListener(null);
        }
    }

    private void releaseAuotHelpTextUpdateMgr() {
        if (null != mHelpTextUpdateMgr) {
            mHelpTextUpdateMgr.stop();
            mHelpTextUpdateMgr.setHelpTextList(null);
            mHelpTextUpdateMgr = null;
        }
        if (null != mHelpTextList) {
            mHelpTextList.clear();
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Logger.d(TAG, "!--->onDestroy...");

        releaseAuotHelpTextUpdateMgr();
        unregisterReceiver(mFinishReceiver);
        unregisterReceiver(mNetworkStateReceiver);// XD added 20150722
        unregisterReceiver(receiver);
        unregisterReceiver(showInfoReceiver);
        UserPerferenceUtil.unregisterOnSharedPreferenceChangeListener(mContext,
                mPreferenceChangeListener);

        unregistPushReceiver();
    }


    //唤醒词改变的事件   把popupWindow的消失掉
    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (pop != null) {
                pop.dismiss();
            }
        }

    };

    // 显示用户信息
    public static final String SHOW_SUCCESS_INFO = "com.unisound.unicar.gui.SHOW_SUCCESS_INFO";

    private void registShowInfoBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(SHOW_SUCCESS_INFO);
        registerReceiver(showInfoReceiver, filter);
    }

    BroadcastReceiver showInfoReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Logger.d(TAG, "action : " + action);
            if (SHOW_SUCCESS_INFO.equals(action)) {
                String infoContent = intent.getStringExtra("infoContent");
                Logger.d(TAG, "infoContent:" + infoContent);
                JSONObject infoJsonObject;
                String type;
                JSONObject typeJsonObject;
                String headImageUrl = "";
                if (!TextUtils.isEmpty(infoContent)) {
                    try {
                        infoJsonObject = new JSONObject(infoContent);
                        type = JsonTool.getJsonValue(infoJsonObject, "type");
                        typeJsonObject = new JSONObject(type);
                        headImageUrl = JsonTool.getJsonValue(typeJsonObject, "headimgurl");
                        Logger.d(TAG, "headImageUrl: " + headImageUrl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // 加载网络图片
                    if (!TextUtils.isEmpty(headImageUrl)) {
                        Logger.d(TAG, "123456 headImageUrl :" + headImageUrl);
                        x.image().bind(mIvConnectMobie, headImageUrl);
                        // mIvConnectMobie.setImageURI(headImageUrl);
                    }
                }
            }
        }
    };
}
