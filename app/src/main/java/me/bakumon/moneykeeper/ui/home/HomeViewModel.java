package me.bakumon.moneykeeper.ui.home;

import android.arch.lifecycle.ViewModel;

import java.util.List;

import io.reactivex.Flowable;
import me.bakumon.moneykeeper.bean.UIRecord;
import me.bakumon.moneykeeper.database.entity.Record;
import me.bakumon.moneykeeper.datasource.AppDataSource;

/**
 * 主页 ViewModel
 *
 * @author Bakumon https://bakumon.me
 */
public class HomeViewModel extends ViewModel {
    private static final String TAG = HomeViewModel.class.getSimpleName();
    private final AppDataSource mDataSource;

    public HomeViewModel(AppDataSource dataSource) {
        mDataSource = dataSource;
    }

    public Flowable<List<Record>> getAllRecord() {
        return mDataSource.getAllRecord();
    }

    public Flowable<List<UIRecord>> getAllUIRecord() {
        return mDataSource.getAllUIRecord();
    }
}
