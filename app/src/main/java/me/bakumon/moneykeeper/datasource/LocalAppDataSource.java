package me.bakumon.moneykeeper.datasource;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.bakumon.moneykeeper.database.AppDatabase;
import me.bakumon.moneykeeper.database.entity.Record;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.database.entity.RecordWithType;
import me.bakumon.moneykeeper.ui.addtype.TypeImgBean;
import me.bakumon.moneykeeper.utill.DateUtils;

/**
 * 数据源本地实现类
 *
 * @author Bakumon https://bakumon.me
 */
public class LocalAppDataSource implements AppDataSource {
    private final AppDatabase mAppDatabase;

    public LocalAppDataSource(AppDatabase appDatabase) {
        mAppDatabase = appDatabase;
    }

    @Override
    public Flowable<List<RecordType>> getAllRecordType() {
        return mAppDatabase.recordTypeDao().getAllRecordTypes();
    }

    @Override
    public Completable initRecordTypes() {
        return Completable.fromAction(() -> {
            if (mAppDatabase.recordTypeDao().getRecordTypeCount() < 1) {
                // 没有记账类型数据记录，插入默认的数据类型
                mAppDatabase.recordTypeDao().insertRecordTypes(RecordTypeInitCreator.createRecordTypeData());
            }
        });
    }

    @Override
    public Completable deleteRecord(Record record) {
        return Completable.fromAction(() -> mAppDatabase.recordDao().deleteRecord(record));
    }

    @Override
    public Flowable<List<RecordWithType>> getCurrentMonthRecordWithTypes() {
        Date dateFrom = DateUtils.getCurrentMonthStart();
        Date dateTo = DateUtils.getCurrentMonthEnd();
        return mAppDatabase.recordDao().getRangeRecordWithTypes(dateFrom, dateTo);
    }

    @Override
    public Completable insertRecord(Record record) {
        return Completable.fromAction(() -> mAppDatabase.recordDao().insertRecord(record));
    }

    @Override
    public Completable updateRecordTypes(RecordType... recordTypes) {
        return Completable.fromAction(() -> mAppDatabase.recordTypeDao().updateRecordTypes(recordTypes));
    }

    @Override
    public Completable sortRecordTypes(List<RecordType> recordTypes) {
        return Completable.fromAction(() -> {
            if (recordTypes != null && recordTypes.size() > 1) {
                List<RecordType> sortTypes = new ArrayList<>();
                for (int i = 0; i < recordTypes.size(); i++) {
                    RecordType type = recordTypes.get(i);
                    if (type.ranking != i) {
                        type.ranking = i;
                        sortTypes.add(type);
                    }
                }
                RecordType[] typeArray = new RecordType[sortTypes.size()];
                mAppDatabase.recordTypeDao().updateRecordTypes(typeArray);
            }
        });
    }

    @Override
    public Completable deleteRecordType(RecordType recordType) {
        return Completable.fromAction(() -> {
            recordType.state = RecordType.STATE_DELETED;
            mAppDatabase.recordTypeDao().updateRecordTypes(recordType);
        });
    }

    @Override
    public Flowable<List<RecordType>> getRecordTypes(int type) {
        return mAppDatabase.recordTypeDao().getRecordTypes(type);
    }

    @Override
    public Flowable<List<TypeImgBean>> getAllTypeImgBeans(int type) {
        return Flowable.create(e -> {
            List<TypeImgBean> beans = TypeImgListCreator.createTypeImgBeanData(type);
            e.onNext(beans);
            e.onComplete();
        }, BackpressureStrategy.BUFFER);
    }

    @Override
    public Completable addRecordType(int type, String imgName, String name) {
        return Completable.fromAction(() -> {
            RecordType recordType = mAppDatabase.recordTypeDao().getTypeByName(name);
            if (recordType != null) {
                // name 类型存在
                if (recordType.state == RecordType.STATE_DELETED) {
                    // 已删除状态
                    recordType.state = RecordType.STATE_NORMAL;
                    recordType.ranking = System.currentTimeMillis();
                    recordType.imgName = imgName;
                    mAppDatabase.recordTypeDao().updateRecordTypes(recordType);
                } else {
                    // 提示用户该类型已经存在
                    throw new IllegalStateException("该名称已经存在");
                }
            } else {
                // 不存在，直接新增
                RecordType insertType = new RecordType(name, imgName, type, System.currentTimeMillis());
                mAppDatabase.recordTypeDao().insertRecordTypes(insertType);
            }
        });
    }

    @Override
    public Completable updateRecordType(String oldName, String oldImgName, RecordType recordType) {
        return Completable.fromAction(() -> {
            if (!TextUtils.equals(oldName, recordType.name)) {
                RecordType recordTypeFromDb = mAppDatabase.recordTypeDao().getTypeByName(recordType.name);
                if (recordTypeFromDb != null) {
                    // 提示用户该类型已经存在
                    throw new IllegalStateException("该名称已经存在");
                } else {
                    mAppDatabase.recordTypeDao().updateRecordTypes(recordType);
                }
            } else if (!TextUtils.equals(oldImgName, recordType.imgName)) {
                mAppDatabase.recordTypeDao().updateRecordTypes(recordType);
            }
        });
    }
}
