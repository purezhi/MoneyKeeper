package me.bakumon.moneykeeper.datasource;

import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import me.bakumon.moneykeeper.database.AppDatabase;
import me.bakumon.moneykeeper.database.entity.Record;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.database.entity.RecordWithType;
import me.bakumon.moneykeeper.utill.DateUtils;

/**
 * 数据源本地实现类
 *
 * @author Bakumon https://bakumon.me
 */
public class LocalAppDataSource implements AppDataSource {
    private final AppDatabase mAppDatabase;

    public LocalAppDataSource(AppDatabase appDatabase) {
        mAppDatabase = appDatabase;
    }

    @Override
    public Flowable<List<RecordType>> getAllRecordType() {
        return mAppDatabase.recordTypeDao().getAll();
    }

    @Override
    public long getRecordTypeCount() {
        return mAppDatabase.recordTypeDao().getRecordTypeCount();
    }

    @Override
    public void insertAllRecordType(RecordType... recordTypes) {
        mAppDatabase.recordTypeDao().insertAll(recordTypes);
    }

    @Override
    public void deleteRecordType(RecordType recordType) {
        mAppDatabase.recordTypeDao().delete(recordType);
    }

    @Override
    public Flowable<List<RecordWithType>> getCurrentMonthRecordWithTypes() {
        Date dateFrom = DateUtils.getCurrentMonthStart();
        Date dateTo = DateUtils.getCurrentMonthEnd();
        return mAppDatabase.recordDao().getRangeRecordWithTypes(dateFrom, dateTo);
    }

    @Override
    public void insertRecord(Record record) {
        mAppDatabase.recordDao().insert(record);
    }
}
