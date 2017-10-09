package com.example.a731.aclass.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Asus on 2017/9/28.
 */

public class ToastUtil {

    private static String oldMsg ;

    private static Toast toast = null ;
    //第一次时间
    private static long oneTime = 0 ;
    //第二次时间
    private static long twoTime = 0 ;

    /**
     * 显示Toast
     * @param context 必须传入application的context
     * @param message
     */
    public static void showToast(Context context, String message){
        if(toast == null){
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.show() ;
            oneTime = System.currentTimeMillis() ;
        }else{
            twoTime = System.currentTimeMillis() ;
            if(message.equals(oldMsg)){
                if(twoTime - oneTime > Toast.LENGTH_SHORT){
                    toast.show() ;
                }
            }else{
                oldMsg = message ;
                toast.setText(message) ;
                toast.show() ;
            }
        }
        oneTime = twoTime ;
    }
}
