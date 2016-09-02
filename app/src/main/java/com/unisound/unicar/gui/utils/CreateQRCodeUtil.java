/**
 * Copyright (c) 2012-2015 Beijing Unisound Information Technology Co., Ltd. All right reserved.
 * 
 * @FileName : CreateQRCodeUtil.java
 * @ProjectName : UniCarGUI_GUI
 * @PakageName : com.unisound.unicar.gui.utils
 * @version : 1.0
 * @Author : Xiaodong.He
 * @CreateDate : 2015-12-14
 */
package com.unisound.unicar.gui.utils;

import java.util.Hashtable;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * 
 * @author xiaodong.he
 * @date 2015-12-14
 */
public class CreateQRCodeUtil {

    private static int QR_WIDTH = 200, QR_HEIGHT = 200;

    /**
     * logo width
     */
    private static final int IMAGE_HALFWIDTH = 56;

    /**
     * 
     * @param url
     * @param width
     * @return Bitmap
     */
    public static Bitmap createQRCode(String url, int width) {
        Bitmap bitmap = null;
        if (url == null || "".equals(url) || url.length() < 1 || width < 1) {
            return bitmap;
        }
        QR_WIDTH = QR_HEIGHT = width;
        try {
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix =
                    new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT,
                            hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * createQRCode
     * 
     * @param content
     * @param logo
     * @return Bitmap
     */
    public static Bitmap createQRCode(String content, Bitmap logo, int codeWidth) {
        Bitmap bitmap = null;
        if (content == null || "".equals(content) || content.length() < 1 || codeWidth < 1) {
            return bitmap;
        }
        QR_WIDTH = QR_HEIGHT = codeWidth;
        if (null == logo) {
            return createQRCode(content, codeWidth);
        }

        Matrix m = new Matrix();
        float sx = (float) 2 * IMAGE_HALFWIDTH / logo.getWidth();
        float sy = (float) 2 * IMAGE_HALFWIDTH / logo.getHeight();
        m.setScale(sx, sy);
        logo = Bitmap.createBitmap(logo, 0, 0, logo.getWidth(), logo.getHeight(), m, false);
        MultiFormatWriter writer = new MultiFormatWriter();
        Hashtable<EncodeHintType, String> hst = new Hashtable<EncodeHintType, String>();
        hst.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix matrix;
        try {
            matrix = writer.encode(content, BarcodeFormat.QR_CODE, codeWidth, codeWidth, hst);
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int halfW = width / 2;
            int halfH = height / 2;
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
                            && y > halfH - IMAGE_HALFWIDTH && y < halfH + IMAGE_HALFWIDTH) {
                        pixels[y * width + x] =
                                logo.getPixel(x - halfW + IMAGE_HALFWIDTH, y - halfH
                                        + IMAGE_HALFWIDTH);
                    } else {
                        if (matrix.get(x, y)) {
                            pixels[y * width + x] = 0xff000000;
                        } else {
                            pixels[y * width + x] = 0xffffffff;
                        }
                    }

                }
            }
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
