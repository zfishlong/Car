package com.unisound.unicar.gui.preference;

public class SessionPreference {
    /********************** session key *********************/
    // common
    public static final String KEY_DOMAIN = "domain"; // session status JSON-->domain
    public static final String KEY_TYPE = "type";
    public static final String KEY_VOLUME = "volume";
    public static final String KEY_WAKEUP_WORD = "wakeupWord";
    public static final String KEY_UPDATE_WAKEUP_WORDS_STATUS = "updateWakeupWordsStatus";



    // xiaodong added begin
    public static final String KEY_DATA_SCHEDULE_TYPE = "schedule_type";// JSON-->data-->schedule_type
    public static final String KEY_DATA_CONTACTS = "contacts";
    public static final String KEY_FUNCTION_ID = "functionID";
    public static final String KEY_FUNCTION_NAME = "functionName";
    // xiaodong added end

    public static final String KEY_ORIGIN_TYPE = "origin_type";
    public static final String KEY_ORIGIN_CODE = "origin_code";
    public static final String KEY_DATA = "data";
    public static final String KEY_PARAMS = "params"; // xd added
    public static final String KEY_CALL_ID = "callID";// xd added

    public static final String KEY_LOCATION = "location";


    public static final String KEY_QUESTION = "question";
    public static final String KEY_ANSWER = "answer";
    public static final String KEY_TTS_ANSWER = "tts";
    public static final String KEY_RESULT = "result";
    public static final String KEY_ON_CANCEL = "on_confirm_cancel";// xd modify 20150702
    public static final String KEY_ON_OK = "on_confirm_ok";// xd modify 20150702
    public static final String KEY_URL = "url";
    public static final String KEY_KEYWORD = "keyword";
    public static final String KEY_CLASS_NAME = "class_name";
    public static final String KEY_PACKAGE_NAME = "package_name";

    public static final String KEY_CATEGORY = "category";
    public static final String KEY_NAME = "name";
    public static final String KEY_NUMBER = "number"; // xd added
    public static final String KEY_TO_SELECT = "to_select"; // xd added
    public static final String KEY_CALL_TO_SELECT = "call_to_select";// add tyz 0824

    // music XD added 20150901
    public static final String KEY_MUSIC_RESULT_SONG = "song";
    public static final String KEY_MUSIC_RESULT_ARTIST = "artist";

    // Audio FM XD added 20151106
    public static final String KEY_AUDIO_KEYWORD = "keyword";
    public static final String KEY_AUDIO_CATEGORY = "category";
    public static final String KEY_AUDIO_EPISODE = "episode";

    // chat
    public static final String KEY_CHAT_H5URL = "h5Url"; // TODO: XD 200151013 added

    // tv
    public static final String KEY_CONTENT = "content";
    public static final String KEY_PIC = "pic";

    public static final String KEY_FREQUENCY_LIST = "frequencyList";
    public static final String KEY_FREQUENCY = "frequency";
    public static final String KEY_UNIT = "unit";
    public static final String KEY_UNSUPPROT_TEXT = "unsupportText";
    public static final String KEY_ERROR_CODE = "errorcode";

    /** XD 2016-1-11 added */
    public static final String KEY_ENGINE_INFO_ASRKEY = "asrkey";
    public static final String KEY_ENGINE_INFO_ASRVALUE = "asrvalue";

    public static final String KEY_OPERATOR = "operator";
    public static final String KEY_OPERANDS = "operands";

    /********************** session value *********************/
    // xiaodong 20150618 added Begin
    // wakeup初始化完成
    public static final int VALUE_SET_STATE_TYPE_WAKEUP_INIT_DONE = 0;
    // 唤醒成功
    public static final int VALUE_SET_STATE_TYPE_WAKEUP_SUCCESS = 1;
    // 数据编译完成
    public static final int VALUE_SET_STATE_TYPE_ASR_COMPILE_FINISH = 2;
    // 取联系人
    public static final int VALUE_SET_STATE_TYPE_WRITE_CONTACT_INFO = 5;
    // 取媒体数据
    public static final int VALUE_SET_STATE_TYPE_WRITE_MEDIA_INFO = 9;
    // TODO: GET SUPPORT DOMAIN LIST
    public static final int VALUE_SET_STATE_TYPE_GET_SUPPORT_DOMAIN_LIST = 8;
    // 读取app add by tyz 0709
    public static final int VALUE_SET_STATE_TYPE_WRITE_APPS_INFO = 10;
    // DORESO 唤醒成功
    public static final int VALUE_SET_STATE_TYPE_WAKEUP_SUCCESS_DORESO = 19;

    /** XD 2016-1-11 added */
    public static final int VALUE_ENGINE_INFO_ASRKEY_NO_LIMIT = 0;
    public static final int VALUE_ENGINE_INFO_ASRKEY_TIME_LIMIT = 1;
    public static final int VALUE_ENGINE_INFO_ASRKEY_PACKAGE_LIMIT = 2;
    public static final int VALUE_ENGINE_INFO_ASRKEY_TIME_PACKAGE_LIMIT = 3;


    // functionID = 0
    public static final String VALUE_FUNCTION_NAME_SET_STATE = "setState";
    // functionID = 1
    public static final String VALUE_FUNCTION_NAME_ON_RECORDING_START = "onRecordingStart";
    // functionID = 2
    public static final String VALUE_FUNCTION_NAME_ON_RECORDING_STOP = "onRecordingStop";
    // functionID = 3
    public static final String VALUE_FUNCTION_NAME_ON_TALK_PROTOCOL = "onTalkProtocol";
    // functionID = 4
    public static final String VALUE_FUNCTION_NAME_IS_ASR_COMPILE_DONE = "isASRCompileDone";
    // functionID = 5
    public static final String VALUE_FUNCTION_NAME_ON_UPDATE_VOLUME = "onUpdateVolume";
    // functionID = 6
    public static final String VALUE_FUNCTION_NAME_ON_TTS_PLAY_END = "onTTSPlayEnd";
    // functionID = 7
    public static final String VALUE_FUNCTION_NAME_ON_RECORDING_PREPARED = "onRecordingPrepared";
    /** TODO: functionID = 8 XD added 20150807 */
    public static final String VALUE_FUNCTION_NAME_ON_RECORDING_EXCEPTION = "onRecordingException";
    // functionid= 16
    public static final String VALUE_FUNCTION_NAME_ON_TALK_RESULT = "onTalkResult";
    // functionid= 17
    public static final String VALUE_FUNCTION_NAME_ON_RECOGNIZER_TIMEOUT = "onRecognizerTimeout";
    public static final String VALUE_FUNCTION_NAME_ON_ONESHOT_RECOGNIZER_TIMEOUT =
            "onOneShotRecognizerTimeout";
    // functionid= 20
    public static final String VALUE_FUNCTION_NAME_START_FAKE_RECORDING_ANIMATION =
            "onStartFakeAnimation";

    // functionid= 22
    public static final String VALUE_FUNCTION_NAME_REQUEST_WAKEUP_WORDS = "requestWakeupWords";

    // functionid= 18
    public static final String VALUE_FUNCTION_NAME_ON_CTT_CANCEL = "onCTTCancel";

    public static final String VALUE_FUNCTION_NAME_ON_AEC_CANCEL = "onAecCancel";

    // functionid= 24
    public static final String VALUE_FUNCTION_NAME_MAIN_ACTION = "onClickMainActionButton";
    // functionid= 23
    public static final String VALUE_FUNCTION_NAME_MAIN_ACTION_CALL = "onClickMainActionButtonCall";
    // functionid= 25
    public static final String VALUE_FUNCTION_NAME_MAIN_ACTION_MUSIC =
            "onClickMainActionButtonMusic";
    // functionid= 26
    public static final String VALUE_FUNCTION_NAME_MAIN_ACTION_LOCALSEARCH =
            "onClickMainActionButtonLocaltion";

    public static final String VALUE_FUNCTION_NAME_ON_CONTROL_WAKEUP_SUCCESS =
            "onControlWakeupSuccess";

    // functionid= 27
    public static final String VALUE_FUNCTION_NAME_ON_UPDATE_WAKEUP_WORD_STATUS =
            "onUpdateWakeupWordsStatus";

    // functionid= 28
    public static final String VALUE_FUNCTION_NAME_ON_RECODING_IDLE = "onRecodingIdle";

    // functionid= 47
    public static final String VALUE_FUNCTION_NAME_BIND_SUCCESS = "bindSuccessShowInfoToGui";

