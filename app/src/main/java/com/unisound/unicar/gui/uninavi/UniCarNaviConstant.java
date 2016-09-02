/**
 * Copyright (c) 2012-2016 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : NaviConstant.java
 * @ProjectName : uniCarSolution
 * @PakageName : com.unisound.unicar.gui.uninavi
 * @version : 1.0
 * @Author : Xiaodong.He
 * @CreateDate : 2016-1-6
 */
package com.unisound.unicar.gui.uninavi;

/**
 * 
 * @author xiaodong.he
 * @Date : 2016-1-6
 */
public abstract class UniCarNaviConstant {

    public static final String BASE_ACTION = "com.unisound.intent.action";

    // GUI Service Received Intent Actions:
    /** START UniCarNaviGuiService */
    public static final String ACTION_START_UNICARNAVI_GUI_SERVICE = BASE_ACTION
            + ".START_UNICARNAVI_GUI_SERVICE";

    /** START UniCarNavi to Navigation */
    public static final String ACTION_START_UNICARNAVI = BASE_ACTION + ".START_UNICARNAVI";

    /** Begin Navigation, XD added 2016-1-22 */
    public static final String ACTION_BEGIN_NAVIGAION = BASE_ACTION + ".BEGIN_NAVIGAION";

    /** ACTION_CANCEL */
    public static final String ACTION_CANCEL = BASE_ACTION + ".navi.CANCEL";

    /** ACTION_EXIT */
    public static final String ACTION_EXIT = BASE_ACTION + ".navi.EXIT";

    /** ACTION_SET_ROUTE_PLAN */
    public static final String ACTION_SET_ROUTE_PLAN = BASE_ACTION + ".SET_ROUTE_PLAN";

    /** SET_DISPLAY_MODE */
    public static final String ACTION_SET_DISPLAY_MODE = BASE_ACTION + ".SET_DISPLAY_MODE";

    /** CONTROL UniCarNavi navigation & emulate navigation */
    public static final String ACTION_CONTROL_UNICARNAVI = BASE_ACTION + ".CONTROL_UNICARNAVI";

    /** CONTROL UniCarNavi road condition show */
    public static final String ACTION_CONTROL_ROAD_CONDITION_SHOW = BASE_ACTION
            + ".CONTROL_ROAD_CONDITION_SHOW";

    /** ACTION on TTS play end */
    public static final String ACTION_ON_TTS_PLAY_END = BASE_ACTION + ".ON_TTS_PLAY_END";

    public static final String ACTION_SHOW_UNICAR_NAVI_UI = BASE_ACTION + ".SHOW_UNICAR_NAVI_UI";

    // GUI Service Received Intent Keys:
    /**
     * @Values: 1-Standard; 2-Night mode; 3-Satellite mode;
     *          {@link UniCarNaviConstant#VALUE_DISPLAY_MODE_STANDARD}
     *          {@link UniCarNaviConstant#VALUE_DISPLAY_MODE_NIGHT}
     *          {@link UniCarNaviConstant#VALUE_DISPLAY_MODE_SATELLITE}
     */
    public static final String KEY_DISPLAY_MODE = "DISPLAY_MODE";

    /**
     * @Values: {@link UniCarNaviConstant#VALUE_OPERATION_*}
     */
    public static final String KEY_OPERATION = "OPERATION";


    /** XD 2016-2-4 added */
    public static final String KEY_IS_PLAY_TTS = "IS_PLAY_TTS";


    // ------------------Common Constant as follow:----------------------------

    /** Bind UniCarNaviService Action */
    public static final String ACTION_START_UNICARNAVI_SERVICE =
            "com.unisound.intent.action.navi.START_UNICARNAVI_SERVICE";

    /** NAVI PARAMS JSON */
    public static final String KEY_NAVI_PARAMS = "NAVI_PARAMS";

    // start navigation keys
    /** mode: driving */
    public static final String KEY_MODE = "MODE";
    public static final String KEY_FROM_LAT = "FROM_LATITUDE";
    public static final String KEY_FROM_LNG = "FROM_LONGITUDE";
    public static final String KEY_FROM_CITY = "FROM_CITY";
    public static final String KEY_FROM_POI = "FROM_POI";
    public static final String KEY_TO_LAT = "TO_LATITUDE";
    public static final String KEY_TO_LNG = "TO_LONGITUDE";
    public static final String KEY_TO_CITY = "TO_CITY";
    public static final String KEY_TO_NAME = "TO_NAME";
    public static final String KEY_TO_ADDRESS = "TO_ADDRESS";
    public static final String KEY_ROUTE_PLAN = "ROUTE_PLAN";

    /** -------------------onControlCommand KEY & VALUE Begin------------------- */
    // MODULE Key & Values
    public static final String KEY_CMD_MODULE = "MODULE";
    public static final String VALUE_CMD_MODULE_ROUTE = "route";
    public static final String VALUE_CMD_MODULE_NAVIGATION = "navigation";
    public static final String VALUE_CMD_MODULE_COMMON = "common";

