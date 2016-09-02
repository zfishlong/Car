package com.unisound.unicar.gui.pushserver;

public interface PlayerState {

    public static final int MPS_ERROR = -1001; // 未就绪

    public static final int MPS_RELEASE = 0; // 未就绪

    public static final int MPS_PLAYING = 1; // 播放中

    public static final int MPS_PAUSE = 2; // 暂停

    public static final int MPS_STOP = 3; // 停止

    public static final int MPS_COMPLETE = 4; // 播放完毕
}
