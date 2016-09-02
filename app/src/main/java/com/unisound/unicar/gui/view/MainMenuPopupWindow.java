/**
 * Copyright (c) 2012-2015 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : MainMenuPopupWindow.java
 * @ProjectName : uniCarGUI_UI3
 * @PakageName : com.unisound.unicar.gui.view
 * @version : 1.0
 * @Author : Xiaodong.He
 * @CreateDate : 2015-11-10
 */
package com.unisound.unicar.gui.view;

import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import com.unisound.unicar.gui.R;
import com.unisound.unicar.gui.pushserver.PushDb;
import com.unisound.unicar.gui.utils.Logger;

/**
 * MainMenuPopupWindow
 * 
 * @author xiaodong.he
 * @date 2015-12-04
 */
public class MainMenuPopupWindow extends PopupWindow {

    private static final String TAG = MainMenuPopupWindow.class.getSimpleName();
    private Context mContext;

    private LinearLayout mAllFunctionLayout;
    private LinearLayout mSettingLayout;
    private LinearLayout mPushLayout;
    private ImageView mIvPushNoRead;

    private MainMenuClickListener mMainMenuClickListener;

    public MainMenuPopupWindow(Context context) {
        mContext = context;

        LayoutInflater mLayoutInflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = mLayoutInflater.inflate(R.layout.pop_main_menu, null);
        this.setContentView(contentView);
        int width = context.getResources().getDimensionPixelSize(R.dimen.width_main_menu);
        Logger.d(TAG, "MainMenuPopupWindow--width = " + width);
        this.setWidth(width);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // set PopupWindow clickable
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        contentView.setFocusableInTouchMode(true);
        // update state
        this.update();

        findViews(contentView);
        refershPushNoRead();

    }

    private void refershPushNoRead() {
        if (PushDb.hasNoRead()) {
            mIvPushNoRead.setVisibility(View.VISIBLE);
        } else {
            mIvPushNoRead.setVisibility(View.GONE);
        }
    }

    private void findViews(View contentView) {
        mAllFunctionLayout = (LinearLayout) contentView.findViewById(R.id.menu_layout_all_function);
        mSettingLayout = (LinearLayout) contentView.findViewById(R.id.menu_layout_setting);
        mPushLayout = (LinearLayout) contentView.findViewById(R.id.menu_layout_push_function);
        mIvPushNoRead = (ImageView) contentView.findViewById(R.id.push_no_read_icon);

        mAllFunctionLayout.setOnClickListener(mOnClickListener);
        mSettingLayout.setOnClickListener(mOnClickListener);
        mPushLayout.setOnClickListener(mOnClickListener);

        contentView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MainMenuPopupWindow.this.isShowing()) {
                    dismiss();
                }
                return false;
            }
        });

        contentView.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                }
                return false;
            }
        });
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.menu_layout_all_function:
                    if (null != mMainMenuClickListener) {
                        mMainMenuClickListener.onAllFunctionClick();
                    }
                    dismiss();
                    break;
                case R.id.menu_layout_setting:
                    if (null != mMainMenuClickListener) {
                        mMainMenuClickListener.onSetingClick();
                    }
                    dismiss();
                    break;
                case R.id.menu_layout_push_function:
                    if (null != mMainMenuClickListener) {
                        mMainMenuClickListener.onPushClick();
                    }
                    dismiss();
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
        Logger.d(TAG, "showPopWindow-----parent = " + parent);
        if (!this.isShowing()) {
            int[] location = new int[2];
            parent.getLocationOnScreen(location);

            int x = location[0] - 20;
            int y = location[1] - this.getHeight();
            Logger.d(TAG, "showPopWindow---x = " + x + "; y = " + y);
            // Show on parent TOP
            this.showAtLocation(parent, Gravity.NO_GRAVITY, location[0] - 20,
                    location[1] - this.getHeight());
        }
    }

    public void setMainMenuClickListener(MainMenuClickListener listener) {
        mMainMenuClickListener = listener;
    }

    public interface MainMenuClickListener {

        public void onAllFunctionClick();

        public void onSetingClick();

        public void onPushClick();

    }

}
