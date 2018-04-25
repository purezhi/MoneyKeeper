package me.bakumon.moneykeeper.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 记账记录
 *
 * @author bakumon https://bakumon.me
 */
@Entity(foreignKeys = @ForeignKey(entity = RecordType.class, parentColumns = "id", childColumns = "record_type_id"),
        indices = {@Index(value = {"record_type_id", "time"})})
public class Record {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public BigDecimal money;

    public String remark;

    public Date time;

    @ColumnInfo(name = "create_time")
    public Date createTime;

    @ColumnInfo(name = "record_type_id")
    public int recordTypeId;
}
