package com.pk4pk.baseappmoudle.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by wenfengcai on 2017/5/9.
 *
 * @Autor CaiWF
 * @TODO
 */

public class DateUtil {

    /**
     * Calendar cal = Calendar.getInstance();

     // 当前年
     int year = cal.get(Calendar.YEAR);
     // 当前月
     int month = (cal.get(Calendar.MONTH)) + 1;
     // 当前月的第几天：即当前日
     int day_of_month = cal.get(Calendar.DAY_OF_MONTH);
     // 当前时：HOUR_OF_DAY-24小时制；HOUR-12小时制
     int hour = cal.get(Calendar.HOUR_OF_DAY);
     // 当前分
     int minute = cal.get(Calendar.MINUTE);
     // 当前秒
     int second = cal.get(Calendar.SECOND);
     // 0-上午；1-下午
     int ampm = cal.get(Calendar.AM_PM);
     // 当前年的第几周
     int week_of_year = cal.get(Calendar.WEEK_OF_YEAR);
     // 当前月的第几周
     int week_of_month = cal.get(Calendar.WEEK_OF_MONTH);
     // 当前年的第几天
     int day_of_year = cal.get(Calendar.DAY_OF_YEAR);
     * @return
     */

    public static int getYear(){
//        Date date=new Date();
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    public static int getMonth(){
        Date date=new Date();
        return date.getMonth();
    }

    public static int getDay(){
//        Date date=new Date();
        Calendar cal = Calendar.getInstance();
        return  cal.get(Calendar.DAY_OF_MONTH);
    }
}
