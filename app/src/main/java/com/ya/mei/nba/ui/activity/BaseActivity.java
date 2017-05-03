package com.ya.mei.nba.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.ya.mei.nba.R;
import com.ya.mei.nba.app.AppService;

import butterknife.ButterKnife;

/**
 * Created by chenliang3 on 2016/3/3.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected abstract void initViews();
    protected abstract int getContentViewId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            setTranslucentStatus(true);
        }
        setSystemBarTint();
        AppService.getInstance().addCompositeSub(getTaskId());
        AppService.getInstance().getBus().register(this);
        ButterKnife.bind(this);
        initViews();
    }

    private void setSystemBarTint(){
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.primary);
        tintManager.setNavigationBarTintResource(R.color.transparent);
    }

    private void setTranslucentStatus(boolean on){
        Window window = getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//        final int bit2 = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
        if (on){
            winParams.flags |= bits;
//            winParams.flags |= bit2;
        }else {
            winParams.flags &= ~bits;
//            winParams.flags &= ~bit2;
        }
        window.setAttributes(winParams);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppService.getInstance().removeCompositeSub(getTaskId());
        AppService.getInstance().getBus().unregister(this);
    }
}
