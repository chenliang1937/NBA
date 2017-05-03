package com.ya.mei.nba.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ya.mei.nba.Conf.Constant;
import com.ya.mei.nba.R;
import com.ya.mei.nba.app.AppService;
import com.ya.mei.nba.event.TeamSortEvent;
import com.ya.mei.nba.model.TeamSorts;
import com.ya.mei.nba.ui.adapter.TeamSortAdapter;
import com.ya.mei.nba.ui.fragment.base.SwipeRefreshBaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by chenliang3 on 2016/3/8.
 */
public class TeamSortFragment extends SwipeRefreshBaseFragment {

    @Bind(R.id.newsRecycleView)
    RecyclerView recyclerView;

    private TeamSortAdapter teamSortAdapter;
    private List<TeamSorts.TeamsortEntity> teamsortEntities = new ArrayList<>();

    public static TeamSortFragment newInstance(){
        TeamSortFragment teamSortFragment = new TeamSortFragment();
        return teamSortFragment;
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
        teamSortAdapter = new TeamSortAdapter(getActivity(), teamsortEntities);
        recyclerView.setAdapter(teamSortAdapter);
        setRefreshing();
    }

    @Override
    public void onRefresh() {
        AppService.getInstance().getTeamSort(getTaskId());
    }

    public void onEventMainThread(TeamSortEvent teamSortEvent){
        stopRefreshing();
        if (Constant.Result.FAIL.equals(teamSortEvent.getmEventResult())){
            return;
        }
        teamsortEntities.clear();
        teamsortEntities.addAll(teamSortEvent.getTeamSorts().getTeamsort());
        teamSortAdapter.notifyDataSetChanged();
    }

}
