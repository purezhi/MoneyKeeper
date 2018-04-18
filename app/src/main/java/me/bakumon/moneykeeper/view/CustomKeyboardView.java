package me.bakumon.moneykeeper.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.databinding.LayoutKeyboardBinding;
import me.bakumon.moneykeeper.utill.CustomKeyboardHelper;

/**
 * @author Bakumon
 */
public class CustomKeyboardView extends LinearLayout {

    private OnAffirmClickListener mOnAffirmClickListener;

    public CustomKeyboardView(Context context) {
        this(context, null);
    }

    public CustomKeyboardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomKeyboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

    public void setAffirmClickListener(OnAffirmClickListener listener) {
        mOnAffirmClickListener = listener;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(Context context) {
        setOrientation(VERTICAL);
        LayoutKeyboardBinding mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_keyboard, this, true);
        mBinding.editInput.setFocusable(true);
        mBinding.editInput.setFocusableInTouchMode(true);
        mBinding.editInput.requestFocus();
        CustomKeyboardHelper.with(((Activity) context).getWindow(), mBinding.editInput)
                .setInputTextViews(mBinding.keyboardNum0, mBinding.keyboardNum1,
                        mBinding.keyboardNum2, mBinding.keyboardNum3,
                        mBinding.keyboardNum4, mBinding.keyboardNum5,
                        mBinding.keyboardNum6, mBinding.keyboardNum7,
                        mBinding.keyboardNum8, mBinding.keyboardNum9,
                        mBinding.keyboardNumPoint)
                .setDeleteView(mBinding.keyboardDelete)
                .setAffirmListener(mBinding.keyboardAffirm, v -> {
                    if (mOnAffirmClickListener != null) {
                        mOnAffirmClickListener.onAffirmClick(mBinding.editInput.getText().toString());
                    }
                });
        // 点击 editText，不弹出软键盘，点击时如果已经有软键盘，则隐藏
        mBinding.editInput.setOnTouchListener((v, event) -> {
            CustomKeyboardHelper.hideSoftInput(mBinding.editInput);
            return false;
        });
    }

}
