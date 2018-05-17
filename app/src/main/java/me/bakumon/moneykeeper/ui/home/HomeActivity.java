package me.bakumon.moneykeeper.ui.home;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.bakumon.moneykeeper.Injection;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.Router;
import me.bakumon.moneykeeper.base.BaseActivity;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.database.entity.RecordWithType;
import me.bakumon.moneykeeper.database.entity.SumMoneyBean;
import me.bakumon.moneykeeper.databinding.ActivityHomeBinding;
import me.bakumon.moneykeeper.utill.ToastUtils;
import me.bakumon.moneykeeper.viewmodel.ViewModelFactory;
import me.drakeet.floo.Floo;

/**
 * HomeActivity
 *
 * @author bakumon https://bakumon.me
 * @date 2018/4/9
 */
public class HomeActivity extends BaseActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final int MAX_ITEM_TIP = 5;
    private ActivityHomeBinding binding;

    private HomeViewModel mViewModel;
    private HomeAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        binding = getDataBinding();
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel.class);

        initView();
        initData();
    }

    private void initView() {
        binding.rvHome.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new HomeAdapter(null);
        binding.rvHome.setAdapter(mAdapter);

        mAdapter.setOnItemChildLongClickListener((adapter, view, position) -> {
            showOperateDialog(mAdapter.getData().get(position));
            return false;
        });
    }

    public void settingClick(View view) {
        ToastUtils.show("设置");
    }

    public void statisticsClick(View view) {
        ToastUtils.show("统计");
    }

    public void addRecordClick(View view) {
        Floo.navigation(this, Router.ADD_RECORD).start();
    }

    private void showOperateDialog(RecordWithType record) {
        new AlertDialog.Builder(this)
                .setItems(new String[]{"修改", "删除"}, (dialog, which) -> {
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
        ToastUtils.show("修改" + record.mRecordTypes.get(0).name);
    }

    private void deleteRecord(RecordWithType record) {
        mDisposable.add(mViewModel.deleteRecord(record)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                        },
                        throwable -> {
                            ToastUtils.show("删除失败");
                            Log.e(TAG, "删除记账记录失败", throwable);
                        }));
    }

    private void initData() {
        getCurrentMonthRecords();
        getCurrentMontySumMonty();
    }

    private void getCurrentMontySumMonty() {
        mDisposable.add(mViewModel.getCurrentMonthSumMoney()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sumMoneyBean -> {
                            String outlay = "0";
                            String inCome = "0";
                            if (sumMoneyBean != null && sumMoneyBean.size() > 0) {
                                for (SumMoneyBean bean : sumMoneyBean) {
                                    if (bean.type == RecordType.TYPE_OUTLAY) {
                                        outlay = bean.sumMoney;
                                    } else if (bean.type == RecordType.TYPE_INCOME) {
                                        inCome = bean.sumMoney;
                                    }
                                }
                            }
                            binding.tvMonthOutlay.setText(outlay);
                            binding.tvMonthIncome.setText(inCome);
                        },
                        throwable ->
                                Log.e(TAG, "获取记录列表失败", throwable)));
    }

    private void getCurrentMonthRecords() {
        mDisposable.add(mViewModel.getCurrentMonthRecordWithTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recordWithTypes -> {
                            setListData(recordWithTypes);
                            if (recordWithTypes == null || recordWithTypes.size() < 1) {
                                setEmptyView();
                            }
                        },
                        throwable ->
                                Log.e(TAG, "获取记录列表失败", throwable)));
    }

    private void setListData(List<RecordWithType> recordWithTypes) {
        mAdapter.setNewData(recordWithTypes);
        boolean isShowFooter = recordWithTypes != null
                && recordWithTypes.size() > MAX_ITEM_TIP;
        if (isShowFooter) {
            mAdapter.setFooterView(inflate(R.layout.layout_footer_tip));
        } else {
            mAdapter.removeAllFooterView();
        }
    }

    private void setEmptyView() {
        mAdapter.setEmptyView(inflate(R.layout.layout_home_empty));
    }

}
