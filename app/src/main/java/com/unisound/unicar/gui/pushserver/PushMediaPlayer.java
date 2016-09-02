package com.unisound.unicar.gui.pushserver;

import java.io.File;

import com.unisound.unicar.gui.utils.Logger;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;

public class PushMediaPlayer {

    private static final String TAG = PushMediaPlayer.class.getSimpleName();

    private MediaPlayer mMediaPlayer;

    private AudioManager mAudioManager;

    private String filePath;
    private String id;

    private int mMediaPlayerState = PlayerState.MPS_RELEASE;

    private IMediaPlayerStateListener mMediaPlayerStateListener;

    public PushMediaPlayer(Context context) {
        mMediaPlayer = new MediaPlayer();
        // get Audio Manager
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    /**
     * 
     * @param path
     * @param mediaPlayerStateListener
     */

    public void play(String id, String path, IMediaPlayerStateListener mediaPlayerStateListener) {
        this.id = id;
        play(path, mediaPlayerStateListener);
    }

    public void play(String path, IMediaPlayerStateListener mediaPlayerStateListener) {
        callBackPlayerState(PlayerState.MPS_STOP);
        Logger.d(TAG, "start path : " + path + ";mMediaPlayerState : " + PlayerState.MPS_PAUSE);
        mMediaPlayerStateListener = mediaPlayerStateListener;
        mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Logger.d(TAG, "onCompletion");
                callBackPlayerState(PlayerState.MPS_COMPLETE);
                mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
            }
        });

        mMediaPlayer.setOnErrorListener(new OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Logger.d(TAG, "onError");
                callBackPlayerState(PlayerState.MPS_ERROR);
                return false;
            }
        });


        File file = new File(path);
        filePath = path;

        if (requestFocus() && file.exists() && file.length() > 0 && mMediaPlayer != null) {
            if (mMediaPlayerState == PlayerState.MPS_PAUSE) {
                Logger.d(TAG, "mMediaPlayerState : " + PlayerState.MPS_PAUSE);
                mMediaPlayer.start();
                mMediaPlayerState = PlayerState.MPS_PLAYING;
                callBackPlayerState(mMediaPlayerState);
            } else {
                mMediaPlayer.stop();
                mMediaPlayerState = PlayerState.MPS_STOP;

                try {
                    mMediaPlayer.reset();
                    mMediaPlayer.setDataSource(path);
                    // 设置音频流的类型
                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    // 通过异步的方式装载媒体资源
                    mMediaPlayer.prepareAsync();
                    mMediaPlayer.setVolume(1.0f, 1.0f);// max volume
                    mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            Logger.d(TAG, "onPrepared");
                            // 装载完毕 开始播放流媒体
                            mMediaPlayer.start();
                            mMediaPlayerState = PlayerState.MPS_PLAYING;
                            callBackPlayerState(mMediaPlayerState);
                        }
                    });
                } catch (Exception e) {
                    Logger.e(TAG, "paly error = " + e.toString());
                    Logger.e(TAG, "目前播放不了这个音频 只能暂时不播 返回 不影响后续流程");
                    callBackPlayerState(PlayerState.MPS_COMPLETE);
                }
            }
        }
    }

    /**
     * pause
     */
    public void pause() {
        Logger.d(TAG, "puase mMediaPlayerState : " + PlayerState.MPS_PAUSE);
        if (mMediaPlayer != null && mMediaPlayerState == PlayerState.MPS_PLAYING) {
            mMediaPlayer.pause();
            mMediaPlayerState = PlayerState.MPS_PAUSE;
            callBackPlayerState(mMediaPlayerState);
        }
    }

    /**
     * resume
     */
    public void resume() {
        if (mMediaPlayer != null && mMediaPlayerState == PlayerState.MPS_PAUSE) {
            mMediaPlayer.start();
            mMediaPlayerState = PlayerState.MPS_PLAYING;
            callBackPlayerState(mMediaPlayerState);
        }
    }

    /**
     * stop
     */
    public void stop() {
        Logger.d(TAG, "stop mMediaPlayerState : " + PlayerState.MPS_PAUSE);
        if (mMediaPlayer != null && mMediaPlayerState == PlayerState.MPS_PLAYING) {
            mMediaPlayer.stop();
            mMediaPlayerState = PlayerState.MPS_STOP;
            callBackPlayerState(mMediaPlayerState);
        }
    }

    /**
     * release
     */
    public void release() {
        Logger.d(TAG, "release mMediaPlayerState : " + PlayerState.MPS_PAUSE);
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayerState = PlayerState.MPS_RELEASE;
            callBackPlayerState(mMediaPlayerState);
            mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
            mMediaPlayer = null;
        }
    }

    /**
     * call Back Player State
     * 
     * @param state
     */
    private void callBackPlayerState(int state) {
        Logger.d(TAG, "callBackPlayerState State : " + state);
        if (state == PlayerState.MPS_COMPLETE) {
            filePath = null;
        }
        if (mMediaPlayerStateListener != null) {
            mMediaPlayerStateListener.onPlayerState(state);
        }
    }

    private boolean requestFocus() {
        // Request audio focus for playback
        int result = mAudioManager.requestAudioFocus(mAudioFocusChangeListener,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);
        return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    public String getNowPlayId() {
        return id;
    }

    public int getNowPlayState() {
        return mMediaPlayerState;
    }

    private OnAudioFocusChangeListener mAudioFocusChangeListener =
            new OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                        // Pause playback
                        // pause();
                        stop();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        // Resume playback
                        // resume();
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        // mAm.unregisterMediaButtonEventReceiver(RemoteControlReceiver);
                        mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
                        // Stop playback
                        stop();
                    }
                }
            };
}
