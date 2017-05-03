package com.ya.mei.nba.event;

import com.ya.mei.nba.Conf.Constant;

/**
 * Created by chenliang3 on 2016/3/8.
 */
public class StatEvent extends Event {

    private String statKind;
    private String[][] lables;
    private String[][] playerUrls;
    private float[][] statValues;
    private Constant.GETNEWSWAY getnewsway;

    public StatEvent(String statKind, String[][] lables, float[][] statValues, String[][] playerUrls){
        this.statKind = statKind;
        this.lables = lables;
        this.statValues = statValues;
        this.playerUrls = playerUrls;
    }

    public void setGetnewsway(Constant.GETNEWSWAY getnewsway){
        this.getnewsway = getnewsway;
    }

    public String getStatKind() {
        return statKind;
    }

    public String[][] getLables() {
        return lables;
    }

    public String[][] getPlayerUrls() {
        return playerUrls;
    }

    public float[][] getStatValues() {
        return statValues;
    }
}
