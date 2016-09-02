package com.unisound.unicar.gui.pushserver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.client.ClientProtocolException;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.loopj.android.http.SyncHttpClient;
import com.unisound.unicar.gui.application.CrashApplication;
import com.unisound.unicar.gui.preference.SharedPreference;
import com.unisound.unicar.gui.preference.UserPerferenceUtil;
import com.unisound.unicar.gui.pushbean.PushModle;
import com.unisound.unicar.gui.pushbean.PushType;
import com.unisound.unicar.gui.subscribebean.SubscribeMessage;
import com.unisound.unicar.gui.subscribebean.WelcomeData;
import com.unisound.unicar.gui.ui.IWindowService;
import com.unisound.unicar.gui.ui.WindowService;
import com.unisound.unicar.gui.utils.Logger;
import com.unisound.unicar.gui.utils.Util;


public class PushBlockQueue {
    private String TAG = PushBlockQueue.class.getSimpleName();
    private IWindowService.Stub guiCallBack;
    private SyncHttpClient mSyncHttpClient;


    private LinkedBlockingQueue<PushModle> messageQueue = new LinkedBlockingQueue<PushModle>();
    private static PushBlockQueue pushBlockQueue;
    private Handler guiHandler;

    private PushBlockQueue() {
        mSyncHttpClient = new SyncHttpClient();
        mSyncHttpClient.setConnectTimeout(30000);
        mSyncHttpClient.setResponseTimeout(30000);
        mSyncHttpClient.setTimeout(30000);
    }

    public Handler getGuiHandler() {
        return guiHandler;
    }

