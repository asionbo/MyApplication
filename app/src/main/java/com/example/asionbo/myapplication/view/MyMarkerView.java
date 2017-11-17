package com.example.asionbo.myapplication.view;

import android.content.Context;
import android.widget.TextView;

import com.example.asionbo.myapplication.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;

/**
 * Created by asionbo on 2017/10/9.
 */

public class MyMarkerView extends MarkerView {

    private TextView mContent;
    private IAxisValueFormatter xAxisValueFormatter;

    private DecimalFormat format;

    public MyMarkerView(Context context, IAxisValueFormatter xAxisValueFormatter) {
        super(context, R.layout.item_marketview);

        this.xAxisValueFormatter = xAxisValueFormatter;
        mContent = (TextView) findViewById(R.id.tv_content);
        format = new DecimalFormat("##0.0");

    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        mContent.setText("x: " + xAxisValueFormatter.getFormattedValue(e.getX(), null)
                + ", y: " + format.format(e.getY())+"百万");
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-getWidth(),-getHeight());
    }
}