    public static final String VALUE_FUNCTION_NAME_FETCH_UPDATE_CONTACT_DONE =
            "onFetchUpDateContactDone";
    public static final String VALUE_FUNCTION_NAME_FETCH_UPDATE_MEDIA_DONE =
            "onFetchUpDateMediaDone";
    public static final String VALUE_FUNCTION_NAME_FETCH_UPDATE_APP_DONE = "onFetchUpDateAppDone";

    /** functionid= 30, XD 2015-12-22 added */
    public static final String VALUE_FUNCTION_NAME_REQUEST_UUID = "requestUuid";

    /** onEngineInfo: state = 1 is TIME LIMIT Test Version XD 2016-01-11 added */
    public static final String VALUE_FUNCTION_NAME_ON_ENGINE_INFO = "onEngineInfo";

    /** onSwitchTTS Success XD 2016-01-11 added */
    public static final String VALUE_FUNCTION_NAME_ON_SWITCH_TTS = "onSwitchTTS";

    /** onCopyTTSModel Success/Fail, XD 2016-01-11 added */
    public static final String VALUE_FUNCTION_NAME_ON_COPY_TTS_MODEL = "CopyTTSModle";

    /** TTSModleList, XD 2016-01-18 added */
    public static final String VALUE_FUNCTION_NAME_TTS_MODLE_LIST = "TTSModleList";

    /** onAddedFavoriteAddress, XD 2016-01-29 added */
    public static final String VALUE_FUNCTION_NAME_ON_ADDED_FAVORITE_ADDRESS =
            "onAddedFavoriteAddress";
    public static final String VALUE_FUNCTION_NAME_PUSH_SERVICE = "pushServerResult";

    public static final String VALUE_FUNCTION_NAME_ON_SPEAKER_SPEECH_STARTED =
            "onSpeakerSpeechDetected";

    public static final String VALUE_FUNCTION_NAME_GET_CURRENT_TTS_TYPE = "getCurrentTTSType";
    // add tyz 2016-03-21 for pushserver
    // public static final String VALUE_FUNCTION_NAME_PUSH_SERVER_PARAM = "pushServerResult";

    public static final int SAVE_CONTACT_DATA_DONE = 0;
    public static final int SAVE_MEDIA_DATA_DONE = 1;
    public static final int SAVE_APPS_DATA_DONE = 2;
    public static final int SAVE_UPDATE_CONTACT_DATA_DONE = 3;
    public static final int SAVE_UPDATE_MEDIA_DATA_DONE = 4;
    public static final int SAVE_UPDATE_APPS_DATA_DONE = 5;

    public static final String VALUE_SCHEDULE_TYPE_UNSUPPORT = "VALUE_UNSUPPORT";
    /** first exception in domain */
    public static final String VALUE_SCHEDULE_TYPE_SCHEDULE_ERROR = "SCHEDULE_ERROR"; // add
                                                                                      // 20150701
    public static final String VALUE_SCHEDULE_TYPE_SCHEDULE_ERROR_END = "SCHEDULE_ERROR_END"; // add
                                                                                              // 20150703
    public static final String VALUE_SCHEDULE_TYPE_NO_PERSON = "NO_PERSON";// can't find contacts
    public static final String VALUE_SCHEDULE_TYPE_NO_NUMBER = "NO_NUMBER";// find contacts but no
                                                                           // number
    public static final String VALUE_SCHEDULE_TYPE_ACTION_CANCEL = "ACTION_CANCEL";
    public static final String VALUE_SCHEDULE_TYPE_ONLINE_ERROR = "ONLINE_ERROR";// not connect
                                                                                 // network, add
                                                                                 // 20150706
    public static final String VALUE_SCHEDULE_TYPE_WEATHER_ERROR_SHOW = "WEATHER_ERROR_SHOW";// added
                                                                                             // 20150715

    public static final String VALUE_SCHEDULE_TYPE_INCOMING_CALL = "INCOMING_CALL_SHOW";
    public static final String VALUE_SCHEDULE_TYPE_INCOMING_CALL_OPERATOR =
            "INCOMING_CALL_OPERATOR_SHOW";

    // added 20150717 for incoming SMS
    public static final String VALUE_SCHEDULE_TYPE_INCOMING_SMS = "INCOMING_SMS_SHOW";
    public static final String VALUE_SCHEDULE_TYPE_INCOMING_SMS_OPERATOR =
            "INCOMING_SMS_OPERATOR_SHOW";


    public static final String VALUE_SCHEDULE_TYPE_INCOMING_PUSH_MESSAGE =
            "INCOMING_PUSH_MESSAGE_SHOW";
    public static final String VALUE_SCHEDULE_TYPE_INCOMING_PUSH_MESSAGE_OPERATOR =
            "INCOMING_PUSH_MESSAGE_OPERATOR_SHOW";

    public static final String VALUE_SCHEDULE_TYPE_MAIN_ACTION_NAVI = "MAIN_ACTION_NAVI";
    public static final String VALUE_SCHEDULE_TYPE_MAIN_ACTION_AROUND = "MAIN_ACTION_LOCALSEARCH";

    public static final String VALUE_SCHEDULE_TYPE_LOCATE_CURRENT_POI = "LOCATE_CURRENT_POI";

    // OPERATOR VALUE
    public static final String VALUE_OPERATOR_INCOMING_CALL_ANSWER = "OK";
    public static final String VALUE_OPERATOR_INCOMING_CALL_HUNGUP = "CANCEL";

    public static final String VALUE_OPERATOR_INCOMING_SMS_BROADCAST = "OK";
    // public static final String VALUE_OPERATOR_INCOMING_SMS_FAST_REPLY = "REPLY";
    public static final String VALUE_OPERATOR_INCOMING_SMS_CANCEL = "CANCEL";
    // xiaodong 20150618 added End


    public static final String VALUE_OPERATOR_INCOMING_PUSH_MESSAGE_CONFIRM = "OK";

    public static final String VALUE_OPERATOR_INCOMING_PUSH_MESSAGE_CANCEL = "CANCEL";

    public static final String VALUE_SCHEDULE_TYPE_UNKNOWN_ERROR = "UNKNOWN_ERROR";


    public static final String VALUE_SCHEDULE_TYPE_NO_APP = "NO_APP";// add tyz 0710 no app

    public static final String VALUE_SCHEDULE_TYPE_LOCATION_NO_RESULT = "LOCATION_NO_RESULT";
    public static final String VALUE_SCHEDULE_TYPE_LOCATION_RESULT_ERROR = "LOCATION_RESULT_ERROR";
    public static final String VALUE_LOCATION_RESULT_TIMEOUT = "LOCATION_RESULT_TIMEOUT";
    public static final String VALUE_SCHEDULE_TYPE_CHAT = "CHAT_SHOW"; // XD added 20150820
    public static final String VALUE_SCHEDULE_TYPE_CHAT_WEB_SHOW = "CHAT_WEB_SHOW"; // XD added
                                                                                    // 20151013
    public static final String VALUE_SCHEDULE_TYPE_MUSIC_RESULT_SHOW = "MUSIC_RESULT_SHOW";// XD
                                                                                           // added
                                                                                           // 20150828
    public static final String VALUE_SCHEDULE_TYPE_AUDIO_SHOW = "AUDIO_SHOW";// XD added 20150922
                                                                             // for XmFM

    public static final String VALUE_SCHEDULE_TYPE_HAND_CHOOSE = "HAND_CHOOSE";// XD added 20150922
                                                                               // for XmFM

    public static final String VALUE_SCHEDULE_TYPE_STOCK_NO_RESULT = "STOCK_NO_RESULT";// WLP added
                                                                                       // 20150821

    // add tyz 20151026
    public static final String VALUE_SCHEDULE_TYPE_LOCALSEARCH_SHOW_ITEM = "LOCALSEARCH_SHOW_ITEM";

    // add tyz 20160224 for wechat
    public static final String VALUE_SCHEDULE_TYPE_INPUT_WECHAT_CONTACTS = "INPUT_WECHAT_CONTACTS";
    public static final String VALUE_SCHEDULE_TYPE_INPUT_WECHAT_CONTENT = "INPUT_WECHAT_CONTENT";
    public static final String VALUE_SCHEDULE_TYPE_NO_WECHAT_CONTACTS = "NO_WECHAT_CONTACTS";
    public static final String VALUE_SCHEDULE_TYPE_MORE_WECHAT_CONTACTS = "MORE_WECHAT_CONTACTS";

    /* < XD 20160121 added for uniCarNavi control Begin */
    public static final String VALUE_SCHEDULE_TYPE_NAVI_CONTROL = "NAVI_CONTROL";

