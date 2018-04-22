package me.bakumon.moneykeeper.ui.home;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.bakumon.moneykeeper.Injection;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.base.BaseActivity;
import me.bakumon.moneykeeper.database.entity.RecordWithType;
import me.bakumon.moneykeeper.databinding.ActivityHomeBinding;
import me.bakumon.moneykeeper.ui.add.AddActivity;
import me.bakumon.moneykeeper.viewmodel.ViewModelFactory;

/**
 * HomeActivity
 *
 * @author bakumon https://bakumon.me
 * @date 2018/4/9
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final int MAX_ITEM_TIP = 7;
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

        binding.fabHomeRandom.setOnClickListener(this);
        binding.itSetting.setOnClickListener(this);
        binding.itStatistics.setOnClickListener(this);
    }

    private void initData() {

        mDisposable.add(mViewModel.getCurrentMonthRecordWithTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(recordWithTypes -> {
                            setListData(recordWithTypes);
                            Log.e(TAG, "获取记录列表成功");
                        },
                        throwable ->
                                Log.e(TAG, "获取记录列表失败", throwable)));
    }

    private void setListData(List<RecordWithType> recordWithTypes) {
        mAdapter.setNewData(recordWithTypes);
        if (recordWithTypes != null && recordWithTypes.size() > MAX_ITEM_TIP && mAdapter.getFooterLayoutCount() == 0) {
            View view = getLayoutInflater().inflate(R.layout.layout_footer_tip, null, false);
            mAdapter.setFooterView(view);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.it_setting:
                Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show();
                break;
            case R.id.it_statistics:
                Toast.makeText(this, "统计", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab_home_random:
                Intent intent = new Intent();
                intent.setClass(this, AddActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
