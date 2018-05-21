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

/**
 * 自定义键盘
 *
 * @author Bakumon https://bakumon.me
 */
public class KeyboardView extends LinearLayout {

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
                if (TextUtils.isEmpty(mBinding.editInput.getText().toString())) {
                    Animation animation = AnimationUtils.loadAnimation(App.getINSTANCE(), R.anim.shake);
                    mBinding.editInput.startAnimation(animation);
                } else {
                    mOnAffirmClickListener.onAffirmClick(mBinding.editInput.getText().toString());
                }
            }
        });
    }

    /**
     * 设置键盘输入字符的textView，注意，textView点击后text将会输入到editText上
     */
    private void setInputTextViews(final TextView... textViews) {
        if (textViews == null || textViews.length < 1) {
            return;
        }
        for (int i = 0; i < textViews.length; i++) {
            final int finalI = i;
            textViews[i].setOnClickListener(view -> {
                EditText target = mBinding.editInput;
                if (target != null) {
                    StringBuilder sb = new StringBuilder(target.getText().toString().trim());
                    int selectedEnd = target.getSelectionEnd();
                    int selectedStart = target.getSelectionStart();
                    // 没有多选
                    if (selectedStart == selectedEnd) {
                        sb.insert(selectedStart, textViews[finalI].getText());
                        target.setText(sb.toString());
                        if (selectedStart + 1 <= sb.length()) {
                            target.setSelection(selectedStart + 1);
                        }
                        // 多选
                    } else {
                        sb.replace(selectedStart, selectedEnd, textViews[finalI].getText().toString());
                        target.setText(sb.toString());
                        if (selectedStart + 1 <= sb.length()) {
                            target.setSelection(selectedStart + 1);
                        }
                    }
                }
            });
        }
    }

    /**
     * 设置删除键
     */
    public void setDeleteView(final View deleteView) {
        final EditText target = mBinding.editInput;
        deleteView.setOnClickListener(view -> {

            if (target != null) {
                // TODO: 2018/5/21  限制输入
                // 整数9位，小数2位
                StringBuilder sb = new StringBuilder(target.getText().toString().trim());
                if (sb.length() > 0) {
                    int selectedEnd = target.getSelectionEnd();
                    int selectedStart = target.getSelectionStart();
                    // 没有多选
                    if (selectedStart == selectedEnd) {
                        if (selectedStart - 1 >= 0) {
                            sb.deleteCharAt(selectedStart - 1);
                            target.setText(sb.toString());
                            target.setSelection(selectedStart - 1);
                        }
                        // 多选
                    } else {
                        sb.delete(selectedStart, selectedEnd);
                        target.setText(sb.toString());
                        target.setSelection(selectedStart);
                    }
                }
            }
        });
        deleteView.setOnLongClickListener(v -> {
            if (target != null) {
                StringBuilder sb = new StringBuilder(target.getText().toString().trim());
                if (sb.length() > 0) {
                    target.setText("");
                    target.setSelection(0);
                }
            }
            return false;
        });
    }
}
