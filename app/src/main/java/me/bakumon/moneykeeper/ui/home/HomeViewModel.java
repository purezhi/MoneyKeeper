package me.bakumon.moneykeeper.ui.home;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.bakumon.moneykeeper.base.BaseViewModel;
import me.bakumon.moneykeeper.database.entity.RecordWithType;
import me.bakumon.moneykeeper.datasource.AppDataSource;

/**
 * 主页 ViewModel
 *
 * @author Bakumon https://bakumon.me
 */
public class HomeViewModel extends BaseViewModel {
    public HomeViewModel(AppDataSource dataSource) {
        super(dataSource);
    }

    public Flowable<List<RecordWithType>> getCurrentMonthRecordWithTypes() {
        return mDataSource.getCurrentMonthRecordWithTypes();
    }

    public Completable deleteRecord(RecordWithType record) {
        return mDataSource.deleteRecord(record);
    }
}
