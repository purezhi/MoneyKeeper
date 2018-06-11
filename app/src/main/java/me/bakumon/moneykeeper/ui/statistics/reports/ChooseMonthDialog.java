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

package me.bakumon.moneykeeper.ui.statistics.reports;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.base.BaseDataBindingAdapter;
import me.bakumon.moneykeeper.utill.DateUtils;
import me.bakumon.moneykeeper.view.PickerLayoutManager;

/**
 * 选择月份
 *
 * @author Bakumon https://bakumon
 */
public class ChooseMonthDialog implements DialogInterface.OnDismissListener {

    /**
     * 为什么是 1900
     * 添加记账记录时，选时间 dialog 最小可选的年份是 1900
     */
    private static final int MIN_YEAR = 1900;
    private static final int MAX_YEAR = DateUtils.getCurrentYear();

    private Context mContext;
    private RecyclerView mRvMonth;
    private PickerAdapter mYearAdapter;
    private PickerAdapter mMonthAdapter;

    private OnChooseAffirmListener mOnChooseAffirmListener;
    private OnDismissListener mOnDismissListener;
    private AlertDialog.Builder builder;

    private int mYear = DateUtils.getCurrentYear();
    private int mMonth = DateUtils.getCurrentMonth();

    public ChooseMonthDialog(Context context) {
        mContext = context;
        setupDialog();
    }

    public ChooseMonthDialog(Context context, int year, int month) {
        mContext = context;
        mYear = year;
        mMonth = month;
        setupDialog();
    }

    private void setupDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View contentView = layoutInflater.inflate(R.layout.dialog_choose_month, null, false);
        RecyclerView rvYear = contentView.findViewById(R.id.rv_year);
        mRvMonth = contentView.findViewById(R.id.rv_month);

        // 设置 pickerLayoutManage
        PickerLayoutManager lmYear = new PickerLayoutManager(mContext, rvYear, PickerLayoutManager.VERTICAL, false, 3, 0.4f, true);
        rvYear.setLayoutManager(lmYear);
        PickerLayoutManager lmMonth = new PickerLayoutManager(mContext, mRvMonth, PickerLayoutManager.VERTICAL, false, 3, 0.4f, true);
        mRvMonth.setLayoutManager(lmMonth);

        mYearAdapter = new PickerAdapter(null);
        rvYear.setAdapter(mYearAdapter);
        mMonthAdapter = new PickerAdapter(null);
        mRvMonth.setAdapter(mMonthAdapter);

        setYearAdapter();

        lmYear.OnSelectedViewListener((view, position) -> {
            mYear = mYearAdapter.getData().get(position);
            // 重新设置月份数据
            setMonthAdapter();
        });
        // 选中对于年
        for (int i = mYearAdapter.getData().size() - 1; i >= 0; i--) {
            if (mYearAdapter.getData().get(i) == mYear) {
                rvYear.scrollToPosition(i);
                break;
            }
        }

        setMonthAdapter();

        lmMonth.OnSelectedViewListener((view, position) -> mMonth = mMonthAdapter.getData().get(position));
        // 选中对于月份
        for (int i = 0; i < mMonthAdapter.getData().size(); i++) {
            if (mMonthAdapter.getData().get(i) == mMonth) {
                mRvMonth.scrollToPosition(i);
                break;
            }
        }

        builder = new AlertDialog.Builder(mContext)
                .setTitle(R.string.text_choose_month)
                .setView(contentView)
                .setNegativeButton(R.string.text_button_cancel, null)
                .setPositiveButton(R.string.text_affirm, (dialog, which) -> {
                    if (mOnChooseAffirmListener != null) {
                        mOnChooseAffirmListener.onClick(mYear, mMonth);
                    }
                });
        builder.setOnDismissListener(this);

    }

    private void setYearAdapter() {
        List<Integer> yearList = new ArrayList<>();
        for (int i = MIN_YEAR; i <= MAX_YEAR; i++) {
            yearList.add(i);
        }
        mYearAdapter.setNewData(yearList);
    }

    private void setMonthAdapter() {
        List<Integer> monthList = new ArrayList<>();
        // 当前年，月份最大是当前月
        int maxMonth = mYear == DateUtils.getCurrentYear() ? DateUtils.getCurrentMonth() : 12;
        for (int i = 1; i <= maxMonth; i++) {
            monthList.add(i);
        }
        List<Integer> lastData = mMonthAdapter.getData();
        if (lastData.size() > monthList.size()) {
            mMonthAdapter.setNewData(monthList);
            // 修正月份
            mMonth = 1;
            mRvMonth.scrollToPosition(0);
        } else {
            mMonthAdapter.setNewData(monthList);
        }
    }

    public void show() {
        builder.create().show();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss();
        }
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
    }

    public interface OnDismissListener {
        /**
         * dialog 取消
         */
        void onDismiss();
    }

    public void setOnChooseAffirmListener(OnChooseAffirmListener onChooseAffirmListener) {
        mOnChooseAffirmListener = onChooseAffirmListener;
    }

    /**
     * 选择完成月份后，点击确定按钮监听
     */
    public interface OnChooseAffirmListener {
        /**
         * 确定按钮点击事件
         *
         * @param year  选择的年份
         * @param month 选择的月份
         */
        void onClick(int year, int month);
    }

    class PickerAdapter extends BaseDataBindingAdapter<Integer> {

        PickerAdapter(@Nullable List<Integer> data) {
            super(R.layout.item_picker, data);
        }

        @Override
        protected void convert(DataBindingViewHolder helper, Integer item) {
            helper.setText(R.id.tv_text, item + "");
        }
    }
}
