package com.ya.mei.nba.ui.activity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.ya.mei.nba.Conf.Constant;
import com.ya.mei.nba.R;
import com.ya.mei.nba.app.AppService;
import com.ya.mei.nba.event.DataIconEvent;
import com.ya.mei.nba.event.Event;
import com.ya.mei.nba.event.RefreshIconEvent;
import com.ya.mei.nba.ui.adapter.NRFragmentPagerItemAdapter;
import com.ya.mei.nba.ui.fragment.BlogFragment;
import com.ya.mei.nba.ui.fragment.GameFragment;
import com.ya.mei.nba.ui.fragment.NewsFragment;
import com.ya.mei.nba.ui.fragment.StatisticsFragment;
import com.ya.mei.nba.ui.fragment.TeamSortFragment;

import butterknife.Bind;

public class MainActivity extends BaseActivity {

    @Bind(R.id.main_smartTab)
    SmartTabLayout smartTabLayout;
    @Bind(R.id.main_viewPager)
    ViewPager viewPager;
    @Bind(R.id.main_toolBar)
    Toolbar toolbar;
    @Bind(R.id.main_toobarOt)
    ImageView toolbarOt;

    private LayoutInflater inflater;
    private String[] titles = {"新闻", "博客", "数据", "排名", "赛程"};

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        setupSmartTabLayout();
    }

    private void setupSmartTabLayout(){
        inflater = LayoutInflater.from(this);
        smartTabLayout.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
                ImageView icon = (ImageView) inflater.inflate(R.layout.tab_icon, container, false);
                switch (position) {
                    case 0:
                        icon.setImageDrawable(getResources().getDrawable(R.mipmap.ic_news));
                        break;
                    case 1:
                        icon.setImageDrawable(getResources().getDrawable(R.mipmap.ic_blog));
                        break;
                    case 4:
                        icon.setImageDrawable(getResources().getDrawable(R.mipmap.ic_summary));
                        break;
                    case 2:
                        icon.setImageDrawable(getResources().getDrawable(R.mipmap.ic_sort));
                        break;
                    case 3:
                        icon.setImageDrawable(getResources().getDrawable(R.mipmap.ic_gamedate));
                        break;
                    default:
                        throw new IllegalStateException("Invalid position: " + position);
                }

                return icon;
            }
        });

        FragmentPagerItems pages = new FragmentPagerItems(this);
        pages.add(FragmentPagerItem.of(titles[0], NewsFragment.class));
        pages.add(FragmentPagerItem.of(titles[1], BlogFragment.class));
        pages.add(FragmentPagerItem.of(titles[2], TeamSortFragment.class));
        pages.add(FragmentPagerItem.of(titles[3], GameFragment.class));
        pages.add(FragmentPagerItem.of(titles[4], StatisticsFragment.class));

        NRFragmentPagerItemAdapter adapter = new NRFragmentPagerItemAdapter(
                getSupportFragmentManager(), pages);

        viewPager.setAdapter(adapter);
        smartTabLayout.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                if (position == 4){
                    toolbarOt.setVisibility(View.VISIBLE);
                    toolbarOt.setImageResource(R.mipmap.ic_refresh_white);
                }else if (position == 3){
                    toolbarOt.setVisibility(View.VISIBLE);
                    toolbarOt.setImageResource(R.mipmap.ic_event);
                }else {
                    toolbarOt.setVisibility(View.GONE);
                }
                toolbarOt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position == 4){
                            RefreshIconEvent refreshIconEvent = new RefreshIconEvent();
                            refreshIconEvent.setmEventResult(Constant.Result.NORMAL);
                            AppService.getBus().post(refreshIconEvent);
                        }else if (position == 3){
                            DataIconEvent dataIconEvent = new DataIconEvent();
                            dataIconEvent.setmEventResult(Constant.Result.NORMAL);
                            AppService.getBus().post(dataIconEvent);
                        }
                    }
                });
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.about:
                AboutActivity.navigateFrom(MainActivity.this);
                break;
            case R.id.setting:
                SettingActivity.navigateFrom(MainActivity.this);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onEventMainThread(Event event){

    }
}
