package me.bakumon.moneykeeper.datasource;

import java.util.List;

import io.reactivex.Flowable;
import me.bakumon.moneykeeper.database.entity.Record;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.database.entity.RecordWithType;
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
     * 批量新增记账类型
     *
     * @param recordTypes 记账类型数组
     */
    void insertAllRecordType(RecordType... recordTypes);

    /**
     * 初始化默认的记账类型
     */
    void initRecordTypes();

    /**
     * 获取当前月份的记账记录数据
     *
     * @return 当前月份的记录数据的 Flowable 对象
     */
    Flowable<List<RecordWithType>> getCurrentMonthRecordWithTypes();

    /**
     * 新增一条记账记录
     *
     * @param record 记账记录实体
     */
    void insertRecord(Record record);

    /**
     * 删除一天记账记录
     *
     * @param record 要删除的记账记录
     */
    void deleteRecord(Record record);

    /**
     * 修改记账类型
     *
     * @param recordTypes 记账类型对象
     */
    void updateRecordTypes(RecordType... recordTypes);

    /**
     * 删除记账类型
     *
     * @param recordType 要删除的记账类型对象
     */
    void deleteRecordType(RecordType recordType);

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
    void addRecordType(int type, String imgName, String name);
}
