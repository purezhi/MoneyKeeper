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

package me.bakumon.moneykeeper.binding;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;

import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.utill.BigDecimalUtil;
import me.bakumon.moneykeeper.utill.SizeUtils;

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

    @BindingAdapter("custom_margin_bottom")
    public static void setMarginBottom(View view, int bottomMargin) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        ViewGroup.MarginLayoutParams marginParams;
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            marginParams = (ViewGroup.MarginLayoutParams) layoutParams;
        } else {
            marginParams = new ViewGroup.MarginLayoutParams(layoutParams);
        }
        marginParams.bottomMargin = SizeUtils.dp2px(bottomMargin);
    }

    @BindingAdapter("text_check_null")
    public static void setText(TextView textView, String text) {
        textView.setText(text);
        textView.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
    }

    @BindingAdapter("src_img_name")
    public static void setImg(ImageView imageView, String imgName) {
        Context context = imageView.getContext();
        if (TextUtils.isEmpty(imgName)) {
            imgName = "type_item_default";
        }
        int resId = context.getResources().getIdentifier(imgName, "mipmap", context.getPackageName());
        imageView.setImageResource(resId);
    }

    @BindingAdapter("text_money")
    public static void setMoneyText(TextView textView, BigDecimal bigDecimal) {
        textView.setText(BigDecimalUtil.fen2Yuan(bigDecimal));
    }

    @BindingAdapter("text_money_with_prefix")
    public static void setMoneyTextWithPrefix(TextView textView, BigDecimal bigDecimal) {
        String symbol = textView.getResources().getString(R.string.text_money_symbol);
        textView.setText(symbol + BigDecimalUtil.fen2Yuan(bigDecimal));
    }
}
