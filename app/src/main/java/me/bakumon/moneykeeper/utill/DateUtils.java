package me.bakumon.moneykeeper.utill;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.bakumon.moneykeeper.App;
import me.bakumon.moneykeeper.R;

/**
 * 时间工具类
 *
 * @author Bakumon https://bakumon.me
 */
public class DateUtils {
    @SuppressLint("SimpleDateFormat")
    public static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    public static final DateFormat MONTH_DAY_FORMAT = new SimpleDateFormat("MM月dd日");
    @SuppressLint("SimpleDateFormat")
    public static final DateFormat YEAR_MONTH_DAY_FORMAT = new SimpleDateFormat("yyyy年MM月dd日");

    /**
     * 获取今天 Date 对象，精确到日
     *
     * @return Date 对象
     */
    public static Date getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 根据年月日创建 Date 对象
     *
     * @param year        年
     * @param monthOfYear 月
     * @param dayOfMonth  日
     * @return Date 对象
     */
    public static Date getDate(int year, int monthOfYear, int dayOfMonth) {
        String time = year + "-" + monthOfYear + "-" + dayOfMonth;
        return string2Date(time, FORMAT);
    }

    /**
     * 时间字符串转化为 Date 对象
     *
     * @param time   时间字符串
     * @param format DateFormat 格式化类
     * @return Date 对象
     */
    public static Date string2Date(final String time, final DateFormat format) {
        try {
            return format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Date 对象转化为 时间字符串
     *
     * @param date   Date 对象
     * @param format DateFormat 格式化类
     * @return 时间字符串
     */
    public static String date2String(Date date, DateFormat format) {
        return format.format(date);
    }

    /**
     * Date 对象转化为xx月xx日格式字符串
     *
     * @param date Date 对象
     * @return xx月xx日 字符串
     */
    public static String date2MonthDay(Date date) {
        return date2String(date, MONTH_DAY_FORMAT);
    }

    /**
     * 判断两个 Date 是否是同一天
     *
     * @param date1 Date 对象
     * @param date2 Date 对象
     * @return true:同一天
     */
    public static boolean isSameDay(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);

        int year1 = calendar1.get(Calendar.YEAR);
        int year2 = calendar2.get(Calendar.YEAR);

        int month1 = calendar1.get(Calendar.MONTH);
        int month2 = calendar2.get(Calendar.MONTH);

        int day1 = calendar1.get(Calendar.DAY_OF_MONTH);
        int day2 = calendar2.get(Calendar.DAY_OF_MONTH);

        return year1 == year2 && month1 == month2 && day1 == day2;
    }

    /**
     * 获取当前月份开始时刻的 Date
     * 比如当前是 2018年4月
     * 返回的 Date 是 format 后： 2018-04-01T00:00:00.000+0800
     *
     * @return 当前月份开始的 Date
     */
    public static Date getCurrentMonthStart() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取当前月份结束时刻的 Date
     * 比如当前是 2018年4月
     * 返回的 Date 是 format 后： 2018-04-30T23:59:59.999+0800
     *
     * @return 当前月份结束的 Date
     */
    public static Date getCurrentMonthEnd() {
        Calendar calendar = Calendar.getInstance();
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int maxHour = calendar.getActualMaximum(Calendar.HOUR_OF_DAY);
        int maxMinute = calendar.getActualMaximum(Calendar.MINUTE);
        int maxSecond = calendar.getActualMaximum(Calendar.SECOND);
        int maxMillisecond = calendar.getActualMaximum(Calendar.MILLISECOND);

        calendar.set(Calendar.DAY_OF_MONTH, maxDay);
        calendar.set(Calendar.HOUR_OF_DAY, maxHour);
        calendar.set(Calendar.MINUTE, maxMinute);
        calendar.set(Calendar.SECOND, maxSecond);
        calendar.set(Calendar.MILLISECOND, maxMillisecond);

        return calendar.getTime();
    }

    /**
     * 获取当前月份开始时间戳
     * 比如当前是 2018年4月24日
     * 返回的时间是 2018年4月24日 零点整时间戳
     *
     * @return 当前月份开始时间戳
     */
    public static long getTodayStartMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取当前月份结束时间戳
     * 比如当前是 2018年4月24日
     * 返回的时间是 2018年4月24日 23:59:59:999
     *
     * @return 当前月份结束时间戳
     */
    public static long getTodayEndMillis() {
        Calendar calendar = Calendar.getInstance();
        int maxHour = calendar.getActualMaximum(Calendar.HOUR_OF_DAY);
        int maxMinute = calendar.getActualMaximum(Calendar.MINUTE);
        int maxSecond = calendar.getActualMaximum(Calendar.SECOND);
        int maxMillisecond = calendar.getActualMaximum(Calendar.MILLISECOND);

        calendar.set(Calendar.HOUR_OF_DAY, maxHour);
        calendar.set(Calendar.MINUTE, maxMinute);
        calendar.set(Calendar.SECOND, maxSecond);
        calendar.set(Calendar.MILLISECOND, maxMillisecond);

        return calendar.getTimeInMillis();
    }

    /**
     * 获取字面时间
     * 如：
     * 1.今天
     * 2.昨天
     * 3.当年内，4月20日
     * 4.超过当年，2017年3月30日
     *
     * @return 字面时间
     */
    public static String getWordTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        long dateMillis = cal.getTimeInMillis();

        long todayStartMillis = getTodayStartMillis();
        long todayEndMillis = getTodayEndMillis();


        long oneDayMillis = todayEndMillis - todayStartMillis;

        String result;
        if (dateMillis >= todayStartMillis && dateMillis <= todayEndMillis) {
            result = App.getINSTANCE().getResources().getString(R.string.text_today);
        } else if (dateMillis >= (todayStartMillis - oneDayMillis) && dateMillis <= (todayEndMillis - oneDayMillis)) {
            result = App.getINSTANCE().getResources().getString(R.string.text_yesterday);
        } else if (cal.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
            result = date2String(date, MONTH_DAY_FORMAT);
        } else {
            result = date2String(date, YEAR_MONTH_DAY_FORMAT);
        }
        return result;
    }
}
