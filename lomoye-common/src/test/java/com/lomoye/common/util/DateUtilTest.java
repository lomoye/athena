package com.lomoye.common.util;

import junit.framework.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by tommy on 2016/8/28.
 */
public class DateUtilTest {

    @Test
    public void testIsSameDay() throws Exception {
        Date date1 = DateUtil.parseDateTime("2015-09-16 23:59:59");
        Date date2 = DateUtil.parseDateTime("2015-09-16 23:59:59");

        Assert.assertTrue(DateUtil.isSameDay(date1, date2));
        Assert.assertTrue(DateUtil.isSameDay(date1, date1));

        date2 = DateUtil.parseDateTime("2015-09-17 00:00:00");
        Assert.assertTrue(!DateUtil.isSameDay(date1, date2));
    }

    @Test
    public void testFormat() throws Exception {
        Date date = DateUtil.parseDateTime("2015-09-16 23:59:59");

        Assert.assertTrue("2015-09-16 23:59:59".equals(DateUtil.format(date, "yyyy-MM-dd HH:mm:ss")));

        Assert.assertTrue("2015/09/16 23:59:59".equals(DateUtil.format(date, "yyyy/MM/dd HH:mm:ss")));
    }

    @Test
    public void testAddDateDays() throws Exception {
        Date date1 = DateUtil.parseDateTime("2015-09-16 23:59:59");
        Date date2 = DateUtil.addDateDays(date1, 5);
        Assert.assertTrue(date2.getTime() - date1.getTime() == 5 * 24 * 60 * 60 * 1000L);
    }

    @Test
    public void testAddDateMinute() throws Exception {
        Date date1 = DateUtil.parseDateTime("2015-09-16 23:59:59");
        Date date2 = DateUtil.addDateMinute(date1, 5);
        Assert.assertTrue(date2.getTime() - date1.getTime() == 5 * 60 * 1000L);
    }

    @Test
    public void testGetDailyStartTime() throws Exception {
        Date date1 = DateUtil.parseDateTime("2015-09-16 23:59:59");
        Date date2 = DateUtil.getDailyStartTime(date1);
        Assert.assertTrue("2015-09-16 00:00:00".equals(DateUtil.format(date2, "yyyy-MM-dd HH:mm:ss")));
    }

    @Test
    public void testGetDailyStartTime1() throws Exception {
        Date date1 = DateUtil.parseDateTime("2015-09-16 23:59:59");
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        DateUtil.getDailyStartTime(cal1);
        Assert.assertTrue("2015-09-16 00:00:00".equals(DateUtil.format(cal1.getTime(), "yyyy-MM-dd HH:mm:ss")));
    }

    @Test
    public void testGetDailyEndTime() throws Exception {
        Date date1 = DateUtil.parseDateTime("2015-09-16 23:58:58");
        Date date2 = DateUtil.getDailyEndTime(date1);
        Assert.assertTrue("2015-09-16 23:59:59".equals(DateUtil.format(date2, "yyyy-MM-dd HH:mm:ss")));
    }

    @Test
    public void testGetYesterdayStartTime() throws Exception {
        Date date1 = DateUtil.parseDateTime("2015-09-16 23:59:59");
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        DateUtil.getYesterdayStartTime(cal1);
        Assert.assertTrue("2015-09-15 00:00:00".equals(DateUtil.format(cal1.getTime(), "yyyy-MM-dd HH:mm:ss")));
    }

    @Test
    public void testGetYesterdayEndTime() throws Exception {
        Date date1 = DateUtil.parseDateTime("2015-09-16 22:58:58");
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        DateUtil.getYesterdayEndTime(cal1);
        Assert.assertTrue("2015-09-15 23:59:59".equals(DateUtil.format(cal1.getTime(), "yyyy-MM-dd HH:mm:ss")));
    }

    @Test
    public void testClearSeconds() throws Exception {
        Date date1 = DateUtil.parseDateTime("2015-09-16 22:58:58.234");

        date1 = DateUtil.clearSeconds(date1);
        Assert.assertTrue("2015-09-16 22:58:00".equals(DateUtil.format(date1, "yyyy-MM-dd HH:mm:ss")));
    }

    @Test
    public void testParseDateTime() throws Exception {
        Date date = DateUtil.parseDateTime("2015-09-16 23:59:59");

        Assert.assertTrue("2015-09-16 23:59:59".equals(DateUtil.format(date, "yyyy-MM-dd HH:mm:ss")));
    }

    @Test
    public void testParseDateTime1() throws Exception {
        Date date = DateUtil.parseDateTime("yyyy/MM/dd HH:mm:ss", "2015/09/16 23:59:59");

        Assert.assertTrue("2015-09-16 23:59:59".equals(DateUtil.format(date, "yyyy-MM-dd HH:mm:ss")));
    }

    @Test
    public void testGetDayMinute() throws Exception {
        Date date = DateUtil.parseDateTime("yyyy/MM/dd HH:mm:ss", "2015/09/16 23:59:59");

        Assert.assertTrue(DateUtil.getDayMinute(date) == 1440);

        date = DateUtil.parseDateTime("yyyy/MM/dd HH:mm:ss", "2015/09/16 23:59:00");

        Assert.assertTrue(DateUtil.getDayMinute(date) == 1439);

        date = DateUtil.parseDateTime("yyyy/MM/dd HH:mm:ss", "2015/09/16 11:59:59");

        Assert.assertTrue(DateUtil.getDayMinute(date) == 720);

        date = DateUtil.parseDateTime("yyyy/MM/dd HH:mm:ss", "2015/09/16 11:59:00");

        Assert.assertTrue(DateUtil.getDayMinute(date) == 719);
    }

    @Test
    public void testGetDayMinute1() throws Exception {
        Date date = DateUtil.parseDateTime("yyyy/MM/dd HH:mm:ss", "2015/09/16 23:59:59");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        Assert.assertTrue(DateUtil.getDayMinute(calendar) == 1440);
    }

    @Test
    public void testGetDistanceTime() throws Exception {
        Date date1 = DateUtil.parseDateTime("yyyy/MM/dd HH:mm:ss", "2015/09/16 23:59:59");

        Date date2 = DateUtil.parseDateTime("yyyy/MM/dd HH:mm:ss", "2015/09/17 00:00:11");

        Assert.assertTrue(DateUtil.getDistanceTime(date1, date2).equals("0天0小时0分12秒"));
    }

    @Test
    public void testGetDiffDay() throws Exception {
        Date date1 = DateUtil.parseDateTime("yyyy/MM/dd HH:mm:ss", "2015/09/16 23:59:59");

        Date date2 = DateUtil.parseDateTime("yyyy/MM/dd HH:mm:ss", "2015/09/17 00:00:11");

        Assert.assertTrue(DateUtil.getDiffDay(date1, date2) == 0);
    }


    @Test
    public void testGetDayOfWeek() throws Exception {
        Date date1 = DateUtil.parseDateTime("yyyy/MM/dd HH:mm:ss", "2016/08/28 12:59:59");
        Assert.assertTrue(DateUtil.getDayOfWeek(date1) == 7);

        date1 = DateUtil.parseDateTime("yyyy/MM/dd HH:mm:ss", "2016/08/29 12:59:59");
        Assert.assertTrue(DateUtil.getDayOfWeek(date1) == 1);
    }

    @Test
    public void testGetHourOfDay() throws Exception {
        Date date1 = DateUtil.parseDateTime("yyyy/MM/dd HH:mm:ss", "2016/08/28 13:59:59");
        Assert.assertTrue(DateUtil.getHourOfDay(date1) == 13);
    }
}