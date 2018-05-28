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
