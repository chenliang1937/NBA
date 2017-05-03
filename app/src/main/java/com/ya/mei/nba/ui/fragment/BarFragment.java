package com.ya.mei.nba.ui.fragment;

import android.animation.PropertyValuesHolder;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.db.chart.Tools;
import com.db.chart.listener.OnEntryClickListener;
import com.db.chart.model.BarSet;
import com.db.chart.view.BarChartView;
import com.db.chart.view.ChartView;
import com.db.chart.view.Tooltip;
import com.db.chart.view.XController;
import com.db.chart.view.YController;
import com.db.chart.view.animation.Animation;
import com.thefinestartist.finestwebview.FinestWebView;
import com.ya.mei.nba.R;
import com.ya.mei.nba.event.StatEvent;
import com.ya.mei.nba.ui.fragment.base.BaseFragment;
import com.ya.mei.nba.utils.AnimatorUtils;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by chenliang3 on 2016/3/8.
 */
public class BarFragment extends BaseFragment {

    @Bind(R.id.cardItem)
    View cardItem;
    @Bind(R.id.barchart1)
    BarChartView statChart;
    @Bind(R.id.rl_change)
    View changeLayout;
    @Bind(R.id.type)
    TextView typeTV;
    @Bind(R.id.change)
    ImageView change;

    private static final String STATKIND = "STATKIND";
    private static final String CURRENTCOLOR = "CURRENTCOLOR";
    private static final int PRECOLOR = Color.parseColor("#bcaaa4");
    private static final int CHART_TEXT_COLOR = Color.parseColor("#ffffff");
    private static final int STEP = 2;
    private boolean isAnimating;
    private int currentColor;
    private String statKind;
    private boolean showDaily = true;
    private String[] lables;
    private String[] playerUrls;
    private float[] statValues;
    private String[] playerUrlsDaily;
    private String[] lablesDaily;
    private float[] statValuesDaily;
    private String[] lablesEverage;
    private String[] playerUrlsEverage;
    private float[] statValuesEverage;
    private int max = 16;
    Paint gridPaint;
    BarChartView barChart;

    public static BarFragment newInstance(String statKind, int currentColor){
        BarFragment barFragment = new BarFragment();
        Bundle args = new Bundle();
        args.putString(STATKIND, statKind);
        args.putInt(CURRENTCOLOR, currentColor);
        barFragment.setArguments(args);
        return barFragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_bar;
    }