    // OPERATOR VALUES
    public static final String VALUE_OPERATOR_START_NAVIGATION = "START_NAVIGATION";
    public static final String VALUE_OPERATOR_ROUTE_AVOID_CONGESTION = "ROUTE_AVOID_CONGESTION";
    public static final String VALUE_OPERATOR_ROUTE_NO_HIGHWAY = "ROUTE_NO_HIGHWAY";
    public static final String VALUE_OPERATOR_ROUTE_SAVE_MONEY = "ROUTE_SAVE_MONEY";
    public static final String VALUE_OPERATOR_ROUTE_HIGHWAY = "ROUTE_HIGHWAY";
    public static final String VALUE_OPERATOR_ACT_NAVMODE_STANDARD = "ACT_NAVMODE_STANDARD";
    public static final String VALUE_OPERATOR_ACT_NAVMODE_NIGHT = "ACT_NAVMODE_NIGHT";
    // SIMULATE
    public static final String VALUE_OPERATOR_ACT_NAVMODE_DEMO = "ACT_NAVMODE_DEMO";
    public static final String VALUE_OPERATOR_ACT_NAV_PAUSE = "ACT_NAV_PAUSE";
    public static final String VALUE_OPERATOR_ACT_NAV_CONTINUE = "ACT_NAV_CONTINUE";
    public static final String VALUE_OPERATOR_ACT_NAV_SHOWTRAFFIC = "ACT_NAV_SHOWTRAFFIC";
    public static final String VALUE_OPERATOR_ACT_NAV_CLOSETRAFFIC = "ACT_NAV_CLOSETRAFFIC";
    // 关闭|退出导航
    public static final String VALUE_OPERATOR_ACT_NAV_CLOSE = "ACT_NAV_CLOSE";
    /* XD 20160121 added for uniCarNavi control End > */


    public static final String VALUE_OPERATOR_MAP_TO_NARROW = "MAP_TO_NARROW";
    public static final String VALUE_OPERATOR_MAP_TO_ZOOM = "MAP_TO_ZOOM";
    public static final String VALUE_OPERATOR_PREVIEW_THE_WHOLE = "PREVIEW_THE_WHOLE";
    public static final String VALUE_OPERATOR_CONTINUE_NAVI = "CONTINUE_NAVI";
    public static final String VALUE_OPERATOR_MODE_CAR_UP = "MODE_CAR_UP";
    public static final String VALUE_OPERATOR_MODE_NORTH_UP = "MODE_NORTH_UP";
    public static final String VALUE_OPERATOR_MODE_SIMULATE_NAVI = "SIMULATE_NAVI";


    public static final String VALUE_SESSION_BENGIN = "SESSION_BEGIN";
    public static final String VALUE_SESSION_SHOW = "SESSION_SHOW";
    public static final String VALUE_SESSION_END = "SESSION_END";

    public static final String VALUE_TYPE_MUTIPLE_UNSUPPORT = "MUTIPLE_UNSUPPORT";
    public static final String VALUE_TYPE_CONFIRM_UNSUPPORT = "CONFIRM_UNSUPPORT";
    public static String VALUE_TYPE_UNSUPPORT_BEGIN_SHOW = "UNSUPPORT_BEGIN_SHOW";
    public static String VALUE_TYPE_UNSUPPORT_END_SHOW = "UNSUPPORT_END_SHOW";

    public static final String VALUE_TYPE_MUTIPLE_LOCALSEARCH = "MUTIPLE_LOCALSEARCH";

    public static final String VALUE_TYPE_LOCALSEARCH_CONFIRM = "LOCALSEARCH_CONFIRM";

    public static final String VALUE_TYPE_ROUTE_CONFIRM = "ROUTE_CONFIRM";

    public static final String VALUE_TYPE_WAITING = "WAITING";
    public static final String VALUE_TYPE_WEATHER_SHOW = "WEATHER_SHOW";
    public static final String VALUE_TYPE_WEB_SHOW = "WEB_SHOW";
    public static final String VALUE_TYPE_TRANSLATION_SHOW = "TRANSLATION_SHOW";
    public static final String VALUE_TYPE_STOCK_SHOW = "STOCK_SHOW";
    // TODO add by ch Traffic
    public static final String VALUE_TYPE_TRAFFIC_SHOW = "TRAFFIC_SHOW";
    public static final String VALUE_TYPE_TRAFFIC_CONTROL_SHOW = "TRAFFIC_CONTROL_SHOW";
    public static final String VALUE_TYPE_MUSIC_SHOW = "MUSIC_SHOW";
    public static final String VALUE_TYPE_PROG_RECOMMEND = "PROG_RECOMMEND";
    public static final String VALUE_TYPE_PROG_SEARCH_RESULT = "PROG_SEARCH_RESULT";
    public static final String VALUE_TYPE_CHANNEL_PROG_LIST = "CHANNEL_PROG_LIST";
    public static final String VALUE_TYPE_ROUTE_SHOW = "ROUTE_SHOW";
    public static final String VALUE_TYPE_POSITION_SHOW = "POSITION_SHOW";
    public static final String VALUE_TYPE_TALK_SHOW = "TALK_SHOW";
    public static final String VALUE_TYPE_ERROR_SHOW = "ERROR_SHOW";
    public static final String VALUE_TYPE_APP_LAUNCH = "APP_LAUNCH";
    public static final String VALUE_TYPE_FM_SHOW = "FM_SHOW";
    public static final String VALUE_TYPE_FM_NO_SHOW = "FM_NO_SHOW";
    public static final String VALUE_TYPE_APP_UNINSTALL = "APP_UNINSTALL";
    public static final String VALUE_TYPE_APP_EXIT = "APP_EXIT";
    public static final String VALUE_TYPE_SETTING = "SETTING_SHOW";
    public static final String VALUE_TYPE_REMINDER_CONFIRM = "REMINDER_SHOW";
    public static final String VALUE_TYPE_REMINDER_OK = "REMINDER_OK";
    public static final String VALUE_TYPE_POI_SHOW = "POI_SHOW";
    public static final String VALUE_TYPE_MULTIPLE_SHOW = "MULTIPLE_SHOW";
    public static final String VALUE_TYPE_UI_HANDLE_SHOW = "UI_HANDLE_SHOW";
    // add tyz 20150701
    public static final String VALUE_TYPE_LOCALSEARCH_SHOW = "LOCALSEARCH_SHOW";
    public static final String VALUE_TYPE_LOCALSEARCH_CALL_SHOW = "LOCALSEARCH_CALL_SHOW";// add tyz
                                                                                          // 0824
    public static final String VALUE_SCHEDULE_TYPE_LOCALSEARCH_SHOW = "LOCALSEARCH_SHOW";
    public static final String VALUE_SCHEDULE_TYPE_CALENDAR_SHOW = "CALENDAR_SHOW";
    public static final String VALUE_SCHEDULE_TYPE_WEB_SEARCH_SHOW = "WEB_SEARCH_SHOW";
    // add tyz 20150701

    // note
    public static final String VALUE_TYPE_NOTE_SHOW = "NOTE_SHOW";
    // alarm
    public static final String VALUE_TYPE_ALARM_SHOW = "ALARM_SHOW";
    // flight
    public static final String VALUE_FLIGHT_ONEWAY = "FLIGHT_ONEWAY";
    public static final String VALUE_FLIGHT_ABNORMAL = "FLIGHT_ABNORMAL";
    // position
    public static final String SHOW_TYPE_POSITION = "POSITION_SHOW";

    // multiple app
    public static final String VALUE_TYPE_APP_MUTIPLEP_SHOW = "MUTIPLE_APP";

    // music value
    public static final String VALUE_MUSIC_RESULT_TYPE_DORESO = "doreso"; // xd added 20150901

    // sms
    public static final String VALUE_SCHEDULE_TYPE_INPUT_CONTENT_SMS = "INPUT_CONTENT_SMS"; // xd
                                                                                            // added
                                                                                            // 20150706
    public static final String VALUE_SCHEDULE_TYPE_INPUT_SMS_CONFIRM = "INPUT_SMS_CONFIRM";// xd
                                                                                           // added
                                                                                           // 20150709

    public static final String VALUE_SCHEDULE_TYPE_SMS_OK = "SMS_OK";
    public static final String VALUE_TYPE_SMS_CANCEL = "SMS_CANCEL";
    public static final String VALUE_TYPE_SMS_READ = "SMS_READ";

    // contact
    public static final String VALUE_TYPE_CONTACT_SHOW = "CONTACT_SHOW";
    public static final String VALUE_TYPE_CONTACT_ADD = "CONTACT_ADD";

    // code
    public static final String VALUE_CODE_ANSWER = "ANSWER";
    public static final String VALUE_TYPE_CHANNEL_SWITCH_SHOW = "CHANNEL_SWITCH_SHOW";
    public static final String VALUE_TYPE_SHOP_SHOW = "SHOP_SHOW";
    public static final String VALUE_TYPE_BROADCAST_SHOW = "BROADCAST_SHOW";

