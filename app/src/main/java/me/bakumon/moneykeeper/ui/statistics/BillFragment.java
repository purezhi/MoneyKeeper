package me.bakumon.moneykeeper.ui.statistics;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

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
import me.bakumon.moneykeeper.base.BaseFragment;
import me.bakumon.moneykeeper.databinding.FragmentBillBinding;
import me.bakumon.moneykeeper.ui.home.HomeAdapter;
import me.bakumon.moneykeeper.utill.ToastUtils;
import me.bakumon.moneykeeper.view.BarChartMarkerView;
import me.bakumon.moneykeeper.viewmodel.ViewModelFactory;

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
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getContext());
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(BillViewModel.class);

        mYear = 2018;
        mMonth = 5;
        mType = 0;

        initView();
    }

    private void initView() {
        mBinding.rvRecordBill.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new HomeAdapter(null);
        mBinding.rvRecordBill.setAdapter(mAdapter);

        initBarChart();
    }

    private void initBarChart() {

        mBinding.barChart.setNoDataText("");
        mBinding.barChart.setScaleEnabled(false);
        mBinding.barChart.getDescription().setEnabled(false);

        XAxis xAxis = mBinding.barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        xAxis.setTextColor(getResources().getColor(R.color.colorTextGray));
        xAxis.setLabelCount(5);

        mBinding.barChart.getLegend().setEnabled(false);

        mBinding.barChart.getAxisLeft().setAxisMinimum(0);
        mBinding.barChart.getAxisLeft().setEnabled(false);
        mBinding.barChart.getAxisRight().setEnabled(false);

        BarChartMarkerView mv = new BarChartMarkerView(getContext());
        mv.setChartView(mBinding.barChart);
        mBinding.barChart.setMarker(mv);

    }

    private void setData(List<BarEntry> barEntries) {

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
            mBinding.barChart.invalidate();

        }
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
//        getOrderData();
    }

    @Override
    protected void lazyInitData() {
        getOrderData();
        getDaySumData();
    }

    private void getOrderData() {
        mDisposable.add(mViewModel.getRecordWithTypes(mYear, mMonth, mType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recordWithTypes -> mAdapter.setNewData(recordWithTypes),
                        throwable -> {
                            ToastUtils.show(R.string.toast_records_fail);
                            Log.e(TAG, "获取记录列表失败", throwable);
                        }));
    }

    private void getDaySumData() {
        mDisposable.add(mViewModel.getDaySumMoney(mYear, mMonth, mType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setData,
                        throwable -> {
                            ToastUtils.show(R.string.toast_get_statistics_fail);
                            Log.e(TAG, "获取统计数据失败", throwable);
                        }));
    }
}