    @Override
    protected void initView() {
        parseArguments();
        changeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lables != null && statValues != null){
                    dismissChart(0, statChart, change);
                }
            }
        });
    }

    private void parseArguments(){
        Bundle args = getArguments();
        if (args == null){
            return;
        }
        statKind = args.getString(STATKIND);
        currentColor = args.getInt(CURRENTCOLOR);
    }

    public void onEventMainThread(StatEvent statEvent){
        if (statKind.equals(statEvent.getStatKind())){
            lablesDaily = statEvent.getLables()[0];
            lablesEverage = statEvent.getLables()[1];
            playerUrlsDaily = statEvent.getPlayerUrls()[0];
            playerUrlsEverage = statEvent.getPlayerUrls()[1];
            statValuesDaily = statEvent.getStatValues()[0];
            statValuesEverage = statEvent.getStatValues()[1];
            if (barChart != null){
                showDaily = true;
                dismissChart(0, statChart, change);
            }else {
                showChart(0, statChart, change);
            }
        }
    }

    /**
     * show a cardView chart
     * @param tag
     * @param chart
     * @param btn
     */
    private void showChart(final int tag, final ChartView chart, final View btn){
        if (isAnimating){
            return;
        }
        isAnimating = true;
        dismissPlay(btn);
        Runnable action = new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showPlay(btn);
                    }
                }, 500);
            }
        };
        switch (tag){
            case 0:
                produceOne(chart, action);
                break;
            default:
        }
    }

    /**
     * dismiss a cardView chart
     * @param tag
     * @param chart
     * @param btn
     */
    private void dismissChart(final int tag, final ChartView chart, final View btn){
        dismissPlay(btn);

        Runnable action = new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showPlay(btn);
                        showChart(tag, chart, btn);
                    }
                }, 500);
            }
        };
        switch (tag){
            case 0:
                dismissOne(chart, action);
                break;
            default:
        }
    }

    /**
     * show cardView play button
     * @param btn
     */
    private void showPlay(View btn){
        btn.setEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            btn.animate().alpha(1).scaleX(1).scaleY(1);
        }else {
            btn.setVisibility(View.VISIBLE);
        }
    }

    /**
     * dismiss cardView play button
     * @param btn
     */
    private void dismissPlay(View btn){
        btn.setEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            btn.animate().alpha(0).scaleX(0).scaleY(0);
        }else {
            btn.setVisibility(View.INVISIBLE);
        }
    }

    private void prepareStat(){
        lables = showDaily ? lablesDaily : lablesEverage;
        playerUrls = showDaily ? playerUrlsDaily : playerUrlsEverage;
        statValues = showDaily ? statValuesDaily : statValuesEverage;
        typeTV.setText(showDaily ? R.string.daily : R.string.everage);
        max = ((int)statValues[0]/STEP + 1) * STEP;
        showDaily = !showDaily;
    }

    public void produceOne(ChartView chart, Runnable action){
        if (getActivity() == null){
            return;
        }
        int mCurrentColor;
        int preColor;
        preColor = showDaily ? PRECOLOR : currentColor;
        mCurrentColor = showDaily ? currentColor : PRECOLOR;
        prepareStat();

        AnimatorUtils.showCardBackgroundColorAnimation(cardItem, preColor, mCurrentColor, 400);
        if (lables.length < 5 || statValues.length < 5){
            return;
        }

        barChart = (BarChartView) chart;
        barChart.setOnEntryClickListener(new OnEntryClickListener() {
            @Override
            public void onClick(int setIndex, int entryIndex, Rect rect) {
                new FinestWebView.Builder(getActivity())
                        .gradientDivider(false)
                        .show(playerUrls[entryIndex]);
            }
        });

        Tooltip tooltip = new Tooltip(getActivity(), R.layout.barchart_one_tooltip);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            tooltip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1));
            tooltip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 0));
        }
        barChart.setTooltips(tooltip);

        BarSet barSet = new BarSet(lables, statValues);
        barSet.setColor(preColor);
        barChart.addData(barSet);
        barChart.setSetSpacing(Tools.fromDpToPx(-15));
        barChart.setBarSpacing(Tools.fromDpToPx(20));
        barChart.setRoundCorners(Tools.fromDpToPx(4));

        gridPaint = new Paint();
        gridPaint.setColor(CHART_TEXT_COLOR);
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(.75f));

        barChart.setBorderSpacing(5)
                .setAxisBorderValues(0, max, STEP)
                .setGrid(BarChartView.GridType.FULL, max, 6, gridPaint)
                .setYAxis(false)
                .setXLabels(XController.LabelPosition.OUTSIDE)
                .setYLabels(YController.LabelPosition.OUTSIDE)
                .setLabelsColor(CHART_TEXT_COLOR)
                .setAxisColor(CHART_TEXT_COLOR);

        int[] order = {2, 1, 3, 0, 4};
        final Runnable auxAction = action;
        Runnable chartOneAction = new Runnable() {
            @Override
            public void run() {
                showTooltipOne();
                auxAction.run();
            }
        };
        barChart.show(new Animation()
                .setOverlap(.5f, order)
                .setEndAction(chartOneAction));
    }

    public void dismissOne(ChartView chart, Runnable action){
        dismissTooltipOne();

        int[] order = {0, 4, 1, 3, 2};
        chart.dismiss(new Animation()
                .setOverlap(.5f, order)
                .setEndAction(action));
    }

    private void showTooltipOne(){
        ArrayList<Rect> areas = statChart.getEntriesArea(0);
        for (int i = 0; i < areas.size(); i++){
            if (getActivity() == null){
                return;
            }
            Tooltip tooltip = new Tooltip(getActivity(), R.layout.barchart_one_tooltip, R.id.value);
            tooltip.prepare(areas.get(i), statValues[i]);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
                tooltip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1));
                tooltip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 0));
            }
            statChart.showTooltip(tooltip, true);
            if (i == areas.size()-1){
                isAnimating = false;
            }
        }
    }

    private void dismissTooltipOne(){
        statChart.dismissAllTooltips();
    }

}