    // route
    public static final String VALUE_ROUTE_METHOD_UNKNOWN = "ROUTE_METHOD_UNKNOWN";
    public static final String VALUE_ROUTE_METHOD_BUS = "BUS";
    public static final String VALUE_ROUTE_METHOD_WALK = "WALK";
    public static final String VALUE_ROUTE_METHOD_CAR = "CAR";

    public static final String VALUE_MUTIPLE_LOCATION = "MUTIPLE_LOCATION";

    // setting
    public static final String VALUE_SETTING_ACT_OPEN = "ACT_OPEN";
    public static final String VALUE_SETTING_ACT_CLOSE = "ACT_CLOSE";
    public static final String VALUE_SETTING_ACT_INCREASE = "ACT_INCREASE";
    public static final String VALUE_SETTING_ACT_DECREASE = "ACT_DECREASE";
    public static final String VALUE_SETTING_ACT_MAX = "ACT_MAX";
    public static final String VALUE_SETTING_ACT_MIN = "ACT_MIN";
    public static final String VALUE_SETTING_ACT_CLEAR = "ACT_CLEAR";
    public static final String VALUE_SETTING_ACT_SET = "ACT_SET";
    public static final String VALUE_SETTING_ACT_LOOKUP = "ACT_LOOKUP";
    public static final String VALUE_SETTING_ACT_OPEN_CHANNEL = "ACT_OPEN_CHANNEL";
    public static final String VALUE_SETTING_ACT_RECEIVED = "ACT_RECEIVED";
    public static final String VALUE_SETTING_ACT_MISSED = "ACT_MISSED";
    public static final String VALUE_SETTING_ACT_REDIAL = "ACT_REDIAL";
    public static final String VALUE_SETTING_ACT_DIALED = "ACT_DIALED";
    /** incoming call answer */
    public static final String VALUE_SETTING_ACT_ANSWER = "ACT_ANSWER";
    /** incoming call hang up */
    public static final String VALUE_SETTING_ACT_HANG_UP = "ACT_HANG_UP";
    public static final String VALUE_SETTING_ACT_MATCH = "ACT_MATCH";
    public static final String VALUE_SETTING_ACT_DISCONNECTED = "ACT_DISCONNECTED";
    public static final String VALUE_SETTING_ACT_CANCEL = "ACT_CANCEL";
    public static final String VALUE_SETTING_ACT_PLAY = "ACT_PLAY";
    public static final String VALUE_SETTING_ACT_PAUSE = "ACT_PAUSE";
    public static final String VALUE_SETTING_ACT_STOP = "ACT_STOP";
    public static final String VALUE_SETTING_ACT_RESUME = "ACT_RESUME";
    public static final String VALUE_SETTING_ACT_PREV = "ACT_PREV";
    public static final String VALUE_SETTING_ACT_NEXT = "ACT_NEXT";
    public static final String VALUE_SETTING_ACT_SWITCH = "ACT_SWITCH";
    public static final String VALUE_SETTING_ACT_SET_DESTINATION = "ACT_SET_DESTINATION";
    public static final String VALUE_SETTING_ACT_GO_BACK_HOME = "ACT_GO_BACK_HOME";
    public static final String VALUE_SETTING_ACT_GO_BACK_COMPANY = "ACT_GO_BACK_COMPANY";
    public static final String VALUE_SETTING_ACT_TURN_ON = "ACT_TURN_ON";
    public static final String VALUE_SETTING_ACT_TURN_OFF = "ACT_TURN_OFF";
    public static final String VALUE_SETTING_ACT_REDUCE_SPEED = "ACT_REDUCE_SPEED";
    public static final String VALUE_SETTING_ACT_DEFROST = "ACT_DEFROST";
    public static final String VALUE_SETTING_ACT_ACT_DEMIST = "ACT_DEMIST";
    public static final String VALUE_SETTING_ACT_BLOW_WINDOW = "ACT_BLOW_WINDOW";
    public static final String VALUE_SETTING_ACT_BLOW_FACE = "ACT_BLOW_FACE";
    public static final String VALUE_SETTING_ACT_READ = "ACT_READ";
    public static final String VALUE_SETTING_ACT_UPLOAD = "ACT_UPLOAD";
    public static final String VALUE_SETTING_ACT_PLAY_MESSAGE = "ACT_PLAY_MESSAGE";

    public static final String VALUE_SETTING_OBJ_VALUMN = "OBJ_VALUMN";
    public static final String VALUE_SETTING_OBJ_LIGHT = "OBJ_LIGHT";
    public static final String VALUE_SETTING_OBJ_FLOW = "OBJ_FLOW";
    public static final String VALUE_SETTING_OBJ_RINGTONE = "OBJ_RINGTONE";
    public static final String VALUE_SETTING_OBJ_WALLPAPER = "OBJ_WALLPAPER";
    public static final String VALUE_SETTING_OBJ_FACE = "OBJ_FACE";
    public static final String VALUE_SETTING_OBJ_TIME = "OBJ_TIME";
    public static final String VALUE_SETTING_OBJ_3G = "OBJ_3G";
    public static final String VALUE_SETTING_OBJ_WIFI = "OBJ_WIFI";
    public static final String VALUE_SETTING_OBJ_WIFI_SPOT = "OBJ_WIFI_SPOT";
    public static final String VALUE_SETTING_OBJ_BLUETOOTH = "OBJ_BLUETOOTH";
    public static final String VALUE_SETTING_OBJ_GPS = "OBJ_GPS";
    public static final String VALUE_SETTING_OBJ_ROTATION = "OBJ_ROTATION";
    public static final String VALUE_SETTING_OBJ_AUTOLIGHT = "OBJ_AUTOLIGHT";
    public static final String VALUE_SETTING_OBJ_MODEL_STANDARD = "OBJ_MODEL_STANDARD";
    public static final String VALUE_SETTING_OBJ_MODEL_MUTE = "OBJ_MODEL_MUTE";
    public static final String VALUE_SETTING_OBJ_MODEL_VIBRA = "OBJ_MODEL_VIBRA";
    public static final String VALUE_SETTING_OBJ_MODEL_INAIR = "OBJ_MODEL_INAIR";
    public static final String VALUE_SETTING_OBJ_MODEL_INCAR = "OBJ_MODEL_INCAR";
    public static final String VALUE_SETTING_OBJ_MODEL_SAVEPOWER = "OBJ_MODEL_SAVEPOWER";
    public static final String VALUE_SETTING_OBJ_MODEL_SAVEFLOW = "OBJ_MODEL_SAVEFLOW";
    public static final String VALUE_SETTING_OBJ_MODEL_GUEST = "OBJ_MODEL_GUEST";
    public static final String VALUE_SETTING_OBJ_MODEL_OUTDOOR = "OBJ_MODEL_OUTDOOR";
    public static final String VALUE_SETTING_OBJ_MEMORY = "OBJ_MEMORY";
    public static final String VALUE_SETTING_OBJ_DEVICE = "OBJ_DEVICE";
    public static final String VALUE_SETTING_OBJ_CALL = "OBJ_CALL";
    public static final String VALUE_SETTING_OBJ_MUSIC_LOOP = "OBJ_MUSIC_LOOP";
    public static final String VALUE_SETTING_OBJ_MUSIC_SHUFFLE_PLAYBACK =
            "OBJ_MUSIC_SHUFFLE_PLAYBACK";
    public static final String VALUE_SETTING_OBJ_MUSIC_ORDER_PLAYBACK = "OBJ_MUSIC_ORDER_PLAYBACK";
    public static final String VALUE_SETTING_OBJ_MUSIC_SINGLE_CYCLE = "OBJ_MUSIC_SINGLE_CYCLE";
    public static final String VALUE_SETTING_OBJ_MUSIC_LIST_CYCLE = "OBJ_MUSIC_LIST_CYCLE";
    public static final String VALUE_SETTING_OBJ_MUSIC_FULL_CYCLE = "OBJ_MUSIC_FULL_CYCLE";
    public static final String VALUE_SETTING_OBJ_MUSIC_PREVIOUS_ITEM = "OBJ_MUSIC_PREVIOUS_ITEM";
    public static final String VALUE_SETTING_OBJ_MUSIC_NEXT_ITEM = "OBJ_MUSIC_NEXT_ITEM";
    public static final String VALUE_SETTING_OBJ_AUDIO = "OBJ_AUDIO";
    public static final String VALUE_SETTING_OBJ_NAVI = "OBJ_NAVI";
    public static final String VALUE_SETTING_OBJ_AIR_CONDITION = "OBJ_AIR_CONDITION";
    public static final String VALUE_SETTING_OBJ_INSIDE_MODEL = "OBJ_INSIDE_MODEL";
    public static final String VALUE_SETTING_OBJ_OUTSIDE_MODEL = "OBJ_OUTSIDE_MODEL";
    public static final String VALUE_SETTING_OBJ_AUTO_LOOP_MODEL = "OBJ_AUTO_LOOP_MODEL";
    public static final String VALUE_SETTING_OBJ_AIR_CONDITION_MODEL = "OBJ_AIR_CONDITION_MODEL";
    public static final String VALUE_SETTING_OBJ_NEWEST_SMS = "OBJ_NEWEST_SMS";
    public static final String VALUE_SETTING_OBJ_UNREAD_SMS = "OBJ_UNREAD_SMS";
    public static final String VALUE_SETTING_OBJ_MUSIC_BLUETOOTH = "OBJ_MUSIC_BLUETOOTH";
    public static final String VALUE_SETTING_OBJ_VOLUMN = "OBJ_VOLUMN";
    public static final String VALUE_SETTING_OBJ_SCREEN = "OBJ_SCREEN";
    public static final String VALUE_SETTING_OBJ_MUSIC = "OBJ_MUSIC";
    public static final String VALUE_SETTING_OBJ_FRONTCAMERA = "OBJ_FRONTCAMERA";
    public static final String VALUE_SETTING_OBJ_REARCAMERA = "OBJ_REARCAMERA";
    public static final String VALUE_SETTING_OBJ_PIP = "OBJ_PIP";
    public static final String VALUE_SETTING_OBJ_RADIO = "OBJ_RADIO";
    public static final String VALUE_SETTING_OBJ_CARCORDER = "OBJ_CARCORDER";
    public static final String VALUE_SETTING_OBJ_SDCARD = "OBJ_SDCARD";
    public static final String VALUE_SETTING_OBJ_VIDEO = "OBJ_VIDEO";
    public static final String VALUE_SETTING_OBJ_POWER = "OBJ_POWER";
    public static final String VALUE_SETTING_OBJ_STANDBY = "OBJ_STANDBY";
    public static final String VALUE_SETTING_OBJ_HOMEPAGE = "OBJ_HOMEPAGE";
    public static final String VALUE_SETTING_OBJ_ADDRESS_BOOK = "OBJ_ADDRESS_BOOK";
    public static final String VALUE_SETTING_OBJ_CALL_LOG = "OBJ_CALL_LOG";

