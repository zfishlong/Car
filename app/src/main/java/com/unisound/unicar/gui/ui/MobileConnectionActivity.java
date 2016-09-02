/**
 * Copyright (c) 2012-2015 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : MobileConnectionActivity.java
 * @ProjectName : uniCarGUI
 * @PakageName : com.unisound.unicar.gui.ui
 * @version : 1.0
 * @Author : Xiaodong.He
 * @CreateDate : 2015-12-14
 */
package com.unisound.unicar.gui.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.unisound.unicar.gui.R;
import com.unisound.unicar.gui.preference.UserPerferenceUtil;
import com.unisound.unicar.gui.utils.CreateQRCodeUtil;
import com.unisound.unicar.gui.utils.DeviceTool;
import com.unisound.unicar.gui.utils.Logger;

/**
 * 
 * @author xiaodong.he
 * @date 20151214
 */
public class MobileConnectionActivity extends BaseActivity {

    private static final String TAG = MobileConnectionActivity.class.getSimpleName();
    private Context mContext;

    private ImageView mIvShowImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_connection);
        mContext = getApplicationContext();

        ImageButton returnBtn = (ImageButton) findViewById(R.id.backBtn);
        returnBtn.setOnClickListener(mReturnListerner);

        mIvShowImage = (ImageView) findViewById(R.id.iv_show_image);
    }

    private OnClickListener mReturnListerner = new OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();

        showImage(mContext);
    }

    private void showImage(Context context) {
        int screenHight = DeviceTool.getScreenHight(mContext);
        int binnerHight =
                mContext.getResources().getDimensionPixelSize(R.dimen.second_page_banner_height);
        int imageWidth = screenHight - binnerHight - 80;
        Logger.d(TAG, "screenHight = " + screenHight + "; binnerHight = " + binnerHight
                + "; imageWidth = " + imageWidth);

        // String uuid = DeviceTool.getIMEI(context) + "_" + DeviceTool.getMac(context);
        String uuid = UserPerferenceUtil.getUuid(context);
        Logger.d(TAG, "showImage--uuid = " + uuid);
        Bitmap logo = BitmapFactory.decodeResource(super.getResources(), R.drawable.ic_launcher);
        Bitmap bitmap = CreateQRCodeUtil.createQRCode(uuid, logo, imageWidth);
        if (bitmap != null) {
            mIvShowImage.setImageBitmap(bitmap);
        }
    }


    protected void onDestroy() {
        super.onDestroy();
        Logger.d(TAG, "onDestroy----");
    };

}
