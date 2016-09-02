package com.unisound.unicar.gui.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * 
 * @Author : xiaodong
 * @CreateDate : 2015-7-6
 */
@SuppressLint("ClickableViewAccessibility")
public class LoadViewLinearLayout extends LinearLayout {

    private DispatchKeyEventListener mDispatchKeyEventListener;
    private OnTouchEventListener mOnTouchEventListener;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public LoadViewLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LoadViewLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadViewLinearLayout(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // Logger.d(TAG, "!--->dispatchKeyEvent: getKeyCode = " + event.getKeyCode() + ", Action = "
        // + event.getAction());
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            // Do not response ACTION_UP
            // Logger.d("!--->Back pressed. KeyCode = " + event.getKeyCode() + ", Action = "
            // + event.getAction());
            return true;
        }
        if (mDispatchKeyEventListener != null) {
            return mDispatchKeyEventListener.dispatchKeyEvent(event);
        }

        return super.dispatchKeyEvent(event);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Logger.d(TAG, "!--->onKeyDown: keyCode = " + keyCode);
        if (mDispatchKeyEventListener != null) {
            return mDispatchKeyEventListener.dispatchKeyEvent(event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Logger.d(TAG, "!--->onTouchEvent: getAction = " + event.getAction());
        if (mOnTouchEventListener != null) {
            return mOnTouchEventListener.onTouchEvent(event);
        }

        return super.onTouchEvent(event);
    }

    public DispatchKeyEventListener getDispatchKeyEventListener() {
        return mDispatchKeyEventListener;
    }

    public void setDispatchKeyEventListener(DispatchKeyEventListener mDispatchKeyEventListener) {
        this.mDispatchKeyEventListener = mDispatchKeyEventListener;
    }

    public static interface DispatchKeyEventListener {
        boolean dispatchKeyEvent(KeyEvent event);
    }


    public OnTouchEventListener getOnTouchEventListener() {
        return mOnTouchEventListener;
    }

    public void setOnTouchEventListener(OnTouchEventListener onTouchEventListener) {
        this.mOnTouchEventListener = onTouchEventListener;
    }

    public static interface OnTouchEventListener {
        boolean onTouchEvent(MotionEvent event);
    }
}
