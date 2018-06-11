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

package me.bakumon.moneykeeper.ui.home;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.bakumon.moneykeeper.ConfigManager;
import me.bakumon.moneykeeper.Injection;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.Router;
import me.bakumon.moneykeeper.base.BaseActivity;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.database.entity.RecordWithType;
import me.bakumon.moneykeeper.database.entity.SumMoneyBean;
import me.bakumon.moneykeeper.databinding.ActivityHomeBinding;
import me.bakumon.moneykeeper.datasource.BackupFailException;
import me.bakumon.moneykeeper.utill.BigDecimalUtil;
import me.bakumon.moneykeeper.utill.ToastUtils;
import me.bakumon.moneykeeper.viewmodel.ViewModelFactory;
import me.drakeet.floo.Floo;
import me.drakeet.floo.StackCallback;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

/**
 * HomeActivity
 *
 * @author bakumon https://bakumon.me
 * @date 2018/4/9
 */
public class HomeActivity extends BaseActivity implements StackCallback, EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final int MAX_ITEM_TIP = 5;
    private ActivityHomeBinding mBinding;

    private HomeViewModel mViewModel;
    private HomeAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel.class);

        initView();
        initData();
        checkPermissionForBackup();
    }

    private void initView() {
        mBinding.rvHome.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new HomeAdapter(null);
        mBinding.rvHome.setAdapter(mAdapter);

        mAdapter.setOnItemChildLongClickListener((adapter, view, position) -> {
            showOperateDialog(mAdapter.getData().get(position));
            return false;
        });
    }

    public void settingClick(View view) {
        Floo.navigation(this, Router.Url.URL_SETTING)
                .start();
    }

    public void statisticsClick(View view) {
        Floo.navigation(this, Router.Url.URL_STATISTICS)
                .start();
    }

    public void addRecordClick(View view) {
        Floo.navigation(this, Router.Url.URL_ADD_RECORD).start();
    }

    private void showOperateDialog(RecordWithType record) {
        new AlertDialog.Builder(this)
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
        Floo.navigation(this, Router.Url.URL_ADD_RECORD)
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

    private void initData() {
        initRecordTypes();
        getCurrentMonthRecords();
        getCurrentMontySumMonty();
    }

    private void initRecordTypes() {
        mDisposable.add(mViewModel.initRecordTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                        },
                        throwable -> {
                            if (throwable instanceof BackupFailException) {
                                ToastUtils.show(throwable.getMessage());
                                Log.e(TAG, "备份失败（初始化类型数据失败的时候）", throwable);
                            } else {
                                ToastUtils.show(R.string.toast_init_types_fail);
                                Log.e(TAG, "初始化类型数据失败", throwable);
                            }
                        }));
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
                                        outlay = BigDecimalUtil.fen2Yuan(bean.sumMoney);
                                    } else if (bean.type == RecordType.TYPE_INCOME) {
                                        inCome = BigDecimalUtil.fen2Yuan(bean.sumMoney);
                                    }
                                }
                            }
                            mBinding.tvMonthOutlay.setText(outlay);
                            mBinding.tvMonthIncome.setText(inCome);
                        },
                        throwable -> {
                            ToastUtils.show(R.string.toast_current_sum_money_fail);
                            Log.e(TAG, "本月支出收入总数获取失败", throwable);
                        }));
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
                        throwable -> {
                            ToastUtils.show(R.string.toast_records_fail);
                            Log.e(TAG, "获取记录列表失败", throwable);
                        }));
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

    @Nullable
    @Override
    public String indexKeyForStackTarget() {
        return Router.IndexKey.INDEX_KEY_HOME;
    }

    @Override
    public void onReceivedResult(@Nullable Object result) {
        initData();
    }

    ///////////////////////////////
    //// 自动备份打开时，检查是否有权限
    ///////////////////////////////

    private static final int REQUEST_CODE_STORAGE = 11;
    private boolean isUserFirst;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void checkPermissionForBackup() {
        if (!ConfigManager.isAutoBackup()) {
            return;
        }
        if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            return;
        }
        // 当自动备份打开，并且没有存储权限，提示用户需要申请权限
        EasyPermissions.requestPermissions(
                new PermissionRequest.Builder(this, REQUEST_CODE_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .setRationale(R.string.text_storage_content)
                        .setPositiveButtonText(R.string.text_affirm)
                        .setNegativeButtonText(R.string.text_button_cancel)
                        .build());
    }

    public void updateConfig(boolean isAutoBackup) {
        if (isAutoBackup) {
            ConfigManager.setIsAutoBackup(true);
        } else {
            if (ConfigManager.setIsAutoBackup(false)) {
                ToastUtils.show(R.string.toast_open_auto_backup);
            }
        }
    }

    @Override
    public void onRationaleAccepted(int requestCode) {
        isUserFirst = true;
    }

    @Override
    public void onRationaleDenied(int requestCode) {
        isUserFirst = true;
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        updateConfig(true);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            if (!isUserFirst) {
                new AppSettingsDialog.Builder(this)
                        .setRationale(R.string.text_storage_permission_tip)
                        .setTitle(R.string.text_storage)
                        .setPositiveButton(R.string.text_affirm)
                        .setNegativeButton(R.string.text_button_cancel)
                        .build()
                        .show();
            }
        } else {
            updateConfig(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                updateConfig(true);
            } else {
                updateConfig(false);
            }
        }
    }

}
