/**
 * Copyright (c) 2012-2015 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : GuiProtocolUtil.java
 * @ProjectName : uniCarGui
 * @PakageName : com.unisound.unicar.gui.utils
 * @version : 1.0
 * @Author : Xiaodong.He
 * @CreateDate : 2015-08-19
 */
package com.unisound.unicar.gui.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.unisound.unicar.gui.preference.SessionPreference;

/**
 * 
 * @author xiaodong
 * @date 20150819
 */
public class GuiProtocolUtil {

    // public static final String EVENT_PARAM_KEY_TTS_END_RECOGNIZER = "RECOGNIZER";
    // public static final String EVENT_PARAM_KEY_TTS_END_WAKEUP = "WAKEUP";


    /**
     * get Register SystemCall Protocol
     * 
     * @param context
     * @return
     */
    public static String getRegisterSystemCallProtocol(Context context) {
        JSONObject main = new JSONObject();
        try {
            main.put("type", "REG");
            JSONObject data = new JSONObject();
            data.put("version", PackageUtil.getAppVersionName(context));
            data.put("moduleName", "GUI");
            data.put("callNameList", getSystemCallList());
            main.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return main.toString();
    }

    /**
     * SystemCall add here
     * 
     * @return
     */
    private static JSONArray getSystemCallList() {
        ArrayList<String> namelist = new ArrayList<String>();
        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_SET_STATE);
        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_IS_ASR_COMPILE_DONE);
        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_ON_RECORDING_START);
        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_ON_RECORDING_STOP);
        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_ON_TALK_PROTOCOL);
        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_ON_UPDATE_VOLUME);
        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_ON_TTS_PLAY_END);
        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_ON_RECORDING_PREPARED);
        // XD added 20150807
        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_ON_RECORDING_EXCEPTION);
        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_ON_TALK_RESULT);
        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_FETCH_UPDATE_CONTACT_DONE);
        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_FETCH_UPDATE_MEDIA_DONE);
        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_FETCH_UPDATE_APP_DONE);
        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_ON_RECOGNIZER_TIMEOUT);
        // add tyz ctt cancel systemcall
        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_ON_CTT_CANCEL);
        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_MAIN_ACTION);// do main action

        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_ON_ONESHOT_RECOGNIZER_TIMEOUT);
        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_START_FAKE_RECORDING_ANIMATION);
        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_REQUEST_WAKEUP_WORDS);
        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_ON_CONTROL_WAKEUP_SUCCESS);
        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_ON_UPDATE_WAKEUP_WORD_STATUS);
        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_ON_RECODING_IDLE);
        // XD 2015-12-22 added
        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_REQUEST_UUID);
        // XD 2016-01-11 added
        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_ON_ENGINE_INFO);
        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_ON_SWITCH_TTS);
        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_ON_COPY_TTS_MODEL);
        // XD 2016-01-18 added
        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_TTS_MODLE_LIST);
        // XD 2016-01-29 added
        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_ON_ADDED_FAVORITE_ADDRESS);

        // tyz add 2016-03-21
        // namelist.add(SessionPreference.VALUE_FUNCTION_NAME_PUSH_SERVER_PARAM);
        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_PUSH_SERVICE);

        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_BIND_SUCCESS);

        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_ON_SPEAKER_SPEECH_STARTED);


        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_ON_AEC_CANCEL);
        namelist.add(SessionPreference.VALUE_FUNCTION_NAME_GET_CURRENT_TTS_TYPE);


        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < namelist.size(); i++) {
            JSONObject name = new JSONObject();
            try {
                name.put("callName", namelist.get(i).toString());
                jsonArray.put(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }

    /**
     * 
     * @author xiaodong.he
     * @date 2016-2-1
     * @param type:{@link SessionPreference#PARAM_TYPE_PLAY_TTS}
     *        {@link SessionPreference#PARAM_TYPE_NOT_PLAY_TTS}
     * @param reason
     * @return
     */
    public static String getCTTProtocol(String type, String reason) {
        JSONObject param = new JSONObject();
        try {
            param.put(SessionPreference.PARAM_KEY_TYPE, type);
            param.put(SessionPreference.KEY_REASON, reason);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return param.toString();
    }

    /**
     * get Setting Protocol Param
     * 
     * @author xiaodong.he
     * @date 2015-11-23
     * @param state
     * @param type
     * @return
     */
    public static String getSettingProtocolParam(String state, String type) {
        return getSettingProtocolParam(state, type, null);
    }

    public static String getSettingProtocolParam(String state, String type, String actionType) {
        JSONObject param = new JSONObject();
        try {
            param.put(SessionPreference.PARAM_KEY_STATE, state);
            param.put("service", "cn.yunzhisheng.setting");
            param.put("code", "SETTING_EXEC");
            if (!TextUtils.isEmpty(type)) {
                param.put(SessionPreference.PARAM_KEY_TYPE, type);
                param.put(SessionPreference.PARAM_KEY_ACTION_TYPE, actionType);
            }

            JSONObject semantic = new JSONObject(); // semantic
            JSONObject intent = new JSONObject(); // intent
            intent.put("confirm", state);
            semantic.putOpt("intent", intent);
            param.put("semantic", semantic);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return param.toString();
    }

    /**
     * 
     * @author xiaodong.he
     * @date 2016-1-28
     * @param locationJson
     * @param setType {@link SessionPreference#PARAM_ADDRESS_TYPE_HOME}
     *        {@link SessionPreference#PARAM_ADDRESS_TYPE_COMPANY}
     * @return
     */
    public static String getSetFavoriteAddressProtocol(String locationJson, String setType) {
        return getSettingProtocolParam(locationJson, setType);
    }

    /**
     * @param locationJson
     * @param setType {@link SessionPreference#PARAM_ADDRESS_TYPE_HOME}
     *        {@link SessionPreference#PARAM_ADDRESS_TYPE_COMPANY}
     * @return
     */
    public static String getGoFavoriteAddressProtocol(String locationJson, String type) {
        JSONObject param = new JSONObject();
        try {
            param.put("service", "cn.yunzhisheng.setting");
            param.put("code", "SETTING_EXEC");
            param.put(SessionPreference.PARAM_KEY_TYPE, type);
            param.put(SessionPreference.KEY_LOCATION, locationJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return param.toString();
    }

    /**
     * getSetVersionLevelProtocol
     * 
     * @author xiaodong.he
     * @date 2015-11-23
     * @param level {@link SessionPreference#PARAM_VERSION_LEVEL_EXP}
     *        {@link SessionPreference#PARAM_VERSION_LEVEL_STANDRARD}
     *        {@link SessionPreference#PARAM_VERSION_LEVEL_HIGH}
     * @param type {@link SessionPreference#PARAM_VERSION_LEVEL_TYPE_IDEL}
     *        {@link SessionPreference#PARAM_VERSION_LEVEL_TYPE_RECORDING}
     * @return
     */
    public static String getSetVersionLevelProtocol(String level, String type) {
        return getSettingProtocolParam(level, type);
    }

    /**
     * 
     * @author xiaodong.he
     * @date 2016-01-12
     * @param state {@link SessionPreference#PARAM_TTS_TIMBRE_STARNAND}
     *        {@link SessionPreference#PARAM_TTS_TIMBRE_SEXY}
     * @param isNeedPlayTTS
     * @return
     */
    public static String getSetTtsTimbreProtocol(String state, String type, String actionType) {
        return getSettingProtocolParam(state, type, actionType);
    }

    /**
     * 
     * @param protocolName
     * @param name
     * @param number
     * @return
     */
    public static String getSmsReplyEventProtocol(String protocolName, String name, String number) {
        return "{\"service\":\"cn.yunzhisheng.sms\",\"semantic\":{\"intent\":{\"confirm\":\""
                + protocolName + "\",\"name\":\"" + name + "\",\"number\":\"" + number
                + "\"}},\"code\":\"SMS_REPLY\",\"rc\":0}";
    }

    /**
     * 
     * @param protocolName
     * @param name
     * @param number
     * @param content
     * @return
     */
    public static String getSmsFastReplyEventProtocol(String protocolName, String name,
            String number, String content) {
        return "{\"service\":\"cn.yunzhisheng.sms\",\"semantic\":{\"intent\":{\"confirm\":\""
                + protocolName + "\",\"name\":\"" + name + "\",\"number\":\"" + number
                + "\",\"content\":\"" + content + "\"}},\"code\":\"SMS_FAST_REPLY\",\"rc\":0}";
    }



    /**
     * get PlayTTS Event Param
     * 
     * @param ttsText
     * @param recognizerType : wakeup / recognizer
     * @return
     */
    public static String getPlayTTSEventParam(String ttsText, String recognizerType) {
        return getPlayTTSEventParam(ttsText, "", SessionPreference.PARAM_TTS_FROM_UNICARGUI,
                recognizerType);
    }

    /**
     * 
     * @date 2016-1-25
     * @author xiaodong.he
     * 
     * @param ttsText
     * @param ttsId : stringId
     * @param ttsFrom: {@link SessionPreference#PARAM_TTS_FROM_UNICARGUI} 、
     *        {@link SessionPreference#PARAM_TTS_FROM_UNICARNAVI}
     * @param recognizerType : wakeup / recognizer
     * @return
     */
    public static String getPlayTTSEventParam(String ttsText, String ttsId, String ttsFrom,
            String recognizerType) {
        JSONObject param = new JSONObject();
        try {
            param.put(SessionPreference.EVENT_PARAM_KEY_TTS_END_KEY_RECOGNIZER_TYPE, recognizerType);
            param.put(SessionPreference.EVENT_PARAM_KEY_TTS_TEXT, ttsText);
            if (!TextUtils.isEmpty(ttsId)) {
                param.put(SessionPreference.EVENT_PARAM_KEY_TTS_ID, ttsId);
            }
            param.put(SessionPreference.EVENT_PARAM_KEY_TTS_FROM, ttsFrom);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return param.toString();
    }

    /**
     * 
     * @param type
     * @return
     */
    public static String getTypeParamProtocol(String type) {
        JSONObject protocol = new JSONObject();
        try {
            protocol.put(SessionPreference.EVENT_PARAM_KEY_TYPE, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return protocol.toString();
    }

    /**
     * 
     * @param type: SessionPreference.EVENT_PARAM_SWITCH_CLOSE /
     *        SessionPreference.EVENT_PARAM_SWITCH_OPEN
     * @return
     */
    public static String getTimeOutParamProtocol(String type) {
        return getTypeParamProtocol(type);
    }

    /**
     * get Recording Control Param Protocol
     * 
     * @param param: SessionPreference.PARAM_RECORDING_CONTROL_START /
     *        SessionPreference.PARAM_RECORDING_CONTROL_STOP
     * @param domain
     * @return
     */
    public static String getRecordingControlParamProtocol(String param, String domain) {
        JSONObject protocol = new JSONObject();
        try {
            protocol.put(SessionPreference.EVENT_PARAM_KEY_TYPE, param);
            protocol.put(SessionPreference.KEY_DOMAIN, domain);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return protocol.toString();
    }

    /**
     * get Change Location Param Protocol
     * 
     * @param keyword
     * @return
     */
    public static String getChangeLocationParamProtocol(String keyword) {
        JSONObject protocol = new JSONObject();
        try {
            protocol.put(SessionPreference.EVENT_PARAM_KEY_LOACTION_KEYWORD, keyword);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return protocol.toString();
    }

    /**
     * 
     * @param poiSearchType
     * @return
     */
    public static String getUpdateAroundSearchParamProtocol(String poiSearchType) {
        return getChangeLocationParamProtocol(poiSearchType);
    }

    /**
     * get LoadMore ParamProtocol
     * 
     * @author xiaodong.he
     * @date 2015-12-18
     * @param domian
     * @return
     */
    public static String getLoadMoreParamProtocol(String domian) {
        JSONObject protocol = new JSONObject();
        try {
            protocol.put(SessionPreference.KEY_DOMAIN, domian);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return protocol.toString();
    }

}
