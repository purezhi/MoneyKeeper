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

import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemDragListener;

import java.util.List;

import me.bakumon.moneykeeper.BR;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.base.BaseDraggableAdapter;
import me.bakumon.moneykeeper.database.entity.RecordType;

/**
 * 类型排序列表适配器
 *
 * @author bakumon https://bakumon.me
 * @date 2018/5/10
 */

public class TypeSortAdapter extends BaseDraggableAdapter<RecordType> implements OnItemDragListener {

    public TypeSortAdapter(@Nullable List<RecordType> data) {
        super(R.layout.item_type_sort, data);
        setOnItemDragListener(this);
    }

    @Override
    protected void convert(DataBindingViewHolder helper, RecordType item) {
        ViewDataBinding binding = helper.getBinding();
        binding.setVariable(BR.recordType, item);
        binding.executePendingBindings();
    }

    @Override
    public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
        ((BaseViewHolder) viewHolder)
                .itemView
                .setAlpha(0.8f);
    }

    @Override
    public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {

    }

    @Override
    public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
        ((BaseViewHolder) viewHolder)
                .itemView
                .setAlpha(1f);
    }
}
