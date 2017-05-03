package com.ya.mei.nba.network;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by chenliang3 on 2016/3/4.
 */
public class NbaClient {

    final NbaAPI nbaAPI;
    final NewsDetailAPI newsDetailAPI;

    NbaClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://nbaplus.sinaapp.com/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        nbaAPI = retrofit.create(NbaAPI.class);

        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl("http://reader.res.meizu.com/reader/articlecontent/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        newsDetailAPI = retrofit1.create(NewsDetailAPI.class);
    }

    public NbaAPI getClient(){
        return nbaAPI;
    }

    public NewsDetailAPI getNewsDetailAPI(){
        return newsDetailAPI;
    }

}
