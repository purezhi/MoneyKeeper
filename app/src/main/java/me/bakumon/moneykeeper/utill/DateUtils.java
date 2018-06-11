/*
 * Copyright 2018 Bakumon. https://github.com/Bakumon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
    public static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM");
    @SuppressLint("SimpleDateFormat")
    public static final DateFormat ALL_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    @SuppressLint("SimpleDateFormat")
    public static final DateFormat MONTH_DAY_FORMAT = new SimpleDateFormat("MM-dd");
    @SuppressLint("SimpleDateFormat")
    public static final DateFormat YEAR_MONTH_DAY_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    public static final DateFormat FORMAT_FOR_FIEL_NAME = new SimpleDateFormat("_yy_MM_dd_HH_mm_ss");

    /**
     * 获取今天 Date 对象，精确到日
     *
     * @return Date 对象
     */
    public static Date getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        // 这里特别续一秒
        calendar.set(Calendar.SECOND, 1);
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
        // 这里特别续一秒
        String time = year + "-" + monthOfYear + "-" + dayOfMonth + "-00-00-01";
        return string2Date(time, ALL_FORMAT);
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

    public static String getCurrentDateString(){
        return date2String(new Date(), FORMAT_FOR_FIEL_NAME);
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
     * 获取当前年份
     */
    public static String getCurrentYearMonth() {
        Calendar calendar = Calendar.getInstance();
        return date2String(calendar.getTime(), FORMAT);
    }

    /**
     * 格式化年月
     *
     * @param year  年份
     * @param month 月份（字面）
     */
    public static String getYearMonthFormatString(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        return date2String(calendar.getTime(), FORMAT);
    }

    /**
     * 获取当前年份
     */
    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取修正后的当前月份
     */
    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取某月有多少天
     *
     * @param year  年份
     * @param month 月份
     * @return 该月的天数
     */
    public static int getDayCount(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);

        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取某月份开始时刻的 Date
     *
     * @param year  年份
     * @param month 月份
     * @return 当前月份开始的 Date
     */
    public static Date getMonthStart(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取某月份结束时刻的 Date
     *
     * @param year  年份
     * @param month 月份
     * @return 当前月份结束的 Date
     */
    public static Date getMonthEnd(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
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
     * 获取当前月份开始时刻的 Date
     * 比如当前是 2018年4月
     * 返回的 Date 是 format 后： 2018-04-01T00:00:00.000+0800
     *
     * @return 当前月份开始的 Date
     */
    public static Date getCurrentMonthStart() {
        Calendar calendar = Calendar.getInstance();
        return getMonthStart(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
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
        return getMonthEnd(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
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


        long oneDayMillis = todayEndMillis - todayStartMillis + 1;

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
