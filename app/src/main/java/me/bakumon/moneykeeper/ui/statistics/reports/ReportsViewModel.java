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
