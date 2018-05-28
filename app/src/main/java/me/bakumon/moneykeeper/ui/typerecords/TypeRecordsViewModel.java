package me.bakumon.moneykeeper.ui.typerecords;

import java.util.Date;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.bakumon.moneykeeper.base.BaseViewModel;
import me.bakumon.moneykeeper.database.entity.RecordWithType;
import me.bakumon.moneykeeper.datasource.AppDataSource;
import me.bakumon.moneykeeper.utill.DateUtils;

/**
 * 某一类型的记账记录
 *
 * @author Bakumon https://bakumon.me
 */
public class TypeRecordsViewModel extends BaseViewModel {
    public TypeRecordsViewModel(AppDataSource dataSource) {
        super(dataSource);
    }

    public Flowable<List<RecordWithType>> getRecordWithTypes(int sortType, int type, int typeId, int year, int month) {
        Date dateFrom = DateUtils.getMonthStart(year, month);
        Date dateTo = DateUtils.getMonthEnd(year, month);
        if (sortType == TypeRecordsFragment.SORT_TIME) {
            return mDataSource.getRecordWithTypes(dateFrom, dateTo, type, typeId);
        } else {
            return mDataSource.getRecordWithTypesSortMoney(dateFrom, dateTo, type, typeId);
        }
    }

    public Completable deleteRecord(RecordWithType record) {
        return mDataSource.deleteRecord(record);
    }

}
