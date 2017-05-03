package com.ya.mei.nba.ui.fragment;

import android.graphics.Color;
import android.support.v4.view.ViewPager;

import com.ya.mei.nba.Conf.Constant;
import com.ya.mei.nba.R;
import com.ya.mei.nba.app.AppService;
import com.ya.mei.nba.event.Event;
import com.ya.mei.nba.event.RefreshIconEvent;
import com.ya.mei.nba.event.RhythmPositionEvent;
import com.ya.mei.nba.ui.adapter.StatPageAdapter;
import com.ya.mei.nba.ui.fragment.base.BaseFragment;
import com.ya.mei.nba.ui.widget.RhythmAdapter;
import com.ya.mei.nba.ui.widget.RhythmLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by chenliang3 on 2016/3/8.
 */
public class StatisticsFragment extends BaseFragment{

    @Bind(R.id.statistics_rhythmLayout)
    RhythmLayout rhythmLayout;
    @Bind(R.id.statistics_viewPager)
    ViewPager viewPager;

    private List<BarFragment> barFragments = new ArrayList<>(5);
    private int currentPosition;
    private static final String[] statKinds = {"points", "reb", "assi", "ste", "blk"};
    private static final int[] chartColors = {Color.parseColor("#26a69a"),Color.parseColor("#5c6bc0"),
            Color.parseColor("#42a5f5"), Color.parseColor("#4dd0e1"),Color.parseColor("#66bb6a")};

    public static StatisticsFragment newInstance(){
        StatisticsFragment statisticsFragment = new StatisticsFragment();
        return statisticsFragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_statistics;
    }

    @Override
    protected void initView() {
        for (int index = 0; index < statKinds.length; index++){
            barFragments.add(BarFragment.newInstance(statKinds[index], chartColors[index]));
        }
        RhythmAdapter adapter = new RhythmAdapter(getContext(), chartColors);
        rhythmLayout.setAdapter(adapter);
        viewPager.setAdapter(new StatPageAdapter(getChildFragmentManager(), barFragments));
        viewPager.setOffscreenPageLimit(5);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                rhythmLayout.showRhythmAtPosition(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        refreshData();
    }

    private void refreshData(){
        AppService.getInstance().getPerStat(getTaskId(), statKinds);
    }

    //no need initdata temporary
    private void initData(){
        AppService.getInstance().initPerStat(getTaskId(), statKinds[0]);
    }

    public void onEventMainThread(Event event){
        if (event instanceof RhythmPositionEvent){
            currentPosition = ((RhythmPositionEvent) event).getPosition();
            viewPager.setCurrentItem(currentPosition, true);
        }else if (event instanceof RefreshIconEvent){
            if (event.getmEventResult().equals(Constant.Result.NORMAL)){
                refreshData();
            }
        }
    }

}
