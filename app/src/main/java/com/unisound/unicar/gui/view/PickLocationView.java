/**
 * Copyright (c) 2012-2014 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * 
 * @FileName : PickLocationView.java
 * @ProjectName : uniCarSolotion
 * @PakageName : com.unisound.unicar.gui.view
 * @Author : Alieen
 * @CreateDate : 2015-07-08
 */
package com.unisound.unicar.gui.view;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unisound.unicar.gui.R;
import com.unisound.unicar.gui.model.LocationInfo;
import com.unisound.unicar.gui.utils.Logger;

/**
 * @Module : 隶属模块名
 * @Comments : 描述
 * @Author : Alieen
 * @CreateDate : 2015-07-08
 * @ModifiedBy : Alieen
 * @ModifiedDate: 2015-07-08
 * @Modified: 2015-07-08: 实现基本功能
 */
public class PickLocationView extends PickBaseView {

    public static final String TAG = "PickLocationView";
    private Context mContext = null;

    private LinearLayout mLoadMoreLay;
    private boolean btn_more_use = false;
    private int numberContent = 0;
    private int pageMaxNumber = 5;
    private ArrayList<LocationInfo> mLocationInfos;

    private ImageView iv_next;
    private ImageView iv_pre;
    private TextView tv_pages;
    private PickLocationViewListener mPickLocationViewListener;

    public void setPickLocationViewListener(PickLocationViewListener pickLocationViewListener) {
        mPickLocationViewListener = pickLocationViewListener;
    }

    public PickLocationView(Context context) {
        super(context);
        mContext = context;
    }

    /**
     * 
     * @param locationInfos
     * @param locationToPoi: locationKeyword
     */
    public void initView(ArrayList<LocationInfo> locationInfos, String locationToPoi,
            int totalPage, int current_page) {
        Logger.d(TAG, "initView---locationToPoi = " + locationToPoi);
        mLocationInfos = locationInfos;
        View header = mLayoutInflater.inflate(R.layout.pickview_header_location, this, false);
        TextView tvHead = (TextView) header.findViewById(R.id.tv_header_location_keyword);
        tvHead.setText(locationToPoi);

        ImageView ivEditLocation = (ImageView) header.findViewById(R.id.iv_edit_location);
        ivEditLocation.setOnClickListener(mOnClickListener);
        setHeader(header);

        Logger.d(TAG, "initView---locationInfos size = " + locationInfos.size()
                + "; pageMaxNumber = " + pageMaxNumber);
        // XD modify for FIX BUG-4468
        if (locationInfos.size() > pageMaxNumber) {
            for (int i = 0; i < pageMaxNumber; i++) {
                btn_more_use = true;
                numberContent += 1;
                addItem(getLocalItemView(locationInfos.get(i)));
            }
            addedMoreButton();
        } else if (locationInfos.size() <= pageMaxNumber) {
            for (int i = 0; i < locationInfos.size(); i++) {
                btn_more_use = false;
                numberContent += 1;
                addItem(getLocalItemView(locationInfos.get(i)));
            }
        }
        if (totalPage > 1) {
            addedNextAndPreBtn(totalPage, current_page);
        }

        updateLayoutParams();
    }

    /**
     * added MoreButton
     * 
     * @author xiaodong.he
     * @date 20151102
     */
    private void addedMoreButton() {
        View buttomLay = mLayoutInflater.inflate(R.layout.list_item_load_more, null, false);
        mLoadMoreLay = (LinearLayout) buttomLay.findViewById(R.id.btn_more_local);
        /* < BUG-2792 XD 20150722 modify Begin */
        addBottomButton(buttomLay);
        // addItem(buttomLay);
        /* BUG-2792 20150722 XD modify End > */
        mLoadMoreLay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Logger.d(TAG, "onClick--Load more location--");
                // addMoreView();
                if (mPickLocationViewListener != null) {
                    mPickLocationViewListener.onLoadmoreClick();
                }
            }
        });
    }

    private void addedNextAndPreBtn(int totalPage, int currentPage) {
        View nextAndPre = mLayoutInflater.inflate(R.layout.list_item_load_next_pre, null, false);
        iv_next = (ImageView) nextAndPre.findViewById(R.id.btn_next_local);
        iv_pre = (ImageView) nextAndPre.findViewById(R.id.btn_pre_local);
        tv_pages = (TextView) nextAndPre.findViewById(R.id.tv_pages);
        String test = currentPage + "/" + totalPage;
        tv_pages.setText(test);
        iv_next.setOnClickListener(myclick);
        iv_pre.setOnClickListener(myclick);
        addBottomButton(nextAndPre);

    }

    public void addMoreView() {
        mLoadMoreLay.setVisibility(View.GONE);

        if (btn_more_use) {
            for (int i = pageMaxNumber; i < (mLocationInfos.size() >= 20 ? 20 : mLocationInfos
                    .size()); i++) {
                numberContent += 1;
                addItem(getLocalItemView(mLocationInfos.get(i)));
            }
            btn_more_use = false;
        }
    }

    public View getLocalItemView(LocationInfo contactInfo) {
        View view = mLayoutInflater.inflate(R.layout.pickview_item_location, mContainer, false);
        TextView tvName = (TextView) view.findViewById(R.id.textViewName);
        TextView tvAddress = (TextView) view.findViewById(R.id.textViewAddress);
        String name = contactInfo.getName();
        if (TextUtils.isEmpty(name)) {
            tvName.setVisibility(View.GONE);
            tvAddress.setTextColor(getContext().getResources().getColor(R.color.black));
        } else {
            tvName.setText(name);
        }
        if (TextUtils.isEmpty(contactInfo.getAddress())) {
            // tvAddress.setVisibility(View.INVISIBLE);
            tvAddress.setText(R.string.text_search_location_no_address);
        } else {
            tvAddress.setText(contactInfo.getAddress());
        }
        TextView noText = (TextView) view.findViewById(R.id.textViewNo);
        noText.setText(String.valueOf(numberContent + ""));
        return view;
    }

    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_edit_location:
                    Logger.d(TAG, "!--->iv_edit_location click");
                    if (null != mPickLocationViewListener) {
                        mPickLocationViewListener.onEditLocationClick();
                    }
                    break;

                default:
                    break;
            }
        }
    };


    private OnClickListener myclick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_next_local:
                    if (null != mPickListener) {
                        mPickListener.onNext();
                    }
                    break;
                case R.id.btn_pre_local:
                    if (null != mPickListener) {
                        mPickListener.onPre();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 
     * @author xiaodong
     * @date 20151016
     */
    public interface PickLocationViewListener {

        public void onEditLocationClick();

        public void onLoadmoreClick(); // XD added 20151218

    }

}
