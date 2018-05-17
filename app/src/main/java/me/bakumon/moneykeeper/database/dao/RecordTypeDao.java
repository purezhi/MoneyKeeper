package me.bakumon.moneykeeper.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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

    @Query("SELECT * FROM recordtype WHERE state = 0 ORDER BY ranking")
    Flowable<List<RecordType>> getAllRecordTypes();

    @Query("SELECT * FROM recordtype WHERE state = 0 AND type = :type ORDER BY ranking")
    Flowable<List<RecordType>> getRecordTypes(int type);

    @Query("SELECT count(recordtype.id) FROM recordtype")
    long getRecordTypeCount();

    @Query("SELECT * FROM recordtype WHERE type = :type AND name = :name")
    RecordType getTypeByName(int type, String name);

    @Insert
    void insertRecordTypes(RecordType... recordTypes);

    @Update
    void updateRecordTypes(RecordType... recordTypes);

    @Delete
    void deleteRecordType(RecordType recordType);
}
