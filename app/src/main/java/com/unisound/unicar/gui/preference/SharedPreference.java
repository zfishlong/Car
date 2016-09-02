package com.unisound.unicar.gui.preference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.SharedPreferences;

import com.unisound.unicar.gui.application.CrashApplication;

public class SharedPreference {
    private String TAG = SharedPreference.class.getSimpleName();
    private final String SharedPreferenceName = "Setting";
    private static SharedPreference sharedPreference;

    public static final String WelcomeData = "WelcomeData";

    public static final String SubMessage = "SubMessage";

    private String getTodayStr() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return df.format(new Date());
    }

    public String getTodaySub() {
        return SubMessage + getTodayStr();
    }

    private SharedPreference() {

    }

    public static SharedPreference getInstance() {
        if (sharedPreference == null) {
            synchronized (SharedPreference.class) {
                if (sharedPreference == null) {
                    sharedPreference = new SharedPreference();
                }
            }
        }
        return sharedPreference;
    }

    public void putData(String key, String value) {
        SharedPreferences settings =
                CrashApplication.getAppContext().getSharedPreferences(SharedPreferenceName, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void putData(String key, Boolean value) {
        SharedPreferences settings =
                CrashApplication.getAppContext().getSharedPreferences(SharedPreferenceName, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public String getData(String key) {
        SharedPreferences settings =
                CrashApplication.getAppContext().getSharedPreferences(SharedPreferenceName, 0);
        String value = settings.getString(key, null);
        return value;
    }

    public boolean getData(String key, boolean defaultValue) {
        SharedPreferences settings =
                CrashApplication.getAppContext().getSharedPreferences(SharedPreferenceName, 0);
        boolean value = settings.getBoolean(key, defaultValue);
        return value;
    }
}
