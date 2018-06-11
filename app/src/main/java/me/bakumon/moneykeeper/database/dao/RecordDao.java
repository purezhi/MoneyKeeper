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
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import me.bakumon.moneykeeper.database.entity.DaySumMoneyBean;
import me.bakumon.moneykeeper.database.entity.Record;
import me.bakumon.moneykeeper.database.entity.RecordWithType;
import me.bakumon.moneykeeper.database.entity.SumMoneyBean;
import me.bakumon.moneykeeper.database.entity.TypeSumMoneyBean;

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

    @Transaction
    @Query("SELECT record.* from record LEFT JOIN RecordType ON record.record_type_id=RecordType.id WHERE (RecordType.type=:type AND time BETWEEN :from AND :to) ORDER BY time DESC, create_time DESC")
    Flowable<List<RecordWithType>> getRangeRecordWithTypes(Date from, Date to, int type);

    @Transaction
    @Query("SELECT record.* from record LEFT JOIN RecordType ON record.record_type_id=RecordType.id WHERE (RecordType.type=:type AND record.record_type_id=:typeId AND time BETWEEN :from AND :to) ORDER BY time DESC, create_time DESC")
    Flowable<List<RecordWithType>> getRangeRecordWithTypes(Date from, Date to, int type, int typeId);

    @Transaction
    @Query("SELECT record.* from record LEFT JOIN RecordType ON record.record_type_id=RecordType.id WHERE (RecordType.type=:type AND record.record_type_id=:typeId AND time BETWEEN :from AND :to) ORDER BY money DESC, create_time DESC")
    Flowable<List<RecordWithType>> getRecordWithTypesSortMoney(Date from, Date to, int type, int typeId);

    @Insert
    void insertRecord(Record record);

    @Update
    void updateRecords(Record... records);

    @Delete
    void deleteRecord(Record record);

    @Query("SELECT recordType.type AS type, sum(record.money) AS sumMoney FROM record LEFT JOIN RecordType ON record.record_type_id=RecordType.id WHERE time BETWEEN :from AND :to GROUP BY RecordType.type")
    Flowable<List<SumMoneyBean>> getSumMoney(Date from, Date to);

    @Query("SELECT count(id) FROM record WHERE record_type_id = :typeId")
    long getRecordCountWithTypeId(int typeId);

    @Query("SELECT * FROM record WHERE record_type_id = :typeId")
    List<Record> getRecordsWithTypeId(int typeId);

    /**
     * 尽量使用 Flowable 返回，因为当数据库数据改变时，会自动回调
     * 而直接用 List ，在调用的地方自己写 Flowable 不会自动回调
     */
    @Query("SELECT recordType.type AS type, record.time AS time, sum(record.money) AS daySumMoney FROM record LEFT JOIN RecordType ON record.record_type_id=RecordType.id where (RecordType.type=:type and record.time BETWEEN :from AND :to) GROUP BY record.time")
    Flowable<List<DaySumMoneyBean>> getDaySumMoney(Date from, Date to, int type);

    @Query("SELECT t_type.img_name AS imgName,t_type.name AS typeName, record.record_type_id AS typeId,sum(record.money) AS typeSumMoney, count(record.record_type_id) AS count FROM record LEFT JOIN RecordType AS t_type ON record.record_type_id=t_type.id where (t_type.type=:type and record.time BETWEEN :from AND :to) GROUP by record.record_type_id Order by sum(record.money) DESC")
    Flowable<List<TypeSumMoneyBean>> getTypeSumMoney(Date from, Date to, int type);
}
