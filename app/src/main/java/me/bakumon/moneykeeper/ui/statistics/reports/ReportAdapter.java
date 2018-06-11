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

import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;

import java.util.List;

import me.bakumon.moneykeeper.BR;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.base.BaseDataBindingAdapter;
import me.bakumon.moneykeeper.database.entity.TypeSumMoneyBean;

/**
 * ReportAdapter
 *
 * @author bakumon https://bakumon.me
 * @date 2018/5/25
 */

public class ReportAdapter extends BaseDataBindingAdapter<TypeSumMoneyBean> {

    public ReportAdapter(@Nullable List<TypeSumMoneyBean> data) {
        super(R.layout.item_report, data);
    }

    @Override
    protected void convert(DataBindingViewHolder helper, TypeSumMoneyBean item) {
        ViewDataBinding binding = helper.getBinding();
        binding.setVariable(BR.typeSumMoney, item);
        binding.executePendingBindings();
    }
}
