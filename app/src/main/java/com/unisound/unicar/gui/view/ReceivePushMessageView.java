package com.unisound.unicar.gui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unisound.unicar.gui.R;
import com.unisound.unicar.gui.pushbean.PushModle;
import com.unisound.unicar.gui.pushbean.PushType;
import com.unisound.unicar.gui.utils.Logger;

/**
 * 
 * @author xiaodong
 * @date 20150717
 */
public class ReceivePushMessageView extends PushView implements ISessionView, OnClickListener {

    public static final String TAG = ReceivePushMessageView.class.getSimpleName();

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private IReceivePushMessageViewListener mListener;

    private String mScheduleType = "";

    private GlideImageView imgIcon;
    private TextView txtTitle;
    private TextView txtContent;
    private ImageView imgCancel;
    private PushModle pushModle;
    private Button butBlank;
    private Button btnConfirm;
    private Button btnCancel;
    private LinearLayout layoutConfirm;

    @SuppressLint("NewApi")
    public ReceivePushMessageView(Context context, PushModle pushModle, String scheduleType) {
        super(context);
        this.pushModle = pushModle;
        mContext = context;
        mScheduleType = scheduleType;
        mLayoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        findViews();

    }


    private void findViews() {
        View view = mLayoutInflater.inflate(R.layout.push_message_received_view, this, true);
        imgIcon = (GlideImageView) view.findViewById(R.id.imgIcon);
        txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        txtContent = (TextView) view.findViewById(R.id.txtContent);
        layoutConfirm = (LinearLayout) view.findViewById(R.id.layoutConfirm);
        imgCancel = (ImageView) view.findViewById(R.id.imgCancel);
        butBlank = (Button) view.findViewById(R.id.butBlank);
        btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        imgCancel.setOnClickListener(this);
        butBlank.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        txtTitle.setText(pushModle.getPushBaseInfo().getNotificationTitle());
        txtContent.setText(pushModle.getPushBaseInfo().getNotificationContent());
        imgIcon.setImageURI(pushModle.getPushBaseInfo().getNotificationIcon());

        if (pushModle.getPushBaseInfo().getPushType() == PushType.ConfirmMessage) {
            btnConfirm.setText(pushModle.getPushAction().getPushCommand().getConfirms().get(0));
            btnCancel.setText(pushModle.getPushAction().getPushCommand().getCancels().get(0));
            imgCancel.setVisibility(GONE);
        } else {
            imgCancel.setVisibility(VISIBLE);
            layoutConfirm.setVisibility(GONE);
        }

    }

    @Override
    public boolean isTemporary() {
        return true;
    }

    @Override
    public void release() {
        Logger.d(TAG, "!--->release---");
        removeAllViews();
    }


    public IReceivePushMessageViewListener getListener() {
        return mListener;
    }

    public void setListener(IReceivePushMessageViewListener listener) {
        this.mListener = listener;
    }


    public static interface IReceivePushMessageViewListener {
        public void onConfirm();

        public void onCancel();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgCancel:
                if (mListener != null) {
                    mListener.onCancel();
                }
                break;
            case R.id.butBlank:
                if (mListener != null) {
                    mListener.onCancel();
                }
                break;
            case R.id.btnConfirm:
                if (mListener != null) {
                    mListener.onConfirm();
                }
                break;
            case R.id.btnCancel:
                if (mListener != null) {
                    mListener.onCancel();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void cancel() {
        if (mListener != null) {
            mListener.onCancel();
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (mListener != null) {
                mListener.onCancel();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
