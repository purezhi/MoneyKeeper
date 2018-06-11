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

package me.bakumon.moneykeeper.ui.addtype;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.bakumon.moneykeeper.App;
import me.bakumon.moneykeeper.Injection;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.Router;
import me.bakumon.moneykeeper.base.BaseActivity;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.databinding.ActivityAddTypeBinding;
import me.bakumon.moneykeeper.datasource.BackupFailException;
import me.bakumon.moneykeeper.utill.ToastUtils;
import me.bakumon.moneykeeper.viewmodel.ViewModelFactory;

/**
 * 添加或修改记账类型
 *
 * @author Bakumon https://bakumon.me
 */
public class AddTypeActivity extends BaseActivity {

    private static final String TAG = AddTypeActivity.class.getSimpleName();

    private static final int COLUMN = 4;

    private ActivityAddTypeBinding mBinding;
    private AddTypeViewModel mViewModel;
    private TypeImgAdapter mAdapter;

    private int mType;
    private RecordType mRecordType;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_type;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(AddTypeViewModel.class);

        initView();
        initData();
    }

    private void initView() {
        mType = getIntent().getIntExtra(Router.ExtraKey.KEY_TYPE, RecordType.TYPE_OUTLAY);
        mRecordType = (RecordType) getIntent().getSerializableExtra(Router.ExtraKey.KEY_TYPE_BEAN);

        String prefix = mRecordType == null ? getString(R.string.text_add) : getString(R.string.text_modify);
        String type = mType == RecordType.TYPE_OUTLAY ? getString(R.string.text_outlay_type) : getString(R.string.text_income_type);

        mBinding.edtTypeName.setText(mRecordType == null ? "" : mRecordType.name);
        mBinding.edtTypeName.setSelection(mBinding.edtTypeName.getText().length());

        mBinding.titleBar.setTitle(prefix + type);
        mBinding.titleBar.tvRight.setText(R.string.text_save);
        mBinding.titleBar.ibtClose.setOnClickListener(v -> finish());
        mBinding.titleBar.tvRight.setOnClickListener(v -> saveType());

        mBinding.rvType.setLayoutManager(new GridLayoutManager(this, COLUMN));
        mAdapter = new TypeImgAdapter(null);
        mBinding.rvType.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> checkItem(position));
    }

    private void checkItem(int position) {
        mAdapter.checkItem(position);
        int resId = getResources().getIdentifier(mAdapter.getCurrentItem().imgName, "mipmap", getPackageName());
        mBinding.ivType.setImageResource(resId);
    }

    private void initData() {
        getAllTypeImg();
    }

    private void getAllTypeImg() {
        mDisposable.add(mViewModel.getAllTypeImgBeans(mType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((typeImgBeans) -> {
                    mAdapter.setNewData(typeImgBeans);
                    if (mRecordType == null) {
                        checkItem(0);
                    } else {
                        for (int i = 0; i < typeImgBeans.size(); i++) {
                            if (TextUtils.equals(mRecordType.imgName, typeImgBeans.get(i).imgName)) {
                                checkItem(i);
                                return;
                            }
                        }
                    }
                }, throwable -> {
                    ToastUtils.show(R.string.toast_type_img_fail);
                    Log.e(TAG, "类型图片获取失败", throwable);
                }));
    }

    private void saveType() {
        mBinding.titleBar.tvRight.setEnabled(false);
        String text = mBinding.edtTypeName.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            Animation animation = AnimationUtils.loadAnimation(App.getINSTANCE(), R.anim.shake);
            mBinding.edtTypeName.startAnimation(animation);
            mBinding.titleBar.tvRight.setEnabled(true);
            return;
        }
        TypeImgBean bean = mAdapter.getCurrentItem();
        mDisposable.add(mViewModel.saveRecordType(mRecordType, mType, bean.imgName, text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::finish, throwable -> {
                    if (throwable instanceof BackupFailException) {
                        ToastUtils.show(throwable.getMessage());
                        Log.e(TAG, "备份失败（类型保存失败的时候）", throwable);
                        finish();
                    } else {
                        mBinding.titleBar.tvRight.setEnabled(true);
                        String failTip = TextUtils.isEmpty(throwable.getMessage()) ? getString(R.string.toast_type_save_fail) : throwable.getMessage();
                        ToastUtils.show(failTip);
                        Log.e(TAG, "类型保存失败", throwable);
                    }
                }));
    }
}
