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

package me.bakumon.moneykeeper.ui.setting;

import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;

import java.util.List;

import me.bakumon.moneykeeper.BR;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.base.BaseDataBindingAdapter;

/**
 * 开源许可证数据适配器
 *
 * @author Bakumon https://bakumon.me
 */
public class OpenSourceAdapter extends BaseDataBindingAdapter<OpenSourceBean> {
    public OpenSourceAdapter(@Nullable List<OpenSourceBean> data) {
        super(R.layout.item_open_source, data);
    }

    @Override
    protected void convert(DataBindingViewHolder helper, OpenSourceBean item) {
        ViewDataBinding binding = helper.getBinding();
        binding.setVariable(BR.openSource, item);
        binding.executePendingBindings();
    }
}
