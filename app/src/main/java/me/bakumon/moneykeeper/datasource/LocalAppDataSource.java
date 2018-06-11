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

package me.bakumon.moneykeeper.datasource;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.bakumon.moneykeeper.App;
import me.bakumon.moneykeeper.ConfigManager;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.database.AppDatabase;
import me.bakumon.moneykeeper.database.entity.DaySumMoneyBean;
import me.bakumon.moneykeeper.database.entity.Record;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.database.entity.RecordWithType;
import me.bakumon.moneykeeper.database.entity.SumMoneyBean;
import me.bakumon.moneykeeper.database.entity.TypeSumMoneyBean;
import me.bakumon.moneykeeper.ui.addtype.TypeImgBean;
import me.bakumon.moneykeeper.utill.BackupUtil;
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

    /**
     * 自动备份
     */
    private void autoBackup() throws Exception {
        if (ConfigManager.isAutoBackup()) {
            boolean isSuccess = BackupUtil.autoBackup();
            if (!isSuccess) {
                throw new BackupFailException();
            }
        }
    }

    /**
     * 自动备份
     */
    private void autoBackupForNecessary() throws Exception {
        if (ConfigManager.isAutoBackup()) {
            boolean isSuccess = BackupUtil.autoBackupForNecessary();
            if (!isSuccess) {
                throw new BackupFailException();
            }
        }
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
                autoBackupForNecessary();
            }
        });
    }

    @Override
    public Completable deleteRecord(Record record) {
        return Completable.fromAction(() -> {
            mAppDatabase.recordDao().deleteRecord(record);
            autoBackup();
        });
    }

    @Override
    public Flowable<List<RecordWithType>> getCurrentMonthRecordWithTypes() {
        Date dateFrom = DateUtils.getCurrentMonthStart();
        Date dateTo = DateUtils.getCurrentMonthEnd();
        return mAppDatabase.recordDao().getRangeRecordWithTypes(dateFrom, dateTo);
    }

    @Override
    public Flowable<List<RecordWithType>> getRecordWithTypes(Date dateFrom, Date dateTo, int type) {
        return mAppDatabase.recordDao().getRangeRecordWithTypes(dateFrom, dateTo, type);
    }

    @Override
    public Flowable<List<RecordWithType>> getRecordWithTypes(Date dateFrom, Date dateTo, int type, int typeId) {
        return mAppDatabase.recordDao().getRangeRecordWithTypes(dateFrom, dateTo, type, typeId);
    }

    @Override
    public Flowable<List<RecordWithType>> getRecordWithTypesSortMoney(Date dateFrom, Date dateTo, int type, int typeId) {
        return mAppDatabase.recordDao().getRecordWithTypesSortMoney(dateFrom, dateTo, type, typeId);
    }

    @Override
    public Completable insertRecord(Record record) {
        return Completable.fromAction(() -> {
            mAppDatabase.recordDao().insertRecord(record);
            autoBackup();
        });
    }

    @Override
    public Completable updateRecord(Record record) {
        return Completable.fromAction(() -> {
            mAppDatabase.recordDao().updateRecords(record);
            autoBackup();
        });
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
                mAppDatabase.recordTypeDao().updateRecordTypes(sortTypes.toArray(typeArray));
                autoBackup();
            }
        });
    }

    @Override
    public Completable deleteRecordType(RecordType recordType) {
        return Completable.fromAction(() -> {
            if (mAppDatabase.recordDao().getRecordCountWithTypeId(recordType.id) > 0) {
                recordType.state = RecordType.STATE_DELETED;
                mAppDatabase.recordTypeDao().updateRecordTypes(recordType);
            } else {
                mAppDatabase.recordTypeDao().deleteRecordType(recordType);
            }
            autoBackup();
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
            RecordType recordType = mAppDatabase.recordTypeDao().getTypeByName(type, name);
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
                    throw new IllegalStateException(name + App.getINSTANCE().getString(R.string.toast_type_is_exist));
                }
            } else {
                // 不存在，直接新增
                RecordType insertType = new RecordType(name, imgName, type, System.currentTimeMillis());
                mAppDatabase.recordTypeDao().insertRecordTypes(insertType);
            }
            autoBackup();
        });
    }

    @Override
    public Completable updateRecordType(RecordType oldRecordType, RecordType recordType) {
        return Completable.fromAction(() -> {
            String oldName = oldRecordType.name;
            String oldImgName = oldRecordType.imgName;
            if (!TextUtils.equals(oldName, recordType.name)) {
                RecordType recordTypeFromDb = mAppDatabase.recordTypeDao().getTypeByName(recordType.type, recordType.name);
                if (recordTypeFromDb != null) {
                    if (recordTypeFromDb.state == RecordType.STATE_DELETED) {

                        // 1。recordTypeFromDb 改成正常状态，name改成recordType#name，imageName同理
                        // 2。更新 recordTypeFromDb
                        // 3。判断是否有 oldRecordType 类型的 record 记录
                        // 4。如果有记录，把这些记录的 type_id 改成 recordTypeFromDb.id
                        // 5。删除 oldRecordType 记录

                        recordTypeFromDb.state = RecordType.STATE_NORMAL;
                        recordTypeFromDb.name = recordType.name;
                        recordTypeFromDb.imgName = recordType.imgName;
                        recordTypeFromDb.ranking = System.currentTimeMillis();

                        mAppDatabase.recordTypeDao().updateRecordTypes(recordTypeFromDb);

                        List<Record> recordsWithOldType = mAppDatabase.recordDao().getRecordsWithTypeId(oldRecordType.id);
                        if (recordsWithOldType != null && recordsWithOldType.size() > 0) {
                            for (Record record : recordsWithOldType) {
                                record.recordTypeId = recordTypeFromDb.id;
                            }
                            mAppDatabase.recordDao().updateRecords(recordsWithOldType.toArray(new Record[recordsWithOldType.size()]));
                        }

                        mAppDatabase.recordTypeDao().deleteRecordType(oldRecordType);
                    } else {
                        // 提示用户该类型已经存在
                        throw new IllegalStateException(recordType.name + App.getINSTANCE().getString(R.string.toast_type_is_exist));
                    }
                } else {
                    mAppDatabase.recordTypeDao().updateRecordTypes(recordType);
                }
            } else if (!TextUtils.equals(oldImgName, recordType.imgName)) {
                mAppDatabase.recordTypeDao().updateRecordTypes(recordType);
            }
            autoBackup();
        });
    }

    @Override
    public Flowable<List<SumMoneyBean>> getCurrentMonthSumMoney() {
        Date dateFrom = DateUtils.getCurrentMonthStart();
        Date dateTo = DateUtils.getCurrentMonthEnd();
        return mAppDatabase.recordDao().getSumMoney(dateFrom, dateTo);
    }

    @Override
    public Flowable<List<SumMoneyBean>> getMonthSumMoney(Date dateFrom, Date dateTo) {
        return mAppDatabase.recordDao().getSumMoney(dateFrom, dateTo);
    }

    @Override
    public Flowable<List<DaySumMoneyBean>> getDaySumMoney(int year, int month, int type) {
        Date dateFrom = DateUtils.getMonthStart(year, month);
        Date dateTo = DateUtils.getMonthEnd(year, month);
        return mAppDatabase.recordDao().getDaySumMoney(dateFrom, dateTo, type);
    }

    @Override
    public Flowable<List<TypeSumMoneyBean>> getTypeSumMoney(Date from, Date to, int type) {
        return mAppDatabase.recordDao().getTypeSumMoney(from, to, type);
    }
}
