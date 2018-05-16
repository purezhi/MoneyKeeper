package me.bakumon.moneykeeper.ui.typemanage;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.bakumon.moneykeeper.base.BaseViewModel;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.datasource.AppDataSource;

/**
 * 记一笔界面 ViewModel
 *
 * @author Bakumon https://bakumon.me
 */
public class TypeManageViewModel extends BaseViewModel {
    public TypeManageViewModel(AppDataSource dataSource) {
        super(dataSource);
    }

    public Flowable<List<RecordType>> getAllRecordTypes() {
        return mDataSource.getAllRecordType();
    }

    public Completable deleteRecordType(RecordType recordType) {
        return mDataSource.deleteRecordType(recordType);
    }
}