    public static final String VALUE_TYPE_SOME_PERSON = "MUTIPLE_CONTACTS";
    public static final String VALUE_TYPE_INPUT_CONTACT = "INPUT_CONTACT";// call who
    public static final String VALUE_TYPE_CALL_START = "CALL";
    public static final String VALUE_TYPE_SOME_NUMBERS = "MUTIPLE_NUMBERS";
    public static final String VALUE_TYPE_CALL_ONE_NUMBER = "CONFIRM_CALL";
    public static final String VALUE_TYPE_CALL_OK = "CALL_OK";
    public static final String VALUE_TYPE_CALL_CANCEL = "CALL_CANCEL";
    // public static final String VALUE_TYPE_INPUT_MSG_CONTENT =
    // "INPUT_FREETEXT_SMS";//INPUT_SMS_CONTENT

    public static final String VALUE_UI_PROTOCAL_SHOW = "UI_PROTOCAL_SHOW";

    /* KW设置命令 */
    public static final String VALUE_SETTING_ACT_OPEN_DESK_LYRIC = "ACT_OPEN_LYRIC";
    public static final String VALUE_SETTING_ACT_CLOSE_DESK_LYRIC = "ACT_CLOSE_LYRIC";
    public static final String ACTION_MUSIC_START_KWAPP = "ACTION_MUSIC_START_KWAPP";
    /* KW设置命令 */

    // TTS Model copy key & values, XD 20160118 added
    public static final String VALUE_STATE_SUCCESS = "success";
    public static final String VALUE_STATE_FAIL = "fail";

    // TTS fail cause
    public static final String KEY_TTS_FAILCAUSE = "failcause";
    public static final String VALUE_TTS_FAILCAUSE_UNMOUNT_SDCARD = "unmount_sdcard";
    public static final String VALUE_TTS_FAILCAUSE_UNAVAILABLE_SDCARD = "unavailable_sdcard";
    public static final String VALUE_TTS_FAILCAUSE_UNAVAILABLE_MEMORY = "unavailable_memory";
    public static final String VALUE_TTS_FAILCAUSE_UNAVAILABLE_FILE = "unavailable_file";

    // modelList
    public static final String KEY_TTS_MODEL_LIST = "modelList";
    public static final String KEY_TTS_MODEL_NAME = "modelName";
    /** is TTS model init done: 0-not enable / 1-enable */
    public static final String KEY_TTS_MODEL_IS_ENABLE = "isEnable";
    /** is TTS model on Local: 0-Cloud / 1-Local */
    public static final String KEY_TTS_MODEL_IS_LOCAL = "isLocal";

    public static final String KEY_TTS_MODEL_LIST_ACTION = "action";
    public static final String VALUE_TTS_MODEL_LIST_ACTION_REQUEST = "request";
    public static final String VALUE_TTS_MODEL_LIST_ACTION_UPDATE = "update";
    public static final String KEY_TTS_UPDATE_MODEL_NAME = "updateModel";

    // origin type - domain
    public static final String DOMAIN_ALARM = "cn.yunzhisheng.alarm";
    public static final String DOMAIN_APP = "cn.yunzhisheng.appmgr";
    public static final String DOMAIN_CALL = "cn.yunzhisheng.call";
    public static final String DOMAIN_CONTACT = "cn.yunzhisheng.contact";
    public static final String DOMAIN_CONTACT_SEND = "cn.yunzhisheng.contact";
    public static final String DOMAIN_FLIGHT = "cn.yunzhisheng.flight";
    public static final String DOMAIN_INCITY_SEARCH = "cn.yunzhisheng.localsearch";
    public static final String DOMAIN_MUSIC = "cn.yunzhisheng.music";
    public static final String DOMAIN_NEARBY_SEARCH = "cn.yunzhisheng.localsearch";
    public static final String DOMAIN_NOTE = "cn.yunzhisheng.note";
    public static final String DOMAIN_POSITION = "cn.yunzhisheng.map";
    public static final String DOMAIN_ROUTE = "cn.yunzhisheng.map";
    public static final String DOMAIN_REMINDER = "cn.yunzhisheng.reminder";
    public static final String DOMAIN_SEARCH = "cn.yunzhisheng.websearch";
    public static final String DOMAIN_SITEMAP = "cn.yunzhisheng.website";
    public static final String DOMAIN_SMS = "cn.yunzhisheng.sms";
    public static final String DOMAIN_STOCK = "cn.yunzhisheng.stock";
    public static final String DOMAIN_TRAIN = "cn.yunzhisheng.train";
    public static final String DOMAIN_TV = "cn.yunzhisheng.tv";
    public static final String DOMAIN_WEATHER = "cn.yunzhisheng.weather";
    public static final String DOMAIN_YELLOWPAGE = "cn.yunzhisheng.hotline";
    public static final String DOMAIN_WEBSEARCH = "cn.yunzhisheng.websearch";
    public static final String DOMAIN_CALENDAR = "cn.yunzhisheng.calendar";
    public static final String DOMAIN_NEWS = "cn.yunzhisheng.news";
    public static final String DOMAIN_COOKBOOK = "cn.yunzhisheng.cookbook";
    public static final String DOMAIN_TRANSLATION = "cn.yunzhisheng.translation";
    public static final String DOMAIN_DIANPING = "cn.yunzhisheng.localsearch";
    public static final String DOMAIN_SETTING = "cn.yunzhisheng.setting";
    public static final String DOMAIN_MOVIE = "cn.yunzhisheng.movie";
    public static final String DOMAIN_NOVEL = "cn.yunzhisheng.novel";
    public static final String DOMAIN_VIDEO = "cn.yunzhisheng.video";
    public static final String DOMAIN_CHAT = "cn.yunzhisheng.chat";
    public static final String DOMAIN_BROADCAST = "cn.yunzhisheng.broadcast";
    /* < XD added 20150729 Begin */
    public static final String DOMAIN_LOCAL = "cn.yunzhisheng.local";
    public static final String DOMAIN_TRAFFIC = "cn.yunzhisheng.traffic";
    public static final String DOMAIN_LIMIT = "cn.yunzhisheng.limit";
    /** Online FM audio */
    public static final String DOMAIN_AUDIO = "cn.yunzhisheng.audio";
    public static final String DOMAIN_WECHAT = "cn.yunzhisheng.wechat";
    /* < XD added 20150729 End */

