package me.bakumon.moneykeeper.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import me.bakumon.moneykeeper.database.converters.Converters;
import me.bakumon.moneykeeper.database.dao.RecordDao;
import me.bakumon.moneykeeper.database.dao.RecordTypeDao;
import me.bakumon.moneykeeper.database.entity.Record;
import me.bakumon.moneykeeper.database.entity.RecordType;


/**
 * 数据库
 *
 * @author Bakumon https:bakumon.me
 */
@Database(entities = {Record.class, RecordType.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public static final String DB_NAME = "moneykeeper.db";

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DB_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 获取记账类型操作类
     *
     * @return RecordTypeDao 记账类型操作类
     */
    public abstract RecordTypeDao recordTypeDao();

    /**
     * 获取记账操作类
     *
     * @return RecordDao 记账操作类
     */
    public abstract RecordDao recordDao();

}
