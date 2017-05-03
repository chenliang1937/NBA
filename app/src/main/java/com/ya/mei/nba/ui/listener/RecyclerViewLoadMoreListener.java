package com.ya.mei.nba.ui.listener;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by chenliang3 on 2016/3/7.
 */
public class RecyclerViewLoadMoreListener extends RecyclerView.OnScrollListener {

    private LinearLayoutManager linearLayoutManager;
    private OnLoadMoreListener onLoadMoreListener;
    private int limit;

    public RecyclerViewLoadMoreListener(@Nullable LinearLayoutManager linearLayoutManager,
                                        @Nullable OnLoadMoreListener onLoadMoreListener, int limit){
        super();
        this.linearLayoutManager = linearLayoutManager;
        this.onLoadMoreListener = onLoadMoreListener;
        this.limit = limit;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (linearLayoutManager.getItemCount() >= limit &&
                linearLayoutManager.findLastVisibleItemPosition() == linearLayoutManager.getItemCount() - 1){ //向下滑动，判断最后一个item是不是显示中
            onLoadMoreListener.onLoadMore();
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

}
