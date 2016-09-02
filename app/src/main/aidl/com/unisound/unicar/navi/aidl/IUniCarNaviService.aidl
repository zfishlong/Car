package com.unisound.unicar.navi.aidl;

import com.unisound.unicar.navi.aidl.IUniCarNaviCallback;

interface IUniCarNaviService{

    void startNavi(String json);
    
    void onControlCommand(String json);

    void registerCallback(IUniCarNaviCallback callback);

    IUniCarNaviCallback getCallback();

}