    public static final String DOMAIN_ERROR = "DOMAIN_ERROR";

    public static final String DOMAIN_CHANNEL_SWITCH = "cn.yunzhisheng.setting.tv";
    public static final String DOMAIN_SHOP = "cn.yunzhisheng.shopping";
    public static final String DOMAIN_MOBILE_CONTROL = "DOMAIN_MOBILE_CONTROL";

    public static final String DOMAIN_CODE_FAVORITE_ROUTE = "FAVORITE_ROUTE";

    public static final String VALUE_ORIGIN_TYPE_ERROR = "cn.yunzhisheng.error";// xd added 20150706
    // memo
    public static final String KEY_TEXT = "text";
    public static final String KEY_MORNING = "am";
    public static final String KEY_AFTERNOON = "pm";

    public static final String KEY_YEAR_REPEAT = "yearrep";
    public static final String KEY_MONTH_REPEAT = "monthrep";
    public static final String KEY_WEEK_REPEAT = "weekrep";
    public static final String KEY_YEAR = "yy";
    public static final String KEY_MONTH = "MM";
    public static final String KEY_DAY = "dd";
    public static final String KEY_HOUR = "hh";
    public static final String KEY_MINUTE = "mm";
    public static final String KEY_WEEKDAY = "ww";

    public static final String KEY_YEAR_STEP = "year";
    public static final String KEY_MON_STEP = "month";
    public static final String KEY_DAY_STEP = "day";
    public static final String KEY_HOUR_STEP = "hour";
    public static final String KEY_MIN_STEP = "minute";
    public static final String KEY_WEEK_STEP = "week";

    public static final String KEY_REPEATDATE = "repeatDate";
    public static final String KEY_DATETIME = "dateTime";
    public static final String KEY_LABEL = "label";

    /********************** session message *********************/
    public static final int MESSAGE_CANCEL_TALK = 1003;
    public static final int MESSAGE_SESSION_DONE = 1004;
    public static final int MESSAGE_SESSION_CANCEL = 1005;
    public static final int MESSAGE_WAITING_SESSION_CANCEL = 1006;
    public static final int MESSAGE_SESSION_DONE_DELAY = 1007; // XD added 20150819
    public static final int MESSAGE_SESSION_DISMISS_WINDOW_SERVICE = 1008; // XD added 20150819

    /** release current session but don't dismiss window, XD 20151123 added for experience mode */
    public static final int MESSAGE_SESSION_RELEASE_ONLY = 1009;

    public static final int MESSAGE_ADD_ANSWER_VIEW = 1013;
    public static final int MESSAGE_ADD_ANSWER_TEXT = 1014;
    public static final int MESSAGE_ADD_QUESTION_TEXT = 1015;
    public static final int MESSAGE_UI_OPERATE_PROTOCAL = 1016;
    public static final int MESSAGE_SEND_RESP_PROTOCAL = 1017;
    public static final int MESSAGE_SMS_SENDING = 1020; // XD added 20150709
    public static final int MESSAGE_ANSWER_CALL = 1027;
    public static final int MESSAGE_REJECT_CALL = 1028; // XD added 20150710
    public static final int MESSAGE_ADD_SESSION_LIST_VIEW = 1029; // XD added 20150716
    public static final int MESSAGE_PLAY_TTS = 1030;// add tts
    public static final int MESSAGE_START_LOCALSEARCH_NAVI = 1031;
    public static final int MESSAGE_START_LOCALSEARCH_CALL = 1032;
    public static final int MESSAGE_SHOW_EDIT_LOCATION_POP = 1033;// XD added 20151016
    public static final int MESSAGE_UPDATE_MIC_EANBLE_STATE = 1034;// XD added 20151102
    public static final int MESSAGE_DIMISS_WELCOME_ACTIVITY = 1035;// XD added 20151102
    public static final int MESSAGE_ADD_PUSH_VIEW = 1036;

    public static String getMessageName(int what) {
        switch (what) {
            case MESSAGE_CANCEL_TALK:
                return "MESSAGE_CANCEL_TALK";
            case MESSAGE_SESSION_DONE:
                return "MESSAGE_SESSION_DONE";
            case MESSAGE_SESSION_CANCEL:
                return "MESSAGE_SESSION_CANCEL";
            case MESSAGE_ADD_ANSWER_VIEW:
                return "MESSAGE_ADD_ANSWER_VIEW";
            case MESSAGE_ADD_ANSWER_TEXT:
                return "MESSAGE_ADD_ANSWER_TEXT";
            case MESSAGE_ADD_QUESTION_TEXT:
                return "MESSAGE_ADD_QUESTION_TEXT";
            case MESSAGE_UI_OPERATE_PROTOCAL:
                return "MESSAGE_UI_OPERATE_PROTOCAL";
        }
        return String.valueOf(what);
    }

    /* < xiaodong added 20150702 for MESSAGE_UI_OPERATE_PROTOCAL BUNDLE begin */
    public static final String KEY_BUNDLE_EVENT_NAME = "EVENT_NAME";
    public static final String KEY_BUNDLE_PROTOCAL = "PROTOCAL";

    /* EVENT_NAME */
    public static final String EVENT_NAME_PTT = "PTT";
    public static final String EVENT_MUSIC_DONE = "MUSIC_DONE";
    public static final String EVENT_NAME_PTT_DORESO = "PTT_DORESO"; // XD added 20150828
    public static final String EVENT_NAME_CTT = "CTT";
    public static final String EVENT_NAME_LOCALSERACH_NAVI = "LOCALSERACH_NAVI";
    public static final String EVENT_NAME_LOCALSERACH_CALL = "LOCALSERACH_CALL";
    public static final String EVENT_NAME_WAITTING_CANCEL = "WATTING_CANCEL";
    public static final String EVENT_NAME_SWITCH_WAKEUP = "SWITCH_WAKEUP";
    public static final String EVENT_NAME_SWITCH_TTS_SPEED = "SWITCH_TTS_SPEED";
    public static final String EVENT_NAME_SWITCH_GPS = "SWITCH_GPS";
    public static final String EVENT_NAME_SWITCH_ONESHOT = "SWITCH_ONESHOT";
    // ADD TYZ 20151204
    public static final String EVENT_NAME_SWITCH_LOGCAT = "SWITCH_LOGCAT";
    /** VERSION_LEVEL XD added 20151123 */
    public static final String EVENT_NAME_SWITCH_VERSION_LEVEL = "VERSION_LEVEL";
    /** SWITCH_TTS_MODEL XD added 20160112 */
    public static final String EVENT_NAME_SWITCH_TTS_MODEL = "SWITCH_TTS_MODEL";
    public static final String EVENT_NAME_SWITCH_AEC = "SWITCH_AEC";
    public static final String EVENT_NAME_GENERAL_GPS = "GENERAL_GPS";

    public static final String EVENT_NAME_RESET_WAKEUP_WORD = "RESET_WAKEUP_WORD";

    public static final String EVENT_NAME_ON_CONFIRM_OK = "ON_CONFIRM_OK";
    public static final String EVENT_NAME_ON_CONFIRM_TIME_UP = "ON_CONFIRM_TIME_UP"; // xd added
                                                                                     // 20150704
    public static final String EVENT_NAME_ON_CONFIRM_CANCEL = "ON_CONFIRM_CANCEL";
    public static final String EVENT_NAME_SELECT_ITEM = "SELECT_ITEM";

    public static final String EVENT_NAME_CLICK_NEXT = "CLICK_NEXT";
    public static final String EVENT_NAME_CLICK_PRE = "CLICK_PRE";


    // ADD TYZ 20151027 WHEN YOU CLICK LOCALSEARCH ITEM USE THIS
    public static final String EVENT_NAME_SELECT_LOCALSEARCH_ITEM = "SELECT_LOCALSEARCH_ITEM";

    public static final String EVENT_NAME_SELECT_LOCALSEARCH_ITEM_ACTION =
            "SELECT_LOCALSEARCH_ITEM_ACTION";

    public static final String EVENT_NAME_SAVE_CONTACTS_DONE = "SAVE_CONTACTS_DONE";
    public static final String EVENT_NAME_SAVE_MEDIAS_DONE = "SAVE_MEDIAS_DONE";
    public static final String EVENT_NAME_SAVE_APPS_DONE = "SAVE_APPS_DONE";
    public static final String EVENT_NAME_LOCAL_SEARCH = "LOCAL_SEARCH"; // XD added 20150713

    public static final String EVENT_NAME_SAVE_UPDATE_CONTACTS_DONE = "SAVE_UPDATE_CONTACTS_DONE";
    public static final String EVENT_NAME_SAVE_UPDATE_MEDIAS_DONE = "SAVE_UPDATE_MEDIAS_DONE";
    public static final String EVENT_NAME_SAVE_UPDATE_APPS_DONE = "SAVE_UPDATE_APPS_DONE";

