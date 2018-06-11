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

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import me.bakumon.moneykeeper.R;

/**
 * @author Bakumon https://bakumon.me
 */
public class SettingAdapter extends BaseSectionQuickAdapter<SettingSectionEntity, BaseViewHolder> {

    public SettingAdapter(List<SettingSectionEntity> data) {
        super(R.layout.item_setting, R.layout.item_setting_head, data);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, SettingSectionEntity item) {
        helper.setText(R.id.tv_head, item.header);
    }

    @Override
    protected void convert(BaseViewHolder helper, SettingSectionEntity item) {
        helper.setText(R.id.tv_title, item.t.title)
                .setGone(R.id.tv_title, !TextUtils.isEmpty(item.t.title))
                .setText(R.id.tv_content, item.t.content)
                .setGone(R.id.tv_content, !TextUtils.isEmpty(item.t.content))
                .setVisible(R.id.switch_item, item.t.isShowSwitch)
                .setChecked(R.id.switch_item, item.t.isConfigOpen)
                .addOnClickListener(R.id.switch_item);
    }
}
