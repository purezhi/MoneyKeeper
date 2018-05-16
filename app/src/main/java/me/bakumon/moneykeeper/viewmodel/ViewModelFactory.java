package me.bakumon.moneykeeper.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import me.bakumon.moneykeeper.datasource.AppDataSource;
import me.bakumon.moneykeeper.ui.add.AddRecordViewModel;
import me.bakumon.moneykeeper.ui.addtype.AddTypeViewModel;
import me.bakumon.moneykeeper.ui.home.HomeViewModel;
import me.bakumon.moneykeeper.ui.typemanage.TypeManageViewModel;
import me.bakumon.moneykeeper.ui.typesort.TypeSortViewModel;

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
        if (modelClass.isAssignableFrom(AddRecordViewModel.class)) {
            return (T) new AddRecordViewModel(mDataSource);
        } else if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(mDataSource);
        } else if (modelClass.isAssignableFrom(TypeManageViewModel.class)) {
            return (T) new TypeManageViewModel(mDataSource);
        } else if (modelClass.isAssignableFrom(TypeSortViewModel.class)) {
            return (T) new TypeSortViewModel(mDataSource);
        } else if (modelClass.isAssignableFrom(AddTypeViewModel.class)) {
            return (T) new AddTypeViewModel(mDataSource);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
