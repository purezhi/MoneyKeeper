package me.bakumon.moneykeeper.ui.addtype;

import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;

import java.util.List;

import me.bakumon.moneykeeper.BR;
import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.base.BaseDataBindingAdapter;

/**
 * 类型图片适配器
 *
 * @author bakumon https://bakumon.me
 * @date 2018/5/16
 */

public class TypeImgAdapter extends BaseDataBindingAdapter<TypeImgBean> {

    private int mCurrentCheckPosition;

    public TypeImgAdapter(@Nullable List<TypeImgBean> data) {
        super(R.layout.item_type_img, data);
    }

    @Override
    protected void convert(DataBindingViewHolder helper, TypeImgBean item) {
        ViewDataBinding binding = helper.getBinding();
        binding.setVariable(BR.typeImg, item);
        binding.executePendingBindings();
    }

    public void checkItem(int position) {
        // 选中某一个 item
        TypeImgBean temp;
        for (int i = 0; i < getData().size(); i++) {
            temp = getData().get(i);
            if (temp != null) {
                temp.isChecked = i == position;
            }
        }
        mCurrentCheckPosition = position;
        notifyDataSetChanged();
    }

    /**
     * 获取当前选中的 item
     */
    public TypeImgBean getCurrentItem() {
        return getItem(mCurrentCheckPosition);
    }
}
