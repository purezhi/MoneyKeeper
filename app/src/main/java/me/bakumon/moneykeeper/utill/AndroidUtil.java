package me.bakumon.moneykeeper.utill;

import android.content.Context;
import android.content.Intent;

import me.bakumon.moneykeeper.R;

/**
 * AndroidUtil
 *
 * @author Bakumon https://bakumon.me
 */
public class AndroidUtil {
    /**
     * 使用系统发送分享数据
     *
     * @param context 上下文
     * @param text    要分享的文本
     */
    public static void share(Context context, String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, context.getString(R.string.text_share_to)));
    }
}
