/**
 * Copyright (c) 2012-2015 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : SystemCallTransition.java
 * @ProjectName : uniCarSolution
 * @PakageName : com.unisound.unicar.gui.msg
 * @version : 1.2
 * @Author :Xiaodong.He
 * @CreateDate : 2015-06-09
 */
package com.unisound.unicar.gui.msg;

import org.json.JSONObject;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.unisound.unicar.gui.preference.SessionPreference;
import com.unisound.unicar.gui.pushbean.PushModle;
import com.unisound.unicar.gui.utils.JsonTool;
import com.unisound.unicar.gui.utils.Logger;


/**
 * 处理系统电话
 */
public class SystemCallTransition {

    private static final String TAG = SystemCallTransition.class.getSimpleName();

    private ISystemCallTransitionListener mMessageTransListener = null;

    /**
     * handle System Call
     * 
     * @param callBackJson
     */
    public void handlerSystemCall(String callBackJson) {
        JSONObject obj = JsonTool.parseToJSONObject(callBackJson);
        if (obj != null) {
            JSONObject jobj = JsonTool.parseToJSONObject(callBackJson);
            JSONObject data = JsonTool.getJSONObject(jobj, SessionPreference.KEY_DATA);
            JSONObject dataParams = JsonTool.getJSONObject(data, SessionPreference.KEY_PARAMS); // xd
                                                                                                // added
            String functionName =
                    JsonTool.getJsonValue(data, SessionPreference.KEY_FUNCTION_NAME, "");// xd added
            Logger.d(TAG, "!--->onCallBackFunction---mFunctionName = " + functionName);

            if (SessionPreference.VALUE_FUNCTION_NAME_SET_STATE.equals(functionName)) {
                // JSONObject dataParams = JsonTool.getJSONObject(data,
                // SessionPreference.KEY_PARAMS); //xd delete
                String sType = JsonTool.getJsonValue(dataParams, SessionPreference.KEY_TYPE, "");
                int type = Integer.parseInt(sType);
                if (mMessageTransListener != null) {
                    mMessageTransListener.setState(type);
                }
            } else if (SessionPreference.VALUE_FUNCTION_NAME_ON_ENGINE_INFO.equals(functionName)) {
                // XD added 2016-01-11
                Logger.d(TAG, "onCallBackFunction--VALUE_FUNCTION_NAME_ON_ENGINE_INFO");
                // {"asrvalue":"2016-06-30","asrkey":"1"}
                String engineType = JsonTool.getJsonValue(dataParams, "type");
                if (mMessageTransListener != null) {
                    mMessageTransListener.onEngineInfo(JsonTool.parseToJSONObject(engineType));
                }
            } else if (SessionPreference.VALUE_FUNCTION_NAME_ON_RECORDING_PREPARED
                    .equals(functionName)) {
                if (mMessageTransListener != null) {
                    mMessageTransListener.onTalkRecordingPrepared();
                }
            } else if (SessionPreference.VALUE_FUNCTION_NAME_ON_RECORDING_EXCEPTION
                    .equals(functionName)) {
                if (mMessageTransListener != null) {
                    mMessageTransListener.onTalkRecordingException();
                }
            } else if (SessionPreference.VALUE_FUNCTION_NAME_ON_RECORDING_START
                    .equals(functionName)) {
                if (mMessageTransListener != null) {
                    mMessageTransListener.onTalkRecordingStart();
                }
            } else if (SessionPreference.VALUE_FUNCTION_NAME_ON_RECORDING_STOP.equals(functionName)) {
                if (mMessageTransListener != null) {
                    mMessageTransListener.onTalkRecordingStop();
                }
            } else if (SessionPreference.VALUE_FUNCTION_NAME_ON_TALK_RESULT.equals(functionName)) {
                // XD added 20150730
                Logger.d(TAG, "!--->VALUE_FUNCTION_NAME_ON_TALK_RESULT");
                String result = JsonTool.getJsonValue(dataParams, SessionPreference.KEY_TYPE, "");
                if (mMessageTransListener != null) {
                    mMessageTransListener.onTalkResult(result);
                }
            } else if (SessionPreference.VALUE_FUNCTION_NAME_ON_TALK_PROTOCOL.equals(functionName)) {
                // get protocol value
                // JSONObject dataParams = JsonTool.getJSONObject(data,
                // SessionPreference.KEY_PARAMS); //xd delete
                String sessionProtocol =
                        JsonTool.getJsonValue(dataParams, SessionPreference.KEY_TYPE, "");
                if (mMessageTransListener != null) {
                    mMessageTransListener.onSessionProtocal(sessionProtocol);
                }
            } else if (SessionPreference.VALUE_FUNCTION_NAME_ON_TTS_PLAY_END.equals(functionName)) {
                // XD 2016-1-25 modify
                String ttsID =
                        JsonTool.getJsonValue(dataParams, SessionPreference.EVENT_PARAM_KEY_TTS_ID);
                if (mMessageTransListener != null) {
                    mMessageTransListener.onPlayEnd(ttsID);
                }
            } else if (SessionPreference.VALUE_FUNCTION_NAME_ON_UPDATE_VOLUME.equals(functionName)) {
                int volume = JsonTool.getJsonValue(dataParams, SessionPreference.KEY_VOLUME, 0);
                if (mMessageTransListener != null) {
                    mMessageTransListener.onUpdateVolume(volume);
                }
            } else if (SessionPreference.VALUE_FUNCTION_NAME_ON_RECOGNIZER_TIMEOUT
                    .equals(functionName)) {
                Logger.d(TAG, "!--->VALUE_FUNCTION_NAME_ON_RECOGNIZER_TIMEOUT");
                if (mMessageTransListener != null) {
                    mMessageTransListener.onRecognizerTimeout();
                }
            } else if (SessionPreference.VALUE_FUNCTION_NAME_ON_CTT_CANCEL.equals(functionName)) {
                Logger.d(TAG, "!--->VALUE_FUNCTION_NAME_ON_CTT_CANCEL");
                if (mMessageTransListener != null) {
                    mMessageTransListener.onCTTCancel();
                }
            } else if (SessionPreference.VALUE_FUNCTION_NAME_ON_AEC_CANCEL.equals(functionName)) {
                Logger.d(TAG, "!--->VALUE_FUNCTION_NAME_ON_AEC_CANCEL");
                if (mMessageTransListener != null) {
                    mMessageTransListener.onAecCancel();
                }
            } else if (SessionPreference.VALUE_FUNCTION_NAME_ON_ONESHOT_RECOGNIZER_TIMEOUT
                    .equals(functionName)) {
                Logger.d(TAG, "!--->VALUE_FUNCTION_NAME_ON_ONESHOT_RECOGNIZER_TIMEOUT");
                if (mMessageTransListener != null) {
                    mMessageTransListener.onOneShotRecognizerTimeout();
                }
            } else if (SessionPreference.VALUE_FUNCTION_NAME_START_FAKE_RECORDING_ANIMATION
                    .equals(functionName)) {
                if (mMessageTransListener != null) {
                    mMessageTransListener.onStartFakeAnimation();
                }
            } else if (SessionPreference.VALUE_FUNCTION_NAME_REQUEST_WAKEUP_WORDS
                    .equals(functionName)) {
                String wakeupWords =
                        JsonTool.getJsonValue(dataParams, SessionPreference.KEY_TYPE, "");
                Logger.d(TAG, "getWakeupWords wakeupWords : " + wakeupWords);
                if (mMessageTransListener != null) {
                    mMessageTransListener.onGetWakeupWords(wakeupWords);
                }
            } else if (SessionPreference.VALUE_FUNCTION_NAME_MAIN_ACTION.equals(functionName)) {
                // add tyz 20151020
                String style = JsonTool.getJsonValue(dataParams, SessionPreference.KEY_TYPE, "");
                Logger.d(TAG, "getStyle style : " + style);
                if (!TextUtils.isEmpty(style)) {
                    int type = Integer.parseInt(style);
                    if (mMessageTransListener != null) {
                        mMessageTransListener.onClickMainActionButton(type);
                    }
                }
            } else if (SessionPreference.VALUE_FUNCTION_NAME_ON_CONTROL_WAKEUP_SUCCESS
                    .equals(functionName)) {
                String wakeupWord =
                        JsonTool.getJsonValue(dataParams, SessionPreference.KEY_WAKEUP_WORD, "");
                if (mMessageTransListener != null) {
                    mMessageTransListener.onControlWakeupSuccess(wakeupWord);
                }
            } else if (SessionPreference.VALUE_FUNCTION_NAME_ON_UPDATE_WAKEUP_WORD_STATUS
                    .equals(functionName)) {
                String status =
                        JsonTool.getJsonValue(dataParams,
                                SessionPreference.KEY_UPDATE_WAKEUP_WORDS_STATUS, "");
                Logger.d(TAG, "onUpdateWakeupWordsStatus status : " + status);
                if (mMessageTransListener != null) {
                    mMessageTransListener.onUpdateWakeupWordsStatus(status);
                }
            } else if (SessionPreference.VALUE_FUNCTION_NAME_ON_RECODING_IDLE.equals(functionName)) {
                Logger.d(TAG, "VALUE_FUNCTION_NAME_ON_RECODING_IDLE status : ");
                if (mMessageTransListener != null) {
                    mMessageTransListener.onTalkRecodingIdle();
                }
            } else if (SessionPreference.VALUE_FUNCTION_NAME_REQUEST_UUID.equals(functionName)) {
                String uuid = JsonTool.getJsonValue(dataParams, SessionPreference.KEY_TYPE, "");
                Logger.d(TAG, "VALUE_FUNCTION_NAME_REQUEST_UUID--UUID : " + uuid);
                if (mMessageTransListener != null) {
                    mMessageTransListener.onGetUuid(uuid);
                }
            } else if (SessionPreference.VALUE_FUNCTION_NAME_ON_SWITCH_TTS.equals(functionName)) {
                String ttsTimbre =
                        JsonTool.getJsonValue(dataParams, SessionPreference.KEY_TYPE, "");
                Logger.d(TAG, "onCallBackFunction--ON_SWITCH_TTS-" + ttsTimbre);
                if (mMessageTransListener != null) {
                    mMessageTransListener.onSwitchTTS(ttsTimbre);
                }
            } else if (SessionPreference.VALUE_FUNCTION_NAME_ON_COPY_TTS_MODEL.equals(functionName)) {
                JSONObject typeObj = JsonTool.getJSONObject(dataParams, SessionPreference.KEY_TYPE);
                String modelName = JsonTool.getJsonValue(typeObj, "name", "");
                String state =
                        JsonTool.getJsonValue(typeObj, SessionPreference.PARAM_KEY_STATE,
                                SessionPreference.VALUE_STATE_SUCCESS);// success / fail
                Logger.d(TAG, "onCallBackFunction--ON_COPY_TTS_MODEL--name:" + modelName
                        + "; state:" + state);
                if (mMessageTransListener != null) {
                    if (SessionPreference.VALUE_STATE_SUCCESS.equals(state)) {
                        mMessageTransListener.onCopyTTSModelSuccess(modelName);
                    } else if (SessionPreference.VALUE_STATE_FAIL.equals(state)) {
                        String failcause =
                                JsonTool.getJsonValue(typeObj, SessionPreference.KEY_TTS_FAILCAUSE,
                                        "");
                        mMessageTransListener.onCopyTTSModelFail(modelName, failcause);
                    }
                }
            } else if (SessionPreference.VALUE_FUNCTION_NAME_TTS_MODLE_LIST.equals(functionName)) {
                Logger.d(TAG, "onCallBackFunction--TTS_MODLE_LIST");
                JSONObject typeObj = JsonTool.getJSONObject(dataParams, SessionPreference.KEY_TYPE);
                if (mMessageTransListener != null) {
                    mMessageTransListener.onGetTTSModelList(typeObj.toString());
                }
            } else if (SessionPreference.VALUE_FUNCTION_NAME_ON_ADDED_FAVORITE_ADDRESS
                    .equals(functionName)) {
                JSONObject typeObj = JsonTool.getJSONObject(dataParams, SessionPreference.KEY_TYPE);
                String locationType = JsonTool.getJsonValue(typeObj, "locationtype");
                String locationInfo = JsonTool.getJsonValue(typeObj, "locationinfo");
                Logger.d(TAG, "ON_ADDED_FAVORITE_ADDRESS--locationInfo:" + locationInfo);
                if (mMessageTransListener != null) {
                    mMessageTransListener.onAddedFavoriteAddress(locationType, locationInfo);
                }
            } else if (SessionPreference.VALUE_FUNCTION_NAME_PUSH_SERVICE.equals(functionName)) {
                Logger.d(TAG,
                        "VALUE_FUNCTION_NAME_PUSH_SERVICE--dataParams:" + dataParams.toString());
                String pushModelStr = JsonTool.getJsonValue(dataParams, SessionPreference.KEY_TYPE);
                PushModle pushModle = new Gson().fromJson(pushModelStr, PushModle.class);
                if (mMessageTransListener != null) {
                    mMessageTransListener.onPushMessageReceive(pushModle);
                }

            } else if (SessionPreference.VALUE_FUNCTION_NAME_BIND_SUCCESS.equals(functionName)) {
                Logger.d(TAG,
                        "VALUE_FUNCTION_NAME_BIND_SUCCESS--dataParams:" + dataParams.toString());
                if (mMessageTransListener != null) {
                    mMessageTransListener.onShowBindInfo(dataParams.toString());
                }
            } else if (SessionPreference.VALUE_FUNCTION_NAME_ON_SPEAKER_SPEECH_STARTED
                    .equals(functionName)) {
                mMessageTransListener.onSpeakerSpeechDetected();
            } else if (SessionPreference.VALUE_FUNCTION_NAME_GET_CURRENT_TTS_TYPE
                    .equals(functionName)) {
                Logger.d(
                        TAG,
                        "VALUE_FUNCTION_NAME_GET_CURRENT_TTS_TYPE--dataParams:"
                                + dataParams.toString());

                String ttstype = JsonTool.getJsonValue(dataParams, SessionPreference.KEY_TYPE, "");
                mMessageTransListener.getCurrentTTSType(ttstype);
            }
        }
    }

