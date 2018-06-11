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

package me.bakumon.moneykeeper.ui.statistics.bill;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.bakumon.moneykeeper.Injection;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.Router;
import me.bakumon.moneykeeper.base.BaseFragment;
import me.bakumon.moneykeeper.database.entity.DaySumMoneyBean;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.database.entity.RecordWithType;
import me.bakumon.moneykeeper.database.entity.SumMoneyBean;
import me.bakumon.moneykeeper.databinding.FragmentBillBinding;
import me.bakumon.moneykeeper.datasource.BackupFailException;
import me.bakumon.moneykeeper.ui.home.HomeAdapter;
import me.bakumon.moneykeeper.utill.BigDecimalUtil;
import me.bakumon.moneykeeper.utill.DateUtils;
import me.bakumon.moneykeeper.utill.ToastUtils;
import me.bakumon.moneykeeper.view.BarChartMarkerView;
import me.bakumon.moneykeeper.viewmodel.ViewModelFactory;
import me.drakeet.floo.Floo;

/**
 * 统计-账单
 *
 * @author Bakumon https://bakumon.me
 */
public class BillFragment extends BaseFragment {

    private static final String TAG = BillFragment.class.getSimpleName();
    private FragmentBillBinding mBinding;
    private BillViewModel mViewModel;
    private HomeAdapter mAdapter;

    public int mYear;
    public int mMonth;
    public int mType;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bill;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(BillViewModel.class);

        mYear = DateUtils.getCurrentYear();
        mMonth = DateUtils.getCurrentMonth();
        mType = RecordType.TYPE_OUTLAY;

        initView();
    }

    private void initView() {
        mBinding.rvRecordBill.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new HomeAdapter(null);
        mBinding.rvRecordBill.setAdapter(mAdapter);
        mAdapter.setOnItemChildLongClickListener((adapter, view, position) -> {
            showOperateDialog(mAdapter.getData().get(position));
            return false;
        });

        initBarChart();

        mBinding.rgType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_outlay) {
                mType = RecordType.TYPE_OUTLAY;
            } else {
                mType = RecordType.TYPE_INCOME;
            }
            getOrderData();
            getDaySumData();
            getMonthSumMoney();
        });
    }

    private void initBarChart() {
        mBinding.barChart.setNoDataText("");
        mBinding.barChart.setScaleEnabled(false);
        mBinding.barChart.getDescription().setEnabled(false);
        mBinding.barChart.getLegend().setEnabled(false);

        mBinding.barChart.getAxisLeft().setAxisMinimum(0);
        mBinding.barChart.getAxisLeft().setEnabled(false);
        mBinding.barChart.getAxisRight().setEnabled(false);
        XAxis xAxis = mBinding.barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(getResources().getColor(R.color.colorTextGray));
        xAxis.setLabelCount(5);

        BarChartMarkerView mv = new BarChartMarkerView(getContext());
        mv.setChartView(mBinding.barChart);
        mBinding.barChart.setMarker(mv);
    }

    private void showOperateDialog(RecordWithType record) {
        if (getContext() == null) {
            return;
        }
        new AlertDialog.Builder(getContext())
                .setItems(new String[]{getString(R.string.text_modify), getString(R.string.text_delete)}, (dialog, which) -> {
                    if (which == 0) {
                        modifyRecord(record);
                    } else {
                        deleteRecord(record);
                    }
                })
                .create()
                .show();
    }

    private void modifyRecord(RecordWithType record) {
        if (getContext() == null) {
            return;
        }
        Floo.navigation(getContext(), Router.Url.URL_ADD_RECORD)
                .putExtra(Router.ExtraKey.KEY_RECORD_BEAN, record)
                .start();
    }

    private void deleteRecord(RecordWithType record) {
        mDisposable.add(mViewModel.deleteRecord(record)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                        },
                        throwable -> {
                            if (throwable instanceof BackupFailException) {
                                ToastUtils.show(throwable.getMessage());
                                Log.e(TAG, "备份失败（删除记账记录失败的时候）", throwable);
                            } else {
                                ToastUtils.show(R.string.toast_record_delete_fail);
                                Log.e(TAG, "删除记账记录失败", throwable);
                            }
                        }));
    }

    private void setChartData(List<DaySumMoneyBean> daySumMoneyBeans) {
        if (daySumMoneyBeans == null || daySumMoneyBeans.size() < 1) {
            mBinding.barChart.setVisibility(View.INVISIBLE);
            return;
        } else {
            mBinding.barChart.setVisibility(View.VISIBLE);
        }

        int count = DateUtils.getDayCount(mYear, mMonth);
        List<BarEntry> barEntries = BarEntryConverter.getBarEntryList(count, daySumMoneyBeans);

        BarDataSet set1;
        if (mBinding.barChart.getData() != null && mBinding.barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mBinding.barChart.getData().getDataSetByIndex(0);
            set1.setValues(barEntries);
            mBinding.barChart.getData().notifyDataChanged();
            mBinding.barChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(barEntries, "");
            set1.setDrawIcons(false);
            set1.setDrawValues(false);
            set1.setColor(getResources().getColor(R.color.colorAccent));
            set1.setValueTextColor(getResources().getColor(R.color.colorTextWhite));

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setBarWidth(0.5f);
            data.setHighlightEnabled(true);
            mBinding.barChart.setData(data);
        }
        mBinding.barChart.invalidate();
        mBinding.barChart.animateY(1000);
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
        getDaySumData();
        getMonthSumMoney();
    }

    @Override
    protected void lazyInitData() {
        mBinding.rgType.check(R.id.rb_outlay);
    }

    private void getOrderData() {
        mDisposable.add(mViewModel.getRecordWithTypes(mYear, mMonth, mType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recordWithTypes -> {
                            mAdapter.setNewData(recordWithTypes);
                            if (recordWithTypes == null || recordWithTypes.size() < 1) {
                                mAdapter.setEmptyView(inflate(R.layout.layout_statistics_empty));
                            }
                        },
                        throwable -> {
                            ToastUtils.show(R.string.toast_records_fail);
                            Log.e(TAG, "获取记录列表失败", throwable);
                        }));
    }

    private void getDaySumData() {
        mDisposable.add(mViewModel.getDaySumMoney(mYear, mMonth, mType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setChartData,
                        throwable -> {
                            ToastUtils.show(R.string.toast_get_statistics_fail);
                            Log.e(TAG, "获取统计数据失败", throwable);
                        }));
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
}
