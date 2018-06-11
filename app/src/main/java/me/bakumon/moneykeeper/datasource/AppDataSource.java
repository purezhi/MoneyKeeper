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

import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.bakumon.moneykeeper.database.entity.DaySumMoneyBean;
import me.bakumon.moneykeeper.database.entity.Record;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.database.entity.RecordWithType;
import me.bakumon.moneykeeper.database.entity.SumMoneyBean;
import me.bakumon.moneykeeper.database.entity.TypeSumMoneyBean;
import me.bakumon.moneykeeper.ui.addtype.TypeImgBean;

/**
 * 数据源
 *
 * @author Bakumon https://bakumon.me
 */
public interface AppDataSource {
    /**
     * 获取所有记账类型数据
     *
     * @return 所有记账类型数据
     */
    Flowable<List<RecordType>> getAllRecordType();

    /**
     * 初始化默认的记账类型
     */
    Completable initRecordTypes();

    /**
     * 获取当前月份的记账记录数据
     *
     * @return 当前月份的记录数据的 Flowable 对象
     */
    Flowable<List<RecordWithType>> getCurrentMonthRecordWithTypes();

    /**
     * 根据类型获取某段时间的记账记录数据
     *
     * @return 包含记录数据的 Flowable 对象
     */
    Flowable<List<RecordWithType>> getRecordWithTypes(Date dateFrom, Date dateTo, int type);

    /**
     * 获取某一类型某段时间的记账记录数据
     *
     * @return 包含记录数据的 Flowable 对象
     */
    Flowable<List<RecordWithType>> getRecordWithTypes(Date dateFrom, Date dateTo, int type, int typeId);

    /**
     * 获取某一类型某段时间的记账记录数据，money 排序
     *
     * @return 包含记录数据的 Flowable 对象
     */
    Flowable<List<RecordWithType>> getRecordWithTypesSortMoney(Date dateFrom, Date dateTo, int type, int typeId);

    /**
     * 新增一条记账记录
     *
     * @param record 记账记录实体
     */
    Completable insertRecord(Record record);

    /**
     * 更新一条记账记录
     *
     * @param record 记录对象
     */
    Completable updateRecord(Record record);

    /**
     * 删除一天记账记录
     *
     * @param record 要删除的记账记录
     */
    Completable deleteRecord(Record record);

    /**
     * 记账类型排序
     *
     * @param recordTypes 记账类型对象
     */
    Completable sortRecordTypes(List<RecordType> recordTypes);

    /**
     * 删除记账类型
     *
     * @param recordType 要删除的记账类型对象
     */
    Completable deleteRecordType(RecordType recordType);

    /**
     * 获取指出或收入记账类型数据
     *
     * @param type 类型
     * @return 记账类型数据
     * @see RecordType#TYPE_OUTLAY
     * @see RecordType#TYPE_INCOME
     */
    Flowable<List<RecordType>> getRecordTypes(int type);

    /**
     * 获取类型图片数据
     *
     * @param type 收入或支出类型
     * @return 所有获取类型图片数据
     * @see RecordType#TYPE_OUTLAY
     * @see RecordType#TYPE_INCOME
     */
    Flowable<List<TypeImgBean>> getAllTypeImgBeans(int type);

    /**
     * 添加一个记账类型
     *
     * @param type    类型
     * @param imgName 图片
     * @param name    类型名称
     * @see RecordType#TYPE_OUTLAY
     * @see RecordType#TYPE_INCOME
     */
    Completable addRecordType(int type, String imgName, String name);

    /**
     * 修改记账类型
     *
     * @param oldRecordType 修改之前的 RecordType
     * @param recordType    修改的 RecordType
     */
    Completable updateRecordType(RecordType oldRecordType, RecordType recordType);

    /**
     * 获取本月支出和收入总数
     */
    Flowable<List<SumMoneyBean>> getCurrentMonthSumMoney();

    /**
     * 获取本月支出和收入总数
     */
    Flowable<List<SumMoneyBean>> getMonthSumMoney(Date dateFrom, Date dateTo);

    /**
     * 获取某天的合计
     *
     * @param year  年
     * @param month 月
     * @param type  类型
     */
    Flowable<List<DaySumMoneyBean>> getDaySumMoney(int year, int month, int type);

    /**
     * 获取按类型汇总数据
     */
    Flowable<List<TypeSumMoneyBean>> getTypeSumMoney(Date from, Date to, int type);
}
