package me.bakumon.moneykeeper.database.entity;

import android.arch.persistence.room.Relation;

import java.util.List;

/**
 * 包含 RecordType 的 Record
 *
 * @author Bakumon https://bakumon.me
 */
public class RecordWithType extends Record {
    @Relation(parentColumn = "record_type_id", entityColumn = "id", entity = RecordType.class)
    public List<RecordType> mRecordTypes;
}
