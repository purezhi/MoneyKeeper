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
            BigDecimal typeMoney = typeSumMoneyBeans.get(i).typeSumMoney;
            entryList.add(new PieEntry(typeMoney.intValue(), typeSumMoneyBeans.get(i).typeName, typeSumMoneyBeans.get(i)));
        }
        return entryList;
    }
}
