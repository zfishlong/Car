package com.unisound.unicar.gui.pushbean;

import java.util.ArrayList;
import java.util.List;


public class PushCommand {

    private List<String> confirms = new ArrayList<String>();
    private List<String> cancels = new ArrayList<String>();

    public List<String> getConfirms() {
        return confirms;
    }

    public void setConfirms(List<String> confirms) {
        this.confirms = confirms;
    }

    public List<String> getCancels() {
        return cancels;
    }

    public void setCancels(List<String> cancels) {
        this.cancels = cancels;
    }

}
