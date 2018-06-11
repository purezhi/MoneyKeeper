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

import android.text.TextUtils;

import java.math.BigDecimal;

/**
 * BigDecimal 工具类
 *
 * @author Bakumon https://bakumon.me
 */
public class BigDecimalUtil {
    /**
     * 分转换为元
     */
    public static String fen2Yuan(BigDecimal fenBD) {
        String text;
        if (fenBD != null) {
            text = fenBD.divide(new BigDecimal(100)).toPlainString();
        } else {
            text = "0";
        }
        return text;
    }

    /**
     * 分转换为元
     */
    public static BigDecimal fen2YuanBD(BigDecimal fenBD) {
        BigDecimal result;
        if (fenBD != null) {
            result = fenBD.divide(new BigDecimal(100));
        } else {
            result = new BigDecimal(0);
        }
        return result;
    }

    /**
     * 分转换为元
     */
    public static BigDecimal yuan2FenBD(String strYuan) {
        BigDecimal result;
        if (!TextUtils.isEmpty(strYuan)) {
            result = new BigDecimal(strYuan).multiply(new BigDecimal(100));
        } else {
            result = new BigDecimal(0);
        }
        return result;
    }
}
