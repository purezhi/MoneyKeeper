package me.bakumon.moneykeeper.ui.home;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import me.bakumon.moneykeeper.R;
import me.bakumon.moneykeeper.bean.HomeBean;

/**
 * HomeAdapter
 *
 * @author bakumon https://bakumon.me
 * @date 2018/4/9
 */

public class HomeAdapter extends BaseQuickAdapter<HomeBean, BaseViewHolder> {

    public HomeAdapter(@Nullable List<HomeBean> data) {
        super(R.layout.item_home, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeBean item) {
        View view = helper.getView(R.id.tv_date);
        if (helper.getAdapterPosition() == 0) {
            view.setVisibility(View.VISIBLE);
        } else {
            if (TextUtils.equals(item.createDate, getData().get(helper.getAdapterPosition() - 1).createDate)) {
                view.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.VISIBLE);
            }
        }
    }
}
