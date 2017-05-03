package com.ya.mei.nba.rxmethod;

import com.ya.mei.greendao.GreenNews;
import com.ya.mei.greendao.GreenNewsDao;
import com.ya.mei.nba.Conf.Constant;
import com.ya.mei.nba.app.AppService;
import com.ya.mei.nba.event.NewsEvent;
import com.ya.mei.nba.model.News;

import java.util.List;

import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.Query;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by chenliang3 on 2016/3/4.
 */
public class RxNews {

    /**
     * 从数据库取数据
     * @param newsType
     * @return
     */
    public static Subscription initNews(final String newsType){
        Subscription subscription = Observable.create(new Observable.OnSubscribe<News>() {
            @Override
            public void call(Subscriber<? super News> subscriber) {
                News news = getCacheNews(newsType);
                subscriber.onNext(news);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .subscribe(new Action1<News>() {
                    @Override
                    public void call(News news) {
                        NewsEvent newsEvent = new NewsEvent(news, Constant.GETNEWSWAY.INIT, newsType);
                        if (news == null) {
                            newsEvent.setmEventResult(Constant.Result.FAIL);
                        }
                        AppService.getBus().post(newsEvent);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        NewsEvent newsEvent = new NewsEvent(new News(), Constant.GETNEWSWAY.INIT, newsType);
                        newsEvent.setmEventResult(Constant.Result.FAIL);
                        AppService.getBus().post(newsEvent);
                    }
                });
        return subscription;
    }

    /**
     * 从服务器取数据
     * @param newsType
     * @return
     */
    public static Subscription updateNews(final String newsType){
        Subscription subscription = AppService.getNbaAPI().updateNews(newsType)
                .subscribeOn(Schedulers.io())
                .doOnNext(new Action1<News>() {
                    @Override
                    public void call(News news) {
                        cacheNews(news, newsType);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<News>() {
                    @Override
                    public void call(News news) {
                        AppService.getBus().post(new NewsEvent(news, Constant.GETNEWSWAY.UPDATE, newsType));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        NewsEvent newsEvent = new NewsEvent(new News(), Constant.GETNEWSWAY.UPDATE, newsType);
                        newsEvent.setmEventResult(Constant.Result.FAIL);
                        AppService.getBus().post(newsEvent);
                    }
                });
        return subscription;
    }

    public static Subscription loadMoreNews(final String newsType, final String newsId){
        Subscription subscription = AppService.getNbaAPI().loadMoreNews(newsType, newsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<News>() {
                    @Override
                    public void call(News news) {
                        AppService.getBus().post(new NewsEvent(news, Constant.GETNEWSWAY.LOADMORE, newsType));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        NewsEvent newsEvent = new NewsEvent(new News(), Constant.GETNEWSWAY.LOADMORE, newsType);
                    }
                });
        return subscription;
    }

    public static void cacheNews(final News news, final String newsType){
        GreenNewsDao greenNewsDao = AppService.getDbHelper().getDaoSession().getGreenNewsDao();
        DeleteQuery deleteQuery = greenNewsDao.queryBuilder()
                .where(GreenNewsDao.Properties.Type.eq(newsType))
                .buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
        String newsList = AppService.getGson().toJson(news);
        GreenNews greenNews = new GreenNews(null, newsList, newsType);
        greenNewsDao.insert(greenNews);
    }

    private static News getCacheNews(String newsType){
        News news = null;
        GreenNewsDao greenNewsDao = AppService.getDbHelper().getDaoSession().getGreenNewsDao();
        Query query = greenNewsDao.queryBuilder()
                .where(GreenNewsDao.Properties.Type.eq(newsType))
                .build();
        //查询结果以list返回
        List<GreenNews> greenNewses = query.list();
        if (greenNewses != null && greenNewses.size() > 0){
            news = AppService.getGson().fromJson(greenNewses.get(0).getNewslist(), News.class);
        }
        return news;
    }

}
