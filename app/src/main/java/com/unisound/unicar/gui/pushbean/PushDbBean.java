package com.unisound.unicar.gui.pushbean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import com.google.gson.Gson;

@Table(name = "PushDbBean")
public class PushDbBean {
    @Column(name = "id", isId = true)
    private String id;

    @Column(name = "content")
    private String content;

    @Column(name = "read")
    private boolean read;

    @Column(name = "time")
    private String time;


    public PushDbBean() {

    }

    public PushDbBean(PushModle pushModle) {
        setId(pushModle.getId());
        setTime(pushModle.getTime());
        setContent(new Gson().toJson(pushModle));
        setRead(pushModle.isRead());
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

}
