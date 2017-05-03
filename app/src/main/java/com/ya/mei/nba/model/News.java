package com.ya.mei.nba.model;

import java.util.List;

/**
 * Created by chenliang3 on 2016/3/4.
 */
public class News {

    /**
     * nextId : 116409785
     * newslist : [{"contentType":"ARTICLE","description":"央视体育频道女主播\u201c乌贼刘\u201d刘语熙在社交网络发文\u201c人生若只如初见，再见最前线！\u201d，以此正式宣布离开《","title":"告别NBA！最美女主播宣布退出","putdate":"20151204","imgUrlList":["http://img.res.meizu.com/img/download/reader/2015/1204/8/92edcd73/452f4cc1/81686264/66092ed4/original"],"randomNum":1449186657638,"articleId":116409942,"contentSourceName":"NBA","articleUrl":"http://reader.res.meizu.com/reader/articlecontent/20151204/116409942.json","type":"IMAGETEXT","topicVoteJson":null,"sourceType":"ZAKER"}]
     */
    private String nextId;
    private List<NewslistEntity> newslist;

    public String getNextId() {
        return nextId;
    }

    public void setNextId(String nextId) {
        this.nextId = nextId;
    }

    public List<NewslistEntity> getNewslist() {
        return newslist;
    }

    public void setNewslist(List<NewslistEntity> newslist) {
        this.newslist = newslist;
    }

    /**
     * contentType : ARTICLE
     * description : 央视体育频道女主播“乌贼刘”刘语熙在社交网络发文“人生若只如初见，再见最前线！”，以此正式宣布离开《
     * title : 告别NBA！最美女主播宣布退出
     * putdate : 20151204
     * imgUrlList : ["http://img.res.meizu.com/img/download/reader/2015/1204/8/92edcd73/452f4cc1/81686264/66092ed4/original"]
     * randomNum : 1449186657638
     * articleId : 116409942
     * contentSourceName : NBA
     * articleUrl : http://reader.res.meizu.com/reader/articlecontent/20151204/116409942.json
     * type : IMAGETEXT
     * topicVoteJson : null
     * sourceType : ZAKER
     */
    public static class NewslistEntity {
        private String description;
        private String title;
        private String putdate;
        private String articleId;
        private String contentSourceName;
        private String artitcleUrl;
        private Object topicVoteJson;
        private List<String> imgUrlList;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPutdate() {
            return putdate;
        }

        public void setPutdate(String putdate) {
            this.putdate = putdate;
        }

        public String getArticleId() {
            return articleId;
        }

        public void setArticleId(String articleId) {
            this.articleId = articleId;
        }

        public String getContentSourceName() {
            return contentSourceName;
        }

        public void setContentSourceName(String contentSourceName) {
            this.contentSourceName = contentSourceName;
        }

        public String getArtitcleUrl() {
            return artitcleUrl;
        }

        public void setArtitcleUrl(String artitcleUrl) {
            this.artitcleUrl = artitcleUrl;
        }

        public Object getTopicVoteJson() {
            return topicVoteJson;
        }

        public void setTopicVoteJson(Object topicVoteJson) {
            this.topicVoteJson = topicVoteJson;
        }

        public List<String> getImgUrlList() {
            return imgUrlList;
        }

        public void setImgUrlList(List<String> imgUrlList) {
            this.imgUrlList = imgUrlList;
        }
    }

}
