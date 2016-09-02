/**
 * Copyright (c) 2012-2013 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * 
 * @FileName : SettingSession.java
 * @ProjectName : UnicarGUI
 * @PakageName : om.unisound.unicar.gui.session
 * @version : 1.2
 * @Author : Dancindream
 * @CreateDate : 2013-9-6
 */
package com.unisound.unicar.gui.session;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.unisound.unicar.gui.R;
import com.unisound.unicar.gui.application.CrashApplication;
import com.unisound.unicar.gui.oem.RomControl;
import com.unisound.unicar.gui.phone.Telephony;
import com.unisound.unicar.gui.preference.CommandPreference;
import com.unisound.unicar.gui.preference.SessionPreference;
import com.unisound.unicar.gui.preference.UserPerferenceUtil;
import com.unisound.unicar.gui.ui.MessageSender;
import com.unisound.unicar.gui.utils.GuiSettingUpdateUtil;
import com.unisound.unicar.gui.utils.JsonTool;
import com.unisound.unicar.gui.utils.Logger;
import com.unisound.unicar.gui.utils.PlayersControlManager;
import com.unisound.unicar.gui.view.NoPerSonContentView;

/**
 * @Module :
 * @Comments : SettingSession
 * @Author : Dancindream
 * @CreateDate : 2013-9-6
 * @ModifiedBy : xiaodong.he
 * @ModifiedDate: 2015-10-28
 * @Modified: 2015-10-28: update players control
 */
public class SettingSession extends BaseSession {
    public static final String TAG = "SettingSession";

    private MessageSender mMessageSender;

    private String operator;
    private String operands;

    /** isNeedDoOnTTSEnd, XD 20151103 adde */
    private boolean isNeedDoOnTTSEnd = false;

    /**
     * @Author : Dancindream
     * @CreateDate : 2013-9-6
     * @param context
     * @param sessionManagerHandler
     */
    public SettingSession(Context context, Handler sessionManagerHandler) {
        super(context, sessionManagerHandler);
        mMessageSender = new MessageSender(context);
    }

