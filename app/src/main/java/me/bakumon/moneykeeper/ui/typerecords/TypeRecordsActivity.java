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

package me.bakumon.moneykeeper.ui.typerecords;

import android.os.Bundle;
import android.support.annotation.Nullable;

import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.Router;
import me.bakumon.moneykeeper.base.BaseActivity;
import me.bakumon.moneykeeper.databinding.ActivityStatisticsBinding;
import me.bakumon.moneykeeper.ui.statistics.ViewPagerAdapter;

/**
 * 某一类型的记账记录
 *
 * @author Bakumon https://bakumon
 */
public class TypeRecordsActivity extends BaseActivity {

    private ActivityStatisticsBinding mBinding;

    private int mRecordType;
    private int mRecordTypeId;
    private int mYear;
    private int mMonth;

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
        if (getIntent() != null) {
            mBinding.titleBar.setTitle(getIntent().getStringExtra(Router.ExtraKey.KEY_TYPE_NAME));
            mRecordType = getIntent().getIntExtra(Router.ExtraKey.KEY_RECORD_TYPE, 0);
            mRecordTypeId = getIntent().getIntExtra(Router.ExtraKey.KEY_RECORD_TYPE_ID, 0);
            mYear = getIntent().getIntExtra(Router.ExtraKey.KEY_YEAR, 0);
            mMonth = getIntent().getIntExtra(Router.ExtraKey.KEY_MONTH, 0);
        }

        mBinding.titleBar.ibtClose.setOnClickListener(v -> finish());
        mBinding.typeChoice.rbOutlay.setText(R.string.text_sort_time);
        mBinding.typeChoice.rbIncome.setText(R.string.text_sort_money);

        setUpFragment();
    }

    private void setUpFragment() {
        ViewPagerAdapter infoPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        TypeRecordsFragment timeSortFragment = TypeRecordsFragment.newInstance(TypeRecordsFragment.SORT_TIME, mRecordType, mRecordTypeId, mYear, mMonth);
        TypeRecordsFragment moneySortFragment = TypeRecordsFragment.newInstance(TypeRecordsFragment.SORT_MONEY, mRecordType, mRecordTypeId, mYear, mMonth);
        infoPagerAdapter.addFragment(timeSortFragment);
        infoPagerAdapter.addFragment(moneySortFragment);
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
}
