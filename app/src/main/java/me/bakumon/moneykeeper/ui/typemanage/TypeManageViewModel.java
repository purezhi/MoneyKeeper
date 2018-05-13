package me.bakumon.moneykeeper.ui.typemanage;

import android.arch.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.datasource.AppDataSource;

/**
 * 记一笔界面 ViewModel
 *
 * @author Bakumon https://bakumon.me
 */
public class TypeManageViewModel extends ViewModel {
    private final AppDataSource mDataSource;

    public TypeManageViewModel(AppDataSource dataSource) {
        mDataSource = dataSource;
    }

    public Flowable<List<RecordType>> getAllRecordTypes() {
        return mDataSource.getAllRecordType();
    }

    public Completable deleteRecordType(RecordType recordType) {
        return Completable.fromAction(() -> {
            recordType.state = RecordType.STATE_DELETED;
            mDataSource.updateRecordTypes(recordType);
        });
    }
}
