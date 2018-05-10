package me.bakumon.moneykeeper.ui.add;

import android.arch.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.bakumon.moneykeeper.database.entity.Record;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.datasource.AppDataSource;

/**
 * 记一笔界面 ViewModel
 *
 * @author Bakumon https://bakumon.me
 */
public class AddRecordViewModel extends ViewModel {
    private final AppDataSource mDataSource;

    public AddRecordViewModel(AppDataSource dataSource) {
        mDataSource = dataSource;
    }

    public Flowable<List<RecordType>> getAllRecordTypes() {
        return mDataSource.getAllRecordType();
    }

    public Completable initRecordTypes() {
        return Completable.fromAction(mDataSource::initRecordTypes);
    }

    public Completable insertRecord(Record record) {
        return Completable.fromAction(() -> mDataSource.insertRecord(record));
    }
}
