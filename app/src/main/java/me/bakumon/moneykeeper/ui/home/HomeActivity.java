package me.bakumon.moneykeeper.ui.home;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.bakumon.moneykeeper.Injection;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.base.BaseActivity;
import me.bakumon.moneykeeper.database.entity.RecordWithType;
import me.bakumon.moneykeeper.databinding.ActivityHomeBinding;
import me.bakumon.moneykeeper.ui.add.AddActivity;
import me.bakumon.moneykeeper.utill.ToastUtils;
import me.bakumon.moneykeeper.viewmodel.ViewModelFactory;

/**
 * HomeActivity
 *
 * @author bakumon https://bakumon.me
 * @date 2018/4/9
 */
public class HomeActivity extends BaseActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final int MAX_ITEM_TIP = 6;
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
    }

    private void initData() {

        mDisposable.add(mViewModel.getCurrentMonthRecordWithTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recordWithTypes -> {
                            if (recordWithTypes == null || recordWithTypes.size() < 1) {
                                setEmptyView();
                            } else {
                                setListData(recordWithTypes);
                            }
                        },
                        throwable ->
                                Log.e(TAG, "获取记录列表失败", throwable)));
    }

    private void setListData(List<RecordWithType> recordWithTypes) {
        mAdapter.setNewData(recordWithTypes);
        if (recordWithTypes != null
                && recordWithTypes.size() > MAX_ITEM_TIP
                && mAdapter.getFooterLayoutCount() == 0) {
            mAdapter.setFooterView(inflate(R.layout.layout_footer_tip));
        }
    }

    private void setEmptyView() {
        mAdapter.setEmptyView(inflate(R.layout.layout_home_empty));
    }

    public void settingClick(View view) {
        ToastUtils.show("设置");
    }

    public void statisticsClick(View view) {
        ToastUtils.show("统计");
    }

    public void addClick(View view) {
        Intent intent = new Intent();
        intent.setClass(this, AddActivity.class);
        startActivity(intent);
    }

}
