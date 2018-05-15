package me.bakumon.moneykeeper.ui.typesort;

import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.datasource.AppDataSource;

/**
 * 类型排序 ViewModel
 *
 * @author Bakumon https://bakumon.me
 */
public class TypeSortViewModel extends ViewModel {
    private final AppDataSource mDataSource;

    public TypeSortViewModel(AppDataSource dataSource) {
        mDataSource = dataSource;
    }

    public Flowable<List<RecordType>> getRecordTypes(int type) {
        return mDataSource.getRecordTypes(type);
    }

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
                mDataSource.updateRecordTypes(sortTypes.toArray(typeArray));
            }
        });
    }
}
