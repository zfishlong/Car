package com.unisound.unicar.gui.pushbean;

public enum PushType {
    ShowMessage, // 只显示的内容 播完TTS自动关闭
    ConfirmMessage, // 需要确认的内容,确认的话 目前动作有 1.播一段音频2.播放一段文字3.导航到1个地址4.跳转到某个页面
    BackSettingMessage, // 后台动作 如更改一些设置
    SubscribeMessage // 早报
}
