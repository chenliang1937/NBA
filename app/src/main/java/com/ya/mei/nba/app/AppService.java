package com.ya.mei.nba.app;

import android.os.Handler;
import android.os.HandlerThread;

import com.google.gson.Gson;
import com.ya.mei.greendao.DBHelper;
import com.ya.mei.nba.network.NbaAPI;
import com.ya.mei.nba.network.NbaFactory;
import com.ya.mei.nba.network.NewsDetailAPI;
import com.ya.mei.nba.rxmethod.RxGames;
import com.ya.mei.nba.rxmethod.RxNews;
import com.ya.mei.nba.rxmethod.RxNewsDetail;
import com.ya.mei.nba.rxmethod.RxStats;
import com.ya.mei.nba.rxmethod.RxTeamSort;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by chenliang3 on 2016/3/4.
 */
public class AppService {

    private static final AppService NBA_SERVICE = new AppService();
    private static Gson gson;
    private static EventBus eventBus;
    private static DBHelper dbHelper;
    private static NbaAPI nbaAPI;
    private static NewsDetailAPI newsDetailAPI;
    private Map<Integer, CompositeSubscription> compositeSubByTaskId;
    private Handler handler;

    private AppService(){

    }

    public static AppService getInstance(){
        return NBA_SERVICE;
    }

    public void initService(){
        eventBus = EventBus.getDefault();
        gson = new Gson();
        compositeSubByTaskId = new HashMap<>();
        backGroundInit();
    }

    private void backGroundInit(){
        HandlerThread ioThread = new HandlerThread("IoThread");
        ioThread.start();
        handler = new Handler(ioThread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                nbaAPI = NbaFactory.getNbaInstance();
                newsDetailAPI = NbaFactory.getNewsDetailInstance();
                dbHelper = DBHelper.getInstance(App.getContext());
            }
        });
    }

    public void addCompositeSub(int taskId){
        CompositeSubscription compositeSubscription;
        if (compositeSubByTaskId.get(taskId) == null){
            compositeSubscription = new CompositeSubscription();
            compositeSubByTaskId.put(taskId, compositeSubscription);
        }
    }

    public void removeCompositeSub(int taskId){
        CompositeSubscription compositeSubscription;
        if (compositeSubByTaskId != null && compositeSubByTaskId.get(taskId) != null){
            compositeSubscription = compositeSubByTaskId.get(taskId);
            compositeSubscription.unsubscribe();
            compositeSubByTaskId.remove(taskId);
        }
    }

    private CompositeSubscription getCompositeSubscription(int taskId){
        CompositeSubscription compositeSubscription;
        if (compositeSubByTaskId.get(taskId) == null){
            compositeSubscription = new CompositeSubscription();
            compositeSubByTaskId.put(taskId, compositeSubscription);
        }else {
            compositeSubscription = compositeSubByTaskId.get(taskId);
        }
        return compositeSubscription;
    }

    public void initNews(int taskId, String type){
        getCompositeSubscription(taskId).add(RxNews.initNews(type));
    }

    public void updateNews(int taskId, String type){
        getCompositeSubscription(taskId).add(RxNews.updateNews(type));
    }

    public void loadMoreNews(int taskId, String type, String newsId){
        getCompositeSubscription(taskId).add(RxNews.loadMoreNews(type, newsId));
    }

    public void getNewsDetail(int taskId, String date, String detailId){
        getCompositeSubscription(taskId).add(RxNewsDetail.getNewsDetail(date, detailId));
    }

    public void initPerStat(int taskId, String statKind){
        getCompositeSubscription(taskId).add(RxStats.initStat(statKind));
    }

    public void getPerStat(int taskId, String ...statKinds){
        getCompositeSubscription(taskId).add(RxStats.getPerStat(statKinds));
    }

    public void getTeamSort(int taskId){
        getCompositeSubscription(taskId).add(RxTeamSort.getTeams());
    }

    public void getGames(int taskId, String date){
        getCompositeSubscription(taskId).add(RxGames.getTeams(date));
    }

    public static EventBus getBus(){
        return eventBus;
    }

    public static  NbaAPI getNbaAPI(){
        return nbaAPI;
    }

    public static NewsDetailAPI getNewsDetailAPI(){
        return newsDetailAPI;
    }

    public static DBHelper getDbHelper(){
        return dbHelper;
    }

    public static Gson getGson(){
        return gson;
    }

}
