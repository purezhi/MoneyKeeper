package me.bakumon.moneykeeper.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import me.bakumon.moneykeeper.datasource.AppDataSource;
import me.bakumon.moneykeeper.ui.add.AddViewModel;
import me.bakumon.moneykeeper.ui.home.HomeViewModel;

/**
 * ViewModel 工厂
 *
 * @author Bakumon https://bakumon.me
 */
public class ViewModelFactory implements ViewModelProvider.Factory {
    private final AppDataSource mDataSource;

    public ViewModelFactory(AppDataSource dataSource) {
        mDataSource = dataSource;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AddViewModel.class)) {
            return (T) new AddViewModel(mDataSource);
        } else if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(mDataSource);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
