/**
 * Copyright (c) 2012-2015 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : AboutActivity.java
 * @ProjectName : uniCarGUI
 * @PakageName : com.unisound.unicar.gui.ui
 * @version : 1.0
 * @Author : Xiaodong.He
 * @CreateDate : 2015-07-15
 */
package com.unisound.unicar.gui.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.unisound.unicar.gui.R;

/**
 * 
 * @author xiaodong
 * @date 20150715
 */
public class AboutActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ImageButton returnBtn = (ImageButton) findViewById(R.id.backBtn);
        returnBtn.setOnClickListener(mReturnListerner);

        // TextView textViewVersion = (TextView) findViewById(R.id.tv_version);
        // textViewVersion.setText(String.format(textViewVersion.getText().toString(),
        // RomDevice.getAppVersionName(this)));
    }

    private OnClickListener mReturnListerner = new OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    protected void onDestroy() {
        super.onDestroy();
    };

}
