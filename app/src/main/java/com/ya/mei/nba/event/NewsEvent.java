package com.ya.mei.nba.event;

import com.ya.mei.nba.Conf.Constant;
import com.ya.mei.nba.model.News;

/**
 * Created by chenliang3 on 2016/3/4.
 */
public class NewsEvent extends Event {

    private News news;
    private Constant.GETNEWSWAY getnewsway;
    private String newsType;

    public NewsEvent(News news, Constant.GETNEWSWAY getnewsway, String newsType){
        this.news = news;
        this.getnewsway = getnewsway;
        this.newsType = newsType;
    }

    public News getNews() {
        return news;
    }

    public Constant.GETNEWSWAY getGetnewsway() {
        return getnewsway;
    }

    public String getNewsType() {
        return newsType;
    }
}
