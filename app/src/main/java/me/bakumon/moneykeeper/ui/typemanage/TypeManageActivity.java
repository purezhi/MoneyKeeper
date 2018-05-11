package me.bakumon.moneykeeper.ui.typemanage;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.bakumon.moneykeeper.Injection;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.base.BaseActivity;
import me.bakumon.moneykeeper.databinding.ActivityTypeManageBinding;
import me.bakumon.moneykeeper.ui.typesort.TypeSortActivity;
import me.bakumon.moneykeeper.viewmodel.ViewModelFactory;

/**
 * 类型管理
 *
 * @author bakumon https://bakumon.me
 * @date 2018/5/3
 */
public class TypeManageActivity extends BaseActivity {

    private static final String TAG = TypeManageActivity.class.getSimpleName();

    private ActivityTypeManageBinding mBinding;
    private TypeManageViewModel mViewModel;
    private TypeManageAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_type_manage;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(TypeManageViewModel.class);

        initView();
        initData();
    }

    private void initView() {
        mBinding.title.ibtClose.setOnClickListener(v -> finish());
        mBinding.title.tvTitle.setText(R.string.text_title_type_manage);
        mBinding.title.tvRight.setText(R.string.text_button_sort);
        mBinding.title.tvRight.setOnClickListener(v -> startActivity(new Intent(this, TypeSortActivity.class)));

        mBinding.rvType.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TypeManageAdapter(null);
        mBinding.rvType.setAdapter(mAdapter);

    }

    private void initData() {
        mDisposable.add(mViewModel.getAllRecordTypes().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((recordTypes) -> {
                            mAdapter.setNewData(recordTypes);
                        }, throwable ->
                                Log.e(TAG, "获取类型数据失败", throwable)
                )
        );
    }
}
