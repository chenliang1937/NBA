package com.ya.mei.nba.rxmethod;

import com.ya.mei.nba.Conf.Constant;
import com.ya.mei.nba.app.AppService;
import com.ya.mei.nba.event.TeamSortEvent;
import com.ya.mei.nba.model.TeamSorts;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by chenliang3 on 2016/3/8.
 */
public class RxTeamSort {

    public static Subscription getTeams(){
        Subscription subscription = AppService.getNbaAPI().getTeamSort()
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<TeamSorts>() {
                    @Override
                    public void call(TeamSorts teamSorts) {
                        cacheTeams();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<TeamSorts>() {
                    @Override
                    public void call(TeamSorts teamSorts) {
                        AppService.getBus().post(new TeamSortEvent(teamSorts, Constant.Result.SUCCESS));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        AppService.getBus().post(new TeamSortEvent(null, Constant.Result.FAIL));
                    }
                });
        return subscription;
    }

    private static void cacheTeams(){

    }

}
