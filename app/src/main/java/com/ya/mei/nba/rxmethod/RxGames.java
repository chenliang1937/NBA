package com.ya.mei.nba.rxmethod;

import com.ya.mei.nba.Conf.Constant;
import com.ya.mei.nba.app.AppService;
import com.ya.mei.nba.event.GamesEvent;
import com.ya.mei.nba.model.Games;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by chenliang3 on 2016/3/9.
 */
public class RxGames {

    public static Subscription getTeams(String date){
        Subscription subscription = AppService.getNbaAPI().getGames(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Games>() {
                    @Override
                    public void call(Games games) {
                        AppService.getBus().post(new GamesEvent(games, Constant.Result.SUCCESS));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        AppService.getBus().post(new GamesEvent(null, Constant.Result.FAIL));
                    }
                });
        return subscription;
    }

}
