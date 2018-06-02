package me.bakumon.moneykeeper.ui.typesort;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.bakumon.moneykeeper.base.BaseViewModel;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.datasource.AppDataSource;

/**
 * 类型排序 ViewModel
 *
 * @author Bakumon https://bakumon.me
 */
public class TypeSortViewModel extends BaseViewModel {
    public TypeSortViewModel(AppDataSource dataSource) {
        super(dataSource);
    }

    public Flowable<List<RecordType>> getRecordTypes(int type) {
        return mDataSource.getRecordTypes(type);
    }

    public Completable sortRecordTypes(List<RecordType> recordTypes) {
        return mDataSource.sortRecordTypes(recordTypes);
    }
}
