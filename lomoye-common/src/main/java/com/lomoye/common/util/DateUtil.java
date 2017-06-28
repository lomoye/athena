package com.lomoye.common.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间类的工具.
 */
public class DateUtil {
    /**
     * TOP默认时间格式
     **/
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * TOP Date默认时区
     **/
    public static final String DATE_TIMEZONE = "GMT+8";

    public static boolean isSameDay(Date day1, Date day2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String ds1 = sdf.format(day1);
        String ds2 = sdf.format(day2);

        return ds1.equals(ds2);
    }

    public static String format(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Date addDateDays(Date date, long days) {
        return new Date(date.getTime() + days * 24 * 60 * 60 * 1000);
    }


    public static Date addDateMinute(Date date, long minute) {
        return new Date(date.getTime() + minute * 60 * 1000);
    }

    public static void getDailyStartTime(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    public static Date getDailyStartTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        return cal.getTime();
    }


    public static Date getDailyEndTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MILLISECOND, 999);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        return cal.getTime();
    }


    public static void getYesterdayStartTime(Calendar cal) {
        cal.add(Calendar.DAY_OF_MONTH, -1);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
    }

    public static void getYesterdayEndTime(Calendar cal) {
        cal.add(Calendar.DAY_OF_MONTH, -1);
        cal.set(Calendar.MILLISECOND, 999);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.HOUR_OF_DAY, 23);
    }

    public static Date clearSeconds(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date parseDateTime(String str) {
        if (Strings.isNullOrEmpty(str)) {
            throw new RuntimeException("parseDateTime should not null");
        }
        DateFormat format = new SimpleDateFormat(DATE_TIME_FORMAT);
        format.setTimeZone(TimeZone.getTimeZone(DATE_TIMEZONE));
        try {
            return format.parse(str);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date parseDateTime(String dateFormat, String str) {
        if (Strings.isNullOrEmpty(str)) {
            throw new RuntimeException("parseDateTime should not null");
        }
        DateFormat format = new SimpleDateFormat(dateFormat);
        format.setTimeZone(TimeZone.getTimeZone(DATE_TIMEZONE));
        try {
            return format.parse(str);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getDayMinute(Calendar calendar) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        return hour * 60 + minute + (second > 0 ? 1 : 0);
    }

    public static int getDayMinute(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getDayMinute(calendar);
    }

    /**
     * 两个时间相差距离多少天多少小时多少分多少秒,取得是绝对值
     *
     * @return String 返回值为：xx天xx小时xx分xx秒
     */
    public static String getDistanceTime(Date one, Date two) {
        long time1 = one.getTime();
        long time2 = two.getTime();
        long diff = Math.abs(time2 - time1);

        long day = diff / (24 * 60 * 60 * 1000);
        long hour = (diff / (60 * 60 * 1000) - day * 24);
        long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        return day + "天" + hour + "小时" + min + "分" + sec + "秒";
    }


    /**
     * 得到两个日期相差的天数 (to - from)
     */
    public static int getDiffDay(Date from, Date to) {
        return (int) ((to.getTime() - from.getTime()) / (24 * 60 * 60 * 1000L));
    }

    //获取星期几的序号,周一=1,。。。。周天=7
    public static int getDayOfWeek(Date date) {
        Preconditions.checkNotNull(date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        dayOfWeek--; //JDK中,周一=2,周六=7,周天=1
        if (dayOfWeek == 0) {
            dayOfWeek = 7; //星期天=7
        }

        return dayOfWeek;
    }

    public static int getHourOfDay(Date date) {
        Preconditions.checkNotNull(date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinuteOfHour(Date date) {
        Preconditions.checkNotNull(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.MINUTE);
    }
}
