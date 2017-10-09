package com.example.a731.aclass.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by 郑选辉 on 2017/10/8.
 */

public class ImageLoderUtil {
    public static String getRealPathFromUri(Context context,Uri uri){
        ContentResolver resolver = context.getContentResolver();
        String[] pojo = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(context, uri, pojo, null,null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(pojo[0]));
        return path;
    }
}
