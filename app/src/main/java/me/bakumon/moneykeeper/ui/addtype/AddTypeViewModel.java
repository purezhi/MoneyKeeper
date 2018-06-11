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

package me.bakumon.moneykeeper.ui.addtype;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import me.bakumon.moneykeeper.base.BaseViewModel;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.datasource.AppDataSource;

/**
 * 添加记账类型 ViewModel
 *
 * @author Bakumon https://bakumon.me
 */
public class AddTypeViewModel extends BaseViewModel {
    public AddTypeViewModel(AppDataSource dataSource) {
        super(dataSource);
    }

    public Flowable<List<TypeImgBean>> getAllTypeImgBeans(int type) {
        return mDataSource.getAllTypeImgBeans(type);
    }

    /**
     * 保存记账类型，包括新增和更新
     *
     * @param recordType 修改时传
     * @param type       类型
     * @param imgName    图片
     * @param name       类型名称
     */
    public Completable saveRecordType(RecordType recordType, int type, String imgName, String name) {
        if (recordType == null) {
            // 添加
            return mDataSource.addRecordType(type, imgName, name);
        } else {
            // 修改
            RecordType updateType = new RecordType(recordType.id, name, imgName, recordType.type, recordType.ranking);
            updateType.state = recordType.state;
            return mDataSource.updateRecordType(recordType, updateType);
        }
    }
}
