package com.ya.mei.nba.ui.activity;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ya.mei.nba.Conf.Constant;
import com.ya.mei.nba.R;
import com.ya.mei.nba.app.AppService;
import com.ya.mei.nba.event.NewsDetailEvent;
import com.ya.mei.nba.utils.AppUtils;

import butterknife.Bind;

/**
 * Created by chenliang3 on 2016/3/9.
 */
public class NewsDetailActivity extends SwipeBackActivity {

    @Bind(R.id.webLayout)
    FrameLayout webLayout;
//    @Bind(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
//    @Bind(R.id.titleImage)
    ImageView titleImage;

    private WebView webView;
    private Intent mGetIntent;
    public static final String TITLE = "TITLE";
    public static final String DETILE_ID = "DETILE_ID";
    public static final String DETILE_DATE = "DETILE_DATE";
    public static final String IMAGE_URL = "IMAGE_URL";
    public static final String IMAGE_EXIST = "IMAGE_EXIST";

    private boolean hasTitleImage(){
        return getIntent().getBooleanExtra(IMAGE_EXIST, false);
    }

    @Override
    protected int getContentViewId() {
        return hasTitleImage() ? R.layout.activity_detail : R.layout.activity_detail_noimage;
    }

    @Override
    void setTitle() {
        toolbar.setTitle(R.string.app_name);
    }

    @Override
    protected void initViews() {
        super.initViews();
        mGetIntent = getIntent();
        if (hasTitleImage()){
            collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
            titleImage = (ImageView) findViewById(R.id.titleImage);
            collapsingToolbarLayout.setTitle(mGetIntent.getStringExtra(TITLE));
            titleImage.post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(NewsDetailActivity.this).load(mGetIntent.getStringExtra(IMAGE_URL))
                            .placeholder(R.color.primary)
                            .into(titleImage);
                }
            });
        }else {
            toolbar.setBackgroundResource(R.color.primary);
        }
        webView = new WebView(getApplicationContext());
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setBackgroundColor(0);
        webLayout.addView(webView);

        getNewsDetail();
    }

    private void getNewsDetail(){
        AppService.getInstance().getNewsDetail(getTaskId(), mGetIntent.getStringExtra(DETILE_DATE),
                mGetIntent.getStringExtra(DETILE_ID));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webLayout != null){
            webLayout.removeAllViews();
            webView.destroy();
        }
    }

    public void onEventMainThread(NewsDetailEvent event){
        if (event != null){
            if (Constant.Result.FAIL.equals(event.getmEventResult())){
                AppUtils.showSnackBar(swipeBackLayout, R.string.load_fail);
                return;
            }
            webView.loadDataWithBaseURL(null, event.getContent(), "text/html", "UTF-8", null);
        }
    }

}
