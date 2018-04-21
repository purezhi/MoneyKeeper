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

    public static String date2String(Date date, DateFormat format){
        return format.format(date);
    }

    public static String date2MonthDay(Date date){
        return date2String(date, MONTH_DAY_FORMAT);
    }

    public static boolean isSameDay(Date date1, Date date2){
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
}
