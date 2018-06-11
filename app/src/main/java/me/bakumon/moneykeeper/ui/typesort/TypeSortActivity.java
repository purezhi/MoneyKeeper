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

package me.bakumon.moneykeeper.ui.typesort;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.bakumon.moneykeeper.Injection;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.Router;
import me.bakumon.moneykeeper.base.BaseActivity;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.databinding.ActivityTypeSortBinding;
import me.bakumon.moneykeeper.datasource.BackupFailException;
import me.bakumon.moneykeeper.utill.ToastUtils;
import me.bakumon.moneykeeper.viewmodel.ViewModelFactory;

/**
 * 类型排序
 *
 * @author bakumon https://bakumon.me
 * @date 2018/5/10
 */
public class TypeSortActivity extends BaseActivity {

    private static final String TAG = TypeSortActivity.class.getSimpleName();

    private static final int COLUMN = 4;

    private ActivityTypeSortBinding mBinding;
    private TypeSortViewModel mViewModel;
    private TypeSortAdapter mAdapter;
    private int mType;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_type_sort;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(TypeSortViewModel.class);

        initView();
        initData();
    }

    private void initView() {
        mType = getIntent().getIntExtra(Router.ExtraKey.KEY_TYPE, RecordType.TYPE_OUTLAY);

        mBinding.titleBar.ibtClose.setOnClickListener(v -> finish());
        mBinding.titleBar.setTitle(getString(R.string.text_title_drag_sort));
        mBinding.titleBar.tvRight.setText(R.string.text_done);
        mBinding.titleBar.tvRight.setOnClickListener(v -> sortRecordTypes());

        mBinding.rvType.setLayoutManager(new GridLayoutManager(this, COLUMN));
        mAdapter = new TypeSortAdapter(null);
        mBinding.rvType.setAdapter(mAdapter);

        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(mBinding.rvType);

        // open drag
        mAdapter.enableDragItem(itemTouchHelper);
    }

    private void sortRecordTypes() {
        mBinding.titleBar.tvRight.setEnabled(false);
        mDisposable.add(mViewModel.sortRecordTypes(mAdapter.getData()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::finish, throwable -> {
                    if (throwable instanceof BackupFailException) {
                        ToastUtils.show(throwable.getMessage());
                        Log.e(TAG, "备份失败（类型排序失败的时候）", throwable);
                        finish();
                    } else {
                        mBinding.titleBar.tvRight.setEnabled(true);
                        ToastUtils.show(R.string.toast_sort_fail);
                        Log.e(TAG, "类型排序失败", throwable);
                    }
                }));
    }

    private void initData() {
        mDisposable.add(mViewModel.getRecordTypes(mType).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((recordTypes) -> mAdapter.setNewData(recordTypes),
                        throwable -> {
                            ToastUtils.show(R.string.toast_get_types_fail);
                            Log.e(TAG, "获取类型数据失败", throwable);
                        }));
    }
}
