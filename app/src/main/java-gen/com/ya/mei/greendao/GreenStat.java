package com.ya.mei.greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "GREEN_STAT".
 */
public class GreenStat {

    private Long id;
    private String statentity;
    private String statkind;

    public GreenStat() {
    }

    public GreenStat(Long id) {
        this.id = id;
    }

    public GreenStat(Long id, String statentity, String statkind) {
        this.id = id;
        this.statentity = statentity;
        this.statkind = statkind;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatentity() {
        return statentity;
    }

    public void setStatentity(String statentity) {
        this.statentity = statentity;
    }

    public String getStatkind() {
        return statkind;
    }

    public void setStatkind(String statkind) {
        this.statkind = statkind;
    }

}
