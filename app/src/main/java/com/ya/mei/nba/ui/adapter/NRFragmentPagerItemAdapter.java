package com.ya.mei.nba.ui.adapter;

import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

/**
 * 解决viewpage+fragment来回滑动重新加载数据的问题
 * 重写destroyItem，去掉super.xxx
 * Created by chenliang3 on 2016/3/9.
 */
public class NRFragmentPagerItemAdapter extends FragmentPagerItemAdapter {

    public NRFragmentPagerItemAdapter(FragmentManager fm, FragmentPagerItems pages) {
        super(fm, pages);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }
}
