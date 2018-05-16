package me.bakumon.moneykeeper.base;

import android.arch.lifecycle.ViewModel;

import me.bakumon.moneykeeper.datasource.AppDataSource;

/**
 * ViewModel基类
 * 包含 AppDataSource 数据源
 *
 * @author Bakumon https://bakumon
 */
public class BaseViewModel extends ViewModel {
    protected AppDataSource mDataSource;

    public BaseViewModel(AppDataSource dataSource) {
        mDataSource = dataSource;
    }
}
