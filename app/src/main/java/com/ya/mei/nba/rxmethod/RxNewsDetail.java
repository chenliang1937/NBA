package com.ya.mei.nba.rxmethod;

import com.ya.mei.nba.Conf.Constant;
import com.ya.mei.nba.app.App;
import com.ya.mei.nba.app.AppService;
import com.ya.mei.nba.event.NewsDetailEvent;
import com.ya.mei.nba.model.NewsDetail;
import com.ya.mei.nba.utils.PreferenceUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by chenliang3 on 2016/3/9.
 */
public class RxNewsDetail {

    public static Subscription getNewsDetail(final String date, final String detileId){
        Subscription subscription = AppService.getNewsDetailAPI().getNewsDetail(date, detileId+".json")
                .subscribeOn(Schedulers.io())
                .map(new Func1<NewsDetail, String>() {
                    @Override
                    public String call(NewsDetail newsDetail) {
                        int imageCount = 0;
                        String assStyle = String.format(Constant.CSS_STYLE,
                                PreferenceUtils.getPrefString(App.getContext(), Constant.ACTILEFONTSIZE, "18"), "#333333");
                        String html = "<html><head>" + assStyle + "</head><body>" + newsDetail.getContent()
                                + "<p>（" + newsDetail.getAuthor() + "）<p></body></html>";
                        Pattern p = Pattern.compile("(<p class=\"reader_img_box\"><img id=\"img_\\d\" class=\"reader_img\" src=\"reader_img_src\" /></p>)+");
                        Matcher m = p.matcher(html);
                        while (m.find() && imageCount < newsDetail.getArticleMediaMap().size()){
                            if (imageCount == 0){
                                html = html.replace(html.substring(m.start(), m.end()), "");
                            }else {
                                if (PreferenceUtils.getPrefBoolean(App.getContext(), Constant.LOADIMAGE, true)){
                                    html = html.replace(html.substring(m.start(), m.end()), "<img src="
                                    + newsDetail.getArticleMediaMap().get(String.format("img_%d", imageCount)).getUrl()
                                    + " " + "width=\"100%\" height=\"auto\">");
                                }
                            }
                            imageCount++;
                            m = p.matcher(html);
                        }
                        
                        return html;
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        AppService.getBus().post(new NewsDetailEvent(s));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        NewsDetailEvent newsDetailEvent = new NewsDetailEvent(throwable.toString());
                        newsDetailEvent.setmEventResult(Constant.Result.FAIL);
                        AppService.getBus().post(newsDetailEvent);
                    }
                });
        return subscription;
    }

}
