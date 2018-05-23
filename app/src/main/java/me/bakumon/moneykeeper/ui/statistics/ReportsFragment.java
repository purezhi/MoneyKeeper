package me.bakumon.moneykeeper.ui.statistics;

import android.os.Bundle;
import android.support.annotation.Nullable;

import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.base.BaseFragment;

/**
 * 统计-报表
 *
 * @author Bakumon https://bakumon.me
 */
public class ReportsFragment extends BaseFragment {

    private int mYear;
    private int mMonth;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_reports;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mYear = 2018;
        mMonth = 5;
    }

    /**
     * 设置月份
     */
    public void setYearMonth(int year, int month) {
        if (year == mYear && month == mMonth) {
            return;
        }
        mYear = year;
        mMonth = month;
        // 更新数据
        getOrderData();
    }

    @Override
    protected void lazyInitData() {
        getOrderData();
    }

    private void getOrderData() {

    }
}
