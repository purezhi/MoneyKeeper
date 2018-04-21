package me.bakumon.moneykeeper.utill;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author Bakumon https://bakumon.me
 */
public class DateUtils {
    @SuppressLint("SimpleDateFormat")
    public static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    @SuppressLint("SimpleDateFormat")
    public static final DateFormat MONTH_DAY_FORMAT = new SimpleDateFormat("MM月dd日");

    /**
     * @param year        年
     * @param monthOfYear 月
     * @param dayOfMonth  日
     * @return 精确到时分秒的 Date 对象
     */
    public static Date getAccurateDate(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int milliSecond = calendar.get(Calendar.MILLISECOND);
        String time = year + "-" + monthOfYear + "-" + dayOfMonth + " "
                + hour + ":" + minute + ":" + second + ":" + milliSecond;
        return string2Date(time, FORMAT);
    }

    public static Date string2Date(final String time, final DateFormat format) {
        try {
            return format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String date2String(Date date, DateFormat format) {
        return format.format(date);
    }

    public static String date2MonthDay(Date date) {
        return date2String(date, MONTH_DAY_FORMAT);
    }

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
}
