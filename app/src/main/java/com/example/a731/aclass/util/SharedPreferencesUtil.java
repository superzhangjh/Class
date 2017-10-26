package com.example.a731.aclass.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 郑选辉 on 2017/10/24.
 */

public class SharedPreferencesUtil {
    public static String lodaDataFromSharedPreferences(String key,Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("data",context.MODE_PRIVATE);
        String str = sharedPreferences.getString(key,null);
        return str;
    }

    public static void saveDataToSharedPreferences(String key,String value,Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        editor.putString(key,value);
        editor.apply();
    }
}
