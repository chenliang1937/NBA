package com.ya.mei.nba.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.ya.mei.nba.R;

import butterknife.Bind;

/**
 * Created by chenliang3 on 2016/3/7.
 */
public abstract class SwipeBackActivity extends BaseActivity {

    @Bind(R.id.swipBackLayout)
    RelativeLayout swipeBackLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    abstract void setTitle();

    @Override
    protected void initViews() {
        initToolBar();
    }

    protected void initToolBar(){
        setTitle();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.onCreate(this);
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
                .setSwipeEdgePercent(0.5f)
                .setSwipeSensitivity(0.5f)
                .setClosePercent(0.5f);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }
}
