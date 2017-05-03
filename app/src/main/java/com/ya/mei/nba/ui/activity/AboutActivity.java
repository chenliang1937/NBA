package com.ya.mei.nba.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.ya.mei.nba.R;
import com.ya.mei.nba.event.Event;
import com.ya.mei.nba.ui.widget.RevealBackgroundView;
import com.ya.mei.nba.utils.AppUtils;

import butterknife.Bind;
import de.psdev.licensesdialog.LicensesDialog;

/**
 * Created by chenliang3 on 2016/3/7.
 */
public class AboutActivity extends SwipeBackActivity implements RevealBackgroundView.OnStateChangeListener {

    @Bind(R.id.tv_version)
    TextView versionTextView;
    @Bind(R.id.revealBackgroundView)
    RevealBackgroundView revealBackgroundView;
    @Bind(R.id.aboutView)
    View aboutView;

    public static void navigateFrom(Context context){
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initViews() {
        super.initViews();
        setTitle();
        setupRevealBackground();
    }

    @Override
    void setTitle() {
        toolbar.setTitle(getResources().getString(R.string.about));
        versionTextView.setText("Version" + AppUtils.getVersionName(this));
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_about;
    }

    public void onMultipleClick(final View view){
        new LicensesDialog.Builder(this)
                .setNotices(R.raw.notices)
                .setIncludeOwnLicense(true)
                .build()
                .showAppCompat();
    }

    @Override
    public void onStateChange(int state) {
        if (RevealBackgroundView.STATE_FINISHED == state){
            aboutView.setVisibility(View.VISIBLE);
        }else {
            aboutView.setVisibility(View.INVISIBLE);
        }
    }

    private void setupRevealBackground(){
        revealBackgroundView.setOnStateChangeListener(this);
        int screenWidth = this.getWindowManager().getDefaultDisplay().getWidth();
        final int[] startingLocation = {screenWidth, 0};
        revealBackgroundView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                revealBackgroundView.getViewTreeObserver().removeOnPreDrawListener(this);
                revealBackgroundView.startFromLocation(startingLocation);
                return true;
            }
        });
    }

    public void onEventMainThread(Event event){

    }

}
