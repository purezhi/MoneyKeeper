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

package me.bakumon.moneykeeper.ui.add;

import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import me.bakumon.moneykeeper.App;
import me.bakumon.moneykeeper.BR;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.Router;
import me.bakumon.moneykeeper.base.BaseDataBindingAdapter;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.drakeet.floo.Floo;

/**
 * TypeAdapter
 *
 * @author bakumon https://bakumon.me
 * @date 2018/4/9
 */

public class TypeAdapter extends BaseDataBindingAdapter<RecordType> {

    private int mCurrentCheckPosition;
    private int mCurrentCheckId = -1;
    private int mType;

    public TypeAdapter(@Nullable List<RecordType> data) {
        super(R.layout.item_type, data);
    }

    @Override
    protected void convert(DataBindingViewHolder helper, RecordType item) {
        ViewDataBinding binding = helper.getBinding();
        binding.setVariable(BR.recordType, item);
        binding.executePendingBindings();
    }

    /**
     * 筛选出支出和收入
     *
     * @param data 支出和收入总数据
     * @param type 类型 0：支出 1：收入
     * @see RecordType#TYPE_OUTLAY 支出
     * @see RecordType#TYPE_INCOME 收入
     */
    public void setNewData(@Nullable List<RecordType> data, int type) {
        mType = type;
        if (data != null && data.size() > 0) {
            List<RecordType> result = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).type == type) {
                    result.add(data.get(i));
                }
            }
            // 增加设置 item， type == -1 表示是设置 item
            RecordType settingItem = new RecordType(App.getINSTANCE().getString(R.string.text_setting), "type_item_setting", -1);
            result.add(settingItem);
            // 找出上次选中的 item
            int checkPosition = 0;
            if (result.get(0).type != -1) {
                for (int i = 0; i < result.size(); i++) {
                    if (result.get(i).id == mCurrentCheckId) {
                        checkPosition = i;
                        break;
                    }
                }
                super.setNewData(result);
                clickItem(checkPosition);
            } else {
                super.setNewData(result);
            }
        } else {
            super.setNewData(null);
        }
    }

    /**
     * 选中某一个 item，或点击设置 item
     *
     * @param position 选中 item 的索引
     */
    public void clickItem(int position) {
        // 点击设置 item
        RecordType item = getItem(position);
        if (item != null && item.type == -1) {
            Floo.navigation(mContext, Router.Url.URL_TYPE_MANAGE)
                    .putExtra(Router.ExtraKey.KEY_TYPE, mType)
                    .start();
            return;
        }
        // 选中某一个 item
        RecordType temp;
        for (int i = 0; i < getData().size(); i++) {
            temp = getData().get(i);
            if (temp != null && temp.type != -1) {
                temp.isChecked = i == position;
            }
        }
        mCurrentCheckPosition = position;
        mCurrentCheckId = getCurrentItem().id;
        notifyDataSetChanged();
    }

    /**
     * 获取当前选中的 item
     */
    public RecordType getCurrentItem() {
        return getItem(mCurrentCheckPosition);
    }
}
