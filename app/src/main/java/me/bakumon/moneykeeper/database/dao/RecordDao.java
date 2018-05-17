package me.bakumon.moneykeeper.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

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

    @Query("SELECT recordType.type as type, sum(record.money) as sumMoney from record left join RecordType on record.record_type_id=RecordType.id WHERE time BETWEEN :from AND :to group by RecordType.type")
    Flowable<List<SumMoneyBean>> getSumMoney(Date from, Date to);
}