    // CMD_NAME Key & Values
    public static final String KEY_CMD_NAME = "CMD_NAME";
    /** on_tts_play_end, XD 2016-1-25 added */
    public static final String VALUE_CMD_NAME_ON_TTS_PLAY_END = "on_tts_play_end";
    public static final String VALUE_CMD_NAME_BEGIN_NAVIGATION = "begin_navigation";
    public static final String VALUE_CMD_NAME_CANCEL = "cancel";
    public static final String VALUE_CMD_NAME_EXIT = "exit";
    public static final String VALUE_CMD_NAME_SET_ROUTE_PLAN = "set_route_plan";
    public static final String VALUE_CMD_NAME_SET_DISPLAY_MODE = "set_display_mode";
    public static final String VALUE_CMD_NAME_CONTROL_NAVIGATION = "control_navigation";
    public static final String VALUE_CMD_NAME_CONTROL_ROAD_CONDITION = "control_road_condition";
    public static final String VALUE_CMD_NAME_SHOW_UNICAR_NAVI = "show_unicar_navi";

    // DATA JSONObject KEY
    public static final String KEY_OBJ_DATA = "DATA";

    // ROUTE_PLAN Key & Values
    /** 1-Fast; 2-No Highway; 3-Save Money; 4-Highway Prior; */
    /**
     * 0.最少时间,1.较少费用 ，2最短路程 ，4.躲避拥堵，5.高速优先，6.躲避收费，7.不要坐地铁，,8.最少步行，9.最少换乘，10.推荐路线
     */
    public static final String DATA_KEY_ROUTE_PLAN = KEY_ROUTE_PLAN;
    public static final int VALUE_ROUTE_PLAN_TIME_FIRST = 0;
    public static final int VALUE_ROUTE_PLAN_NO_HIGHWAY = 1;// VALUE_ROUTE_PLAN_NO_HIGHWAY
    public static final int VALUE_ROUTE_PLAN_ECAR_DIS_FIRST = 2;
    public static final int VALUE_ROUTE_PLAN_ECAR_AVOID_TRAFFIC_JAM = 4;
    public static final int VALUE_ROUTE_PLAN_HIGHWAY = 5;
    public static final int VALUE_ROUTE_PLAN_ECAR_AVOID_TOLLS = 6;
    public static final int VALUE_ROUTE_PLAN_EBUS_NO_SUBWAY = 7;
    public static final int VALUE_ROUTE_PLAN_EBUS_WALK_FIRST = 8;
    public static final int VALUE_ROUTE_PLAN_EBUS_TRANSFER_FIRST = 9;
    public static final int VALUE_ROUTE_PLAN_SUGGESTED = 10;

    // DISPLAY_MODE Key & Values
    public static final String DATA_KEY_DISPLAY_MODE = "DISPLAY_MODE";
    public static final int VALUE_DISPLAY_MODE_STANDARD = 1;
    public static final int VALUE_DISPLAY_MODE_NIGHT = 2;
    public static final int VALUE_DISPLAY_MODE_SATELLITE = 3;

    // OPERATION Key & Values
    public static final String DATA_KEY_OPERATION = "OPERATION";
    // NAVI Control OPERATION :
    /** {@link UniCarNaviConstant#VALUE_CMD_NAME_CONTROL_NAVIGATION} */
    public static final int VALUE_OPERATION_NAVI_START = 2001;
    public static final int VALUE_OPERATION_EMULATE_NAVI_START = 2101;
    public static final int VALUE_OPERATION_EMULATE_NAVI_PAUSE = 2102;
    public static final int VALUE_OPERATION_EMULATE_NAVI_CONTINUE = 2103;

    /** {@link UniCarNaviConstant#VALUE_CMD_NAME_CONTROL_ROAD_CONDITION} */
    public static final int VALUE_OPERATION_ROAD_CONDITION_SHOW = 2201;
    public static final int VALUE_OPERATION_ROAD_CONDITION_CLOSE = 2202;


    /** -------------------onControlCommand KEY & VALUE End---------------------- */

    /**
     * CallbackConstant
     * 
     * @author xiaodong.he
     * 
     */
    public static final class CallbackConstant {

        public static final String KEY_CALLBACK_PACKAGE = "package";

        public static final String KEY_CALLBACK_DATA_OBJ = "data";

        public static final String KEY_CALLBACK_TTS_TEXT = "tts_text";
        public static final String KEY_CALLBACK_TTS_ID = "tts_id";
        // TTS_ID values
        public static final String VALUE_TTS_ID_SELECT_ROUTE_PLAN_AUTO = "select_route_plan_auto";
        public static final String VALUE_TTS_ID_SELECT_ROUTE_PLAN = "select_route_plan";

        // TTS_FROM , XD 2016-02-03 added
        public static final String KEY_CALLBACK_TTS_FROM = "tts_from";
        public static final String VALUE_TTS_FROM_UNICARNAVI = "UniCarNavi";

        // Callback CMD & Values
        public static final String KEY_CALLBACK_CMD = "command";

        public static final String VALUE_CALLBACK_CMD_PLAY_TTS = "cmd_play_tts";

        public static final String VALUE_CALLBACK_CMD_ROUTE_STARTED = "route_started";

        public static final String VALUE_CALLBACK_CMD_ROUTE_QUIT = "route_quit";

        public static final String VALUE_CALLBACK_CMD_NAVI_STARTED = "navi_started";

        public final static int APP_STATE_IDEL = 201;
        public final static int APP_STATE_MAP = 202;
        public final static int APP_STATE_NAVI = 203;
        // public final static int APP_STATE_SIMULATE_NAVI = 204;
    }


}
