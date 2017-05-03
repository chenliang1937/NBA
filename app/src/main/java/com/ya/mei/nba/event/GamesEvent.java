package com.ya.mei.nba.event;

import com.ya.mei.nba.Conf.Constant;
import com.ya.mei.nba.model.Games;

/**
 * Created by chenliang3 on 2016/3/9.
 */
public class GamesEvent extends Event {

    private Games mAllGames;

    public GamesEvent(Games games, Constant.Result result){
        this.mAllGames = games;
        this.mEventResult = result;
    }

    public Games getAllGames() {
        return mAllGames;
    }
}
