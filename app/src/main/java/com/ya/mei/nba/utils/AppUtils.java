package com.ya.mei.nba.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.ya.mei.nba.R;
import com.ya.mei.nba.app.App;

/**
 * Created by chenliang3 on 2016/3/7.
 */
public class AppUtils {

    public static String getVersionName(Context context){
        String versionName = null;
        try {
            versionName = context.getApplicationContext().getPackageManager()
                    .getPackageInfo(context.getApplicationContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static void showSnackBar(View view, int id){
        Resources resources = App.getContext().getResources();
        Snackbar snackbar = Snackbar.make(view, resources.getString(id), Snackbar.LENGTH_SHORT);
        setSnackbarMessageTextColor(snackbar);
        snackbar.getView().setBackgroundColor(resources.getColor(R.color.main_bg));
        snackbar.show();
    }

    public static void setSnackbarMessageTextColor(Snackbar snackbar){
        View view = snackbar.getView();
        ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.parseColor("#448AFF"));
    }

}
