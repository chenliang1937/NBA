package com.ya.mei.nba.ui.fragment;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.ya.mei.nba.Conf.Constant;
import com.ya.mei.nba.R;
import com.ya.mei.nba.app.AppService;
import com.ya.mei.nba.event.DataIconEvent;
import com.ya.mei.nba.event.Event;
import com.ya.mei.nba.event.GamesEvent;
import com.ya.mei.nba.model.Games;
import com.ya.mei.nba.ui.adapter.GamesAdapter;
import com.ya.mei.nba.ui.fragment.base.SwipeRefreshBaseFragment;
import com.ya.mei.nba.utils.DateFormatter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by chenliang3 on 2016/3/9.
 */
public class GameFragment extends SwipeRefreshBaseFragment {

    @Bind(R.id.newsRecycleView)
    RecyclerView recyclerView;

    private GamesAdapter adapter;

    private String date;
    private String dateToday = DateFormatter.formatDate("yyyy-MM-dd");
    private List<Games.GamesEntity> gamesEntities = new ArrayList<>();

    public static GameFragment newInstance(){
        GameFragment gameFragment = new GameFragment();
        return gameFragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView() {
        super.initView();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new GamesAdapter(getActivity(), gamesEntities);
        recyclerView.setAdapter(adapter);
        date = dateToday;
        setRefreshing();
    }

    @Override
    public void onRefresh() {
        AppService.getInstance().getGames(getTaskId(), date);
        date = dateToday;
    }

    public void onEventMainThread(Event event){
        if (event instanceof GamesEvent){
            stopRefreshing();
            if (Constant.Result.FAIL.equals(((GamesEvent) event).getmEventResult())){
                return;
            }
            gamesEntities.clear();
            gamesEntities.addAll(((GamesEvent) event).getAllGames().getGames());
            adapter.notifyDataSetChanged();
        }else if (event instanceof DataIconEvent){
            if (Constant.Result.NORMAL.equals(((DataIconEvent)event).getmEventResult())){
                chooseDate();
            }
        }
    }

    private void chooseDate(){
        if (isRefreshing()){
            return;
        }
        DatePickerPopWin datePickerPopWin = new DatePickerPopWin.Builder(getActivity(), new DatePickerPopWin.OnDatePickedListener() {
            @Override
            public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                date = dateDesc;
                setRefreshing();
            }
        }).colorConfirm(Color.parseColor("#448AFF"))
                .minYear(2015)
                .maxYear(2017)
                .build();
        datePickerPopWin.showPopWin(getActivity());
    }

}
