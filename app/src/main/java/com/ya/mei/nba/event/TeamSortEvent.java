package com.ya.mei.nba.event;

import com.ya.mei.nba.Conf.Constant;
import com.ya.mei.nba.model.TeamSorts;

/**
 * Created by chenliang3 on 2016/3/8.
 */
public class TeamSortEvent extends Event {

    private TeamSorts teamSorts;

    public TeamSortEvent(TeamSorts teamSorts, Constant.Result result){
        this.teamSorts = teamSorts;
        this.mEventResult = result;
    }

    public TeamSorts getTeamSorts(){
        return teamSorts;
    }

}
