/**
 * Copyright (c) 2012-2015 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : ISystemCallTransitionListener.java
 * @ProjectName : uniCarSolution
 * @PakageName : com.unisound.unicar.gui.msg
 * @version : 1.2
 * @Author : Xiaodong.He
 * @CreateDate : 2015-06-09
 */
package com.unisound.unicar.gui.msg;

import org.json.JSONObject;

import com.unisound.unicar.gui.preference.SessionPreference;
import com.unisound.unicar.gui.pushbean.PushModle;

public interface ISystemCallTransitionListener {
    public void setState(int type);

    /**
     * XD 2016-1-11 added
     * 
     * @param infoJson {"asrvalue":"2016-06-30","asrkey":"1"}
     * @value asrkey=0: no limitation;
     * @value asrkey=1: with time limitation;(test version)
     * @value asrkey=2: with package name limitation;
     * @value asrkey=3: with both time and package name limitation;
     */
    public void onEngineInfo(JSONObject infoJson);

    public void onControlWakeupSuccess(String wakeupWord);

    public void onUpdateWakeupWordsStatus(String status);

    public void onTalkRecordingException();

    public void onTalkRecordingPrepared();

    public void onTalkRecordingStart();

    public void onTalkRecordingStop();

    public void onTalkResult(String result);

    public void onSessionProtocal(String protocol);

    /** XD 2016-1-25 modify */
    public void onPlayEnd(String ttsID);

    public void onSendMsg(String msg);

    public void onUpdateVolume(int volume);

    public void onRecognizerTimeout();

    public void onCTTCancel();

    public void onOneShotRecognizerTimeout();

    public void onStartFakeAnimation();

    public void onGetWakeupWords(String wakeupWords);

    public void onClickMainActionButton(int style);

    public void onTalkRecodingIdle();

    public void getCurrentTTSType(String ttsType);

    /**
     * XD 2015-12-22 added
     * 
     * @param uuid
     */
    public void onGetUuid(String uuid);

    /**
     * @date 2016-1-12
     * @param ttsTimbre {@link SessionPreference#PARAM_TTS_TIMBRE_STARNAND}
     *        {@link SessionPreference#PARAM_TTS_TIMBRE_SEXY}
     */
    public void onSwitchTTS(String ttsTimbre);

    /**
     * 
     * @date 2016-1-18
     * @param modleName
     */
    public void onCopyTTSModelSuccess(String modleName);

    /**
     * 
     * @date 2016-1-18
     * @param modleName
     * @param failcause
     */
    public void onCopyTTSModelFail(String modleName, String failcause);

    /**
     * 
     * @date 2016-1-18
     * @param json
     */
    public void onGetTTSModelList(String json);

    /**
     * @date 2016-1-29
     * @param locationType
     * @param locationInfoJson
     */
    public void onAddedFavoriteAddress(String locationType, String locationInfoJson);

    /**
     * ��ʾ�û���Ϣ
     * 
     * @param info
     */

    public void onShowBindInfo(String info);

    public void onPushMessageReceive(PushModle pushModle);

    public void onSpeakerSpeechDetected();


    public void onAecCancel();

}
