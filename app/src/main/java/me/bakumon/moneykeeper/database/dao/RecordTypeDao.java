package me.bakumon.moneykeeper.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import me.bakumon.moneykeeper.database.entity.RecordType;

/**
 * 记账类型表操作类
 *
 * @author Bakumon https://bakumon.me
 */
@Dao
public interface RecordTypeDao {

    @Query("SELECT * FROM recordtype")
    Flowable<List<RecordType>> getAllRecordTypes();

    @Query("SELECT count(*) FROM recordtype")
    long getRecordTypeCount();

    @Insert
    void insertRecordTypes(RecordType... recordTypes);
}