    public void putProtocol(JSONObject jsonProtocol) {
        super.putProtocol(jsonProtocol);

        operator = JsonTool.getJsonValue(mDataObject, "operator", "");
        operands = JsonTool.getJsonValue(mDataObject, "operands", "");
        Logger.d(TAG, "!--->putProtocol:---operator = " + operator + "; operands = " + operands);

        if (TextUtils.isEmpty(mAnswer)) {
            mAnswer = mQuestion;
        }
        if (SessionPreference.VALUE_SETTING_OBJ_3G.equals(operands)) {
            if (SessionPreference.VALUE_SETTING_ACT_OPEN.equals(operator)) {
                mAnswer = mContext.getString(R.string.open_3g);
                RomControl.enterControl(mContext, RomControl.ROM_OPEN_3G);
            } else if (SessionPreference.VALUE_SETTING_ACT_CLOSE.equals(operator)) {
                mAnswer = mContext.getString(R.string.close_3g);
                RomControl.enterControl(mContext, RomControl.ROM_CLOSE_3G);
            }
        } else if (SessionPreference.VALUE_SETTING_OBJ_AUTOLIGHT.equals(operands)) {
            if (SessionPreference.VALUE_SETTING_ACT_OPEN.equals(operator)) {} else if (SessionPreference.VALUE_SETTING_ACT_CLOSE
                    .equals(operator)) {}
            mAnswer = mContext.getString(R.string.open_display_settings);
            RomControl.enterControl(mContext, RomControl.ROM_OPEN_DISPLAY_SETTINGS);
        } else if (SessionPreference.VALUE_SETTING_OBJ_BLUETOOTH.equals(operands)) {
            if (SessionPreference.VALUE_SETTING_ACT_OPEN.equals(operator)) {
                mAnswer = mContext.getString(R.string.open_bluetooth);
                RomControl.enterControl(mContext, RomControl.ROM_OPEN_BLUETOOTH);
            } else if (SessionPreference.VALUE_SETTING_ACT_CLOSE.equals(operator)) {
                mAnswer = mContext.getString(R.string.close_bluetooth);
                RomControl.enterControl(mContext, RomControl.ROM_CLOSE_BLUETOOTH);
            }
        } else if (SessionPreference.VALUE_SETTING_OBJ_TIME.equals(operands)) {
            if (SessionPreference.VALUE_SETTING_ACT_SET.equals(operator)) {
                mAnswer = mContext.getString(R.string.open_time_settings);
                RomControl.enterControl(mContext, RomControl.ROM_OPEN_TIME_SETTINGS);
            }
        } else if (SessionPreference.VALUE_SETTING_OBJ_LIGHT.equals(operands)) {
            if (SessionPreference.VALUE_SETTING_ACT_INCREASE.equals(operator)) {} else if (SessionPreference.VALUE_SETTING_ACT_DECREASE
                    .equals(operator)) {} else if (SessionPreference.VALUE_SETTING_ACT_MAX
                    .equals(operator)) {} else if (SessionPreference.VALUE_SETTING_ACT_MIN
                    .equals(operator)) {}
            mAnswer = mContext.getString(R.string.open_display_settings);
            RomControl.enterControl(mContext, RomControl.ROM_OPEN_DISPLAY_SETTINGS);
        } else if (SessionPreference.VALUE_SETTING_OBJ_VOLUMN.equals(operands)) {
            if (SessionPreference.VALUE_SETTING_ACT_INCREASE.equals(operator)) {
                mAnswer = mContext.getString(R.string.increase_volumne);
                RomControl.enterControl(mContext, RomControl.ROM_INCREASE_VOLUMNE, 10);
            } else if (SessionPreference.VALUE_SETTING_ACT_DECREASE.equals(operator)) {
                mAnswer = mContext.getString(R.string.decrease_volumne);
                RomControl.enterControl(mContext, RomControl.ROM_DECREASE_VOLUMNE, 10);
            } else if (SessionPreference.VALUE_SETTING_ACT_MAX.equals(operator)) {
                mAnswer = mContext.getString(R.string.max_volumne);
                RomControl.enterControl(mContext, RomControl.ROM_VOLUMNE_MAX);
            } else if (SessionPreference.VALUE_SETTING_ACT_MIN.equals(operator)) {
                mAnswer = mContext.getString(R.string.min_volumne);
                RomControl.enterControl(mContext, RomControl.ROM_VOLUMNE_MIN);
            }
        } else if (SessionPreference.VALUE_SETTING_OBJ_MODEL_INAIR.equals(operands)) {
            if (SessionPreference.VALUE_SETTING_ACT_OPEN.equals(operator)) {
                mAnswer = mContext.getString(R.string.wait_open_setting_inair);
                RomControl.enterControl(mContext, RomControl.ROM_OPEN_MODEL_INAIR);
            } else if (SessionPreference.VALUE_SETTING_ACT_CLOSE.equals(operator)) {
                mAnswer = mContext.getString(R.string.wait_close_setting_inair);
                RomControl.enterControl(mContext, RomControl.ROM_CLOSE_MODEL_INAIR);
            }
        } else if (SessionPreference.VALUE_SETTING_OBJ_MODEL_MUTE.equals(operands)) {
            if (SessionPreference.VALUE_SETTING_ACT_OPEN.equals(operator)) {
                mAnswer = mContext.getString(R.string.open_model_mute);
                RomControl.enterControl(mContext, RomControl.ROM_OPEN_MODEL_MUTE);
            } else if (SessionPreference.VALUE_SETTING_ACT_CLOSE.equals(operator)) {
                mAnswer = mContext.getString(R.string.close_model_mute);
                RomControl.enterControl(mContext, RomControl.ROM_CLOSE_MODEL_MUTE);
            }
        } else if (SessionPreference.VALUE_SETTING_OBJ_MODEL_VIBRA.equals(operands)) {
            if (SessionPreference.VALUE_SETTING_ACT_OPEN.equals(operator)) {
                mAnswer = mContext.getString(R.string.open_model_vibra);
                RomControl.enterControl(mContext, RomControl.ROM_OPEN_MODEL_VIBRA);
            } else if (SessionPreference.VALUE_SETTING_ACT_CLOSE.equals(operator)) {
                mAnswer = mContext.getString(R.string.close_model_vibra);
                RomControl.enterControl(mContext, RomControl.ROM_CLOSE_MODEL_VIBRA);
            }
        } else if (SessionPreference.VALUE_SETTING_OBJ_RINGTONE.equals(operands)) {
            if (SessionPreference.VALUE_SETTING_ACT_SET.equals(operator)) {
                mAnswer = mContext.getString(R.string.open_sound_setting);
                RomControl.enterControl(mContext, RomControl.ROM_OPEN_SOUND_SETTINGS);
            }
        } else if (SessionPreference.VALUE_SETTING_OBJ_ROTATION.equals(operands)) {
            if (SessionPreference.VALUE_SETTING_ACT_OPEN.equals(operator)) {
                mAnswer = mContext.getString(R.string.open_rotation);
                RomControl.enterControl(mContext, RomControl.ROM_OPEN_ROTATION);
            } else if (SessionPreference.VALUE_SETTING_ACT_CLOSE.equals(operator)) {
                mAnswer = mContext.getString(R.string.close_rotation);
                RomControl.enterControl(mContext, RomControl.ROM_CLOSE_ROTATION);
            }
        } else if (SessionPreference.VALUE_SETTING_OBJ_WALLPAPER.equals(operands)) {
            if (SessionPreference.VALUE_SETTING_ACT_SET.equals(operator)) {
                mAnswer = mContext.getString(R.string.open_wallpaper_setting);
                RomControl.enterControl(mContext, RomControl.ROM_OPEN_WALLPAPER_SETTINGS);
            }
        } else if (SessionPreference.VALUE_SETTING_OBJ_WIFI.equals(operands)) {
            if (SessionPreference.VALUE_SETTING_ACT_OPEN.equals(operator)) {
                mAnswer = mContext.getString(R.string.open_wifi);
                RomControl.enterControl(mContext, RomControl.ROM_OPEN_WIFI);
            } else if (SessionPreference.VALUE_SETTING_ACT_CLOSE.equals(operator)) {
                mAnswer = mContext.getString(R.string.close_wifi);
                RomControl.enterControl(mContext, RomControl.ROM_CLOSE_WIFI);
            }
        } else if (SessionPreference.VALUE_SETTING_OBJ_WIFI_SPOT.equals(operands)) {
            if (SessionPreference.VALUE_SETTING_ACT_OPEN.equals(operator)) {
                mAnswer = mContext.getString(R.string.open_wifi_spot);
                RomControl.enterControl(mContext, RomControl.ROM_OPEN_WIFI_SPOT);
            } else if (SessionPreference.VALUE_SETTING_ACT_CLOSE.equals(operator)) {
                mAnswer = mContext.getString(R.string.close_wifi_spot);
                RomControl.enterControl(mContext, RomControl.ROM_CLOSE_WIFI_SPOT);
            }
        }
        /* < XD 20150717 delete begin */
        else if (SessionPreference.VALUE_SETTING_OBJ_CALL.equals(operands)) {
            Logger.d(TAG, "!--->VALUE_SETTING_OBJ_CALL---operator = " + operator);
            if (SessionPreference.VALUE_SETTING_ACT_ANSWER.equals(operator)) {
                mAnswer = mContext.getString(R.string.answer_call);
                Telephony.answerRingingCall(mContext);
            } else if (SessionPreference.VALUE_SETTING_ACT_HANG_UP.equals(operator)) {
                mAnswer = mContext.getString(R.string.end_call);
                Telephony.endCall(mContext);
            }
        }
        /* XD 20150717 delete End > */
        else if (SessionPreference.VALUE_SETTING_OBJ_MUSIC_SHUFFLE_PLAYBACK.equals(operands)) {
            if (SessionPreference.VALUE_SETTING_ACT_CANCEL.equals(operator)) {
                mAnswer = mContext.getString(R.string.cancel_random_play);
            } else if (SessionPreference.VALUE_SETTING_ACT_PLAY.equals(operator)) {
                mAnswer = mContext.getString(R.string.random_play);
                Intent intent = new Intent(CommandPreference.ACTION_SERVICE_CMD);
                intent.putExtra(CommandPreference.CMD_KEY_NAME,
                        CommandPreference.CMD_NAME_SHUFFLE_PLAYBACK);
                mMessageSender.sendMessage(intent, null);

            }
        } else if (SessionPreference.VALUE_SETTING_OBJ_MUSIC_ORDER_PLAYBACK.equals(operands)) {
            if (SessionPreference.VALUE_SETTING_ACT_PLAY.equals(operator)) {
                mAnswer = mContext.getString(R.string.sequence_play);
                Intent intent = new Intent(CommandPreference.ACTION_SERVICE_CMD);
                intent.putExtra(CommandPreference.CMD_KEY_NAME,
                        CommandPreference.CMD_NAME_ORDER_PLAYBACK);
                mMessageSender.sendMessage(intent, null);
            }
        } else if (SessionPreference.VALUE_SETTING_OBJ_MUSIC_SINGLE_CYCLE.equals(operands)) {
            if (SessionPreference.VALUE_SETTING_ACT_PLAY.equals(operator)) {
                mAnswer = mContext.getString(R.string.single_cycle);
                Intent intent = new Intent(CommandPreference.ACTION_SERVICE_CMD);
                intent.putExtra(CommandPreference.CMD_KEY_NAME,
                        CommandPreference.CMD_NAME_SINGLE_CYCLE);
                mMessageSender.sendMessage(intent, null);

            }
        } else if (SessionPreference.VALUE_SETTING_OBJ_MUSIC_LIST_CYCLE.equals(operands)) {
            if (SessionPreference.VALUE_SETTING_ACT_PLAY.equals(operator)) {
                mAnswer = mContext.getString(R.string.list_cycle);
                Intent intent = new Intent(CommandPreference.ACTION_SERVICE_CMD);
                intent.putExtra(CommandPreference.CMD_KEY_NAME,
                        CommandPreference.CMD_NAME_FULL_CYCLE);
                mMessageSender.sendMessage(intent, null);
            }
        } else if (SessionPreference.VALUE_SETTING_OBJ_MUSIC_FULL_CYCLE.equals(operands)) {
            if (SessionPreference.VALUE_SETTING_ACT_PLAY.equals(operator)) {
                mAnswer = mContext.getString(R.string.full_cycle);
                Intent intent = new Intent(CommandPreference.ACTION_SERVICE_CMD);
                intent.putExtra(CommandPreference.CMD_KEY_NAME,
                        CommandPreference.CMD_NAME_FULL_CYCLE);
                mMessageSender.sendMessage(intent, null);
            }
        } else if (SessionPreference.VALUE_SETTING_ACT_CLOSE_DESK_LYRIC.equals(operator)) {
            mAnswer = mContext.getString(R.string.close_desk_lyric);
            Intent intent = new Intent(CommandPreference.ACTION_SERVICE_CMD);
            intent.putExtra(CommandPreference.CMD_KEY_NAME,
                    CommandPreference.CMD_NAME_CLOSE_DESK_LYRIC);
            mMessageSender.sendMessage(intent, null);
        } else if (SessionPreference.VALUE_SETTING_ACT_OPEN_DESK_LYRIC.equals(operator)) {
            mAnswer = mContext.getString(R.string.open_desk_lyric);
            Intent intent = new Intent(CommandPreference.ACTION_SERVICE_CMD);
            intent.putExtra(CommandPreference.CMD_KEY_NAME,
                    CommandPreference.CMD_NAME_OPEN_DESK_LYRIC);
            mMessageSender.sendMessage(intent, null);
        } else if (SessionPreference.VALUE_SETTING_OBJ_MUSIC_SHUFFLE_PLAYBACK.equals(operands)) {
            if (SessionPreference.VALUE_SETTING_ACT_CANCEL.equals(operator)) {
                mAnswer = "已取消随机播放";
            } else if (SessionPreference.VALUE_SETTING_ACT_PLAY.equals(operator)) {
                mAnswer = "已为您随机播放";
                Intent intent = new Intent(CommandPreference.ACTION_SERVICE_CMD);
                intent.putExtra(CommandPreference.CMD_KEY_NAME,
                        CommandPreference.CMD_NAME_SHUFFLE_PLAYBACK);
                mMessageSender.sendMessage(intent, null);

            }
        } else if (SessionPreference.VALUE_SETTING_OBJ_MUSIC_ORDER_PLAYBACK.equals(operands)) {
            if (SessionPreference.VALUE_SETTING_ACT_PLAY.equals(operator)) {
                mAnswer = mContext.getString(R.string.sequence_play);
                Intent intent = new Intent(CommandPreference.ACTION_SERVICE_CMD);
                intent.putExtra(CommandPreference.CMD_KEY_NAME,
                        CommandPreference.CMD_NAME_ORDER_PLAYBACK);
                mMessageSender.sendMessage(intent, null);
            }
        } else if (SessionPreference.VALUE_SETTING_OBJ_MUSIC_SINGLE_CYCLE.equals(operands)) {
            if (SessionPreference.VALUE_SETTING_ACT_PLAY.equals(operator)) {
                mAnswer = mContext.getString(R.string.single_cycle);
                Intent intent = new Intent(CommandPreference.ACTION_SERVICE_CMD);
                intent.putExtra(CommandPreference.CMD_KEY_NAME,
                        CommandPreference.CMD_NAME_SINGLE_CYCLE);
                mMessageSender.sendMessage(intent, null);

            }
        } else if (SessionPreference.VALUE_SETTING_OBJ_MUSIC_LIST_CYCLE.equals(operands)) {
            if (SessionPreference.VALUE_SETTING_ACT_PLAY.equals(operator)) {
                mAnswer = mContext.getString(R.string.list_cycle);
                Intent intent = new Intent(CommandPreference.ACTION_SERVICE_CMD);
                intent.putExtra(CommandPreference.CMD_KEY_NAME,
                        CommandPreference.CMD_NAME_FULL_CYCLE);
                mMessageSender.sendMessage(intent, null);
            }
        } else if (SessionPreference.VALUE_SETTING_OBJ_MUSIC_FULL_CYCLE.equals(operands)) {
            if (SessionPreference.VALUE_SETTING_ACT_PLAY.equals(operator)) {
                mAnswer = mContext.getString(R.string.full_cycle);
                Intent intent = new Intent(CommandPreference.ACTION_SERVICE_CMD);
                intent.putExtra(CommandPreference.CMD_KEY_NAME,
                        CommandPreference.CMD_NAME_FULL_CYCLE);
                mMessageSender.sendMessage(intent, null);
            }
        } else if (SessionPreference.VALUE_SETTING_ACT_CLOSE_DESK_LYRIC.equals(operator)) {
            mAnswer = mContext.getString(R.string.close_desk_lyric);
            Intent intent = new Intent(CommandPreference.ACTION_SERVICE_CMD);
            intent.putExtra(CommandPreference.CMD_KEY_NAME,
                    CommandPreference.CMD_NAME_CLOSE_DESK_LYRIC);
            mMessageSender.sendMessage(intent, null);
        } else if (SessionPreference.VALUE_SETTING_ACT_OPEN_DESK_LYRIC.equals(operator)) {
            mAnswer = mContext.getString(R.string.close_desk_lyric);
            Intent intent = new Intent(CommandPreference.ACTION_SERVICE_CMD);
            intent.putExtra(CommandPreference.CMD_KEY_NAME,
                    CommandPreference.CMD_NAME_OPEN_DESK_LYRIC);
            mMessageSender.sendMessage(intent, null);
        } else if (SessionPreference.OPERAND_STANDARD_TTS.equals(operands)) {
            UserPerferenceUtil.setTtsTimbre(mContext, UserPerferenceUtil.VALUE_TTS_TIMBRE_STANDARD);
            GuiSettingUpdateUtil.sendTTSTimbreConfigure(mContext,
                    SessionPreference.SWITCH_TTS_MODLE_VOICE);
        } else if (SessionPreference.OPERAND_LZL_TTS.equals(operands)) {
            UserPerferenceUtil.setTtsTimbre(mContext, UserPerferenceUtil.VALUE_TTS_TIMBRE_SEXY);
            GuiSettingUpdateUtil.sendTTSTimbreConfigure(mContext,
                    SessionPreference.SWITCH_TTS_MODLE_VOICE);
        } else if (SessionPreference.OPERAND_AUTO_TTS.equals(operands)) {
            UserPerferenceUtil.setTtsTimbre(mContext, UserPerferenceUtil.VALUE_TTS_TIMBRE_AUTO);
            GuiSettingUpdateUtil.sendTTSTimbreConfigure(mContext,
                    SessionPreference.SWITCH_TTS_MODLE_VOICE);
        } else if (SessionPreference.OPERAND_HIGHT_VERSION.equals(operands)) {

            UserPerferenceUtil.setVersionMode(mContext, UserPerferenceUtil.VALUE_VERSION_MODE_HIGH);
            GuiSettingUpdateUtil.sendVersionLevelConfigure(mContext,
                    SessionPreference.PARAM_VERSION_LEVEL_TYPE_IDEL);

        } else if (SessionPreference.OPERAND_STANDARD_VERSION.equals(operands)) {

            UserPerferenceUtil.setVersionMode(mContext,
                    UserPerferenceUtil.VALUE_VERSION_MODE_STANDARD);

            GuiSettingUpdateUtil.sendVersionLevelConfigure(mContext,
                    SessionPreference.PARAM_VERSION_LEVEL_TYPE_IDEL);

        } else if (SessionPreference.OPERAND_SHOW_VERSION.equals(operands)) {

            Logger.i(TAG, "---------fuck code------------------" + operands);
            UserPerferenceUtil.setVersionMode(mContext, UserPerferenceUtil.VALUE_VERSION_MODE_EXP);
            GuiSettingUpdateUtil.sendVersionLevelConfigure(mContext,
                    SessionPreference.PARAM_VERSION_LEVEL_TYPE_RECORDING);

            isNeedDoOnTTSEnd = true;

        } else if (SessionPreference.OPERAND_AMEND_HOME_ADDRESS.equals(operands)) {

        } else if (SessionPreference.OPERAND_AMEND_OFFICE_ADDRESS.equals(operands)) {

        }

        // 如果协议中包含OBJ_XXX,请在这里添加
        else {
            if (operator.equals(SessionPreference.VALUE_SETTING_ACT_OPEN_CHANNEL)) {

            } else if (isPlayerControl(operator, jsonProtocol)) {
                Logger.d(TAG, "putProtocol---isPlayerControl---");
            }
            // 云端的关闭既然不处理就删掉判断
            /*
             * else if (SessionPreference.VALUE_SETTING_ACT_CLOSE.equals(operator)) { mAnswer =
             * "已执行关闭"; Intent intent = new Intent(CommandPreference.SERVICECMD);
             * intent.putExtra(CommandPreference.CMDNAME, CommandPreference.CMDSTOP);
             * mMessageSender.sendMessage(intent, null); }
             */
            else if (SessionPreference.VALUE_SETTING_OBJ_MUSIC_PREVIOUS_ITEM.equals(operands)) {
                if (SessionPreference.VALUE_SETTING_ACT_PREV.equals(operator)) {
                    mAnswer = mContext.getString(R.string.play_previous);
                    // Toast.makeText(mContext, jsonProtocol.toString(),
                    // Toast.LENGTH_SHORT).show();
                }
            } else if (SessionPreference.VALUE_SETTING_ACT_PLAY_MESSAGE.equals(operator)) {
                Logger.d(TAG, "--VALUE_SETTING_ACT_PLAY_MESSAGE Execution Broadcast--");
                mAnswer = mContext.getString(R.string.sms_content);
                // mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_PLAY_REVEIVED_MESSAGE);
            } else if (SessionPreference.VALUE_SETTING_ACT_CANCEL.equals(operator)) {
                mAnswer = mContext.getString(R.string.cancal);
            } else {
                addQuestionViewText(mQuestion);
                addAnswerViewText(mQuestion);
                NoPerSonContentView view = new NoPerSonContentView(mContext);
                view.setShowTitle(mContext.getString(R.string.no_support_cmd, mQuestion)); // TODO:
                addAnswerView(view, true);
                mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_SESSION_DONE);
                return;
            }
            // 如果协议中仅包含ACT_XXX，请在此添加
        }
        addQuestionViewText(mQuestion);
        addAnswerViewText(mAnswer);

