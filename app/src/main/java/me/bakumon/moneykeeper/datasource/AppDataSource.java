package me.bakumon.moneykeeper.datasource;

import java.util.List;

import io.reactivex.Flowable;
import me.bakumon.moneykeeper.database.entity.Record;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.database.entity.RecordWithType;

public interface AppDataSource {
    Flowable<List<RecordType>> getAllRecordType();

    long getRecordTypeCount();

    void insertAllRecordType(RecordType... recordTypes);

    void deleteRecordType(RecordType recordType);

    /**
     * 获取当前月份的记账记录数据
     *
     * @return 当前月份的记录数据的 Flowable 对象
     */
    Flowable<List<RecordWithType>> getCurrentMonthRecordWithTypes();

    /**
     * 新增一条记账记录
     *
     * @param record 记账记录实体
     */
    void insertRecord(Record record);
}
