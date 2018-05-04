package me.bakumon.moneykeeper.ui.typemanage;

import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;

import java.util.List;

import me.bakumon.moneykeeper.BR;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.base.BaseDataBindingAdapter;
import me.bakumon.moneykeeper.base.BaseDraggableAdapter;
import me.bakumon.moneykeeper.database.entity.RecordType;

/**
 * TypeManageAdapter
 *
 * @author bakumon https://bakumon.me
 * @date 2018/5/4
 */

public class TypeManageAdapter extends BaseDraggableAdapter<RecordType> {

    public TypeManageAdapter(@Nullable List<RecordType> data) {
        super(R.layout.item_type_manage, data);
    }

    @Override
    protected void convert(DataBindingViewHolder helper, RecordType item) {
        ViewDataBinding binding = helper.getBinding();

        binding.setVariable(BR.recordType, item);

        binding.executePendingBindings();
    }
}
