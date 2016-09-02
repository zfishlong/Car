/**
 * Copyright (c) 2012-2016 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : LocationAdapter.java
 * @ProjectName : uniCarSolution
 * @PakageName : com.unisound.unicar.gui.adapter
 * @version : 1.0
 * @Author : Xiaodong.He
 * @CreateDate : 2016-01-27
 */
package com.unisound.unicar.gui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.unisound.unicar.gui.R;
import com.unisound.unicar.gui.model.LocationInfo;
import com.unisound.unicar.gui.utils.Logger;

/**
 * 
 * @author xiaodong.he
 * @date 2016-01-27
 */
public class LocationAdapter extends BaseAdapter {

    private static final String TAG = LocationAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<LocationInfo> lists;
    private LayoutInflater inflater;

    public LocationAdapter(Context context, ArrayList<LocationInfo> lists) {
        super();
        this.mContext = context;
        this.lists = lists;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (null == view) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.pickview_item_location, null);
            holder.tvNumber = (TextView) view.findViewById(R.id.textViewNo);
            holder.tvPoiName = (TextView) view.findViewById(R.id.textViewName);
            holder.tvAddress = (TextView) view.findViewById(R.id.textViewAddress);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.tvNumber.setVisibility(View.GONE);

        final LocationInfo info = lists.get(position);
        String name = info.getName();
        String address = info.getAddress();
        holder.tvPoiName.setText(name);
        if (TextUtils.isEmpty(address)) {
            holder.tvAddress.setText(R.string.text_search_location_no_address);
        } else {
            holder.tvAddress.setText(address);
        }
        return view;
    }


    private final static class ViewHolder {
        TextView tvNumber;
        TextView tvPoiName;
        TextView tvAddress;
    }

}
