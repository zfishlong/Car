/**
 * Copyright (c) 2012-2013 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * 
 * @FileName : KWMusicService.java
 * @ProjectName :
 * @PakageName : com.android.kwmusic
 * @Author : kevinliao
 * @CreateDate : 2014-12-2
 */
package com.android.kwmusic;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;
import cn.kuwo.autosdk.api.KWAPI;
import cn.kuwo.autosdk.api.OnPlayerStatusListener;
import cn.kuwo.autosdk.api.OnSearchListener;
import cn.kuwo.autosdk.api.PlayMode;
import cn.kuwo.autosdk.api.PlayState;
import cn.kuwo.autosdk.api.PlayerStatus;
import cn.kuwo.autosdk.api.SearchStatus;
import cn.kuwo.autosdk.bean.Music;

import com.unisound.unicar.gui.model.TrackInfo;
import com.unisound.unicar.gui.preference.CommandPreference;
import com.unisound.unicar.gui.utils.Logger;
import com.unisound.unicar.gui.utils.PlayersControlManager;


@SuppressLint("HandlerLeak")
public class KWMusicService extends Service implements OnSearchListener {
    private static final String TAG = "KWMusicService";
    private static final int MSG_PAUSE = 1001;
    private static final int MSG_PLAY = 1002;
    private static final int MSG_PREVOIUS = 1003;
    private static final int MSG_NEXT = 1004;

    private KWAPI mKwapi;
    private Context mContext;

    // 由于单曲循环的设置和暂停都不能和播放指令同时发出，所以设置一个延时时间
    private Handler mMusicHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_PAUSE:

