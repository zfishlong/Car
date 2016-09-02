package com.unisound.unicar.gui.view;

import java.io.File;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.unisound.unicar.gui.R;

public class GlideImageView extends ImageView {

    private Context mContext;
    private double ratio = 0;// 宽高比

    public GlideImageView(Context context) {
        super(context);
        mContext = context;
    }

    public GlideImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GlideImageView);
        ratio = a.getFloat(R.styleable.GlideImageView_ratio, 0);
    }

    public GlideImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GlideImageView);
        ratio = a.getFloat(R.styleable.GlideImageView_ratio, 0);
    }

    @Override
    public void setImageURI(Uri uri) {
        Glide.with(mContext).load(uri).into(this);
    }

    public void setImageURI(String uri) {
        Glide.with(mContext).load(uri).into(this);
    }

    public void setImageFile(File file) {
        Glide.with(mContext).load(file).into(this);
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (ratio > 0) {
            int height = getMeasuredHeight();
            setMeasuredDimension((int) (height * ratio), height);
        }
    }
}
