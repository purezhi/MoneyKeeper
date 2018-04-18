package me.bakumon.moneykeeper.datasource;

import java.util.List;

import io.reactivex.Flowable;
import me.bakumon.moneykeeper.database.entity.RecordType;

public interface AppDataSource {
    Flowable<List<RecordType>> getAll();

    long getRecordTypeCount();

    void insertAll(RecordType... recordTypes);

    void delete(RecordType recordType);
}
