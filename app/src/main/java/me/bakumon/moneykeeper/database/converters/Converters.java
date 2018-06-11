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
    public static BigDecimal stringToBig(int intDecimal) {
        return new BigDecimal(intDecimal);
    }

    @TypeConverter
    public static int bigToString(BigDecimal bigDecimal) {
        return bigDecimal.intValue();
    }
}
