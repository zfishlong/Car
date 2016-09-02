package com.unisound.unicar.gui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.unisound.unicar.gui.R;

public class CircleImageView extends ImageView {

    private Paint paint = new Paint();

    // private double ratio = 0;// 宽高比

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.GlideImageView);
        // ratio = a.getFloat(R.styleable.GlideImageView_ratio, 0);
    }

    public CircleImageView(Context context, AttributeSet attributeSet, int defstye) {
        super(context, attributeSet, defstye);
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.GlideImageView);
        // ratio = a.getFloat(R.styleable.GlideImageView_ratio, 0);
    }

    // 将图片按比例缩放
    private Bitmap scaleBitmap(Bitmap bitmap) {
        // int width = getWidth();
        // int heigh = getHeight();
        int newWide = 112;
        int newHeight = 112;
        // 一定要强转成float 不然有可能因为精度不够 出现 scale为0 的错误
        float scaleWide = (float) newWide / (float) bitmap.getWidth();
        float scaleHeight = (float) newHeight / (float) bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWide, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,
                true);

    }

    // 将原始图像裁剪成正方形
    private Bitmap dealRawBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // int height = 56;
        // int width = 56;
        // 获取宽度
        int minWidth = width > height ? height : width;
        // 计算正方形的范围
        int leftTopX = (width - minWidth) / 2;
        int leftTopY = (height - minWidth) / 2;
        // 裁剪成正方形
        Bitmap newBitmap =
                Bitmap.createBitmap(bitmap, leftTopX, leftTopY, minWidth, minWidth, null, false);
        return scaleBitmap(newBitmap);
    }

    private Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

        // 指定为 ARGB_4444 可以减小图片大小
        Bitmap output =
                Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        // final Rect rect = new Rect(0, 0, 56, 56);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        int x = bitmap.getWidth();
        canvas.drawCircle(x / 2, x / 2, x / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (null != drawable) {
            Bitmap rawBitmap = ((BitmapDrawable) drawable).getBitmap();

            // 处理Bitmap 转成正方形
            Bitmap newBitmap = dealRawBitmap(rawBitmap);
            // 将newBitmap 转换成圆形
            Bitmap circleBitmap = toRoundCorner(newBitmap, 14);
            final Rect rect = new Rect(0, 0, circleBitmap.getWidth(), circleBitmap.getHeight());
            paint.reset();
            // 绘制到画布上
            canvas.drawBitmap(circleBitmap, rect, rect, paint);
        } else {

            super.onDraw(canvas);
        }
    }

    // @Override
    // protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    //
    // if (ratio > 0) {
    // int height = getMeasuredHeight();
    // setMeasuredDimension((int) (height * ratio), height);
    // }
    // }

}
