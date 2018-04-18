package me.bakumon.moneykeeper.binding;

import android.databinding.BindingAdapter;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import me.bakumon.moneykeeper.database.entity.RecordType;


/**
 * RecordType for DataBinding
 *
 * @author Bakumon https://bakumon.me
 */
public class DBRecordType {

    public DBRecordType(RecordType recordType, int width) {
        mRecordType = recordType;
        mWidth = width;
    }

    public RecordType mRecordType;
    public int mWidth;

    @BindingAdapter("android:visibility")
    public static void showHide(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("android:layout_width")
    public static void setLayoutWidth(View view, int width) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = width;
    }

    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, @DrawableRes int resId) {
        view.setImageResource(resId);
    }
}
