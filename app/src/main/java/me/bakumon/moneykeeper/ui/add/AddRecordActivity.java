package me.bakumon.moneykeeper.ui.add;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.gcssloop.widget.PagerGridLayoutManager;
import com.gcssloop.widget.PagerGridSnapHelper;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.math.BigDecimal;
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
import me.bakumon.moneykeeper.databinding.ActivityAddRecordBinding;
import me.bakumon.moneykeeper.utill.DateUtils;
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
    private static final int ROW = 2;
    private static final int COLUMN = 4;

    private ActivityAddRecordBinding mBinding;

    private AddRecordViewModel mViewModel;

    private TypeAdapter mAdapter;
    private List<RecordType> mRecordTypes;
    private Date mCurrentChooseDate = DateUtils.getTodayDate();
    private Calendar mCurrentChooseCalendar = Calendar.getInstance();
    private int mCurrentType;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_record;
    }

    @Override
    protected void onInit(@Nullable Bundle savedInstanceState) {
        mBinding = getDataBinding();
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(AddRecordViewModel.class);

        initView();
        initData();
    }

    private void initData() {
        initRecordTypes();
    }

    private void initView() {
        mCurrentType = RecordType.TYPE_OUTLAY;

        mBinding.titleBar.ibtClose.setBackgroundResource(R.drawable.ic_close);
        mBinding.titleBar.ibtClose.setOnClickListener(v -> finish());
        mBinding.titleBar.setTitle(getString(R.string.text_add_record));

        configRecyclerView();
        configCustomKeyboard();

        mBinding.qmTvDate.setOnClickListener(v -> {
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    (view, year, monthOfYear, dayOfMonth) -> {
                        mCurrentChooseDate = DateUtils.getDate(year, monthOfYear + 1, dayOfMonth);
                        mCurrentChooseCalendar.setTime(mCurrentChooseDate);
                        mBinding.qmTvDate.setText(DateUtils.getWordTime(mCurrentChooseDate));
                    }, mCurrentChooseCalendar);
            dpd.setMaxDate(Calendar.getInstance());
            dpd.show(getFragmentManager(), "Datepickerdialog");
        });
        mBinding.typeChoice.rgType.setOnCheckedChangeListener((group, checkedId) -> {
            mCurrentType = checkedId == R.id.rb_outlay ? RecordType.TYPE_OUTLAY : RecordType.TYPE_INCOME;
            mAdapter.setNewData(mRecordTypes, mCurrentType);
        });
    }

    /**
     * RecyclerView 配置网格分页、指示器
     */
    private void configRecyclerView() {
        // 1.水平分页布局管理器
        PagerGridLayoutManager layoutManager = new PagerGridLayoutManager(
                ROW, COLUMN, PagerGridLayoutManager.HORIZONTAL);
        mBinding.rvType.setLayoutManager(layoutManager);

        // 2.设置滚动辅助工具
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(mBinding.rvType);

        mAdapter = new TypeAdapter(null);
        mBinding.rvType.setAdapter(mAdapter);
    }

    /**
     * 配置自定义键盘
     */
    private void configCustomKeyboard() {
        mBinding.customKeyboard.setAffirmClickListener(this::insertRecord);
    }

    private void insertRecord(String text) {
        // 防止重复提交
        mBinding.customKeyboard.setAffirmEnable(false);
        Record record = new Record();
        record.money = new BigDecimal(text);
        record.remark = mBinding.edtRemark.getText().toString().trim();
        record.time = mCurrentChooseDate;
        record.createTime = new Date();
        record.recordTypeId = mAdapter.getCurrentItem().id;

        mDisposable.add(mViewModel.insertRecord(record)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::finish,
                        throwable -> {
                            Log.e(TAG, "新增记录失败", throwable);
                            mBinding.customKeyboard.setAffirmEnable(true);
                            ToastUtils.show(R.string.toast_add_record_fail);
                        }
                ));
    }

    private void initRecordTypes() {
        mDisposable.add(mViewModel.initRecordTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getAllRecordTypes,
                        throwable ->
                                Log.e(TAG, "初始化类型数据失败", throwable)));
    }

    private void getAllRecordTypes() {
        mDisposable.add(mViewModel.getAllRecordTypes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((recordTypes) -> {
                            mRecordTypes = recordTypes;
                            int id = mCurrentType == RecordType.TYPE_OUTLAY ? R.id.rb_outlay : R.id.rb_income;
                            mBinding.typeChoice.rgType.clearCheck();
                            mBinding.typeChoice.rgType.check(id);
                        }, throwable ->
                                Log.e(TAG, "获取类型数据失败", throwable)
                )
        );
    }
}
