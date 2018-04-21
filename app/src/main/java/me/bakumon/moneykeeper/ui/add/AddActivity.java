package me.bakumon.moneykeeper.ui.add;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.hhl.gridpagersnaphelper.GridPagerSnapHelper;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.bakumon.moneykeeper.Injection;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.base.BaseActivity;
import me.bakumon.moneykeeper.database.entity.Record;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.databinding.ActivityAddBinding;
import me.bakumon.moneykeeper.utill.DateUtils;
import me.bakumon.moneykeeper.viewModel.ViewModelFactory;

/**
 * HomeActivity
 *
 * @author bakumon https://bakumon.me
 * @date 2018/4/9
 */
public class AddActivity extends BaseActivity {

    private static final String TAG = AddActivity.class.getSimpleName();
    private static final int ROW = 2;
    private static final int COLUMN = 4;

    private ActivityAddBinding mBinding;

    private AddViewModel mViewModel;

    private TypeAdapter mAdapter;
    private List<RecordType> mRecordTypes;
    private Date mCurrentChooseDate = new Date();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(AddViewModel.class);

        initView();
        initData();
    }

    private void initData() {
        initRecordTypes();
    }

    private void initView() {
        configRecyclerView();
        configCustomKeyboard();
        mBinding.ibtClose.setOnClickListener(v -> finish());
        mBinding.qmTvDate.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    (view, year, monthOfYear, dayOfMonth) -> {

                        mCurrentChooseDate = DateUtils.getAccurateDate(year, monthOfYear + 1, dayOfMonth);


                    }, now);
            dpd.setMaxDate(now);
            dpd.show(getFragmentManager(), "Datepickerdialog");
        });
        mBinding.rgType.setOnCheckedChangeListener((group, checkedId) -> {
            mAdapter.setNewData(mRecordTypes, checkedId == R.id.rb_outlay ? RecordType.TYPE_OUTLAY : RecordType.TYPE_INCOME);
            // 数据改变后需要重绘，否则指示器不能立刻变化
            mBinding.indicator.invalidate();
        });
    }

    /**
     * RecyclerView 配置网格分页、指示器
     */
    private void configRecyclerView() {

        mBinding.rvType.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, ROW, LinearLayoutManager.HORIZONTAL, false);
        mBinding.rvType.setLayoutManager(gridLayoutManager);

        // 网格分页
        GridPagerSnapHelper gridPagerSnapHelper = new GridPagerSnapHelper();
        gridPagerSnapHelper.setRow(ROW).setColumn(COLUMN);
        gridPagerSnapHelper.attachToRecyclerView(mBinding.rvType);

        mAdapter = new TypeAdapter(null, COLUMN);
        mAdapter.setOnItemClickListener((adapter, view, position) -> mAdapter.checkItem(position));
        mBinding.rvType.setAdapter(mAdapter);

        // Note: pageColumn must be config
        mBinding.indicator.setPageColumn(COLUMN);
        // setup indicator
        mBinding.indicator.setRecyclerView(mBinding.rvType);
    }

    /**
     * 配置自定义键盘
     */
    private void configCustomKeyboard() {
        mBinding.customKeyboard.setAffirmClickListener(text -> {

            Record record = new Record();
            record.money = text;
            record.remark = mBinding.edtRemark.getText().toString().trim();

            record.time = mCurrentChooseDate;
            record.recordTypeId = mAdapter.getCurrentItem().id;

            insertRecord(record);
        });
    }

    private void insertRecord(Record record) {
        mDisposable.add(mViewModel.insertRecord(record).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::finish,
                        throwable ->
                                Log.e(TAG, "新增记录失败", throwable)));
    }

    private void initRecordTypes() {
        mDisposable.add(mViewModel.initRecordTypes().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getAllRecordTypes,
                        throwable ->
                                Log.e(TAG, "初始化类型数据失败", throwable)));
    }

    private void getAllRecordTypes() {
        mDisposable.add(mViewModel.getAllRecordTypes().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((recordTypes) -> {
                            mRecordTypes = recordTypes;
                            mBinding.rgType.check(R.id.rb_outlay);
                        }, throwable ->
                                Log.e(TAG, "获取类型数据失败", throwable)
                )
        );
    }
}
