package me.bakumon.moneykeeper.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * 记账类型
 *
 * @author bakumon https://bakumon.me
 */
@Entity(indices = {@Index(value = {"type", "ranking"})})
public class RecordType {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    /**
     * 图片 name（本地mipmap）
     */
    @ColumnInfo(name = "img_name")
    public String imgName;

    @Ignore
    public static int TYPE_OUTLAY = 0;
    @Ignore
    public static int TYPE_INCOME = 1;
    /**
     * 类型
     * 0：支出
     * 1：收入
     *
     * @see RecordType#TYPE_OUTLAY
     * @see RecordType#TYPE_INCOME
     */
    public int type;
    /**
     * 排序
     */
    public int ranking;

    /**
     * 是否选中，用于 UI
     */
    @Ignore
    public boolean isChecked;

    @Ignore
    public RecordType(String name, String imgName, int type, int ranking) {
        this.name = name;
        this.imgName = imgName;
        this.type = type;
        this.ranking = ranking;
    }

    public RecordType(int id, String name, String imgName, int type, int ranking) {
        this.id = id;
        this.name = name;
        this.imgName = imgName;
        this.type = type;
        this.ranking = ranking;
    }
}
