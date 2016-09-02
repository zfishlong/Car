/**
 * Copyright (c) 2012-2016 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : InputPoiPopWindow.java
 * @ProjectName : uniCarSolution
 * @PakageName : com.unisound.unicar.gui.view
 * @version : 1.0
 * @Author : Xiaodong.He
 * @CreateDate : 2016-1-28
 */
package com.unisound.unicar.gui.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.unisound.unicar.gui.R;
import com.unisound.unicar.gui.utils.DeviceTool;
import com.unisound.unicar.gui.utils.FunctionHelpUtil;
import com.unisound.unicar.gui.utils.Logger;
import com.unisound.unicar.gui.utils.StringUtil;

/**
 * InputPoiPopWindow
 * 
 * @author xiaodong.he
 * @date 2016-1-28
 */
public class InputPoiPopWindow extends PopupWindow {

    private static final String TAG = InputPoiPopWindow.class.getSimpleName();

    private Context mContext;

    private LinearLayout mAddAddressLayout;
    private EditText mEtInputText;
    private ImageView mBtnOk;

    private TextView mTvErrorMsg;

    private String toPoi = "";

    private OnInputPoiListener mOnInputPoiListener;

    public InputPoiPopWindow(Context context) {
        mContext = context;

        LayoutInflater mLayoutInflater =
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = mLayoutInflater.inflate(R.layout.layout_add_address, null);

        this.setContentView(contentView);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);
        // set PopupWindow clickable
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        contentView.setFocusableInTouchMode(true);
        // update state
        this.update();

        initView(contentView);
    }


    private void initView(View view) {
        mAddAddressLayout = (LinearLayout) view.findViewById(R.id.ll_add_address);
        mTvErrorMsg = (TextView) view.findViewById(R.id.tv_search_poi_error_msg);

        mEtInputText = (EditText) view.findViewById(R.id.et_add_address);
        mEtInputText.setHint(R.string.default_hint_added_address);

        mEtInputText.setFocusable(true);
        mBtnOk = (ImageView) view.findViewById(R.id.iv_add_address_ok);
        ImageView btnCancel = (ImageView) view.findViewById(R.id.iv_add_address_cancel);
        mBtnOk.setOnClickListener(mOnClickListener);
        mBtnOk.setEnabled(false);
        btnCancel.setOnClickListener(mOnClickListener);

        mEtInputText.addTextChangedListener(mEditTextWatcher);
    }

    /**
     * showChangeTextPopWindow
     */
    public void showPopWindow(View parent) {
        if (!this.isShowing()) {
            this.showAtLocation(parent, Gravity.TOP, 0, 0);
            // show Input Keypad
            mEtInputText.requestFocus();
            mEtInputText.setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (null != mEtInputText) {
                            mEtInputText.clearFocus();
                        }
                    }
                    return false;
                }
            });
            DeviceTool.showEditTextKeyboard(mEtInputText, true);
        }
    }

    /**
     * showConfirmOkButton
     * 
     * @param isShow
     */
    private void showConfirmOkButton(boolean isShow) {
        Logger.d(TAG, "showConfirmOkButton--isShow = " + isShow);
        mBtnOk.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    /**
     * set default location
     * 
     * @author xiaodong.he
     * @date 2015-12-14
     * 
     * @param location
     */
    public void setDefaultLocation(String location) {
        if (!TextUtils.isEmpty(location) && mEtInputText != null) {
            mEtInputText.setText(location);
        }
    }

    private TextWatcher mEditTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Logger.d(TAG, "mEditTextWatcher--onTextChanged--s=" + s + "; count = " + count
                    + "; start = " + start + "; before = " + before);
            if (null == mBtnOk) {
                return;
            }
            showConfirmOkButton(true);
            if (TextUtils.isEmpty(s)) {
                mBtnOk.setEnabled(false);
            } else {
                mBtnOk.setEnabled(true);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Logger.d(TAG, "mEditTextWatcher--beforeTextChanged--s=" + s + "; count = " + count
                    + "; start = " + start + "; after = " + after);
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_add_address_ok:
                    if (null != mEtInputText) {
                        String text = mEtInputText.getText().toString();
                        text = StringUtil.clearSpecialCharacter(text);
                        mEtInputText.setText(text);
                        mEtInputText.setSelection(mEtInputText.getText().length());
                        if (TextUtils.isEmpty(text)) {
                            return;
                        }
                        if (null != mOnInputPoiListener) {
                            mOnInputPoiListener.onOkClick(text);
                        }
                    }
                    DeviceTool.showEditTextKeyboard(mEtInputText, false);
                    break;
                case R.id.iv_add_address_cancel:
                    if (null != mOnInputPoiListener) {
                        mOnInputPoiListener.onCancelClick();
                    }
                    InputPoiPopWindow.this.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 
     * @param isKeyword
     */
    private void changeInputTextStyle(boolean isKeyword) {
        if (null == mEtInputText) {
            return;
        }
        String text = mEtInputText.getText().toString();
        Logger.d(TAG, "changeInputTextStyle--text:" + text + "; isKeyword = " + isKeyword);
        if (isKeyword) {
            mEtInputText.setText(FunctionHelpUtil.addDoubleQuotationMarks(text));
            mEtInputText.setTextColor(mContext.getResources().getColor(R.color.color_wakeup_word));
        } else {
            mEtInputText.setText(FunctionHelpUtil.removeDoubleQuotationMarks(text));
            mEtInputText.setTextColor(mContext.getResources().getColor(R.color.grey_white));
        }
    }


    /**
     * 
     * @param msg
     */
    private void showErrorMsg(String msg) {
        if (null != mTvErrorMsg) {
            mTvErrorMsg.setText(msg);
            if (TextUtils.isEmpty(msg)) {
                mTvErrorMsg.setVisibility(View.GONE);
            } else {
                mTvErrorMsg.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setOnInputPoiListener(OnInputPoiListener listener) {
        mOnInputPoiListener = listener;
    }


    /**
     * 
     * @author xiaodong.he
     * @date 2015-11-18
     */
    public interface OnInputPoiListener {

        public void onOkClick(String location);

        public void onCancelClick();

    }

}
