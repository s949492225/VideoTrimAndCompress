package com.syiyi.digger.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间处理
 * Created by songlintao on 2017/7/14.
 */

public class TimeUtil {

    public static String formatTime(Long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;

        if (day > 0) {
            return day + "天";
        }
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;

        String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
        String strSecond = second < 10 ? "0" + second : "" + second;//秒

        return strHour + ":" + strMinute + ":" + strSecond;
    }

    public static String formatSumTime(Long ms) {
        if (ms < 1000) {
            return 0 + "秒";
        }
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;

        StringBuilder sb = new StringBuilder();
        if (day > 0) {
            sb.append(day).append("天");
        }
        if (hour > 0) {
            sb.append(hour).append("小时");
        }
        if (minute > 0) {
            sb.append(minute).append("分");
        }
        if (second > 0) {
            sb.append(second).append("秒");
        }
        return sb.toString();
    }

    public static long time2ms(String time) {
        if (time.contains("frame") && time.contains("time")) {
            int index = time.indexOf("time");
            String timeStr = time.substring(index + 5, index + 16);
            SimpleDateFormat baseDf = new SimpleDateFormat("yyyy", Locale.getDefault());
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());
            try {
                long baseTime = baseDf.parse("1970").getTime();

                Date date = dateFormat.parse(timeStr);
                return Math.abs(baseTime-date.getTime());
            } catch (ParseException e) {
                //ignore
            }
        }
        return 0;
    }
}