    public static final String EVENT_NAME_SMS_CONTENT_RETYPE = "SMS_CONTENT_RETYPE";// xd added
                                                                                    // 20150708
    public static final String EVENT_NAME_SMS_SENT_STATUS = "SMS_SENT_STATUS";// XD added 20150817

    public static final String EVENT_NAME_INCOMING_CALL = "INCOMING_CALL";// XD added 20150713
    public static final String EVENT_NAME_INCOMING_SMS = "INCOMING_SMS";
    public static final String EVENT_NAME_INCOMING_PUSH_MESSAGE = "INCOMING_PUSH_MESSAGE";
    public static final String EVENT_NAME_INCOMING_PUSH_MESSAGE_SHOW = "INCOMING_PUSH_MESSAGE_SHOW";

    public static final String EVENT_NAME_INCOMING_SMS_REPLY = "INCOMING_SMS_REPLY"; // XD added
                                                                                     // 20150819

    public static final String EVENT_NAME_PLAY_TTS = "PLAY_TTS"; // XD added 20150821

    // update POI keyWord
    public static final String EVENT_NAME_UPDATE_POI_KEYWORD = "UPDATE_POI_KEYWORD";
    public static final String EVENT_NAME_UPDATE_POI_TIMEOUT_SWITCH = "UPDATE_POI_TIMEOUT_SWITCH";
    public static final String EVENT_PARAM_SWITCH_OPEN = "OPEN";
    public static final String EVENT_PARAM_SWITCH_CLOSE = "CLOSE";

    // SET FAVORITE ADDRESS, XD added 2016-1-28
    public static final String EVENT_NAME_SET_FAVORITE_ADDRESS = "SET_FAVORITE_ADDRESS";
    public static final String EVENT_NAME_GO_FAVORITE_ADDRESS = "GO_FAVORITE_ADDRESS";

    public static final String PARAM_ADDRESS_TYPE_HOME = "ADDRESS_HOME";
    public static final String PARAM_ADDRESS_TYPE_COMPANY = "ADDRESS_COMPANY";

    public static final String PARAM_ADDRESS_TYPE_NAVI = "ADDRESS_NAVI";

    /** request UUID, XD added 2015-12-22 */
    public static final String EVENT_NAME_REQUEST_UUID = "REQUEST_UUID";

    /** request TTS timbre model list and enable status , XD added 2016-1-18 */
    public static final String EVENT_NAME_REQUEST_TTS_MODEL_LIST = "REQUEST_TTS_MODEL_LIST";

    // request WakeUp words
    public static final String EVENT_NAME_REQUEST_WAKEUP_WORDS = "REQUEST_WAKEUP_WORDS";

    // update WakeUp words
    public static final String EVENT_NAME_UPDATE_WAKEUP_WORD = "UPDATE_WAKEUP_WORD";
    // update wakeupWord
    public static final String EVENT_NAME_UPDATE_WAKEUP_WORD_TIMEOUT_SWITCH =
            "UPDATE_WAKEUP_WORD_TIMEOUT_SWITCH";

    /** UPDATE_LOCALSEARCH_KEYWORD - XD added 20151023 */
    public static final String EVENT_NAME_UPDATE_LOCALSEARCH_KEYWORD = "UPDATE_LOCALSEARCH_KEYWORD";
    /** recording control - XD added 20151023 */
    public static final String EVENT_NAME_RECORDING_CONTROL = "RECORDING_CONTROL";

    // XD added 20151218
    public static final String EVENT_NAME_ONCLICK_LOAD_MORE = "ONCLICK_LOAD_MORE";

    // play TTS
    public static final String EVENT_PARAM_KEY_TTS_TEXT = "TTS_TEXT";
    public static final String EVENT_PARAM_KEY_TTS_ID = "TTS_ID"; // XD 2016-1-25 added
    public static final String EVENT_PARAM_KEY_TTS_END_KEY_RECOGNIZER_TYPE = "RECOGNIZER_TYPE";
    public static final String PARAM_VALUE_TTS_END_RECOGNIZER = "RECOGNIZER";
    public static final String PARAM_VALUE_TTS_END_WAKEUP = "WAKEUP";

    // XD 2016-2-3 added
    public static final String EVENT_PARAM_KEY_TTS_FROM = "TTS_FROM";
    public static final String PARAM_TTS_FROM_UNICARGUI = "UniCarGUI";
    public static final String PARAM_TTS_FROM_UNICARNAVI = "UniCarNavi";

    public static final String EVENT_PARAM_KEY_LOACTION_KEYWORD = "keyword";
    public static final String EVENT_PARAM_KEY_TYPE = "type";

    /* EVENT PARAM_KEY & PARAM_VALUE */
    public static final String PARAM_KEY_STATE = "state";
    public static final String PARAM_KEY_TYPE = "type";
    public static final String PARAM_KEY_ACTION_TYPE = "action_type";

    // incoming Call || incoming SMS param key
    public static final String PARAM_KEY_STATE_INCOMING = "state";

    /** recording control event param - XD added 20151023 */
    public static final String PARAM_RECORDING_CONTROL_START = "START";
    public static final String PARAM_RECORDING_CONTROL_STOP = "STOP";

    // Version Level PARAM State VALUE
    public static final String PARAM_VERSION_LEVEL_EXP = "EXPERIENCE";
    public static final String PARAM_VERSION_LEVEL_STANDRARD = "STANDRARD";
    public static final String PARAM_VERSION_LEVEL_HIGH = "HIGH";
    public static final String PARAM_VERSION_LEVEL_TYPE_IDEL = "IDEL";
    public static final String PARAM_VERSION_LEVEL_TYPE_RECORDING = "RECORDING";

    // TTS Timbre(MODEL) PARAM VALUE
    /** default TTS timbre model */
    public static final String PARAM_TTS_TIMBRE_STARNAND = "TTS_MODEL_FEMALE";
    public static final String PARAM_TTS_TIMBRE_SEXY = "TTS_MODEL_LZL";

    public static final String PARAM_TTS_TIMBRE_AUTO = "TTS_MODEL_AUTO_SWITCH";

    public static final String PARAM_TYPE_PLAY_TTS = "PlayTTS";
    public static final String PARAM_TYPE_NOT_PLAY_TTS = "NotPlayTTS";
    public static final String PARAM_TYPE_NOT_PLAY_TTS_GENDER = "GenderNotPlayTTS";

    // REASON PARAM_KEY & PARAM_VALUE
    public static final String KEY_REASON = "reason";
    public static final String PARAM_REASON_ADD_ADDRESS = "ADD_ADDRESS";

    // UniCarNavi STARTED EVENT param value , XD added 2016-1-21
    public static final String EVENT_NAME_UNICAR_NAVI_STARTED = "UNICAR_NAVI_STARTED";
    public static final String PARAM_TYPE_ROUTE_STARTED = "ROUTE_STARTED";
    public static final String PARAM_TYPE_ROUTE_QUIT = "ROUTE_QUIT";
    public static final String PARAM_TYPE_NAVI_STARTED = "NAVI_STARTED";

    /* EVENT PROTOCAL */
    public static final String EVENT_PROTOCAL_ON_CONFIRM_OK =
            "{\"service\":\"cn.yunzhisheng.setting\",\"semantic\":{\"intent\":{\"confirm\":\"OK\"}},\"text\":\"播放\",\"code\":\"SETTING_EXEC\",\"rc\":0}";
    // public static final String EVENT_PROTOCAL_ON_CONFIRM_CANCEL =
    // "{\"service\":\"cn.yunzhisheng.setting\",\"semantic\":{\"intent\":{\"confirm\":\"CANCEL\"}},\"code\":\"SETTING_EXEC\"}";
    public static final String EVENT_PROTOCAL_ON_CONFIRM_CANCEL_CALL =
            "{\"service\":\"cn.yunzhisheng.setting\",\"semantic\":{\"intent\":{\"confirm\":\"CANCEL\"}},\"code\":\"SETTING_EXEC\",\"type\":\"CALL\"}";
    public static final String EVENT_PROTOCAL_ON_CONFIRM_CANCEL_SMS =
            "{\"service\":\"cn.yunzhisheng.setting\",\"semantic\":{\"intent\":{\"confirm\":\"CANCEL\"}},\"code\":\"SETTING_EXEC\",\"type\":\"SMS\"}";
    public static final String EVENT_PROTOCAL_ON_CONFIRM_CANCEL_PUSH_MESSAGE =
            "{\"service\":\"cn.yunzhisheng.setting\",\"semantic\":{\"intent\":{\"confirm\":\"CANCEL\"}},\"code\":\"SETTING_EXEC\",\"type\":\"PUSH_MESSAGE\"}";
    public static final String EVENT_PROTOCAL_ON_CONFIRM_CANCEL_PUSH_MESSAGE_AUTO =
            "{\"service\":\"cn.yunzhisheng.setting\",\"semantic\":{\"intent\":{\"confirm\":\"CANCEL\",\"type\":\"AUTO\"}},\"code\":\"SETTING_EXEC\",\"type\":\"PUSH_MESSAGE\"}";
    public static final String EVENT_PROTOCAL_ON_CONFIRM_TIME_UP =
            "{\"service\":\"cn.yunzhisheng.setting\",\"semantic\":{\"intent\":{\"confirm\":\"TIME_UP\"}},\"code\":\"SETTING_EXEC\"}";
    public static final String EVENT_PROTOCAL_SMS_CONTENT_RETYPE =
            "{\"service\":\"cn.yunzhisheng.setting\",\"semantic\":{\"intent\":{\"confirm\":\"REINPUT\"}},\"code\":\"SETTING_EXEC\"}";

