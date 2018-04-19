package me.bakumon.moneykeeper.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import me.bakumon.moneykeeper.database.entity.RecordType;

/**
 * @author Bakumon https://bakumon.me
 */
@Dao
public interface RecordTypeDao {

    @Query("SELECT * FROM recordtype WHERE id = :id")
    RecordType getReCordTypeById(int id);

    @Query("SELECT * FROM recordtype")
    Flowable<List<RecordType>> getAll();

    @Query("SELECT count(*) FROM recordtype")
    long getRecordTypeCount();

    @Insert
    void insertAll(RecordType... recordTypes);

    @Delete
    void delete(RecordType recordType);
}
