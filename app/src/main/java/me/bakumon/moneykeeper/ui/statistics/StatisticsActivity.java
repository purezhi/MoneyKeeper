package me.bakumon.moneykeeper.ui.statistics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.base.BaseActivity;
import me.bakumon.moneykeeper.databinding.ActivityStatisticsBinding;
import me.bakumon.moneykeeper.ui.statistics.bill.BillFragment;
import me.bakumon.moneykeeper.ui.statistics.reports.ChooseMonthDialog;
import me.bakumon.moneykeeper.ui.statistics.reports.ReportsFragment;
import me.bakumon.moneykeeper.utill.DateUtils;

/**
 * 统计
 *
 * @author Bakumon https://bakumon
 */
public class StatisticsActivity extends BaseActivity {
    private ActivityStatisticsBinding mBinding;
    private BillFragment mBillFragment;
    private ReportsFragment mReportsFragment;
    private int mCurrentYear = DateUtils.getCurrentYear();
    private int mCurrentMonth = DateUtils.getCurrentMonth();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_statistics;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();

        initView();
    }

    private void initView() {
        String title = DateUtils.getCurrentYearMonth();
        mBinding.titleBar.setTitle(title);
        mBinding.titleBar.ivTitle.setVisibility(View.VISIBLE);
        mBinding.titleBar.llTitle.setOnClickListener(v -> chooseMonth());
        mBinding.titleBar.ibtClose.setOnClickListener(v -> finish());
        mBinding.typeChoice.rbOutlay.setText(R.string.text_order);
        mBinding.typeChoice.rbIncome.setText(R.string.text_reports);

        setUpFragment();
    }

    private void setUpFragment() {
        ViewPagerAdapter infoPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mBillFragment = new BillFragment();
        mReportsFragment = new ReportsFragment();
        infoPagerAdapter.addFragment(mBillFragment);
        infoPagerAdapter.addFragment(mReportsFragment);
        mBinding.viewPager.setAdapter(infoPagerAdapter);
        mBinding.viewPager.setOffscreenPageLimit(2);

        mBinding.typeChoice.rgType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_outlay) {
                mBinding.viewPager.setCurrentItem(0, false);
            } else {
                mBinding.viewPager.setCurrentItem(1, false);
            }
        });
        mBinding.typeChoice.rgType.check(R.id.rb_outlay);
    }

    private void chooseMonth() {
        ChooseMonthDialog chooseMonthDialog = new ChooseMonthDialog(this, mCurrentYear, mCurrentMonth);
        chooseMonthDialog.setOnChooseAffirmListener((year, month) -> {
            mCurrentYear = year;
            mCurrentMonth = month;
            String title = DateUtils.getYearMonthFormatString(year, month);
            mBinding.titleBar.setTitle(title);
            mBillFragment.setYearMonth(year, month);
            mReportsFragment.setYearMonth(year, month);
        });
        chooseMonthDialog.show();
    }
}
