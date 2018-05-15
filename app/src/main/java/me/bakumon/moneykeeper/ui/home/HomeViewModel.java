package me.bakumon.moneykeeper.ui.home;

import android.arch.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.database.entity.RecordWithType;
import me.bakumon.moneykeeper.datasource.AppDataSource;

/**
 * 主页 ViewModel
 *
 * @author Bakumon https://bakumon.me
 */
public class HomeViewModel extends ViewModel {
    private static final String TAG = HomeViewModel.class.getSimpleName();
    private final AppDataSource mDataSource;

    public HomeViewModel(AppDataSource dataSource) {
        mDataSource = dataSource;
    }

    public Flowable<List<RecordWithType>> getCurrentMonthRecordWithTypes() {
        return mDataSource.getCurrentMonthRecordWithTypes();
    }

    public Completable deleteRecord(RecordWithType record) {
        return Completable.fromAction(() -> mDataSource.deleteRecord(record));
    }
}
