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

package me.bakumon.moneykeeper.ui.add;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.bakumon.moneykeeper.Injection;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.Router;
import me.bakumon.moneykeeper.base.BaseActivity;
import me.bakumon.moneykeeper.database.entity.Record;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.database.entity.RecordWithType;
import me.bakumon.moneykeeper.databinding.ActivityAddRecordBinding;
import me.bakumon.moneykeeper.datasource.BackupFailException;
import me.bakumon.moneykeeper.utill.BigDecimalUtil;
import me.bakumon.moneykeeper.utill.DateUtils;
import me.bakumon.moneykeeper.utill.SoftInputUtils;
import me.bakumon.moneykeeper.utill.ToastUtils;
import me.bakumon.moneykeeper.viewmodel.ViewModelFactory;

/**
 * HomeActivity
 *
 * @author bakumon https://bakumon.me
 * @date 2018/4/9
 */
public class AddRecordActivity extends BaseActivity {

    private static final String TAG = AddRecordActivity.class.getSimpleName();
    private static final String TAG_PICKER_DIALOG = "Datepickerdialog";

    private ActivityAddRecordBinding mBinding;

    private AddRecordViewModel mViewModel;

    private Date mCurrentChooseDate = DateUtils.getTodayDate();
    private Calendar mCurrentChooseCalendar = Calendar.getInstance();
    private int mCurrentType;

    private RecordWithType mRecord;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_record;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(AddRecordViewModel.class);

        initView();
        initData();
    }

    private void initData() {
        getAllRecordTypes();
    }

    private void initView() {
        mRecord = (RecordWithType) getIntent().getSerializableExtra(Router.ExtraKey.KEY_RECORD_BEAN);

        mBinding.titleBar.ibtClose.setBackgroundResource(R.drawable.ic_close);
        mBinding.titleBar.ibtClose.setOnClickListener(v -> finish());

        mBinding.edtRemark.setOnEditorActionListener((v, actionId, event) -> {
            SoftInputUtils.hideSoftInput(mBinding.typePageOutlay);
            mBinding.keyboard.setEditTextFocus();
            return false;
        });

        if (mRecord == null) {
            mCurrentType = RecordType.TYPE_OUTLAY;
            mBinding.titleBar.setTitle(getString(R.string.text_add_record));
        } else {
            mCurrentType = mRecord.mRecordTypes.get(0).type;
            mBinding.titleBar.setTitle(getString(R.string.text_modify_record));
            mBinding.edtRemark.setText(mRecord.remark);
            mBinding.keyboard.setText(BigDecimalUtil.fen2Yuan(mRecord.money));
            mCurrentChooseDate = mRecord.time;
            mCurrentChooseCalendar.setTime(mCurrentChooseDate);
            mBinding.qmTvDate.setText(DateUtils.getWordTime(mCurrentChooseDate));
        }

        mBinding.keyboard.setAffirmClickListener(text -> {
            if (mRecord == null) {
                insertRecord(text);
            } else {
                modifyRecord(text);
            }
        });

        mBinding.qmTvDate.setOnClickListener(v -> {
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    (view, year, monthOfYear, dayOfMonth) -> {
                        mCurrentChooseDate = DateUtils.getDate(year, monthOfYear + 1, dayOfMonth);
                        mCurrentChooseCalendar.setTime(mCurrentChooseDate);
                        mBinding.qmTvDate.setText(DateUtils.getWordTime(mCurrentChooseDate));
                    }, mCurrentChooseCalendar);
            dpd.setMaxDate(Calendar.getInstance());
            dpd.show(getFragmentManager(), TAG_PICKER_DIALOG);
        });
        mBinding.typeChoice.rgType.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.rb_outlay) {
                mCurrentType = RecordType.TYPE_OUTLAY;
                mBinding.typePageOutlay.setVisibility(View.VISIBLE);
                mBinding.typePageIncome.setVisibility(View.GONE);
            } else {
                mCurrentType = RecordType.TYPE_INCOME;
                mBinding.typePageOutlay.setVisibility(View.GONE);
                mBinding.typePageIncome.setVisibility(View.VISIBLE);
            }

        });
    }

    private void insertRecord(String text) {
        // 防止重复提交
        mBinding.keyboard.setAffirmEnable(false);
        Record record = new Record();
        record.money = BigDecimalUtil.yuan2FenBD(text);
        record.remark = mBinding.edtRemark.getText().toString().trim();
        record.time = mCurrentChooseDate;
        record.createTime = new Date();
        record.recordTypeId = mCurrentType == RecordType.TYPE_OUTLAY ?
                mBinding.typePageOutlay.getCurrentItem().id : mBinding.typePageIncome.getCurrentItem().id;

        mDisposable.add(mViewModel.insertRecord(record)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::finish,
                        throwable -> {
                            if (throwable instanceof BackupFailException) {
                                ToastUtils.show(throwable.getMessage());
                                Log.e(TAG, "备份失败（新增记录失败的时候）", throwable);
                                finish();
                            } else {
                                Log.e(TAG, "新增记录失败", throwable);
                                mBinding.keyboard.setAffirmEnable(true);
                                ToastUtils.show(R.string.toast_add_record_fail);
                            }
                        }
                ));
    }

    private void modifyRecord(String text) {
        // 防止重复提交
        mBinding.keyboard.setAffirmEnable(false);
        mRecord.money = BigDecimalUtil.yuan2FenBD(text);
        mRecord.remark = mBinding.edtRemark.getText().toString().trim();
        mRecord.time = mCurrentChooseDate;
        mRecord.recordTypeId = mCurrentType == RecordType.TYPE_OUTLAY ?
                mBinding.typePageOutlay.getCurrentItem().id : mBinding.typePageIncome.getCurrentItem().id;

        mDisposable.add(mViewModel.updateRecord(mRecord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::finish,
                        throwable -> {
                            if (throwable instanceof BackupFailException) {
                                ToastUtils.show(throwable.getMessage());
                                Log.e(TAG, "备份失败（记录修改失败的时候）", throwable);
                                finish();
                            } else {
                                Log.e(TAG, "记录修改失败", throwable);
                                mBinding.keyboard.setAffirmEnable(true);
                                ToastUtils.show(R.string.toast_modify_record_fail);
                            }
                        }
                ));
    }

    private void getAllRecordTypes() {
        mDisposable.add(mViewModel.getAllRecordTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((recordTypes) -> {
                    mBinding.typePageOutlay.setNewData(recordTypes, RecordType.TYPE_OUTLAY);
                    mBinding.typePageIncome.setNewData(recordTypes, RecordType.TYPE_INCOME);

                    if (mCurrentType == RecordType.TYPE_OUTLAY) {
                        mBinding.typeChoice.rgType.check(R.id.rb_outlay);
                        mBinding.typePageOutlay.initCheckItem(mRecord);
                    } else {
                        mBinding.typeChoice.rgType.check(R.id.rb_income);
                        mBinding.typePageIncome.initCheckItem(mRecord);
                    }

                }, throwable -> {
                    ToastUtils.show(R.string.toast_get_types_fail);
                    Log.e(TAG, "获取类型数据失败", throwable);
                }));
    }
}
