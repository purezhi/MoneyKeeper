package me.bakumon.moneykeeper.ui.typesort;

import java.util.ArrayList;
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
//        return mDataSource.sortRecordTypes(recordTypes);
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
            return mDataSource.updateRecordTypes(sortTypes.toArray(typeArray));
        } else {
            return Completable.fromAction(() -> {
            });
        }
    }
}
