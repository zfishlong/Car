package com.unisound.unicar.gui.pushserver;

import java.util.ArrayList;
import java.util.List;

import org.xutils.DbManager;
import org.xutils.DbManager.DaoConfig;
import org.xutils.DbManager.DbUpgradeListener;
import org.xutils.x;
import org.xutils.ex.DbException;

import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.unisound.unicar.gui.application.CrashApplication;
import com.unisound.unicar.gui.pushbean.PushDbBean;
import com.unisound.unicar.gui.pushbean.PushModle;

public class PushDb {
    private static String TAG = PushDb.class.getSimpleName();
    private static DbManager.DaoConfig daoConfig;
    private static DbManager dbManager = x.getDb(getDaoConfig());
    private static int NowDbVersion = 1;
    public static final String PushDbUpdate = "com.unisound.push.action.PushDb.Update";


    private static DaoConfig getDaoConfig() {
        if (daoConfig == null) {
            synchronized (PushDb.class) {
                if (daoConfig == null) {
                    daoConfig =
                            new DbManager.DaoConfig().setDbName("push.db")
                                    .setDbVersion(NowDbVersion).setAllowTransaction(true)
                                    .setDbUpgradeListener(new DbUpgradeListener() {
                                        @Override
                                        public void onUpgrade(DbManager db, int oldVersion,
                                                int newVersion) {

                                        }
                                    });
                }
            }
        }
        return daoConfig;
    }


    public static void savePushModel(PushModle pushModle) {
        try {
            PushDbBean pushDbBean = new PushDbBean(pushModle);
            dbManager.save(pushDbBean);
        } catch (DbException e) {
            Log.e(TAG, e.toString());
        }
        Intent it = new Intent();
        it.setAction(PushDbUpdate);
        CrashApplication.getAppContext().sendBroadcast(it);
    }

    public static void upPushModel(PushModle pushModle) {

        try {
            PushDbBean pushDbBean = new PushDbBean(pushModle);
            dbManager.update(pushDbBean);
        } catch (DbException e) {
            Log.e(TAG, e.toString());
        }
        Intent it = new Intent();
        it.setAction(PushDbUpdate);
        CrashApplication.getAppContext().sendBroadcast(it);
    }


    public static List<PushModle> getAllPushModle() {
        List<PushModle> pushModles = new ArrayList<PushModle>();
        try {

            List<PushDbBean> pushDbBeans = dbManager.findAll(PushDbBean.class);
            if (pushDbBeans != null && pushDbBeans.size() > 0) {
                for (PushDbBean pushDbBean : pushDbBeans) {
                    PushModle pushModle =
                            new Gson().fromJson(pushDbBean.getContent(), PushModle.class);
                    pushModles.add(0, pushModle);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return pushModles;
    }

    public static void deletePushModle(PushModle pushModle) {
        try {
            PushDbBean pushDbBean = new PushDbBean(pushModle);
            dbManager.delete(pushDbBean);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public static boolean hasNoRead() {
        try {
            PushDbBean pushDbBean =
                    dbManager.selector(PushDbBean.class).where("read", "=", false).findFirst();
            if (pushDbBean != null) {
                return true;
            }
        } catch (Exception e) {}
        return false;
    }
}
