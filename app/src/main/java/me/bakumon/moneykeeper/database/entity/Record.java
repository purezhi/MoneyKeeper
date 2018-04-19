package me.bakumon.moneykeeper.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

/**
 * 记账记录
 *
 * @author bakumon https://bakumon.me
 */
@Entity(foreignKeys = @ForeignKey(entity = RecordType.class, parentColumns = "id", childColumns = "record_type_id"))
public class Record {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String money;

    public String remark;

    public String date;

    @ColumnInfo(name = "record_type_id")
    public int recordTypeId;
}
