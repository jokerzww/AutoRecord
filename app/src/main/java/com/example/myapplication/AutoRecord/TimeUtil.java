package com.example.myapplication.AutoRecord;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化时间


    public static String getCurrentTime() {
        Date date = new Date();// 获取当前时间
        return sdf.format(date); // 输出已经格式化的现在时间（24小时制）
    }

    public static long calLastedTime(Date startDate) {
        long a = new Date().getTime();
        long b = startDate.getTime();
        long result=b-a;
        if(result<0){
            return result+86400000;
        }else{
            return result;
        }
    }
}
