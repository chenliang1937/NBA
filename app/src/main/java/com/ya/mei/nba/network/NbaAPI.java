package com.ya.mei.nba.network;

import com.ya.mei.nba.model.Games;
import com.ya.mei.nba.model.News;
import com.ya.mei.nba.model.Statistics;
import com.ya.mei.nba.model.TeamSorts;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by chenliang3 on 2016/3/4.
 */
public interface NbaAPI {

    @GET("api/v1.0/{type}/update")
    Observable<News> updateNews(@Path("type") String type);
    @GET("api/v1.0/{type}/loadmore/{newsId}")
    Observable<News> loadMoreNews(@Path("type") String type, @Path("newsId") String newsId);
    @GET("api/v1.0/nbastat/perstat/{statKind}")
    Observable<Statistics> getPerStats(@Path("statKind") String statKind);
    @GET("api/v1.0/teamsort/sort")
    Observable<TeamSorts> getTeamSort();
    @GET("api/v1.0/gamesdate/{date}")
    Observable<Games> getGames(@Path("date") String date);

}
