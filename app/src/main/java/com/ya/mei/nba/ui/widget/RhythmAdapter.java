package com.ya.mei.nba.ui.widget;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ya.mei.nba.R;

/**
 * Created by chenliang3 on 2016/3/7.
 */
public class RhythmAdapter extends BaseAdapter {

    /**
     * item的宽度
     */
    private float itemWidth;
    /**
     * 数据源
     */
    private int[] colorList;
    private static final String[] statNames = {"得分", "篮板", "助攻", "抢断", "盖帽"};
    private LayoutInflater inflater;
    private Context context;

    public RhythmAdapter(Context context, int[] colorList){
        this.context = context;
        this.colorList = colorList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return colorList.length;
    }

    @Override
    public Object getItem(int position) {
        return this.colorList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.adapter_rhythm_icon, null);

        //设置item布局的大小以及Y轴的位置
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams((int)itemWidth,
                context.getResources().getDimensionPixelSize(R.dimen.rhythm_item_height)));
        relativeLayout.setTranslationY(itemWidth*3/7);

        //设置第二层RelativeLayout布局的宽和高
        RelativeLayout childRelativeLayout = (RelativeLayout) relativeLayout.getChildAt(0);
        CardView cardRhythm = (CardView) relativeLayout.findViewById(R.id.card_rhythm);
        TextView statName = (TextView) relativeLayout.findViewById(R.id.stat_name);
        cardRhythm.setCardBackgroundColor(colorList[position]);
        statName.setText(statNames[position]);
        int relativeLayoutWidth = (int) itemWidth - 2 *
                context.getResources().getDimensionPixelSize(R.dimen.rhythm_icon_margin);
        childRelativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(relativeLayoutWidth,
                context.getResources().getDimensionPixelSize(R.dimen.rhythm_item_height) - 2 *
                context.getResources().getDimensionPixelSize(R.dimen.rhythm_icon_margin)));
        return relativeLayout;
    }

    public void setItemWidth(float width){
        this.itemWidth = width;
    }

}
