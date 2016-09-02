package com.unisound.unicar.gui.pushbean;

import java.util.ArrayList;
import java.util.List;

public class PushAction {

    public static final int ActionPlayMusic = 100; // 播一段音频
    public static final int ActionPlayTTS = 101; // 播放一段文字TTS
    public static final int ActionNavi = 102; // 导航到某个地方
    public static final int ActionJump = 103; // 跳转到某个页面


    private PushCommand pushCommand;// 命令词 如导航 播放 确认 取消

    private int action = 0; // 通知动作 默认是0 具体含义见注释

    private String actionMusic;// 音频URL action 为 ActionPlayMusic时有效

    private String actionTTS;// 播放的TTS内容 action 为 ActionPlayTTS时有效

    private PushLocation actionLocation;// 导航地址信息 为 ActionNavi时有效

    private String className; // 跳转页面的名称称 如com.example.testactivity.MainActivity2 ActionJump时有效


    public PushCommand getPushCommand() {
        if (pushCommand != null) {
            if (pushCommand.getConfirms() == null || pushCommand.getConfirms().size() == 0) {
                List<String> confirms = new ArrayList<String>();
                confirms.add("确定");
                pushCommand.setConfirms(confirms);
            }
            if (pushCommand.getCancels() == null || pushCommand.getCancels().size() == 0) {
                List<String> cancels = new ArrayList<String>();
                cancels.add("取消");
                pushCommand.setCancels(cancels);
            }
            return pushCommand;
        } else {
            PushCommand pushCommand = new PushCommand();
            List<String> confirms = new ArrayList<String>();
            confirms.add("确定");
            List<String> cancels = new ArrayList<String>();
            cancels.add("取消");
            pushCommand.setConfirms(confirms);
            pushCommand.setCancels(cancels);
            return pushCommand;
        }
    }

    public void setPushCommand(PushCommand pushCommand) {
        this.pushCommand = pushCommand;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getActionMusic() {
        return actionMusic;
    }

    public void setActionMusic(String actionMusic) {
        this.actionMusic = actionMusic;
    }

    public String getActionTTS() {
        return actionTTS;
    }

    public void setActionTTS(String actionTTS) {
        this.actionTTS = actionTTS;
    }

    public PushLocation getActionLocation() {
        return actionLocation;
    }

    public void setActionLocation(PushLocation actionLocation) {
        this.actionLocation = actionLocation;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public static int getActionplaymusic() {
        return ActionPlayMusic;
    }

    public static int getActionplaytts() {
        return ActionPlayTTS;
    }

    public static int getActionnavi() {
        return ActionNavi;
    }

    public static int getActionjump() {
        return ActionJump;
    }


}
