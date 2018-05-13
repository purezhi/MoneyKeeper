package me.bakumon.moneykeeper.ui.typesort;

import android.arch.lifecycle.ViewModel;

import java.util.List;

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
        return mDataSource.getRecordType(type);
    }

}
