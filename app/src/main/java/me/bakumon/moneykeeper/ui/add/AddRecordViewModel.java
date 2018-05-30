package me.bakumon.moneykeeper.ui.add;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.bakumon.moneykeeper.base.BaseViewModel;
import me.bakumon.moneykeeper.database.entity.Record;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.datasource.AppDataSource;

/**
 * 记一笔界面 ViewModel
 *
 * @author Bakumon https://bakumon.me
 */
public class AddRecordViewModel extends BaseViewModel {
    public AddRecordViewModel(AppDataSource dataSource) {
        super(dataSource);
    }

    public Flowable<List<RecordType>> getAllRecordTypes() {
        return mDataSource.getAllRecordType();
    }

    public Completable insertRecord(Record record) {
        return mDataSource.insertRecord(record);
    }

    public Completable updateRecord(Record record) {
        return mDataSource.updateRecord(record);
    }
}
