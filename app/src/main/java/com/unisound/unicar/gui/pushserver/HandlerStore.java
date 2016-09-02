package com.unisound.unicar.gui.pushserver;

import java.util.HashMap;

import android.os.Handler;


public class HandlerStore {
    private HandlerStore() {

    }

    private HashMap<String, Handler> handlers = new HashMap<String, Handler>();
    private static HandlerStore handlerStore;

    public static HandlerStore getInstance() {
        if (handlerStore == null) {
            synchronized (HandlerStore.class) {
                if (handlerStore == null) {
                    handlerStore = new HandlerStore();

                }
            }
        }
        return handlerStore;
    }

    public void put(String key, Handler handler) {
        handlers.put(key, handler);
    }

    public Handler get(String key) {
        return handlers.get(key);
    }
}
