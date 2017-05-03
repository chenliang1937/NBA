package com.ya.mei.nba.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by chenliang3 on 2016/3/4.
 */
public class App extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        AppService.getInstance().initService();
    }

    public static Context getContext(){
        return context;
    }
}
