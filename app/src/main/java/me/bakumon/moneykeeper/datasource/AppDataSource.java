package me.bakumon.moneykeeper.datasource;

import java.util.List;

import io.reactivex.Flowable;
import me.bakumon.moneykeeper.bean.UIRecord;
import me.bakumon.moneykeeper.database.entity.Record;
import me.bakumon.moneykeeper.database.entity.RecordType;

public interface AppDataSource {
    Flowable<List<RecordType>> getAllRecordType();

    long getRecordTypeCount();

    void insertAllRecordType(RecordType... recordTypes);

    void deleteRecordType(RecordType recordType);


    Flowable<List<Record>> getAllRecord();

    Flowable<List<UIRecord>> getAllUIRecord();

    void insertRecord(Record record);
}
