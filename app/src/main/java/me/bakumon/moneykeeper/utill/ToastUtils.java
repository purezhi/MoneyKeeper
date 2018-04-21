package me.bakumon.moneykeeper.utill;

import android.support.annotation.StringRes;
import android.widget.Toast;

import me.bakumon.moneykeeper.App;

/**
 * Toast 工具类
 *
 * @author Bakumon https://bakumon.me
 */
public class ToastUtils {

    public static void show(@StringRes int resId) {
        Toast.makeText(App.getINSTANCE(), resId, Toast.LENGTH_SHORT).show();
    }

    public static void show(String msg) {
        Toast.makeText(App.getINSTANCE(), msg, Toast.LENGTH_SHORT).show();
    }
}
