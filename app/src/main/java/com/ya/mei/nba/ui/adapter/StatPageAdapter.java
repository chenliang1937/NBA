package com.ya.mei.nba.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ya.mei.nba.ui.fragment.BarFragment;

import java.util.List;

/**
 * Created by chenliang3 on 2016/3/8.
 */
public class StatPageAdapter extends FragmentStatePagerAdapter {

    private List<BarFragment> barFragments;

    public StatPageAdapter(FragmentManager fm, List<BarFragment> barFragments) {
        super(fm);
        this.barFragments = barFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return barFragments.get(position);
    }

    @Override
    public int getCount() {
        return barFragments.size();
    }
}
