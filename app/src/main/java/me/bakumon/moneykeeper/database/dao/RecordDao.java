package me.bakumon.moneykeeper.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import me.bakumon.moneykeeper.database.entity.Record;
import me.bakumon.moneykeeper.database.entity.RecordWithType;

/**
 * @author Bakumon https://bakumon.me
 */
@Dao
public interface RecordDao {

    @Query("SELECT * FROM record WHERE time BETWEEN :from AND :to ORDER BY time DESC")
    Flowable<List<Record>> getRangeRecords(Date from, Date to);

    @Insert
    void insert(Record record);

    @Transaction
    @Query("SELECT * from record WHERE time BETWEEN :from AND :to ORDER BY time DESC")
    Flowable<List<RecordWithType>> getRangeRecordWithTypes(Date from, Date to);

}
