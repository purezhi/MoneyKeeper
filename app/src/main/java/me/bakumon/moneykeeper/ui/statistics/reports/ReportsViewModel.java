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

package me.bakumon.moneykeeper.ui.statistics.reports;

import java.util.Date;
import java.util.List;

import io.reactivex.Flowable;
import me.bakumon.moneykeeper.base.BaseViewModel;
import me.bakumon.moneykeeper.database.entity.SumMoneyBean;
import me.bakumon.moneykeeper.database.entity.TypeSumMoneyBean;
import me.bakumon.moneykeeper.datasource.AppDataSource;
import me.bakumon.moneykeeper.utill.DateUtils;

/**
 * 统计-报表
 *
 * @author Bakumon https://bakumon.me
 */
public class ReportsViewModel extends BaseViewModel {
    public ReportsViewModel(AppDataSource dataSource) {
        super(dataSource);
    }

    public Flowable<List<SumMoneyBean>> getMonthSumMoney(int year, int month) {
        Date dateFrom = DateUtils.getMonthStart(year, month);
        Date dateTo = DateUtils.getMonthEnd(year, month);
        return mDataSource.getMonthSumMoney(dateFrom, dateTo);
    }

    public Flowable<List<TypeSumMoneyBean>> getTypeSumMoney(int year, int month, int type) {
        Date dateFrom = DateUtils.getMonthStart(year, month);
        Date dateTo = DateUtils.getMonthEnd(year, month);
        return mDataSource.getTypeSumMoney(dateFrom, dateTo, type);
    }

}
