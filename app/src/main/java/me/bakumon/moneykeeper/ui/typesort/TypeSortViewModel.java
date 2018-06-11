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

package me.bakumon.moneykeeper.ui.typesort;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.bakumon.moneykeeper.base.BaseViewModel;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.datasource.AppDataSource;

/**
 * 类型排序 ViewModel
 *
 * @author Bakumon https://bakumon.me
 */
public class TypeSortViewModel extends BaseViewModel {
    public TypeSortViewModel(AppDataSource dataSource) {
        super(dataSource);
    }

    public Flowable<List<RecordType>> getRecordTypes(int type) {
        return mDataSource.getRecordTypes(type);
    }

    public Completable sortRecordTypes(List<RecordType> recordTypes) {
        return mDataSource.sortRecordTypes(recordTypes);
    }
}
