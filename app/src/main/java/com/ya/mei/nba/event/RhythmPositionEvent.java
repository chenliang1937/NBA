package com.ya.mei.nba.event;

/**
 * Created by chenliang3 on 2016/3/8.
 */
public class RhythmPositionEvent extends Event {

    private int position;

    public RhythmPositionEvent(int position){
        this.position = position;
    }

    public int getPosition(){
        return position;
    }

}
