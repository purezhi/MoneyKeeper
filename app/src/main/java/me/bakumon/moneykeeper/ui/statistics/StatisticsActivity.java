package me.bakumon.moneykeeper.ui.statistics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.base.BaseActivity;
import me.bakumon.moneykeeper.databinding.ActivityStatisticsBinding;
import me.bakumon.moneykeeper.utill.ToastUtils;

/**
 * 统计
 *
 * @author Bakumon https://bakumon
 */
public class StatisticsActivity extends BaseActivity {
    private ActivityStatisticsBinding mBinding;

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
        mBinding.titleBar.setTitle("2018-05");
        mBinding.titleBar.ivTitle.setVisibility(View.VISIBLE);
        mBinding.titleBar.llTitle.setOnClickListener(v -> chooseMonth());
        mBinding.titleBar.ibtClose.setOnClickListener(v -> finish());
        mBinding.typeChoice.rbOutlay.setText(R.string.text_order);
        mBinding.typeChoice.rbIncome.setText(R.string.text_reports);

        setUpFragment();
    }

    private void setUpFragment() {
        ViewPagerAdapter infoPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        BillFragment billFragment = new BillFragment();
        ReportsFragment reportsFragment = new ReportsFragment();
        infoPagerAdapter.addFragment(billFragment);
        infoPagerAdapter.addFragment(reportsFragment);
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
        ToastUtils.show("选择月份");
    }
}
