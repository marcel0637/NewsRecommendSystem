package top.mar.recommend.model;

import java.util.Date;

public class News {
    String docId;
    String title;
    String cat;
    int commentCount;
    Date newsTime;
    long newsTimeStamp; // 用于前端数据展示
    String url;
    String img;
    int recommendSource;

    public News() {
    }

    public News(String docId, String title, String cat, int commentCount, Date newsTime, long newsTimeStamp, String url, String img) {
        this.docId = docId;
        this.title = title;
        this.cat = cat;
        this.commentCount = commentCount;
        this.newsTime = newsTime;
        this.newsTimeStamp = newsTimeStamp;
        this.url = url;
        this.img = img;
    }

    public News(String docId, String title, String cat, int commentCount, Date newsTime, long newsTimeStamp, String url, String img, int recommendSource) {
        this.docId = docId;
        this.title = title;
        this.cat = cat;
        this.commentCount = commentCount;
        this.newsTime = newsTime;
        this.newsTimeStamp = newsTimeStamp;
        this.url = url;
        this.img = img;
        this.recommendSource = recommendSource;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public Date getNewsTime() {
        return newsTime;
    }

    public void setNewsTime(Date newsTime) {
        setNewsTimeStamp(newsTime.getTime());
        this.newsTime = newsTime;
    }

    public long getNewsTimeStamp() {
        return newsTimeStamp;
    }

    public void setNewsTimeStamp(long newsTimeStamp) {
        this.newsTimeStamp = newsTimeStamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getRecommendSource() {
        return recommendSource;
    }

    public void setRecommendSource(int recommendSource) {
        this.recommendSource = recommendSource;
    }

    @Override
    public String toString() {
        return "News{" +
                "docId='" + docId + '\'' +
                ", title='" + title + '\'' +
                ", cat='" + cat + '\'' +
                ", commentCount=" + commentCount +
                ", newsTime=" + newsTime +
                ", newsTimeStamp=" + newsTimeStamp +
                ", url='" + url + '\'' +
                ", img='" + img + '\'' +
                ", recommendSource=" + recommendSource +
                '}';
    }

}
