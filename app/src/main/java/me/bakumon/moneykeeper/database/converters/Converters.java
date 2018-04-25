package me.bakumon.moneykeeper.database.converters;

import android.arch.persistence.room.TypeConverter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 数据库类型转换器
 *
 * @author Bakumon https://bakumon.me
 */
public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static BigDecimal stringToBig(String strDecimal) {
        return new BigDecimal(strDecimal);
    }

    @TypeConverter
    public static String bigToString(BigDecimal bigDecimal) {
        return bigDecimal.toPlainString();
    }
}
