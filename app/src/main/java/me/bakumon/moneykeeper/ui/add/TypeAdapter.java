package me.bakumon.moneykeeper.ui.add;

import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import me.bakumon.moneykeeper.App;
import me.bakumon.moneykeeper.BR;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.Router;
import me.bakumon.moneykeeper.base.BaseDataBindingAdapter;
import me.bakumon.moneykeeper.database.entity.RecordType;
import me.bakumon.moneykeeper.ui.typemanage.TypeManageActivity;
import me.drakeet.floo.Floo;

/**
 * TypeAdapter
 *
 * @author bakumon https://bakumon.me
 * @date 2018/4/9
 */

public class TypeAdapter extends BaseDataBindingAdapter<RecordType> implements BaseQuickAdapter.OnItemClickListener {

    private int mCurrentCheckPosition;
    private int mType;

    public TypeAdapter(@Nullable List<RecordType> data) {
        super(R.layout.item_type, data);
        setOnItemClickListener(this);
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
            super.setNewData(result);
            // 选中第一个
            if (result.get(0).type != -1) {
                clickItem(0);
            }
        } else {
            super.setNewData(null);
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        clickItem(position);
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
            Floo.navigation(mContext, Router.TYPE_MANAGE)
                    .putExtra(TypeManageActivity.KEY_TYPE, mType)
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
        notifyDataSetChanged();
    }

    /**
     * 获取当前选中的 item
     */
    public RecordType getCurrentItem() {
        return getItem(mCurrentCheckPosition);
    }
}
