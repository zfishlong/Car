package com.unisound.unicar.gui.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.unisound.unicar.gui.R;
import com.unisound.unicar.gui.application.CrashApplication;
import com.unisound.unicar.gui.preference.SessionPreference;
import com.unisound.unicar.gui.pushbean.PushAction;
import com.unisound.unicar.gui.pushbean.PushModle;
import com.unisound.unicar.gui.pushbean.PushType;
import com.unisound.unicar.gui.pushserver.IMediaPlayerStateListener;
import com.unisound.unicar.gui.pushserver.PlayerState;
import com.unisound.unicar.gui.pushserver.PushDb;
import com.unisound.unicar.gui.pushserver.PushMediaPlayer;
import com.unisound.unicar.gui.utils.GuiProtocolUtil;
import com.unisound.unicar.gui.utils.TTSUtil;
import com.unisound.unicar.gui.utils.Util;
import com.unisound.unicar.gui.view.GlideImageView;

public class PushAdapt extends BaseAdapter {

    private static final String TAG = PushAdapt.class.getSimpleName();

    private List<PushModle> pushModels;
    private Context mContext;
    private LayoutInflater mInflater;
    private Handler mSessionManagerHandler;
    private PushMediaPlayer pushMediaPlayer = new PushMediaPlayer(CrashApplication.getAppContext());

    public PushAdapt(Context context, List<PushModle> pushModles, Handler handler) {
        this.pushModels = pushModles;
        this.mContext = context;
        this.mInflater = (LayoutInflater) LayoutInflater.from(mContext);
        this.mSessionManagerHandler = handler;
    }

    @Override
    public int getCount() {
        if (pushModels != null) {
            return pushModels.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return pushModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (null == view) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.push_message_item_view, null);
            holder.imgIcon = (GlideImageView) view.findViewById(R.id.imgIcon);
            holder.txtContent = (TextView) view.findViewById(R.id.txtContent);
            holder.txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            holder.layoutConfirm = (LinearLayout) view.findViewById(R.id.layoutConfirm);
            holder.imgDelete = (ImageView) view.findViewById(R.id.imgDelete);
            holder.btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
            holder.btnDelete = (Button) view.findViewById(R.id.btnDelete);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final PushModle pushModle = pushModels.get(position);
        if (!pushModle.isRead()) {
            pushModle.setRead(true);
            PushDb.upPushModel(pushModle);
        }
        if (pushModle.getPushBaseInfo().getPushType() == PushType.ShowMessage) {
            holder.layoutConfirm.setVisibility(View.GONE);
            holder.imgDelete.setVisibility(View.VISIBLE);
        }
        if (pushModle.getPushBaseInfo().getPushType() == PushType.ConfirmMessage) {
            holder.layoutConfirm.setVisibility(View.VISIBLE);
            holder.imgDelete.setVisibility(View.GONE);
            holder.btnConfirm
                    .setText(pushModle.getPushAction().getPushCommand().getConfirms().get(0));


            if (pushModle.getPushAction().getAction() == PushAction.ActionPlayMusic) {
                String musicUrl = pushModle.getPushAction().getActionMusic();
                String name =
                        Util.stringToMD5(musicUrl) + musicUrl.substring(musicUrl.lastIndexOf("."));
                String catchPath = Util.getWelcomeCachePath(CrashApplication.getAppContext());
                File file = new File(catchPath, name);
                String nowPlay = pushMediaPlayer.getNowPlayId();
                int state = pushMediaPlayer.getNowPlayState();

                if (TextUtils.equals(nowPlay, pushModle.getId())
                        && state == PlayerState.MPS_PLAYING) {
                    holder.btnConfirm.setText("停止");
                    holder.btnConfirm.setOnClickListener(stopMusiClickListener);
                } else {
                    holder.btnConfirm.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            playMusic(pushModle, holder.btnConfirm);
                        }
                    });
                }
            }
            if (pushModle.getPushAction().getAction() == PushAction.ActionNavi) {
                holder.btnConfirm.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        navi(pushModle);
                    }
                });
            }
            if (pushModle.getPushAction().getAction() == PushAction.ActionPlayTTS) {
                holder.btnConfirm.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playTTS(pushModle);
                    }
                });
            }
            if (pushModle.getPushAction().getAction() == PushAction.ActionJump) {
                holder.btnConfirm.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        jump(pushModle);
                    }
                });
            }
        }
        holder.imgIcon.setImageURI(pushModle.getPushBaseInfo().getNotificationIcon());
        holder.txtContent.setText(pushModle.getPushBaseInfo().getNotificationContent());
        holder.txtTitle.setText(pushModle.getPushBaseInfo().getNotificationTitle() + "("
                + pushModle.getTime() + ")");
        OnClickListener deletelClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.btnConfirm.getText().equals("停止")) {
                    pushMediaPlayer.stop();
                }
                PushDb.deletePushModle(pushModle);
                pushModels.remove(pushModle);
                notifyDataSetChanged();
            }
        };
        holder.imgDelete.setOnClickListener(deletelClickListener);
        holder.btnDelete.setOnClickListener(deletelClickListener);
        return view;
    }

    private void playMusic(final PushModle pushModle, final Button button) {
        String musicUrl = pushModle.getPushAction().getActionMusic();
        String name = Util.stringToMD5(musicUrl) + musicUrl.substring(musicUrl.lastIndexOf("."));
        String catchPath = Util.getWelcomeCachePath(CrashApplication.getAppContext());
        File file = new File(catchPath, name);
        pushMediaPlayer.play(pushModle.getId(), file.getPath(), new IMediaPlayerStateListener() {
            @Override
            public void onPlayerState(int state) {
                notifyDataSetChanged();
            }
        });
    }

    private void navi(final PushModle pushModle) {
        String location = new Gson().toJson(pushModle.getPushAction().getActionLocation());
        Message msg = new Message();
        msg.what = SessionPreference.MESSAGE_UI_OPERATE_PROTOCAL;
        Bundle bundle = new Bundle();
        bundle.putString(SessionPreference.KEY_BUNDLE_EVENT_NAME,
                SessionPreference.EVENT_NAME_GO_FAVORITE_ADDRESS);
        bundle.putString(SessionPreference.KEY_BUNDLE_PROTOCAL, GuiProtocolUtil
                .getGoFavoriteAddressProtocol(location, SessionPreference.PARAM_ADDRESS_TYPE_NAVI));
        msg.obj = bundle;

        mSessionManagerHandler.sendMessage(msg);
    }

    private void jump(final PushModle pushModle) {
        if (pushModle != null && pushModle.needJump()) {
            String className = pushModle.getPushAction().getClassName();
            try {
                Class<?> jumpClass = Class.forName(className);
                Intent it = new Intent().setClass(CrashApplication.getAppContext(), jumpClass);
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                CrashApplication.getAppContext().startActivity(it);
            } catch (Exception e) {
                TTSUtil.playTTSWakeUp(mContext, "暂不支持该功能,请升级后再尝试!");
            }
        }
    }

    private void playTTS(final PushModle pushModle) {
        TTSUtil.playTTSWakeUp(mContext, pushModle.getPushAction().getActionTTS());
    }

    OnClickListener stopMusiClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            pushMediaPlayer.stop();
        }
    };

    class ViewHolder {
        GlideImageView imgIcon;
        TextView txtContent;
        TextView txtTitle;
        LinearLayout layoutConfirm;
        ImageView imgDelete;
        Button btnConfirm;
        Button btnDelete;
    }

    public void onDestroy() {
        pushMediaPlayer.release();
    }
}
