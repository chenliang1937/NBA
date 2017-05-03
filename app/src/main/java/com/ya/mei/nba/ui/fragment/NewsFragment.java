package com.ya.mei.nba.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ya.mei.nba.Conf.Constant;
import com.ya.mei.nba.R;
import com.ya.mei.nba.app.AppService;
import com.ya.mei.nba.event.NewsAnimatEndEvent;
import com.ya.mei.nba.event.NewsEvent;
import com.ya.mei.nba.model.News;
import com.ya.mei.nba.ui.adapter.NewsAdapter;
import com.ya.mei.nba.ui.fragment.base.SwipeRefreshBaseFragment;
import com.ya.mei.nba.ui.listener.RecyclerViewLoadMoreListener;
import com.ya.mei.nba.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by chenliang3 on 2016/3/3.
 */
public class NewsFragment extends SwipeRefreshBaseFragment implements RecyclerViewLoadMoreListener.OnLoadMoreListener{

    @Bind(R.id.newsRecycleView)
    RecyclerView newsRecyclerView;

    protected List<News.NewslistEntity> newslistEntities = new ArrayList<>();
    protected NewsAdapter newsAdapter;
    protected String newsId = "";
    private static boolean firstAnimate = true;

    public static NewsFragment newInstance(){
        NewsFragment newsFragment = new NewsFragment();
        return newsFragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView() {
        super.initView();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(newsRecyclerView.getContext());
        newsRecyclerView.setLayoutManager(linearLayoutManager);
        newsRecyclerView.addOnScrollListener(new RecyclerViewLoadMoreListener(linearLayoutManager, this, 0));
//        newsRecyclerView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (swipeRefreshLayout.isRefreshing()) {
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });
        setAdapter();
    }

    private void setAdapter(){
        swipeRefreshLayout.setBackgroundResource(R.color.grey50);
        newsAdapter = new NewsAdapter(getActivity(), newslistEntities);
        newsRecyclerView.setAdapter(newsAdapter);
        newsAdapter.setAnimate(firstAnimate);
        if (firstAnimate){
            firstAnimate = false;
        }

        initCache();
    }

    private void initCache() {
        AppService.getInstance().initNews(getTaskId(), Constant.NEWSTYPE.NEWS.getNewsType());
    }

    protected void stopAll(){
        stopRefreshing();
        stopLoading();
    }

    protected void stopLoading(){
        newsAdapter.notifyItemChanged(newsAdapter.getItemCount() - 1);
        newsAdapter.setLoading(false);
    }

    protected void updateView(NewsEvent newsEvent){
        if (Constant.Result.FAIL.equals(newsEvent.getmEventResult())){
            if (newsEvent.getGetnewsway().equals(Constant.GETNEWSWAY.INIT)){
                setRefreshing();
            }else {
                stopAll();
                AppUtils.showSnackBar(swipeRefreshLayout, R.string.load_fail);
            }
        }else {
            News news = newsEvent.getNews();
            newsId = news.getNextId();
            switch (newsEvent.getGetnewsway()){
                case INIT:
                    newslistEntities.clear();
                    newslistEntities.addAll(news.getNewslist());
                    break;
                case UPDATE:
                    newslistEntities.clear();
                    newslistEntities.addAll(news.getNewslist());
                    stopRefreshing();
                    newsAdapter.setAnimate(false);
                    break;
                case LOADMORE:
                    newslistEntities.addAll(news.getNewslist());
                    stopLoading();
                    newsAdapter.setAnimate(false);
                    break;
                default:
                    break;
            }
            newsAdapter.updateItem();
            if (Constant.GETNEWSWAY.UPDATE.equals(newsEvent.getGetnewsway())){
                AppUtils.showSnackBar(swipeRefreshLayout, R.string.load_success);
            }
        }
    }

    public void onEventMainThread(NewsAnimatEndEvent newsAnimatEndEvent){
        setRefreshing();
    }

    public void onEventMainThread(NewsEvent newsEvent){
        if (newsEvent != null && Constant.NEWSTYPE.NEWS.getNewsType().equals(newsEvent.getNewsType())){
            updateView(newsEvent);
        }
    }

    @Override
    public void onLoadMore() {
        if (newsAdapter.canLoadMore()){
            newsAdapter.setLoading(true);
            newsAdapter.notifyItemChanged(newsAdapter.getItemCount() - 1);
            AppService.getInstance().loadMoreNews(getTaskId(), Constant.NEWSTYPE.NEWS.getNewsType(), newsId);
        }
    }

    @Override
    public void onRefresh() {
        AppService.getInstance().updateNews(getTaskId(), Constant.NEWSTYPE.NEWS.getNewsType());
    }

}
