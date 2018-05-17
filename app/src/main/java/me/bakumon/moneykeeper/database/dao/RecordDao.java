package me.bakumon.moneykeeper.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import me.bakumon.moneykeeper.database.entity.Record;
import me.bakumon.moneykeeper.database.entity.RecordWithType;
import me.bakumon.moneykeeper.database.entity.SumMoneyBean;

/**
 * 记账记录表操作类
 *
 * @author Bakumon https://bakumon.me
 */
@Dao
public interface RecordDao {

    @Transaction
    @Query("SELECT * from record WHERE time BETWEEN :from AND :to ORDER BY time DESC, create_time DESC")
    Flowable<List<RecordWithType>> getRangeRecordWithTypes(Date from, Date to);

    @Insert
    void insertRecord(Record record);

    @Delete
    void deleteRecord(Record record);

    @Query("SELECT recordType.type AS type, sum(record.money) AS sumMoney FROM record LEFT JOIN RecordType ON record.record_type_id=RecordType.id WHERE time BETWEEN :from AND :to GROUP BY RecordType.type")
    Flowable<List<SumMoneyBean>> getSumMoney(Date from, Date to);

    @Query("SELECT count(id) FROM record WHERE record_type_id = :typeId")
    long getRecordCountWithTypeId(int typeId);

    @Query("SELECT * FROM record WHERE record_type_id = :typeId")
    List<Record> getRecordsWithTypeId(int typeId);

    @Update
    void updateRecords(List<Record> records);
}
