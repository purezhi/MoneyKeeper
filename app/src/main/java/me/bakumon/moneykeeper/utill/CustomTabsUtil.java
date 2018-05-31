package me.bakumon.moneykeeper.utill;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;

/**
 * Chrome CustomTabs 工具类
 *
 * @author Baumon https://bakumon.me
 */
public class CustomTabsUtil {
    public static void openWeb(Context context, String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        // github 黑色把
        builder.setToolbarColor(Color.parseColor("#ff24292d"));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }
}
