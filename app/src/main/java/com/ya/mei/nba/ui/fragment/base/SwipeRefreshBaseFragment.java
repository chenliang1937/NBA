package com.ya.mei.nba.ui.fragment.base;

import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;

import com.ya.mei.nba.R;

import java.lang.reflect.Field;

import butterknife.Bind;

/**
 * Created by chenliang3 on 2016/3/4.
 */
public abstract class SwipeRefreshBaseFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    @Bind(R.id.newsSwipeRefreshLayout)
    protected SwipeRefreshLayout swipeRefreshLayout;

    protected void setRefreshing() {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    protected boolean isRefreshing() {
        return swipeRefreshLayout.isRefreshing();
    }

    protected void stopRefreshing() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void initView() {
        modifySwipeRefreshRange();
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.primary_dark, R.color.primary);
    }

    /**
     * 修改SwipeRefreshLayout触发下拉刷新的距离---好像没作用
     */
    private void modifySwipeRefreshRange(){
        ViewTreeObserver vto = swipeRefreshLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {

                final DisplayMetrics metrics = getResources().getDisplayMetrics();
                Float mDistanceToTriggerSync = Math.min(((View) swipeRefreshLayout.getParent()).getHeight() * 0.6f, 500 * metrics.density);

                try {
                    Field field = SwipeRefreshLayout.class.getDeclaredField("mDistanceToTriggerSync");
                    field.setAccessible(true);
                    field.setFloat(swipeRefreshLayout, mDistanceToTriggerSync);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ViewTreeObserver obs = swipeRefreshLayout.getViewTreeObserver();
                obs.removeOnGlobalLayoutListener(this);
            }
        });
    }
}
