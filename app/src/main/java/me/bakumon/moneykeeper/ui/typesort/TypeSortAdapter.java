package me.bakumon.moneykeeper.ui.typesort;

import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;

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

public class TypeSortAdapter extends BaseDraggableAdapter<RecordType> {

    public TypeSortAdapter(@Nullable List<RecordType> data) {
        super(R.layout.item_type_sort, data);
    }

    @Override
    protected void convert(DataBindingViewHolder helper, RecordType item) {
        ViewDataBinding binding = helper.getBinding();

        binding.setVariable(BR.recordType, item);

        binding.executePendingBindings();
    }
}