    public static final String EVENT_PROTOCAL_SMS_STATUS_SUCCESS =
            "{\"service\":\"cn.yunzhisheng.setting\",\"semantic\":{\"intent\":{\"confirm\":\"SMS_SENT_STATUS\"}},\"code\":\"SETTING_EXEC\",\"type\":\"SUCCESS\"}";
    public static final String EVENT_PROTOCAL_SMS_STATUS_FAIL =
            "{\"service\":\"cn.yunzhisheng.setting\",\"semantic\":{\"intent\":{\"confirm\":\"SMS_SENT_STATUS\"}},\"code\":\"SETTING_EXEC\",\"type\":\"FAIL\"}";

    public static final String EVENT_PROTOCAL_ON_ANSWER_CALL =
            "{\"service\":\"cn.yunzhisheng.setting\",\"semantic\":{\"intent\":{\"confirm\":\"OK\"}},\"code\":\"SETTING_EXEC\",\"state\":\"INCOMING_CALL_ANSWER\"}";
    public static final String EVENT_PROTOCAL_ON_REJECT_CALL =
            "{\"service\":\"cn.yunzhisheng.setting\",\"semantic\":{\"intent\":{\"confirm\":\"CANCEL\"}},\"code\":\"SETTING_EXEC\",\"state\":\"INCOMING_CALL_HANGUP\"}";


    /* EVENT PROTOCAL PARAM VALUE */
    public static final String EVENT_PARAM_VALUE_INCOMING_SMS_FAST_REPLY = "FAST_REPLY";
    public static final String EVENT_PARAM_VALUE_INCOMING_SMS_REPLY = "REPLY";
    /* xiaodong added 20150702 for MESSAGE_UI_OPERATE_PROTOCAL BUNDLE End > */

    // Add send switch WakeUp protocol
    public static final String EVENT_PROTOCAL_SWITCH_LOGCAT_OPEN =
            "{\"service\":\"cn.yunzhisheng.setting\",\"semantic\":{\"intent\":{\"confirm\":\"LOGCAT_OPEN\"}},\"code\":\"SETTING_EXEC\",\"state\":\"LOGCAT_OPEN\"}";
    public static final String EVENT_PROTOCAL_SWITCH_LOGCAT_CLOSE =
            "{\"service\":\"cn.yunzhisheng.setting\",\"semantic\":{\"intent\":{\"confirm\":\"LOGCAT_CLOSE\"}},\"code\":\"SETTING_EXEC\",\"state\":\"LOGCAT_CLOSE\"}";

    // Add send switch WakeUp protocol
    public static final String EVENT_PROTOCAL_SWITCH_WAKEUP_START =
            "{\"service\":\"cn.yunzhisheng.setting\",\"semantic\":{\"intent\":{\"confirm\":\"WAKEUP_START\"}},\"code\":\"SETTING_EXEC\",\"state\":\"WAKEUP_START\"}";
    public static final String EVENT_PROTOCAL_SWITCH_WAKEUP_STOP =
            "{\"service\":\"cn.yunzhisheng.setting\",\"semantic\":{\"intent\":{\"confirm\":\"WAKEUP_STOP\"}},\"code\":\"SETTING_EXEC\",\"state\":\"WAKEUP_STOP\"}";
    // Add send switch TTSSpeed protocol
    public static final String EVENT_PROTOCAL_SWITCH_TTS_SPEED_SLOWLY =
            "{\"service\":\"cn.yunzhisheng.setting\",\"semantic\":{\"intent\":{\"confirm\":\"TTS_SPEED_SLOWLY\"}},\"code\":\"SETTING_EXEC\",\"state\":\"TTS_SPEED_SLOWLY\"}";
    public static final String EVENT_PROTOCAL_SWITCH_TTS_SPEED_STANDARD =
            "{\"service\":\"cn.yunzhisheng.setting\",\"semantic\":{\"intent\":{\"confirm\":\"TTS_SPEED_STANDARD\"}},\"code\":\"SETTING_EXEC\",\"state\":\"TTS_SPEED_STANDARD\"}";
    public static final String EVENT_PROTOCAL_SWITCH_TTS_SPEED_FAST =
            "{\"service\":\"cn.yunzhisheng.setting\",\"semantic\":{\"intent\":{\"confirm\":\"TTS_SPEED_FAST\"}},\"code\":\"SETTING_EXEC\",\"state\":\"TTS_SPEED_FAST\"}";

    public static final String EVENT_PROTOCAL_SWITCH_GPS_TRUE =
            "{\"service\":\"cn.yunzhisheng.setting\",\"semantic\":{\"intent\":{\"confirm\":\"TRUE\"}},\"code\":\"SETTING_EXEC\",\"state\":\"TRUE\"}";

    public static final String EVENT_PROTOCAL_SWITCH_GPS_FALSE =
            "{\"service\":\"cn.yunzhisheng.setting\",\"semantic\":{\"intent\":{\"confirm\":\"FALSE\"}},\"code\":\"SETTING_EXEC\",\"state\":\"FALSE\"}";

    // Add send switch OneShot protocol
    public static final String EVENT_PROTOCAL_SWITCH_ONESHOT_START =
            "{\"service\":\"cn.yunzhisheng.setting\",\"semantic\":{\"intent\":{\"confirm\":\"ONESHOT_START\"}},\"code\":\"SETTING_EXEC\",\"state\":\"ONESHOT_START\"}";
    public static final String EVENT_PROTOCAL_SWITCH_ONESHOT_STOP =
            "{\"service\":\"cn.yunzhisheng.setting\",\"semantic\":{\"intent\":{\"confirm\":\"ONESHOT_STOP\"}},\"code\":\"SETTING_EXEC\",\"state\":\"ONESHOT_STOP\"}";

    // Add send switch AEC protocol
    public static final String EVENT_PROTOCAL_SWITCH_AEC_START =
            "{\"service\":\"cn.yunzhisheng.setting\",\"semantic\":{\"intent\":{\"confirm\":\"AEC_START\"}},\"code\":\"SETTING_EXEC\",\"state\":\"AEC_START\"}";
    public static final String EVENT_PROTOCAL_SWITCH_AEC_STOP =
            "{\"service\":\"cn.yunzhisheng.setting\",\"semantic\":{\"intent\":{\"confirm\":\"AEC_STOP\"}},\"code\":\"SETTING_EXEC\",\"state\":\"AEC_STOP\"}";
    public static final String EVENT_NAME_START_WAKEUP = "START_WAKEUP";

    public static final String EVENT_NAME_STOP_WAKEUP = "STOP_WAKEUP";

    public static final String EVENT_NAME_WECHAT_NOT_LOGIN = "WECHAT_NOT_LOGIN";

    public static final String OPERAND_STANDARD_TTS = "STARTDED_TTS";
    public static final String OPERAND_LZL_TTS = "LZL_TTS";
    public static final String OPERAND_AUTO_TTS = "AUTO_TTS";
    public static final String OPERAND_AMEND_HOME_ADDRESS = "AMEND_HOME_ADDRESS";
    public static final String OPERAND_AMEND_OFFICE_ADDRESS = "AMEND_OFFICE_ADDRESS";
    public static final String OPERAND_HIGHT_VERSION = "HIGHT_VERSION";
    public static final String OPERAND_STANDARD_VERSION = "STANDARD_VERSION";
    public static final String OPERAND_SHOW_VERSION = "SHOW_VERSION";

    public static final String SWITCH_TTS_MODLE_VOICE = "VOICE";
    public static final String SWITCH_TTS_MODLE_HAND = "HAND";

}