    /**
     * send Response to VUI service
     * 
     * @param message: call Back Json
     */
    public void handlertSendResponse(String message) {
        Logger.d(TAG, "handlertSendResponse message : " + message);
        JSONObject jobj = JsonTool.parseToJSONObject(message);
        JSONObject data = JsonTool.getJSONObject(jobj, SessionPreference.KEY_DATA);
        String callName = JsonTool.getJsonValue(data, SessionPreference.KEY_FUNCTION_NAME);
        String callID = JsonTool.getJsonValue(jobj, SessionPreference.KEY_CALL_ID);
        String domain = null;// add tyz 0714
        Logger.d(TAG, "!--->testSendResponse()---callName = " + callName + "; callID = " + callID);
        JSONObject dataParams = JsonTool.getJSONObject(data, SessionPreference.KEY_PARAMS); // xd
                                                                                            // added
        String param = dataParams.toString(); // TODO：param
        Logger.d(TAG, "!--->handlertSendResponse()-----param = " + param);
        String state = "SUCCESS";// TODO: SUCCESS / FAIL
        sendResponse(callID, callName, state, domain, param);
    }

    /**
     * send Response to VUI service
     * 
     * @param callID
     * @param callName
     * @param state
     * @param param
     */
    public void sendResponse(String callID, String callName, String state, String domain,
            String params) {
        Logger.d(TAG, "!--->sendResponse()-----state = " + state);
        String response =
                "{\"type\":\"RESP\",\"data\":{\"moduleName\":\"GUI\",\"callID\":\"" + callID
                        + "\",\"callName\":\"" + callName + "\",\"domain\":\"" + domain
                        + "\",\"state\":\"" + state + "\",\"params\":" + params + "}}";
        Logger.d(TAG, "sendResponse()-----response = " + response);

        if (mMessageTransListener != null) {
            mMessageTransListener.onSendMsg(response);
        }
    }

    /**
     * 
     * @param ISystemCallTransitionListener
     */
    public void setMessageTransListener(ISystemCallTransitionListener listener) {
        mMessageTransListener = listener;
    }

}
