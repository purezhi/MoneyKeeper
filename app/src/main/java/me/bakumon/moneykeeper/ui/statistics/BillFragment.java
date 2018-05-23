package me.bakumon.moneykeeper.ui.statistics;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.bakumon.moneykeeper.Injection;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.base.BaseFragment;
import me.bakumon.moneykeeper.databinding.FragmentBillBinding;
import me.bakumon.moneykeeper.ui.home.HomeAdapter;
import me.bakumon.moneykeeper.utill.ToastUtils;
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

        initView();
    }

    private void initView() {
        mBinding.rvRecordBill.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new HomeAdapter(null);
        mBinding.rvRecordBill.setAdapter(mAdapter);
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
    }

    private void getOrderData() {
        mDisposable.add(mViewModel.getRecordWithTypes(mYear, mMonth)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recordWithTypes -> {
                            mAdapter.setNewData(recordWithTypes);
                        },
                        throwable -> {
                            ToastUtils.show(R.string.toast_records_fail);
                            Log.e(TAG, "获取记录列表失败", throwable);
                        }));
    }
}
