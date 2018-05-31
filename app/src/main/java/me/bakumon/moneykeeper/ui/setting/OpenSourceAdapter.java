package me.bakumon.moneykeeper.ui.setting;

import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;

import java.util.List;

import me.bakumon.moneykeeper.BR;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.base.BaseDataBindingAdapter;

/**
 * 开源许可证数据适配器
 *
 * @author Bakumon https://bakumon.me
 */
public class OpenSourceAdapter extends BaseDataBindingAdapter<OpenSourceBean> {
    public OpenSourceAdapter(@Nullable List<OpenSourceBean> data) {
        super(R.layout.item_open_source, data);
    }

    @Override
    protected void convert(DataBindingViewHolder helper, OpenSourceBean item) {
        ViewDataBinding binding = helper.getBinding();
        binding.setVariable(BR.openSource, item);
        binding.executePendingBindings();
    }
}
