package com.unisound.unicar.gui.ui;

import android.app.Activity;
import android.os.Bundle;

import com.unisound.unicar.gui.utils.AppManager;
import com.unisound.unicar.gui.utils.Logger;

/**
 * 应用程序Activity的基类
 * 
 * @author wlp
 * @version 1.0
 * @created 2012-9-18
 */
public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d("BaseActivity", "onCreate----");
        // 添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Logger.d("BaseActivity", "onDestroy----");
        // 结束Activity&从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
    }

}
