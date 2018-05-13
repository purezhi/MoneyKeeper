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
        return mAppDatabase.recordTypeDao().getAllRecordTypes();
    }

    @Override
    public void insertAllRecordType(RecordType... recordTypes) {
        mAppDatabase.recordTypeDao().insertRecordTypes(recordTypes);
    }

    @Override
    public void initRecordTypes() {
        if (mAppDatabase.recordTypeDao().getRecordTypeCount() < 1) {
            // 没有记账类型数据记录，插入默认的数据类型
            insertAllRecordType(RecordTypeInitCreator.createRecordTypeData());
        }
    }

    @Override
    public Flowable<List<RecordWithType>> getCurrentMonthRecordWithTypes() {
        Date dateFrom = DateUtils.getCurrentMonthStart();
        Date dateTo = DateUtils.getCurrentMonthEnd();
        return mAppDatabase.recordDao().getRangeRecordWithTypes(dateFrom, dateTo);
    }

    @Override
    public void insertRecord(Record record) {
        mAppDatabase.recordDao().insertRecord(record);
    }

    @Override
    public void updateRecordTypes(RecordType... recordTypes) {
        mAppDatabase.recordTypeDao().updateRecordTypes(recordTypes);
    }

    @Override
    public Flowable<List<RecordType>> getRecordType(int type) {
        return mAppDatabase.recordTypeDao().getRecordTypes(type);
    }
}
