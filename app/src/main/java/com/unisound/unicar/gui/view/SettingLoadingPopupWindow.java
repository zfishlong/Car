/**
 * Copyright (c) 2012-2015 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : SettingLoadingPopupWindow.java
 * @ProjectName : uniCarSolution
 * @PakageName : com.unisound.unicar.gui.view
 * @version : 1.5
 * @Author : Xiaodong.He
 * @CreateDate : 2016-01-12
 */
package com.unisound.unicar.gui.view;

import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.unisound.unicar.gui.R;
import com.unisound.unicar.gui.utils.Logger;

/**
 * 
 * @author xiaodong.he
 * @date 2016-01-12
 */
public class SettingLoadingPopupWindow extends PopupWindow {

    private static final String TAG = SettingLoadingPopupWindow.class.getSimpleName();
    private Context mContext;

    private TextView mTextViewTitle;

    private LinearLayout mCancelLayout;
    private ImageView mIvCancelSwitch;

    private ISettingLoadingPopListener mSettingLoadingPopListener;

    public SettingLoadingPopupWindow(Context context) {
        mContext = context;

        LayoutInflater mLayoutInflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = mLayoutInflater.inflate(R.layout.pop_setting_loading, null);

        this.setContentView(contentView);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);
        // set PopupWindow clickable
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        contentView.setFocusableInTouchMode(true);
        // update state
        this.update();

        findViews(contentView);

    }

    private void findViews(View contentView) {
        mTextViewTitle = (TextView) contentView.findViewById(R.id.tv_pop_title);

        mCancelLayout = (LinearLayout) contentView.findViewById(R.id.ll_loading_cancel);
        mIvCancelSwitch = (ImageView) contentView.findViewById(R.id.iv_cancel_switch);

        mIvCancelSwitch.setOnClickListener(mOnClickListener);
        contentView.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Logger.d(TAG, "Click BACK, do nothing.");
                    // dismiss();
                    return true;
                }
                return false;
            }
        });
    }


    public void setTitle(String title) {
        mTextViewTitle.setText(title);
    }

    public void setTitle(int titleRes) {
        mTextViewTitle.setText(titleRes);
    }

    /**
     * 
     * @author xiaodong.he
     * @date 2016-2-2
     * @param isEnable
     */
    public void setCancelButtonEnable(boolean isEnable) {
        if (isEnable) {
            mCancelLayout.setVisibility(View.VISIBLE);
        } else {
            mCancelLayout.setVisibility(View.GONE);
        }
    }

    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_cancel_switch:
                    Logger.d(TAG, "onClick--cancel switch");
                    if (null != mSettingLoadingPopListener) {
                        mSettingLoadingPopListener.onCancelClick();
                    }
                    SettingLoadingPopupWindow.this.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * showChangeTextPopWindow
     */
    public void showPopWindow(View parent) {
        if (!this.isShowing()) {
            this.showAtLocation(parent, Gravity.TOP, 0, 0);
        }
    }

    /**
     * 
     * @date 2016-1-27
     * @param parent
     */
    public void showPopWindowCenter(View parent) {
        if (!this.isShowing()) {
            this.showAtLocation(parent, Gravity.CENTER_VERTICAL, 0, 0);
        }
    }

    public void setSettingLoadingPopListener(ISettingLoadingPopListener listener) {
        mSettingLoadingPopListener = listener;
    }

    public interface ISettingLoadingPopListener {

        public void onCancelClick();

    }

}
