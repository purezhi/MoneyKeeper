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

package me.bakumon.moneykeeper.ui.typemanage;

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
import me.bakumon.moneykeeper.databinding.ActivityTypeManageBinding;
import me.bakumon.moneykeeper.datasource.BackupFailException;
import me.bakumon.moneykeeper.utill.ToastUtils;
import me.bakumon.moneykeeper.viewmodel.ViewModelFactory;
import me.drakeet.floo.Floo;

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
    private List<RecordType> mRecordTypes;

    private int mCurrentType;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_type_manage;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(TypeManageViewModel.class);

        initView();
        initData();
    }

    private void initView() {
        mCurrentType = getIntent().getIntExtra(Router.ExtraKey.KEY_TYPE, RecordType.TYPE_OUTLAY);
        mBinding.titleBar.tvRight.setText(R.string.text_button_sort);
        mBinding.titleBar.ibtClose.setOnClickListener(v -> finish());
        mBinding.titleBar.setTitle(getString(R.string.text_title_type_manage));
        mBinding.titleBar.tvRight.setOnClickListener(v ->
                Floo.navigation(this, Router.Url.URL_TYPE_SORT)
                        .putExtra(Router.ExtraKey.KEY_TYPE, mCurrentType)
                        .start());

        mBinding.rvType.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TypeManageAdapter(null);
        mBinding.rvType.setAdapter(mAdapter);

        mAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            if (adapter.getData().size() > 1) {
                showDeleteDialog(mAdapter.getData().get(position));
            } else {
                ToastUtils.show(R.string.toast_least_one_type);
            }
            return true;
        });

        mAdapter.setOnItemClickListener((adapter, view, position) ->
                Floo.navigation(this, Router.Url.URL_ADD_TYPE)
                        .putExtra(Router.ExtraKey.KEY_TYPE_BEAN, mAdapter.getItem(position))
                        .putExtra(Router.ExtraKey.KEY_TYPE, mCurrentType)
                        .start());

        mBinding.typeChoice.rgType.setOnCheckedChangeListener((group, checkedId) -> {
            mCurrentType = checkedId == R.id.rb_outlay ? RecordType.TYPE_OUTLAY : RecordType.TYPE_INCOME;
            mAdapter.setNewData(mRecordTypes, mCurrentType);
            int visibility = mAdapter.getData().size() > 1 ? View.VISIBLE : View.INVISIBLE;
            mBinding.titleBar.tvRight.setVisibility(visibility);
        });

    }

    private void showDeleteDialog(RecordType recordType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.text_dialog_delete) + recordType.name)
                .setMessage(R.string.text_delete_type_note)
                .setNegativeButton(R.string.text_button_cancel, null)
                .setPositiveButton(R.string.text_button_affirm_delete, (dialog, which) -> deleteType(recordType))
                .create()
                .show();
    }

    public void addType(View view) {
        Floo.navigation(this, Router.Url.URL_ADD_TYPE)
                .putExtra(Router.ExtraKey.KEY_TYPE, mCurrentType)
                .start();
    }

    private void deleteType(RecordType recordType) {
        mDisposable.add(mViewModel.deleteRecordType(recordType).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                        },
                        throwable -> {
                            if (throwable instanceof BackupFailException) {
                                ToastUtils.show(throwable.getMessage());
                                Log.e(TAG, "备份失败（类型删除失败的时候）", throwable);
                            } else {
                                ToastUtils.show(R.string.toast_delete_fail);
                                Log.e(TAG, "类型删除失败", throwable);
                            }
                        }
                ));
    }

    private void initData() {
        mDisposable.add(mViewModel.getAllRecordTypes().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((recordTypes) -> {
                            mRecordTypes = recordTypes;
                            int id = mCurrentType == RecordType.TYPE_OUTLAY ? R.id.rb_outlay : R.id.rb_income;
                            mBinding.typeChoice.rgType.clearCheck();
                            mBinding.typeChoice.rgType.check(id);
                        },
                        throwable -> {
                            ToastUtils.show(R.string.toast_get_types_fail);
                            Log.e(TAG, "获取类型数据失败", throwable);
                        }));
    }
}
