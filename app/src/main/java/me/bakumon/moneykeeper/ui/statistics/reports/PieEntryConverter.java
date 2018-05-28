package me.bakumon.moneykeeper.ui.statistics.reports;

import com.github.mikephil.charting.data.PieEntry;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import me.bakumon.moneykeeper.database.entity.TypeSumMoneyBean;

/**
 * 饼状图数据转换器
 *
 * @author Bakumon https://bakumon.me
 */
public class PieEntryConverter {
    /**
     * 获取饼状图所需数据格式 PieEntry
     *
     * @param typeSumMoneyBeans 类型汇总数据
     * @return List<PieEntry>
     */
    public static List<PieEntry> getBarEntryList(List<TypeSumMoneyBean> typeSumMoneyBeans) {
        List<PieEntry> entryList = new ArrayList<>();
        for (int i = 0; i < typeSumMoneyBeans.size(); i++) {
            BigDecimal typeMoney = new BigDecimal(typeSumMoneyBeans.get(i).typeSumMoney);
            entryList.add(new PieEntry(typeMoney.intValue(), typeSumMoneyBeans.get(i).typeName, typeSumMoneyBeans.get(i)));
        }
        return entryList;
    }
}
