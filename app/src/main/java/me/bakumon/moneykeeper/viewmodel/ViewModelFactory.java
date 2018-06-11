/*
 * Copyright 2018 Bakumon. https://github.com/Bakumon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package me.bakumon.moneykeeper.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import me.bakumon.moneykeeper.datasource.AppDataSource;
import me.bakumon.moneykeeper.ui.add.AddRecordViewModel;
import me.bakumon.moneykeeper.ui.addtype.AddTypeViewModel;
import me.bakumon.moneykeeper.ui.home.HomeViewModel;
import me.bakumon.moneykeeper.ui.statistics.bill.BillViewModel;
import me.bakumon.moneykeeper.ui.statistics.reports.ReportsViewModel;
import me.bakumon.moneykeeper.ui.typemanage.TypeManageViewModel;
import me.bakumon.moneykeeper.ui.typerecords.TypeRecordsViewModel;
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
        } else if (modelClass.isAssignableFrom(BillViewModel.class)) {
            return (T) new BillViewModel(mDataSource);
        } else if (modelClass.isAssignableFrom(ReportsViewModel.class)) {
            return (T) new ReportsViewModel(mDataSource);
        } else if (modelClass.isAssignableFrom(TypeRecordsViewModel.class)) {
            return (T) new TypeRecordsViewModel(mDataSource);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
