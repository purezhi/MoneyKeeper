package me.bakumon.moneykeeper.binding;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * binding 属性适配器（自动被 DataBinding 引用）
 *
 * @author Bakumon https://bakumon.me
 */
public class BindAdapter {

    @BindingAdapter("android:visibility")
    public static void showHide(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("android:layout_width")
    public static void setLayoutWidth(View view, int width) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = width;
    }

    @BindingAdapter("text_check_null")
    public static void setText(TextView textView, String text) {
        if (!TextUtils.isEmpty(text)) {
            textView.setText(text);
            return;
        }
        textView.setVisibility(View.GONE);
    }

    @BindingAdapter("src_img_name")
    public static void setImg(ImageView imageView, String imgName) {
        Context context = imageView.getContext();
        if (!TextUtils.isEmpty(imgName)) {
            int resId = context.getResources().getIdentifier(imgName, "mipmap", context.getPackageName());
            imageView.setImageResource(resId);
        }
    }
}
