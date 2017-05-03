package com.ya.mei.nba.event;

/**
 * Created by chenliang3 on 2016/3/9.
 */
public class NewsDetailEvent extends Event {

    private String newsContent;

    public NewsDetailEvent(String newsContent){
        this.newsContent = newsContent;
    }

    public String getContent(){
        return newsContent;
    }

}
