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
