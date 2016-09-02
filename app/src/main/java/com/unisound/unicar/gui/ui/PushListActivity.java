/**
 * Copyright (c) 2012-2015 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : SettingsViewPagerActivity.java
 * @ProjectName : uniCarGui
 * @PakageName : com.unisound.unicar.gui.ui
 * @version : 1.4
 * @Author : Xiaodong.He
 * @CreateDate : 2015-06-10
 * @modifyDate : 2016-01-20
 */
package com.unisound.unicar.gui.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.unisound.unicar.gui.R;
import com.unisound.unicar.gui.adapter.PushAdapt;
import com.unisound.unicar.gui.preference.SharedPreference;
import com.unisound.unicar.gui.pushserver.HandlerStore;
import com.unisound.unicar.gui.pushserver.PushDb;
import com.unisound.unicar.gui.subscribebean.LimitDrive;
import com.unisound.unicar.gui.subscribebean.SubscribeMessage;
import com.unisound.unicar.gui.subscribebean.WeatherInfo;
import com.unisound.unicar.gui.utils.Logger;


public class PushListActivity extends BaseActivity implements OnCheckedChangeListener {
    private static final String TAG = PushListActivity.class.getSimpleName();

    private CheckBox checkPush;
    private CheckBox checkSub;
    private LinearLayout layoutPush;
    private LinearLayout layoutSub;
    private TextView txtNoSub;
    private LinearLayout layoutHasSub;
    private ListView listPush;
    private SubscribeMessage subscribeMessage;
    private WeatherInfo weatherInfo;
    private LimitDrive limitDrive;
    private PushAdapt pushAdapt;

    private TextView txtCity;
    private TextView txtWeather;
    private TextView txtTemp;
    private TextView txtCarWash;
    private TextView txtLimitDrive;
    private ImageView imgWeather;
    private LinearLayout layoutLimitDrive;
    private Integer[] weatherIcons = {R.drawable.ic_weather_overcast, R.drawable.ic_weather_sunny,
            R.drawable.ic_weather_cloudy, R.drawable.ic_weather_overcast,
            R.drawable.ic_weather_foggy, R.drawable.ic_weather_dustblow,
            R.drawable.ic_weather_dust, R.drawable.ic_weather_sandstorm,
            R.drawable.ic_weather_sandstorm_strong, R.drawable.ic_weather_icerain,
            R.drawable.ic_weather_shower, R.drawable.ic_weather_thunder_rain,
            R.drawable.ic_weather_hail, R.drawable.ic_weather_sleety,
            R.drawable.ic_weather_rain_light, R.drawable.ic_weather_rain_moderate,
            R.drawable.ic_weather_rain_heavy, R.drawable.ic_weather_rainstorm,
            R.drawable.ic_weather_rainstorm_big, R.drawable.ic_weather_rainstorm_super,
            R.drawable.ic_weather_snow_shower, R.drawable.ic_weather_snow_light,
            R.drawable.ic_weather_snow_moderate, R.drawable.ic_weather_snow_heavy,
            R.drawable.ic_weather_blizzard, R.drawable.ic_weather_haze};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logger.d(TAG, "!--->onCreate()----");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_push_list);

        String subStr =
                SharedPreference.getInstance()
                        .getData(SharedPreference.getInstance().getTodaySub());
        if (TextUtils.isEmpty(subStr) == false) {
            subscribeMessage = new Gson().fromJson(subStr, SubscribeMessage.class);
            weatherInfo = subscribeMessage.getWeatherInfo();
            limitDrive = subscribeMessage.getLimitDriveInfo();
        }
        checkPush = (CheckBox) findViewById(R.id.checkPush);
        checkSub = (CheckBox) findViewById(R.id.checkSub);
        layoutPush = (LinearLayout) findViewById(R.id.layoutPush);
        layoutSub = (LinearLayout) findViewById(R.id.layoutSub);
        txtNoSub = (TextView) findViewById(R.id.txtNoSub);
        layoutHasSub = (LinearLayout) findViewById(R.id.layoutHasSub);
        listPush = (ListView) findViewById(R.id.listPush);
        layoutLimitDrive = (LinearLayout) findViewById(R.id.layoutLimitDrive);

        txtCity = (TextView) findViewById(R.id.txtCity);
        txtWeather = (TextView) findViewById(R.id.txtWeather);
        txtTemp = (TextView) findViewById(R.id.txtTemp);
        txtCarWash = (TextView) findViewById(R.id.txtCarWash);
        txtLimitDrive = (TextView) findViewById(R.id.txtLimitDrive);
        imgWeather = (ImageView) findViewById(R.id.imgWeather);

        checkPush.setOnCheckedChangeListener(this);
        checkSub.setOnCheckedChangeListener(this);


        ImageButton returnBtn = (ImageButton) findViewById(R.id.backBtn);
        returnBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initData();
    }

    private void initData() {
        if (subscribeMessage != null) {
            layoutHasSub.setVisibility(View.VISIBLE);
            txtNoSub.setVisibility(View.GONE);
        } else {
            layoutHasSub.setVisibility(View.GONE);
            txtNoSub.setVisibility(View.VISIBLE);
        }
        pushAdapt =
                new PushAdapt(this, PushDb.getAllPushModle(), HandlerStore.getInstance().get(
                        "SessionManagerHandler"));
        listPush.setAdapter(pushAdapt);

        layoutPush.setVisibility(View.GONE);
        layoutSub.setVisibility(View.VISIBLE);

        if (weatherInfo != null) {
            txtCarWash.setText("洗车指数:" + weatherInfo.getCarWash());
            txtWeather.setText(weatherInfo.getWeather());
            txtTemp.setText(weatherInfo.getLowTemp() + "至" + weatherInfo.getHighTemp() + "℃");
            String city = weatherInfo.getCity();
            if (TextUtils.isEmpty(weatherInfo.getArea()) == false) {
                city = weatherInfo.getArea();
            }
            txtCity.setText(city + " 天气");
            int type = weatherInfo.getWeatherType();
            if (type >= 0 && type < weatherIcons.length) {
                imgWeather.setImageResource(weatherIcons[type]);
            } else {
                imgWeather.setImageResource(R.drawable.ic_weather_overcast);
            }

        }
        if (limitDrive == null) {
            layoutLimitDrive.setVisibility(View.GONE);
        } else {
            layoutLimitDrive.setVisibility(View.VISIBLE);
            txtLimitDrive.setText(limitDrive.getInfo());
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton view, boolean isChecked) {

        switch (view.getId()) {
            case R.id.checkSub:
                if (isChecked) {
                    checkPush.setChecked(false);
                    checkSub.setEnabled(false);
                    checkPush.setEnabled(true);
                    layoutPush.setVisibility(View.GONE);
                    layoutSub.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.checkPush:
                if (isChecked) {
                    checkSub.setChecked(false);
                    checkPush.setEnabled(false);
                    checkSub.setEnabled(true);
                    layoutPush.setVisibility(View.VISIBLE);
                    layoutSub.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (pushAdapt != null) {
            pushAdapt.onDestroy();
        }
    }
}
