package me.bakumon.moneykeeper.utill;

import me.bakumon.moneykeeper.App;

/**
 * 尺寸转换工具类
 *
 * @author Bakumon https://bakumon.me
 */
public class SizeUtils {
    public static int dp2px(final float dpValue) {
        final float scale = App.getINSTANCE().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
