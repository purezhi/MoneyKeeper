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

package me.bakumon.moneykeeper.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.bakumon.moneykeeper.App;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.databinding.LayoutKeyboardBinding;
import me.bakumon.moneykeeper.utill.SoftInputUtils;
import me.bakumon.moneykeeper.utill.ToastUtils;

/**
 * 自定义键盘
 *
 * @author Bakumon https://bakumon.me
 */
public class KeyboardView extends LinearLayout {
    private static final int MAX_INTEGER_NUMBER = 6;

    private LayoutKeyboardBinding mBinding;
    private OnAffirmClickListener mOnAffirmClickListener;

    public KeyboardView(Context context) {
        this(context, null);
    }

    public KeyboardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KeyboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public interface OnAffirmClickListener {
        /**
         * 确定按钮点击事件
         *
         * @param text 输入框的文字
         */
        void onAffirmClick(String text);
    }

    public void setAffirmEnable(boolean enable) {
        if (mBinding != null) {
            mBinding.keyboardAffirm.setEnabled(enable);
        }
    }

    public void setAffirmClickListener(OnAffirmClickListener listener) {
        mOnAffirmClickListener = listener;
    }

    public void setText(String text) {
        mBinding.editInput.setText(text);
        mBinding.editInput.setSelection(mBinding.editInput.getText().length());
        SoftInputUtils.hideSoftInput(mBinding.editInput);
        if (!mBinding.editInput.isFocused()) {
            mBinding.editInput.requestFocus();
        }
    }

    public void setEditTextFocus() {
        mBinding.editInput.requestFocus();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(Context context) {
        // 当前 activity 打开时不弹出软键盘
        Activity activity = (Activity) context;
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setOrientation(VERTICAL);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_keyboard, this, true);
        mBinding.editInput.requestFocus();
        mBinding.editInput.setOnTouchListener((v, event) -> {
            SoftInputUtils.hideSoftInput(mBinding.editInput);
            mBinding.editInput.requestFocus();
            // 返回 true，拦截了默认的点击和长按操作，这是一个妥协的做法
            // 不再考虑多选粘贴的情况
            return true;
        });

        setInputTextViews(mBinding.keyboardNum0, mBinding.keyboardNum1,
                mBinding.keyboardNum2, mBinding.keyboardNum3,
                mBinding.keyboardNum4, mBinding.keyboardNum5,
                mBinding.keyboardNum6, mBinding.keyboardNum7,
                mBinding.keyboardNum8, mBinding.keyboardNum9,
                mBinding.keyboardNumPoint);
        setDeleteView(mBinding.keyboardDelete);

        mBinding.keyboardAffirm.setOnClickListener(v -> {
            if (mOnAffirmClickListener != null) {
                String text = mBinding.editInput.getText().toString();
                boolean isDigital = !TextUtils.isEmpty(text)
                        && !TextUtils.equals("0", text)
                        && !TextUtils.equals("0.", text);
                if (!isDigital) {
                    Animation animation = AnimationUtils.loadAnimation(App.getINSTANCE(), R.anim.shake);
                    mBinding.editInput.startAnimation(animation);
                } else {
                    mOnAffirmClickListener.onAffirmClick(text);
                }
            }
        });
    }

    /**
     * 设置键盘输入字符的textView，注意，textView点击后text将会输入到editText上
     */
    private void setInputTextViews(final TextView... textViews) {
        EditText target = mBinding.editInput;
        if (target == null || textViews == null || textViews.length < 1) {
            return;
        }
        for (int i = 0; i < textViews.length; i++) {
            final int finalI = i;
            textViews[i].setOnClickListener(view -> {
                StringBuilder sb = new StringBuilder(target.getText().toString().trim());
                String result = inputFilter(sb, textViews[finalI].getText().toString());
                setText(result);
            });
        }
    }

    /**
     * 整数9位，小数2位
     */
    private String inputFilter(StringBuilder sb, String text) {
        if (sb.length() < 1) {
            // 输入第一个字符
            if (TextUtils.equals(text, ".")) {
                sb.insert(0, "0.");
            } else {
                sb.insert(0, text);
            }
        } else if (sb.length() == 1) {
            // 输入第二个字符
            if (sb.toString().startsWith("0")) {
                if (TextUtils.equals(".", text)) {
                    sb.insert(sb.length(), ".");
                } else {
                    sb.replace(0, 1, text);
                }
            } else {
                sb.insert(sb.length(), text);
            }
        } else if (sb.toString().contains(".")) {
            // 已经输入了小数点
            int length = sb.length();
            int index = sb.indexOf(".");
            if (!TextUtils.equals(".", text)) {
                if (length - index < 3) {
                    sb.insert(sb.length(), text);
                }
            }
        } else {
            // 整数
            if (TextUtils.equals(".", text)) {
                sb.insert(sb.length(), text);
            } else {
                if (sb.length() < MAX_INTEGER_NUMBER) {
                    sb.insert(sb.length(), text);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 设置删除键
     */
    public void setDeleteView(final View deleteView) {
        final EditText target = mBinding.editInput;
        if (target == null) {
            return;
        }
        deleteView.setOnClickListener(view -> {
            StringBuilder sb = new StringBuilder(target.getText().toString().trim());
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
                setText(sb.toString());
            }
        });
        deleteView.setOnLongClickListener(v -> {
            StringBuilder sb = new StringBuilder(target.getText().toString().trim());
            if (sb.length() > 0) {
                setText("");
            }
            return false;
        });
    }
}
