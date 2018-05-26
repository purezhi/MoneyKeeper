package me.bakumon.moneykeeper.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import me.bakumon.moneykeeper.R;

/**
 * 柱状图 MarkerView
 *
 * @author Bakumon https://bakumon.me
 */
@SuppressLint("ViewConstructor")
public class BarChartMarkerView extends MarkerView {
    private TextView tvContent;

    public BarChartMarkerView(Context context) {
        super(context, R.layout.bar_chart_marker_view);
        tvContent = findViewById(R.id.tv_content);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        String content = (int) e.getX() + getContext().getString(R.string.text_day) + getResources().getString(R.string.text_money_symbol) + e.getY();
        tvContent.setText(content);
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
