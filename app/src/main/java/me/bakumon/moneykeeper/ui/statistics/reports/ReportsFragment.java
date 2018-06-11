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

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.bakumon.moneykeeper.Injection;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.Router;
import me.bakumon.moneykeeper.base.BaseFragment;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.database.entity.SumMoneyBean;
import me.bakumon.moneykeeper.database.entity.TypeSumMoneyBean;
import me.bakumon.moneykeeper.databinding.FragmentReportsBinding;
import me.bakumon.moneykeeper.utill.BigDecimalUtil;
import me.bakumon.moneykeeper.utill.DateUtils;
import me.bakumon.moneykeeper.utill.ToastUtils;
import me.bakumon.moneykeeper.viewmodel.ViewModelFactory;
import me.drakeet.floo.Floo;

/**
 * 统计-报表
 *
 * @author Bakumon https://bakumon.me
 */
public class ReportsFragment extends BaseFragment {

    private static final String TAG = ReportsFragment.class.getSimpleName();
    private FragmentReportsBinding mBinding;
    private ReportsViewModel mViewModel;
    private ReportAdapter mAdapter;

    public int mYear;
    public int mMonth;
    public int mType;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_reports;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(ReportsViewModel.class);

        mYear = DateUtils.getCurrentYear();
        mMonth = DateUtils.getCurrentMonth();
        mType = RecordType.TYPE_OUTLAY;

        initView();
    }

    private void initView() {
        mBinding.rvRecordReports.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ReportAdapter(null);
        mBinding.rvRecordReports.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            TypeSumMoneyBean bean = mAdapter.getData().get(position);
            navTypeRecords(bean.typeName, bean.typeId);
        });

        initPieChart();

        mBinding.rgType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_outlay) {
                mType = RecordType.TYPE_OUTLAY;
            } else {
                mType = RecordType.TYPE_INCOME;
            }
            getTypeSumMoney();
            getMonthSumMoney();
        });
    }

    private void navTypeRecords(String typeName, int typeId) {
        if (getContext() != null) {
            Floo.navigation(getContext(), Router.Url.URL_TYPE_RECORDS)
                    .putExtra(Router.ExtraKey.KEY_TYPE_NAME, typeName)
                    .putExtra(Router.ExtraKey.KEY_RECORD_TYPE, mType)
                    .putExtra(Router.ExtraKey.KEY_RECORD_TYPE_ID, typeId)
                    .putExtra(Router.ExtraKey.KEY_YEAR, mYear)
                    .putExtra(Router.ExtraKey.KEY_MONTH, mMonth)
                    .start();
        }
    }

    private void initPieChart() {
        mBinding.pieChart.getDescription().setEnabled(false);
        mBinding.pieChart.setNoDataText("");
        mBinding.pieChart.setUsePercentValues(true);
        mBinding.pieChart.setDrawHoleEnabled(false);
        mBinding.pieChart.setRotationEnabled(false);

        mBinding.pieChart.getLegend().setEnabled(false);
        mBinding.pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                String typeName = ((TypeSumMoneyBean) e.getData()).typeName;
                int typeId = ((TypeSumMoneyBean) e.getData()).typeId;
                navTypeRecords(typeName, typeId);
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    private void setChartData(List<TypeSumMoneyBean> typeSumMoneyBeans) {
        if (typeSumMoneyBeans == null || typeSumMoneyBeans.size() < 1) {
            mBinding.pieChart.setVisibility(View.INVISIBLE);
            return;
        } else {
            mBinding.pieChart.setVisibility(View.VISIBLE);
        }

        List<PieEntry> entries = PieEntryConverter.getBarEntryList(typeSumMoneyBeans);
        PieDataSet dataSet;

        if (mBinding.pieChart.getData() != null && mBinding.pieChart.getData().getDataSetCount() > 0) {
            dataSet = (PieDataSet) mBinding.pieChart.getData().getDataSetByIndex(0);
            dataSet.setValues(entries);
            mBinding.pieChart.getData().notifyDataChanged();
            mBinding.pieChart.notifyDataSetChanged();
        } else {
            dataSet = new PieDataSet(entries, "");
            dataSet.setSliceSpace(0f);
            dataSet.setSelectionShift(1.2f);
            dataSet.setValueLinePart1Length(0.3f);
            dataSet.setValueLinePart2Length(1f);
            dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            dataSet.setYValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
            dataSet.setValueTextSize(10f);
            dataSet.setValueLineVariableLength(true);
            dataSet.setValueLineColor(getResources().getColor(R.color.colorTextWhite));

            List<Integer> color;
            if (entries.size() % 7 == 0) {
                color = ColorTemplate.createColors(getResources(),
                        new int[]{R.color.colorPieChart1, R.color.colorPieChart2,
                                R.color.colorPieChart3, R.color.colorPieChart4,
                                R.color.colorPieChart5, R.color.colorPieChart6,
                                R.color.colorPieChart7});
            } else {
                color = ColorTemplate.createColors(getResources(),
                        new int[]{R.color.colorPieChart1, R.color.colorPieChart2,
                                R.color.colorPieChart3, R.color.colorPieChart4,
                                R.color.colorPieChart5, R.color.colorPieChart6});
            }
            dataSet.setColors(color);

            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(10f);
            data.setValueTextColor(Color.WHITE);

            mBinding.pieChart.setData(data);
        }
        // undo all highlights
        mBinding.pieChart.highlightValues(null);
        mBinding.pieChart.invalidate();
        mBinding.pieChart.animateY(1000);
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
        getTypeSumMoney();
        getMonthSumMoney();
    }

    @Override
    protected void lazyInitData() {
        mBinding.rgType.check(R.id.rb_outlay);
    }

    private void getMonthSumMoney() {
        mDisposable.add(mViewModel.getMonthSumMoney(mYear, mMonth)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sumMoneyBean -> {
                            String outlay = getString(R.string.text_month_outlay_symbol) + "0";
                            String income = getString(R.string.text_month_income_symbol) + "0";
                            if (sumMoneyBean != null && sumMoneyBean.size() > 0) {
                                for (SumMoneyBean bean : sumMoneyBean) {
                                    if (bean.type == RecordType.TYPE_OUTLAY) {
                                        outlay = getString(R.string.text_month_outlay_symbol) + BigDecimalUtil.fen2Yuan(bean.sumMoney);
                                    } else if (bean.type == RecordType.TYPE_INCOME) {
                                        income = getString(R.string.text_month_income_symbol) + BigDecimalUtil.fen2Yuan(bean.sumMoney);
                                    }
                                }
                            }
                            mBinding.rbOutlay.setText(outlay);
                            mBinding.rbIncome.setText(income);
                        },
                        throwable -> {
                            ToastUtils.show(R.string.toast_get_month_summary_fail);
                            Log.e(TAG, "获取该月汇总数据失败", throwable);
                        }));
    }

    private void getTypeSumMoney() {
        mDisposable.add(mViewModel.getTypeSumMoney(mYear, mMonth, mType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(typeSumMoneyBeans -> {
                            setChartData(typeSumMoneyBeans);
                            mAdapter.setNewData(typeSumMoneyBeans);
                            if (typeSumMoneyBeans == null || typeSumMoneyBeans.size() < 1) {
                                mAdapter.setEmptyView(inflate(R.layout.layout_statistics_empty));
                            }
                        },
                        throwable -> {
                            ToastUtils.show(R.string.toast_get_type_summary_fail);
                            Log.e(TAG, "获取类型汇总数据失败", throwable);
                        }));
    }
}
