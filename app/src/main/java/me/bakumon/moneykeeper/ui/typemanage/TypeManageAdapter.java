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

package me.bakumon.moneykeeper.ui.typemanage;

import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import me.bakumon.moneykeeper.BR;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.base.BaseDataBindingAdapter;
import me.bakumon.moneykeeper.database.entity.RecordType;

/**
 * 类型管理适配器
 *
 * @author bakumon https://bakumon.me
 * @date 2018/5/4
 */

public class TypeManageAdapter extends BaseDataBindingAdapter<RecordType> {

    public TypeManageAdapter(@Nullable List<RecordType> data) {
        super(R.layout.item_type_manage, data);
    }

    @Override
    protected void convert(DataBindingViewHolder helper, RecordType item) {
        ViewDataBinding binding = helper.getBinding();

        binding.setVariable(BR.recordType, item);
        boolean isLastItem = helper.getAdapterPosition() == mData.size() - 1;
        // 单位是 dp
        binding.setVariable(BR.bottomMargin, isLastItem ? 100 : 0);

        binding.executePendingBindings();
    }

    public void setNewData(List<RecordType> data, int type) {
        if (data != null && data.size() > 0) {
            List<RecordType> result = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).type == type) {
                    result.add(data.get(i));
                }
            }
            super.setNewData(result);
        } else {
            super.setNewData(null);
        }
    }
}
