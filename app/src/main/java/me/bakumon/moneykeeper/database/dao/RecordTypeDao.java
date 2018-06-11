/*
 * Copyright 2018 Bakumon. https://github.com/Bakumon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
