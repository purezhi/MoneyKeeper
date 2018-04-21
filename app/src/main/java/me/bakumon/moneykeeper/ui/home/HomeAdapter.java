package me.bakumon.moneykeeper.ui.home;

import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.List;

import me.bakumon.moneykeeper.BR;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.base.BaseDataBindingAdapter;
import me.bakumon.moneykeeper.bean.UIRecord;
import me.bakumon.moneykeeper.utill.DateUtils;

/**
 * HomeAdapter
 *
 * @author bakumon https://bakumon.me
 * @date 2018/4/9
 */

public class HomeAdapter extends BaseDataBindingAdapter<UIRecord> {

    public HomeAdapter(@Nullable List<UIRecord> data) {
        super(R.layout.item_home, data);
    }

    @Override
    protected void convert(DataBindingViewHolder helper, UIRecord item) {
        ViewDataBinding binding = helper.getBinding();
        binding.setVariable(BR.uiRecord, item);
        boolean isDataShow = helper.getAdapterPosition() == 0 ||
                !DateUtils.isSameDay(item.mRecord.time, getData().get(helper.getAdapterPosition() - 1).mRecord.time);
        binding.setVariable(BR.isDataShow, isDataShow);
        binding.executePendingBindings();
    }
}
