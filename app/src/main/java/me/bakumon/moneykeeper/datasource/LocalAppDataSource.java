package me.bakumon.moneykeeper.datasource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import me.bakumon.moneykeeper.bean.UIRecord;
import me.bakumon.moneykeeper.database.AppDatabase;
import me.bakumon.moneykeeper.database.entity.Record;
import me.bakumon.moneykeeper.database.entity.RecordType;

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
    public Flowable<List<UIRecord>> getAllUIRecord() {
        return mAppDatabase.recordDao().getAll().map(records -> {
            List<UIRecord> uiRecords = new ArrayList<>();
            if (records != null && records.size() > 0) {
                for (int i = 0; i < records.size(); i++) {
                    UIRecord uiRecord = new UIRecord();
                    RecordType recordType = mAppDatabase.recordTypeDao().getReCordTypeById(records.get(i).recordTypeId);
                    uiRecord.mRecord = records.get(i);
                    uiRecord.mRecordType = recordType;
                    uiRecords.add(uiRecord);
                }
            }
            return uiRecords;
        });
    }

    @Override
    public void insertRecord(Record record) {
        mAppDatabase.recordDao().insert(record);
    }
}
