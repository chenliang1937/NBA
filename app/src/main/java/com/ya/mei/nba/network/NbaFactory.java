package com.ya.mei.nba.network;

/**
 * Created by chenliang3 on 2016/3/4.
 */
public class NbaFactory {

    private static NbaAPI nbaInstance = null;
    private static NewsDetailAPI newsDetailInstance = null;
    private static final Object WATCH_DOG = new Object();

    private NbaFactory(){

    }

    public static NbaAPI getNbaInstance(){
        synchronized (WATCH_DOG) {
            if (nbaInstance == null){
                NbaClient nbaClient = new NbaClient();
                nbaInstance = nbaClient.getClient();
            }
            return nbaInstance;
        }
    }

    public static NewsDetailAPI getNewsDetailInstance(){
        synchronized (WATCH_DOG){
            if (newsDetailInstance == null){
                NbaClient nbaClient = new NbaClient();
                newsDetailInstance = nbaClient.getNewsDetailAPI();
            }
            return newsDetailInstance;
        }
    }

}