                    setPlayState(KWPlayState.STATE_PAUSE);
                    break;
                case MSG_PLAY:
                    Logger.d(TAG, "mMusicHandler---setPlayState: STATE_PLAY");
                    setPlayState(KWPlayState.STATE_PLAY);
                    break;
                case MSG_PREVOIUS:
                    setPlayState(KWPlayState.STATE_PRE);
                    break;
                case MSG_NEXT:
                    setPlayState(KWPlayState.STATE_NEXT);
                    break;
                default:
                    break;
            }

        };
    };


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.d(TAG, "--onReceive:intent : " + intent);
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    Logger.d(TAG, "--key : " + key + ",value : " + bundle.get(key));
                }
            }
            String action = intent.getAction();
            String cmd = intent.getStringExtra(CommandPreference.CMD_KEY_NAME);

            Logger.d(TAG, "--cmd : " + cmd + ",action : " + action);
            try {
                if (CommandPreference.CMD_NAME_TOGGLEPAUSE.equals(cmd)
                        || CommandPreference.ACTION_TOGGLEPAUSE.equals(action)) {
                    setPlayState(KWPlayState.STATE_PAUSE);
                } else if (CommandPreference.CMD_NAME_PAUSE.equals(cmd)
                        || CommandPreference.ACTION_PAUSE.equals(action)) {
                    Logger.i(
                            TAG,
                            "!--->onReceive PAUSE CMD , mPlayerStatus = "
                                    + mPlayerStatus.toString());
                    // if (PlayerStatus.PLAYING == mPlayerStatus
                    // || PlayerStatus.BUFFERING == mPlayerStatus) {
                    // Logger.i(TAG, "!--->onReceive PAUSE CMD , Begin PAUSE");
                    // }
                    mMusicHandler.sendEmptyMessageDelayed(MSG_PAUSE,
                            PlayersControlManager.TIME_SET_STATE_DELAY);
                } else if (CommandPreference.CMD_NAME_PLAY.equals(cmd)) {
                    Logger.d(TAG, "!--->onReceive--PLAY CMD, do play "
                            + PlayersControlManager.TIME_SET_STATE_DELAY
                            + "ms delay---mPlayerStatus = " + mPlayerStatus.toString());
                    mMusicHandler.sendEmptyMessageDelayed(MSG_PLAY,
                            PlayersControlManager.TIME_SET_STATE_DELAY);
                    // setPlayState(KWPlayState.STATE_PLAY);
                } else if (CommandPreference.CMD_NAME_PREVIOUS.equals(cmd)
                        || CommandPreference.ACTION_PREVIOUS.equals(action)) {
                    // setPlayState(KWPlayState.STATE_PRE);
                    mMusicHandler.sendEmptyMessageDelayed(MSG_PREVOIUS,
                            PlayersControlManager.TIME_SET_STATE_DELAY);
                } else if (CommandPreference.CMD_NAME_NEXT.equals(cmd)
                        || CommandPreference.ACTION_NEXT.equals(action)) {
                    // setPlayState(KWPlayState.STATE_NEXT);
                    mMusicHandler.sendEmptyMessageDelayed(MSG_NEXT,
                            PlayersControlManager.TIME_SET_STATE_DELAY);
                } else if (CommandPreference.CMD_NAME_STOP.equals(cmd)) {
                    exitKwApp();
                } else if (CommandPreference.CMD_NAME_OPEN_MUSIC.equals(cmd)) {
                    // startKwApp();
                    contiuneKwApp();
                } else if (CommandPreference.CMD_NAME_FULL_CYCLE.equals(cmd)) {
                    setPlayModel(KWPlayMode.MODE_ALL_CIRCLE);
                } else if (CommandPreference.CMD_NAME_SINGLE_CYCLE.equals(cmd)) {
                    setPlayModel(KWPlayMode.MODE_SINGLE_CIRCLE);
                } else if (CommandPreference.CMD_NAME_ORDER_PLAYBACK.equals(cmd)) {
                    setPlayModel(KWPlayMode.MODE_ALL_ORDER);
                } else if (CommandPreference.CMD_NAME_SHUFFLE_PLAYBACK.equals(cmd)) {
                    setPlayModel(KWPlayMode.MODE_ALL_RANDOM);
                } else if (CommandPreference.CMD_NAME_OPEN_DESK_LYRIC.equals(cmd)) {
                    openDeskLyric();
                } else if (CommandPreference.CMD_NAME_CLOSE_DESK_LYRIC.equals(cmd)) {
                    closeDeskLyric();
                } else if (CommandPreference.ACTION_MUSIC_DATA.equals(action)) {
                    // 播放音乐
                    if (bundle != null) {
                        TrackInfo track = (TrackInfo) bundle.getParcelable("track");
                        if (bundle != null && track != null) {
                            playClientMusics(track.getTitle(), track.getArtist(), track.getAlbum());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d(TAG, "--onCreate--");
        mKwapi = KWAPI.createKWAPI(this, "auto");
        mContext = this;
        registReceiver();
        registerPlayListener();
    }

    /**
     * KW PlayerStatus INIT, //启动没播过歌 PLAYING, //正在播放 BUFFERING, //播放中，等待缓冲 PAUSE, //暂停 STOP, //停止
     * XD added 20150929
     */
    private static PlayerStatus mPlayerStatus = PlayerStatus.INIT;

    /**
     * getPlayerStatus
     * 
     * @author xiaodong.he
     * @return PlayerStatus
     */
    public static PlayerStatus getPlayerStatus() {
        return mPlayerStatus;
    }

    /**
     * is KWMusic Playing
     * 
     * @author xiaodong.he
     * @return
     */
    public static boolean isKWMusicPlaying() {
        if (PlayerStatus.PLAYING == mPlayerStatus || PlayerStatus.BUFFERING == mPlayerStatus) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * register WK Play Listener XD added 20150929
     */
    private void registerPlayListener() {
        if (null == mKwapi) {
            return;
        }
        Logger.d(TAG, "registerPlayListener");
        mKwapi.registerPlayerStatusListener(this, mPlayerStatusListener);
    }

    private OnPlayerStatusListener mPlayerStatusListener = new OnPlayerStatusListener() {
        @Override
        public void onPlayerStatus(PlayerStatus status) {
            // do something by yourself
            Logger.i(TAG, "!--->onPlayerStatus---status:" + status.toString());
            mPlayerStatus = status;
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d(TAG, "--onStartCommand--");
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Logger.d(TAG, "--onBind--");
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d(TAG, "--onDestroy--");
        unregistReceiver();
        // 对象销毁时取消注册的监听
        if (null != mKwapi) {
            mKwapi.unRegisterPlayerStatusListener(this);
        }

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void searchFinshed(SearchStatus arg0, boolean arg1, List arg2, boolean arg3) {
        Logger.d(TAG, "--searchFinshed--");
        List<Music> musics = (List<Music>) arg2;
        if (arg0 == SearchStatus.SUCCESS) {
            // 搜索成功，可以将歌曲列表展现出来选择性的播放
            if (musics.size() > 0) {
                mKwapi.playMusic(this, musics.get(0));
            }
        } else {
            Toast.makeText(this, "在线歌曲搜索失败！", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 添加action，注册广播
     */
    private void registReceiver() {
        IntentFilter commandFilter = new IntentFilter();
        commandFilter.setPriority(1100);
        commandFilter.addAction(CommandPreference.ACTION_SERVICE_CMD);
        commandFilter.addAction(CommandPreference.ACTION_CONTROL_KWMUSIC); // XD added 20151026
        commandFilter.addAction(CommandPreference.ACTION_TOGGLEPAUSE);
        commandFilter.addAction(CommandPreference.ACTION_PAUSE);
        commandFilter.addAction(CommandPreference.ACTION_NEXT);
        commandFilter.addAction(CommandPreference.ACTION_PREVIOUS);
        commandFilter.addAction(CommandPreference.ACTION_MUSIC_DATA);
        mContext.registerReceiver(mReceiver, commandFilter);
    }

    /**
     * 取消广播
     */
    private void unregistReceiver() {
        try {
            mContext.unregisterReceiver(mReceiver);
        } catch (Exception e) {
            Logger.printStackTrace(e);
        }
    }



    /*------------------kw接口封装----------------*/
    /**
     * 在线搜索播放第一条
     */
    private void searchOnlineMusic(String key) {
        Logger.d(TAG, "--searchOnlineMusic-->key : " + key);
        if (mKwapi == null) reCreateKWAPI();
        mKwapi.searchOnlineMusic(key, this);
    }



    /**
     * 启动kw，非自动播放
     */
    private void startKwApp() {
        Logger.d(TAG, "--startKwApp--");
        if (mKwapi == null) reCreateKWAPI();
        mKwapi.startAPP(mContext, true);// startApp()是启动程序并自动播放上次播放过的歌曲，你这种情况需要调用playClientMusics这个接口，歌曲的参数信息传空即可。
    }

    /**
     * 启动kw，并自动播放
     */
    private void contiuneKwApp() {
        Logger.d(TAG, "--contiuneKwApp--");
        if (mKwapi == null) reCreateKWAPI();
        mKwapi.playClientMusics(mContext, null, null, null);
    }

    /**
     * 关闭kw
     */
    private void exitKwApp() {
        Logger.d(TAG, "--exitKwApp--");
        if (mKwapi == null) reCreateKWAPI();
        mKwapi.exitAPP(mContext);
    }

    /**
     * 打开桌面歌词
     */
    private void openDeskLyric() {

        Logger.d(TAG, "打开桌面歌词");
        if (mKwapi == null) reCreateKWAPI();
        mKwapi.openDeskLyric(mContext);

    }

    /**
     * 关闭桌面歌词
     */
    private void closeDeskLyric() {
        Logger.d(TAG, "关闭桌面歌词");
        if (mKwapi == null) reCreateKWAPI();
        mKwapi.closeDeskLyric(mContext);
    }

    /**
     * 设置播放模式
     * 
     * @param mode
     */
    private void setPlayModel(KWPlayMode mode) {
        Logger.d(TAG, "--setPlayModel--mode:" + mode);
        if (mKwapi == null) reCreateKWAPI();
        switch (mode) {
            case MODE_ALL_CIRCLE:
                // 循环播放
                mKwapi.setPlayMode(this, PlayMode.MODE_ALL_CIRCLE);
                break;
            case MODE_SINGLE_CIRCLE:
                // 单曲循环
                Logger.d(TAG, "--setPlayModel--MODE_SINGLE_CIRCLE");
                mKwapi.setPlayMode(this, PlayMode.MODE_SINGLE_CIRCLE);
                break;
            case MODE_ALL_ORDER:
                // 顺序播放
                mKwapi.setPlayMode(this, PlayMode.MODE_ALL_ORDER);
                break;
            case MODE_ALL_RANDOM:
                // 随机播放
                mKwapi.setPlayMode(this, PlayMode.MODE_ALL_RANDOM);
            default:
                break;
        }
    }

    /**
     * 设置模式
     * 
     * @param model 模式
     */
    private void setPlayState(KWPlayState state) {
        Logger.d(TAG, "--setPlayState--state = " + state);
        if (mKwapi == null) reCreateKWAPI();
        switch (state) {
            case STATE_PLAY:// 播放
                mKwapi.setPlayState(mContext, PlayState.STATE_PLAY);
                break;
            case STATE_PAUSE:// 暂停
                mKwapi.setPlayState(mContext, PlayState.STATE_PAUSE);
                break;
            case STATE_PRE:// 上一个
                mKwapi.setPlayState(mContext, PlayState.STATE_PRE);
                break;
            case STATE_NEXT:// 下一个
                mKwapi.setPlayState(mContext, PlayState.STATE_NEXT);
                break;
            default:
                break;
        }
    };

    /**
     * 播放用户搜索的歌曲
     * 
     * @param name 歌名
     * @param singer 歌手
     * @param album 专辑
     */
    private void playClientMusics(String name, String singer, String album) {
        Logger.d(TAG, "--playClientMusics--");
        if (mKwapi == null) reCreateKWAPI();
        mKwapi.playClientMusics(mContext, name, singer, album);
        mKwapi.setPlayState(mContext, PlayState.STATE_PLAY);
    }

    /**
     * 根据路径打开本地音乐
     * 
     * @param path
     */
    private void playLocalMusic(String path) {
        Logger.d(TAG, "--playLocalMusic--");
        if (mKwapi == null) reCreateKWAPI();
        mKwapi.playLocalMusic(mContext, path);
    }


    /**
     * 重新创建对象
     * 
     * @return
     */
    private void reCreateKWAPI() {
        mKwapi = KWAPI.createKWAPI(this, "auto");
    }

    /**
     * 播放模式
     * 
     * @author kevin
     * 
     */
    private enum KWPlayMode {
        MODE_ALL_CIRCLE, MODE_SINGLE_CIRCLE, MODE_ALL_ORDER, MODE_ALL_RANDOM;
    }

    /**
     * 播放状态
     * 
     * @author kevin
     * 
     */
    private enum KWPlayState {
        STATE_PLAY, STATE_PAUSE, STATE_PRE, STATE_NEXT;
    }


}
