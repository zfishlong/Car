/**
 * Copyright (c) 2012-2012 Yunzhisheng(Shanghai) Co.Ltd. All right reserved.
 * 
 * @FileName : WaitingView.java
 * @ProjectName : iShuoShuo2
 * @PakageName : cn.yunzhisheng.vui.assistant.view
 * @Author : Brant
 * @CreateDate : 2012-12-26
 */
package com.unisound.unicar.gui.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.unisound.unicar.gui.R;
import com.unisound.unicar.gui.utils.Logger;

/**
 * @Author : Brant
 * @CreateDate : 2012-12-26
 * @ModifiedBy : xiaodong.he
 * @ModifiedDate: 2016-03-22
 * @Modified:
 */
public class LocalSearchRouteConfirmView extends FrameLayout implements ISessionView {
    public static final String TAG = "RouteWaitingContentView";
    private ImageView mImgBuffering;
    private TextView mTextStartPOI, mTextEndPOI;

    private LinearLayout mConditionLayout;
    private TextView mTvCondition;

    private LinearLayout mPathPointLayout;
    private TextView mTvPathPoint;

    private FrameLayout mFlRouteOk;
    private TextView mTvRouteCancelTime;
    private RelativeLayout mRlRouteCancel;
    private CountDownTimer mCountDownTimer;
    private ProgressBar mProgressBarWaiting;

    private IRouteWaitingContentViewListener mListener;

    public LocalSearchRouteConfirmView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Resources res = getResources();

        int left = (int) (res.getDimension(R.dimen.call_content_view_margin_left) + 0.5);
        int right = (int) (res.getDimension(R.dimen.call_content_view_margin_right) + 0.5);
        int top = (int) (res.getDimension(R.dimen.call_content_view_margin_top) + 0.5);
        int bottom = (int) (res.getDimension(R.dimen.call_content_view_margin_bottom) + 0.5);

        setPadding(left, top, right, bottom);
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.localsearch_route_content_view, this, true);

        findViews();
    }

    private void findViews() {
        mTextStartPOI = (TextView) findViewById(R.id.startPOIText);
        mTextEndPOI = (TextView) findViewById(R.id.endPOIText);

        mConditionLayout = (LinearLayout) findViewById(R.id.ll_route_condition);
        mTvCondition = (TextView) findViewById(R.id.tv_route_confirm_condition);
        mPathPointLayout = (LinearLayout) findViewById(R.id.ll_route_path_point);
        mTvPathPoint = (TextView) findViewById(R.id.tv_route_confirm_path_point);

        mImgBuffering = (ImageView) findViewById(R.id.imageViewBuffering);
        mImgBuffering.post(new Runnable() {
            @Override
            public void run() {
                Drawable drawable = mImgBuffering.getDrawable();
                if (drawable != null && drawable instanceof AnimationDrawable) {
                    ((AnimationDrawable) drawable).start();
                }
            }
        });

        mProgressBarWaiting = (ProgressBar) findViewById(R.id.progressBarWaiting);
        mRlRouteCancel = (RelativeLayout) findViewById(R.id.rl_route_cancel);
        mTvRouteCancelTime = (TextView) findViewById(R.id.tv_route_cancel_time);
        mFlRouteOk = (FrameLayout) findViewById(R.id.fl_route_ok);

        mFlRouteOk.setOnClickListener(mOnClickListener);
        mRlRouteCancel.setOnClickListener(mOnClickListener);
    }


    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fl_route_ok:
                    if (mCountDownTimer != null) {
                        mCountDownTimer.cancel();
                    }
                    if (mListener != null) {
                        mListener.onOk();
                    }
                    break;
                case R.id.rl_route_cancel:
                    if (mCountDownTimer != null) {
                        mCountDownTimer.cancel();
                    }

                    if (mListener != null) {
                        mListener.onCancel();
                    }
                    break;
            }

        }
    };

    public LocalSearchRouteConfirmView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LocalSearchRouteConfirmView(Context context) {
        this(context, null);
    }

    @Override
    public boolean isTemporary() {
        return true;
    }

    @Override
    public void release() {

    }

    public void setLisener(IRouteWaitingContentViewListener listener) {
        mListener = listener;
    }

    public interface IRouteWaitingContentViewListener {
        public void onOk();

        public void onCancel();

        public void onTimeUp();
    }

    public void setStartPOI(String text) {
        mTextStartPOI.setText(text);
    }

    public void setEndPOI(String text) {
        mTextEndPOI.setText(text);
    }

    /**
     * @author xiaodong.he
     * @param condition
     */
    public void setCondition(String condition) {
        if (TextUtils.isEmpty(condition)) {
            return;
        }
        mConditionLayout.setVisibility(View.VISIBLE);
        mTvCondition.setText(condition);
    }

    /**
     * @author xiaodong.he
     * @param pathPoints
     */
    public void setPathPoint(String pathPoints) {
        if (TextUtils.isEmpty(pathPoints)) {
            return;
        }
        mPathPointLayout.setVisibility(View.VISIBLE);
        mTvPathPoint.setText(pathPoints);
    }

    public void setListener(IRouteWaitingContentViewListener listener) {
        Logger.d(TAG, "!--->setListener()---mListener = " + listener);
        mListener = listener;
    }

    public void startCountDownTimer(final long countDownMillis) {
        mProgressBarWaiting.setVisibility(View.VISIBLE);
        mTvRouteCancelTime.setText(((countDownMillis + 1) / 1000) + "S");

        mCountDownTimer = new CountDownTimer(countDownMillis, 100) {

            public void onTick(long millisUntilFinished) {

                int progress =
                        (int) ((countDownMillis - millisUntilFinished) / (float) countDownMillis * mProgressBarWaiting
                                .getMax());
                mProgressBarWaiting.setProgress(progress);
                mTvRouteCancelTime.setText(((millisUntilFinished + 1) / 1000) + "S");
            }

            public void onFinish() {
                mProgressBarWaiting.setProgress(0);
                if (mListener != null) {
                    mListener.onTimeUp();
                }
            }
        }.start();
    }

    public void cancelCountDownTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }
}
