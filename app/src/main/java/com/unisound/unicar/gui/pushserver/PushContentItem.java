package com.unisound.unicar.gui.pushserver;

public class PushContentItem {
    private String type;
    private String value;
    private int position;
    private String flag;
    private String param;

    public PushContentItem(String type, String value, int position, String flag, String param) {
        super();
        this.type = type;
        this.value = value;
        this.position = position;
        this.flag = flag;
        this.param = param;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "PushItem [type=" + type + ", value=" + value + ", position=" + position + ", flag="
                + flag + ", param=" + param + "]";
    }

}
