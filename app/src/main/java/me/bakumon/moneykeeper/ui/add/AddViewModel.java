package me.bakumon.moneykeeper.ui.add;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.bakumon.moneykeeper.CreateRecordTypeDataHelper;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.datasource.AppDataSource;

/**
 * 记一笔界面 ViewModel
 *
 * @author Bakumon https://bakumon.me
 */
public class AddViewModel extends ViewModel {
    private static final String TAG = AddViewModel.class.getSimpleName();
    private final AppDataSource mDataSource;

    public AddViewModel(AppDataSource dataSource) {
        mDataSource = dataSource;
    }

    public Flowable<List<RecordType>> getAllRecordTypes() {
        return mDataSource.getAll();
    }

    public Completable initRecordTypes() {
        return Completable.fromAction(() -> {
            // 没有类型数据时，才初始化一些类型
            if (mDataSource.getRecordTypeCount() < 1) {
                mDataSource.insertAll(CreateRecordTypeDataHelper.createRecordTypeData());
                Log.e(TAG, "获取类型数据成功");
            }
        });
    }
}
