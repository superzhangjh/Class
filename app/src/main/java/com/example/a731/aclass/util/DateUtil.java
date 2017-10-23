package com.example.a731.aclass.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/10/17/017.
 */

public class DateUtil {
    //获取当前时间格式的工具类

    public static String yyyyMMdd_hhmmss(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = new Date();
        String now = sdf.format(date);
        return now;
    }

    public static String MMdd_hhss(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd hh:mm");
        Date date = new Date();
        String now = sdf.format(date);
        return now;
    }

    public static String yyyy(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date date = new Date();
        String now = sdf.format(date);
        return now;
    }

    public static String MMdd(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        Date date = new Date();
        String now = sdf.format(date);
        return now;
    }

    public static String hhmm(){
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        Date date = new Date();
        String now = sdf.format(date);
        return now;
    }
}
