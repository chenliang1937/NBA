package com.ya.mei.nba.network;

import com.ya.mei.nba.model.NewsDetail;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by chenliang3 on 2016/3/9.
 */
public interface NewsDetailAPI {
    @GET("{date}/{detileId}")
    Observable<NewsDetail> getNewsDetail(@Path("date") String type, @Path("detileId") String newsId);
}
