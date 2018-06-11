/*
 * Copyright 2018 Bakumon. https://github.com/Bakumon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
        if (e.getY() > 0) {
            tvContent.setVisibility(VISIBLE);
        } else {
            tvContent.setVisibility(GONE);
        }
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
