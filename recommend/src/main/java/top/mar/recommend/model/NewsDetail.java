package top.mar.recommend.model;

import java.util.Date;

public class NewsDetail {
    String docId;
    String url;
    String title;
    String artibody;
    Date newsTime;
    long newsTimeStamp; // 用于前端数据展示
    String newsFrom;
    String tag;
    int commentCount;
    String img;

    public NewsDetail() {
    }

    public NewsDetail(String docId, String url, String title, String artibody, Date newsTime, long newsTimeStamp, String newsFrom, String tag, int commentCount, String img) {
        this.docId = docId;
        this.url = url;
        this.title = title;
        this.artibody = artibody;
        this.newsTime = newsTime;
        this.newsTimeStamp = newsTimeStamp;
        this.newsFrom = newsFrom;
        this.tag = tag;
        this.commentCount = commentCount;
        this.img = img;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtibody() {
        return artibody;
    }

    public void setArtibody(String artibody) {
        this.artibody = artibody;
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

    public String getNewsFrom() {
        return newsFrom;
    }

    public void setNewsFrom(String newsFrom) {
        this.newsFrom = newsFrom;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "NewsDetail{" +
                "docId='" + docId + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", artibody='" + artibody + '\'' +
                ", newsTime=" + newsTime +
                ", newsTimeStamp=" + newsTimeStamp +
                ", newsFrom='" + newsFrom + '\'' +
                ", tag='" + tag + '\'' +
                ", commentCount=" + commentCount +
                ", img='" + img + '\'' +
                '}';
    }
}
