package me.bakumon.moneykeeper.view.utill;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

/**
 * 自定义键盘帮助类
 *
 * @author Bakumon https://bakumon.me
 * @date 2017/10/31
 */

public class CustomKeyboardHelper {

    private static WeakReference<EditText> mEditText;

    private static CustomKeyboardHelper INSTANCE;

    private CustomKeyboardHelper() {
    }

    public static synchronized CustomKeyboardHelper with(Window window, EditText editText) {
        if (INSTANCE == null) {
            INSTANCE = new CustomKeyboardHelper();
        }
        init(window, editText);
        return INSTANCE;
    }

    public static void hideSoftInput(EditText editText) {
        try {
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            setShowSoftInputOnFocus.setAccessible(true);
            setShowSoftInputOnFocus.invoke(editText, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    private static void init(Window window, EditText editText) {
        mEditText = new WeakReference<>(editText);
        // 设置不调用系统键盘
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        hideSoftInput(editText);
    }

    /**
     * 设置键盘输入字符的textview，注意，textview点击后text将会输入到edittext上
     */
    public CustomKeyboardHelper setInputTextViews(final TextView... textViews) {
        if (textViews == null || textViews.length < 1) {
            return this;
        }
        for (int i = 0; i < textViews.length; i++) {
            final int finalI = i;
            textViews[i].setOnClickListener(view -> {
                EditText target = mEditText.get();
                if (target != null) {
                    if (!target.isFocused()) {
                        target.requestFocus();
                        target.setSelection(target.getText().length());
                    }
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
        return this;
    }

    /**
     * 设置删除键
     */
    public CustomKeyboardHelper setDeleteView(final View deleteView) {
        final EditText target = mEditText.get();
        deleteView.setOnClickListener(view -> {

            if (target != null) {
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
        return this;
    }

    public void setAffirmListener(View affirmView, View.OnClickListener listener) {
        if (affirmView == null || listener == null) {
            return;
        }
        affirmView.setOnClickListener(listener);
    }
}
