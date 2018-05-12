package me.bakumon.moneykeeper.ui.typemanage;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.bakumon.moneykeeper.Injection;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.base.BaseActivity;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.databinding.ActivityTypeManageBinding;
import me.bakumon.moneykeeper.ui.typesort.TypeSortActivity;
import me.bakumon.moneykeeper.utill.ToastUtils;
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
    private List<RecordType> mRecordTypes;

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
        mBinding.titleBar.ibtClose.setOnClickListener(v -> finish());
        mBinding.titleBar.setTitle(getString(R.string.text_title_type_manage));
        mBinding.titleBar.setRightText(getString(R.string.text_button_sort));
        mBinding.titleBar.tvRight.setOnClickListener(v -> startActivity(new Intent(this, TypeSortActivity.class)));

        mBinding.rvType.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TypeManageAdapter(null);
        mBinding.rvType.setAdapter(mAdapter);

        mAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            showDeleteDialog(mAdapter.getData().get(position).name);
            return false;
        });

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            ToastUtils.show("点击了" + position);
        });

        mBinding.typeChoice.rgType.setOnCheckedChangeListener((group, checkedId) -> {
            mAdapter.setNewData(mRecordTypes, checkedId == R.id.rb_outlay ? RecordType.TYPE_OUTLAY : RecordType.TYPE_INCOME);
        });

    }

    private void showDeleteDialog(String typeName){
        String msg = "删除 " + typeName + " 分类后，将无法在记账页选择该分类，该分类下原有账单仍保持不变";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除分类");
        builder.setMessage(msg);
        builder.setNegativeButton(R.string.text_button_cancel, null);

        builder.setPositiveButton(R.string.text_button_affirm_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.create();
        builder.show();
    }

    private void initData() {
        mDisposable.add(mViewModel.getAllRecordTypes().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((recordTypes) ->
                                mRecordTypes = recordTypes,
                        throwable ->
                                Log.e(TAG, "获取类型数据失败", throwable)
                )
        );
    }
}
