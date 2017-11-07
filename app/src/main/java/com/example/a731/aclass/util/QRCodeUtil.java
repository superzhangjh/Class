package com.example.a731.aclass.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/3/003.
 */

public class QRCodeUtil {

    public static final int USERCODE = 0;
    public static final int GROUPCODE = 1;
    public static final int SIGNCODE = 2;

    // 生成QR图
    public static Bitmap createImage(int type,String id,Bitmap logo) {
        try {
            String content = null;
            if (type==0){//个人二维码
                content = "user:" + id;
            }else if (type==1){
                //班级二维码
                content = "group:" + id;
            }else if (type == SIGNCODE){
                content = "signature:" + id;
            }

            //指定二维码大小
            int w = 300;
            int h = 300;
            Bitmap scaleLogo = getScaleLogo(logo,w,h);
            int offsetX = (w - scaleLogo.getWidth())/2;
            int offsetY = (h - scaleLogo.getHeight())/2;
            //配置参数
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            //设置空白边距的宽度
            hints.put(EncodeHintType.MARGIN, 0); //default is 4
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(
                    content, BarcodeFormat.QR_CODE, w, h, hints);
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    if(x >= offsetX &&
                            x < offsetX+scaleLogo.getWidth() &&
                            y>= offsetY &&
                            y < offsetY+scaleLogo.getHeight()){
                        int pixel = scaleLogo.getPixel(x-offsetX,y-offsetY);
                        if(pixel == 0){
                            if(bitMatrix.get(x, y)){
                                pixel = 0xff000000;
                            }else{
                                pixel = 0xffffffff;
                            }
                        }
                        pixels[y * w + x] = pixel;
                    }else{
                        if (bitMatrix.get(x, y)) {
                            pixels[y * w + x] = 0xff000000;
                        } else {
                            pixels[y * w + x] = 0xffffffff;
                        }
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(w, h,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Bitmap getScaleLogo(Bitmap logo,int w,int h){
        if(logo == null)return null;
        Matrix matrix = new Matrix();
        float scaleFactor = Math.min(w * 1.0f / 5 / logo.getWidth(), h * 1.0f / 5 /logo.getHeight());
        matrix.postScale(scaleFactor,scaleFactor);
        Bitmap result = Bitmap.createBitmap(logo, 0, 0, logo.getWidth(),   logo.getHeight(), matrix, true);
        return result;
    }
}