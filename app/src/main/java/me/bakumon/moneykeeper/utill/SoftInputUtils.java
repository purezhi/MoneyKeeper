package me.bakumon.moneykeeper.utill;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 软键盘工具类
 *
 * @author Bakumon https://bakumon.me
 */
public class SoftInputUtils {
    /**
     * 隐藏软键盘
     *
     * @param view 当前屏幕中任意一个 view
     */
    public static void hideSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