        // NoPerSonContentView view = new NoPerSonContentView(mContext);
        // view.setShowTitle(mContext.getString(R.string.executed) + mQuestion); // TODO:

        String showText = mContext.getString(R.string.executed) + mQuestion;
        // String wakeupWord = UserPerferenceUtil.getWakeupWord(mContext);
        // showText = StringUtil.clearWakeupWord(showText, wakeupWord);

        Logger.d(TAG, "putProtocol----showText = " + showText);
        addAnswerView(getStatusView(showText), true);
    }

    /**
     * isPlayerControl
     * 
     * @author xiaodong.he
     * @date 20151103
     * @param operator
     * @return
     */
    private boolean isPlayerControl(String operator, JSONObject jsonProtocol) {
        if (SessionPreference.VALUE_SETTING_ACT_PREV.equals(operator)
                || SessionPreference.VALUE_SETTING_ACT_NEXT.equals(operator)
                || SessionPreference.VALUE_SETTING_ACT_PLAY.equals(operator)
                || SessionPreference.VALUE_SETTING_ACT_PAUSE.equals(operator)
                || SessionPreference.VALUE_SETTING_ACT_RESUME.equals(operator)
                || SessionPreference.VALUE_SETTING_ACT_STOP.equals(operator)) {

            try {
                String tts = JsonTool.getJsonValue(jsonProtocol, "tts");
                if (TextUtils.isEmpty(tts)) {
                    isNeedDoOnTTSEnd = false;
                    String text = JsonTool.getJsonValue(mDataObject, "text", "");
                    if (!TextUtils.isEmpty(text)) {
                        Toast.makeText(CrashApplication.getAppContext(), text, Toast.LENGTH_SHORT)
                                .show();
                    }
                    doOnTTSEnd();
                    return true;
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
            isNeedDoOnTTSEnd = true;
            return true;
        } else {
            isNeedDoOnTTSEnd = false;
            return false;
        }
    }

    /**
     * doOnTTSEnd
     * 
     * @author xiaodong.he
     * @date 20151103
     */
    private void doOnTTSEnd() {
        Logger.d(TAG, "doOnTTSEnd----operator = " + operator);
        if (SessionPreference.VALUE_SETTING_ACT_PREV.equals(operator)) {
            mAnswer = mContext.getString(R.string.previous);
            // xiaodong.he 20151028 modify
            PlayersControlManager.getInstance().sendPlayersControlCmd(mContext, mMessageSender,
                    CommandPreference.CMD_NAME_PREVIOUS);
        } else if (SessionPreference.VALUE_SETTING_ACT_NEXT.equals(operator)) {
            mAnswer = mContext.getString(R.string.next);
            PlayersControlManager.getInstance().sendPlayersControlCmd(mContext, mMessageSender,
                    CommandPreference.CMD_NAME_NEXT);
        } else if (SessionPreference.VALUE_SETTING_ACT_PLAY.equals(operator)) {
            mAnswer = mContext.getString(R.string.play);
            PlayersControlManager.getInstance().sendPlayersControlCmd(mContext, mMessageSender,
                    CommandPreference.CMD_NAME_PLAY);
        } else if (SessionPreference.VALUE_SETTING_ACT_PAUSE.equals(operator)) {
            mAnswer = mContext.getString(R.string.pause);
            PlayersControlManager.getInstance().sendPlayersControlCmd(mContext, mMessageSender,
                    CommandPreference.CMD_NAME_PAUSE);
        } else if (SessionPreference.VALUE_SETTING_ACT_RESUME.equals(operator)) {
            // continue play
            mAnswer = mContext.getString(R.string.recovery);
            PlayersControlManager.getInstance().sendPlayersControlCmd(mContext, mMessageSender,
                    CommandPreference.CMD_NAME_PLAY);
        } else if (SessionPreference.VALUE_SETTING_ACT_STOP.equals(operator)) {
            mAnswer = mContext.getString(R.string.stop);
            PlayersControlManager.getInstance().sendPlayersControlCmd(mContext, mMessageSender,
                    CommandPreference.CMD_NAME_STOP);
        }
    }

    /**
     * 
     * @author xiaodong.he
     * @param text
     * @return
     */
    private View getStatusView(String text) {
        LayoutInflater layoutInflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View statusView = layoutInflater.inflate(R.layout.session_mic_status_view, null);
        TextView mTvSessionMicStatus =
                (TextView) statusView.findViewById(R.id.tv_session_mic_status);
        mTvSessionMicStatus.setText(text);
        Button btnCancel = (Button) statusView.findViewById(R.id.cancelBtn);
        btnCancel.setVisibility(View.GONE);
        return statusView;
    }

    @Override
    public void onTTSEnd() {
        Logger.d(TAG, "onTTSEnd--isNeedDoOnTtsEnd = " + isNeedDoOnTTSEnd);
        super.onTTSEnd();

        if (isNeedDoOnTTSEnd) {
            doOnTTSEnd(); // XD added 20151103
        }

        if (UserPerferenceUtil.VALUE_VERSION_MODE_EXP == UserPerferenceUtil
                .getVersionMode(mContext) && !isNeedDoOnTTSEnd) {
            mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_SESSION_RELEASE_ONLY);
        } else {
            mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_SESSION_DONE);
        }
    }
}
