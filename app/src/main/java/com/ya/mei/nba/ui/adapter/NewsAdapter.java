package com.ya.mei.nba.ui.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ya.mei.nba.R;
import com.ya.mei.nba.app.AppService;
import com.ya.mei.nba.event.NewsAnimatEndEvent;
import com.ya.mei.nba.model.News;
import com.ya.mei.nba.ui.activity.NewsDetailActivity;
import com.ya.mei.nba.utils.DateFormatter;
import com.ya.mei.nba.utils.NumericalUtil;

import org.joda.time.DateTime;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by chenliang3 on 2016/3/4.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.BaseViewHolder> {

    private static final int ANIMATED_TIME_DUTATION = 1000;
    private int lastAnimatedPosition = -1;
    private Boolean loading = false;
    private Context context;
    private LayoutInflater inflater;
    private List<News.NewslistEntity> newslist;
    private boolean animate = true;
    private int animateEndCount;

    public NewsAdapter(Context context, List<News.NewslistEntity> newslist){
        this.context = context;
        this.newslist = newslist;
        inflater = LayoutInflater.from(context);
        setAnimateEndCount(4);
    }

    enum VIEWTYPE {
        NORMAL(0), NOPIC(1),MOREPIC(2),LOADMORE(3),ERROR(4);
        private int viewType;
        VIEWTYPE(int viewType){
            this.viewType = viewType;
        }
        public int getViewType(){
            return viewType;
        }
    }

    private void setAnimateEndCount(int animateEndCount){
        this.animateEndCount = animateEndCount;
    }

    @Override
    public int getItemViewType(int position) {
        int itemType;
        if (newslist == null || newslist.get(position) == null){
            itemType = VIEWTYPE.ERROR.getViewType();
        }else if (position == getItemCount() - 1){
            itemType = VIEWTYPE.LOADMORE.getViewType();
        }else if (newslist.get(position).getImgUrlList().size() == 0){
            itemType = VIEWTYPE.NOPIC.getViewType();
        }else if (newslist.get(position).getImgUrlList().size() >= 4){
            itemType = VIEWTYPE.MOREPIC.getViewType();
        }else {
            itemType = VIEWTYPE.NORMAL.getViewType();
        }

        return itemType;
    }

    @Override
    public int getItemCount() {
        return newslist == null ? 0 : newslist.size();
    }

    @Override
    public void onBindViewHolder(NewsAdapter.BaseViewHolder holder, int position) {
        holder.update(position);
        if (animate){
            runEnterAnimation(holder.itemView, position);
        }
    }

    @Override
    public NewsAdapter.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;
        switch (VIEWTYPE.values()[viewType]){
            case LOADMORE:
                viewHolder = new LoadMoreViewHolder(inflater.inflate(R.layout.item_news_load_more, parent, false));
                break;
            case NOPIC:
                viewHolder = new NoPicNewsViewHolder(inflater.inflate(R.layout.item_news_nopic, parent, false));
                break;
            case MOREPIC:
                viewHolder = new MorePicNewsViewHolder(inflater.inflate(R.layout.item_news_morepic, parent, false));
                break;
            case NORMAL:
                viewHolder = new NormalNewsViewHolder(inflater.inflate(R.layout.item_news_normal, parent, false));
                break;
            default:
                break;
        }

        return viewHolder;
    }

    private void runEnterAnimation(View view, final int position){
        if (position > lastAnimatedPosition){
            lastAnimatedPosition = position;
            view.setTranslationY(NumericalUtil.getScreenHeight(context));
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.0f))
                    .setDuration(ANIMATED_TIME_DUTATION)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (position == animateEndCount || position >= getItemCount() - 1){
                                AppService.getBus().post(new NewsAnimatEndEvent());
                            }
                        }
                    })
                    .start();
        }
    }

    public void setLoading(boolean loading){
        this.loading = loading;
    }

    public void setAnimate(boolean animate){
        this.animate = animate;
    }

    public boolean canLoadMore(){
        return !loading;
    }

    public void updateItem(){
        notifyDataSetChanged();
    }

    public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
        public BaseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected abstract void update(int position);
    }

    protected abstract class EntityHolder extends BaseViewHolder implements View.OnClickListener {
        News.NewslistEntity newsEntity;

        public EntityHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        protected void update(int position) {
            newsEntity = newslist.get(position);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra(NewsDetailActivity.TITLE, newsEntity.getTitle());
            intent.putExtra(NewsDetailActivity.DETILE_DATE, new DateTime(Long.parseLong(newsEntity.getPutdate())).toString("yyyyMMdd"));
            intent.putExtra(NewsDetailActivity.DETILE_ID, newsEntity.getArticleId());
            intent.putExtra(NewsDetailActivity.IMAGE_EXIST, newsEntity.getImgUrlList().size() > 0);
            if (newsEntity.getImgUrlList().size() > 0){
                intent.putExtra(NewsDetailActivity.IMAGE_URL, newsEntity.getImgUrlList().get(0));
            }
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(
                    v,//The View that the new activity is animating from
                    (int) v.getWidth()/2, (int)v.getHeight()/2,//拉伸开始的坐标
                    0, 0);//拉伸开始的区域大小，这里用（0，0）表示从无到全屏
            ActivityCompat.startActivity((Activity)context, intent, optionsCompat.toBundle());
        }
    }

    protected class LoadMoreViewHolder extends BaseViewHolder{
        @Bind(R.id.item_news_load_more_loading)
        protected View iconLoading;
        @Bind(R.id.item_news_load_more_fail)
        protected View iconFail;

        public LoadMoreViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void update(int position) {
            iconLoading.setVisibility(loading ? View.VISIBLE : View.GONE);
            iconFail.setVisibility(loading ? View.GONE : View.VISIBLE);
        }
    }

    protected class NormalNewsViewHolder extends EntityHolder {
        @Bind(R.id.newsImage)
        ImageView newsImage;
        @Bind(R.id.newsTitle)
        TextView newsTitleTV;
        @Bind(R.id.newsTime)
        TextView newsTimeTV;

        String showTime;

        public NormalNewsViewHolder(View itemView){
            super(itemView);
        }

        @Override
        protected void update(int position) {
            super.update(position);
            Glide.with(context).load(newsEntity.getImgUrlList().get(0))
                    .placeholder(R.mipmap.placeholder_small)
                    .into(newsImage);
            newsTitleTV.setText(newsEntity.getTitle());
            if ((Long.parseLong(newsEntity.getPutdate())) < 20151207){
                showTime = newsEntity.getPutdate().substring(4, 6) + "月" + newsEntity.getPutdate().substring(6, 8) + "日";
            }else {
                showTime = DateFormatter.getRecentlyTimeFormatText(new DateTime(Long.parseLong(newsEntity.getPutdate())));
            }
            newsTimeTV.setText(showTime);
        }
    }

    protected class NoPicNewsViewHolder extends EntityHolder{
        @Bind(R.id.newsTitle)
        TextView newsTitleTV;
        @Bind(R.id.newsTime)
        TextView newsTimeTV;
        @Bind(R.id.newsDescription)
        TextView newsDescriptionTV;

        String showTime;

        public NoPicNewsViewHolder(View itemView){
            super(itemView);
        }

        @Override
        protected void update(int position) {
            super.update(position);
            newsTitleTV.setText(newsEntity.getTitle());
            newsDescriptionTV.setText(newsEntity.getDescription());
            if ((Long.parseLong(newsEntity.getPutdate())) < 20151207){
                showTime = newsEntity.getPutdate().substring(4, 6) + "月" + newsEntity.getPutdate().substring(6, 8) + "日";
            }else {
                showTime = DateFormatter.getRecentlyTimeFormatText(new DateTime(Long.parseLong(newsEntity.getPutdate())));
            }
            newsTimeTV.setText(showTime);
        }
    }

    protected class MorePicNewsViewHolder extends EntityHolder{
        @Bind(R.id.newsImage1)
        ImageView newsImage1;
        @Bind(R.id.newsImage2)
        ImageView newsImage2;
        @Bind(R.id.newsImage3)
        ImageView newsImage3;
        @Bind(R.id.newsTitle)
        TextView newsTitleTV;
        @Bind(R.id.newsTime)
        TextView newsTimeTV;

        String showTime;

        public MorePicNewsViewHolder(View itemView){
            super(itemView);
        }

        @Override
        protected void update(int position) {
            super.update(position);
            Glide.with(context).load(newsEntity.getImgUrlList().get(1))
                    .placeholder(R.mipmap.placeholder_small)
                    .into(newsImage1);
            Glide.with(context).load(newsEntity.getImgUrlList().get(2))
                    .placeholder(R.mipmap.placeholder_small)
                    .into(newsImage2);
            Glide.with(context).load(newsEntity.getImgUrlList().get(3))
                    .placeholder(R.mipmap.placeholder_small)
                    .into(newsImage3);
            newsTitleTV.setText(newsEntity.getTitle());
            if ((Long.parseLong(newsEntity.getPutdate())) < 20151207){
                showTime = newsEntity.getPutdate().substring(4, 6) + "月" + newsEntity.getPutdate().substring(6, 8) + "日";
            }else {
                showTime = DateFormatter.getRecentlyTimeFormatText(new DateTime(Long.parseLong(newsEntity.getPutdate())));
            }
            newsTimeTV.setText(showTime);
        }
    }

}