    public void setGuiHandler(Handler guiHandler) {
        this.guiHandler = guiHandler;
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            synchronized (handler) {
                Logger.d(TAG, "处理队列");
                if (messageQueue != null && messageQueue.isEmpty() == false) {
                    if (isGuiIdle() == false || guiHandler == null) {
                        Logger.d(TAG, "GUI Busy 不处理通知信息!" + isGuiIdle() + "guihandler" + guiHandler);
                    } else {
                        try {
                            PushModle pushModle = messageQueue.take();
                            if (guiHandler != null) {
                                Message msgSend = guiHandler.obtainMessage();
                                msgSend.what = WindowService.MSG_ON_SEND_PUSH_EVENT;
                                msgSend.obj = pushModle;
                                guiHandler.sendMessage(msgSend);
                            }

                        } catch (InterruptedException e) {

                        }
                    }
                }
                if (messageQueue == null || messageQueue.isEmpty()) {
                    Logger.d(TAG, "队列为空 不处理");
                    handler.removeMessages(0);
                } else {
                    Logger.d(TAG, "队列不空 500ms后处理");
                    handler.removeMessages(0);
                    handler.sendEmptyMessageDelayed(0, 1000);
                }
            }
        }
    };

    public static PushBlockQueue getInstance() {
        if (pushBlockQueue == null) {
            synchronized (PushBlockQueue.class) {
                if (pushBlockQueue == null) {
                    pushBlockQueue = new PushBlockQueue();

                }
            }
        }
        return pushBlockQueue;
    }

    public void addQueue(final PushModle pushModle) {
        if (!UserPerferenceUtil.getSubEnable(CrashApplication.getAppContext())) {
            if (pushModle.getPushBaseInfo().getPushType() == PushType.SubscribeMessage) {
                return;
            }
        }

        try {
            if (pushModle != null) {
                if (pushModle.getPushBaseInfo().getPushType() == PushType.SubscribeMessage) {
                    String content = pushModle.getPushBaseInfo().getNotificationContent();
                    if (!TextUtils.isEmpty(content)) {
                        SubscribeMessage subscribeMessage =
                                new Gson().fromJson(content, SubscribeMessage.class);
                        if (subscribeMessage != null && subscribeMessage.getWelcomeData() != null) {
                            downLoadWelcomeData(subscribeMessage.getWelcomeData());
                        }
                        if (subscribeMessage != null && subscribeMessage.getWeatherInfo() != null) {
                            SharedPreference.getInstance().putData(
                                    SharedPreference.getInstance().getTodaySub(), content);
                            Logger.d(TAG, "保存当天早报");
                        }
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        Logger.d(TAG, "加到处理队列");
        if (!pushModle.needPlayMusic()) {
            messageQueue.add(pushModle);
            handler.sendEmptyMessage(0);
            if (pushModle.getPushBaseInfo().getPushType() != PushType.SubscribeMessage) {
                PushDb.savePushModel(pushModle);
            }
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String musicPath =
                                downloadURI(pushModle.getPushAction().getActionMusic(),
                                        Util.getWelcomeCachePath(CrashApplication.getAppContext()));
                        if (!TextUtils.isEmpty(musicPath)) {
                            messageQueue.add(pushModle);
                            handler.sendEmptyMessage(0);
                            PushDb.savePushModel(pushModle);
                        }

                    } catch (Exception e) {
                        Logger.d(TAG, e.toString());
                    }
                }
            }).start();
        }
    }

    private void downLoadWelcomeData(final WelcomeData welcomeData) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                synchronized (PushBlockQueue.class) {
                    try {
                        String imgPath =
                                downloadURI(welcomeData.getImgUrl(),
                                        Util.getWelcomeCachePath(CrashApplication.getAppContext()));
                        if (!TextUtils.isEmpty(imgPath)) {
                            Logger.d(TAG, "下载图片成功!");
                            if (!TextUtils.isEmpty(welcomeData.getMusicUrl())) {
                                String musicPath =
                                        downloadURI(welcomeData.getMusicUrl(), Util
                                                .getWelcomeCachePath(CrashApplication
                                                        .getAppContext()));
                                if (!TextUtils.isEmpty(musicPath)) {
                                    Logger.d(TAG, "下载音乐成功!");
                                    Logger.d(TAG, "保存欢迎缓存数据!");
                                    SharedPreference.getInstance().putData(
                                            SharedPreference.WelcomeData,
                                            new Gson().toJson(welcomeData));
                                }
                            } else {
                                Logger.d(TAG, "保存欢迎缓存数据!");
                                SharedPreference.getInstance().putData(
                                        SharedPreference.WelcomeData,
                                        new Gson().toJson(welcomeData));
                            }
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }

            }
        }).start();
    }

    public IWindowService.Stub getGuiCallBack() {
        return guiCallBack;
    }

    public void setGuiCallBack(IWindowService.Stub guiCallBack) {
        this.guiCallBack = guiCallBack;
    }

    private boolean isGuiIdle() {
        if (guiCallBack != null) {
            try {
                return guiCallBack.guiIsIdle();
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
            }
        }
        return true;
    }

    public static String downloadURI(String url, String cache) throws ClientProtocolException,
            IOException {
        String name = Util.stringToMD5(url) + url.substring(url.lastIndexOf("."));
        File file = new File(cache, name);
        
        // 如果图片存在本地缓存目录，则不去服务器下载
        if (file.exists()) {
            file.delete();
        }
        // 新建一个默认的连接
        HttpClient client = new HttpClient();
        
        // 新建一个Get方法
        GetMethod getMethod = new GetMethod(url);
        getMethod.setRequestHeader("Referer", "www.baidu.com");
        getMethod.setRequestHeader("Content-Type",
                "application/x-www-form-urlencoded; charset=UTF-8");
        getMethod
                .setRequestHeader("User-Agent",
                        "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63");
        
        // "Opera/9.80 (Windows NT 5.1; U; zh-cn) Presto/2.6.30 Version/10.70");
        // 用逗号分隔显示可以同时接受多种编码
        // 得到网络的回应
        int response = client.executeMethod(getMethod);

        // 如果服务器响应的是OK的话！
        if (response == 200) {
            InputStream is = getMethod.getResponseBodyAsStream();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            is.close();
            fos.close();
            return file.getPath();
        }
        return null;
    }
}
