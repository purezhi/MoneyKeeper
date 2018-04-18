package me.bakumon.moneykeeper.datasource;

import java.util.List;

import io.reactivex.Flowable;
import me.bakumon.moneykeeper.database.dao.RecordTypeDao;
import me.bakumon.moneykeeper.database.entity.RecordType;

public class LocalAppDataSource implements AppDataSource {
    private final RecordTypeDao mRecordTypeDao;

    public LocalAppDataSource(RecordTypeDao recordTypeDao) {
        mRecordTypeDao = recordTypeDao;
    }

    @Override
    public Flowable<List<RecordType>> getAll() {
        return mRecordTypeDao.getAll();
    }

    @Override
    public long getRecordTypeCount() {
        return mRecordTypeDao.getRecordTypeCount();
    }

    @Override
    public void insertAll(RecordType... recordTypes) {
        mRecordTypeDao.insertAll(recordTypes);
    }

    @Override
    public void delete(RecordType recordType) {
        mRecordTypeDao.delete(recordType);
    }
}
