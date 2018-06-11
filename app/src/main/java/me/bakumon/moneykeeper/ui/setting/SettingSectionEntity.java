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

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * 分组布局实体
 *
 * @author Bakumon https://bakumon.me
 */
public class SettingSectionEntity extends SectionEntity<SettingSectionEntity.Item> {
    public SettingSectionEntity(String header) {
        super(true, header);
    }

    public SettingSectionEntity(Item item) {
        super(item);
    }

    public static class Item {
        public Item(String content) {
            this.title = null;
            this.content = content;
            this.isShowSwitch = false;
            this.isConfigOpen = false;
        }

        public Item(String title, String content) {
            this.title = title;
            this.content = content;
            this.isShowSwitch = false;
            this.isConfigOpen = false;
        }

        public Item(String title, String content, boolean isConfigOpen) {
            this.title = title;
            this.content = content;
            this.isShowSwitch = true;
            this.isConfigOpen = isConfigOpen;
        }

        public String title;
        public String content;
        public boolean isShowSwitch;
        public boolean isConfigOpen;
    }
}
