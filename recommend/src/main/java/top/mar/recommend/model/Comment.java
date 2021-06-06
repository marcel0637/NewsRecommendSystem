package top.mar.recommend.model;

import java.util.Date;

public class Comment {
    String docId;
    String content;
    Date createTime;
    long createTimeStamp;
    String avatar;
    String nickName;

    public Comment() {
    }

    public Comment(String docId, String content, Date createTime, long createTimeStamp, String avatar, String nickName) {
        this.docId = docId;
        this.content = content;
        this.createTime = createTime;
        this.createTimeStamp = createTimeStamp;
        this.avatar = avatar;
        this.nickName = nickName;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        setCreateTimeStamp(createTime.getTime());
        this.createTime = createTime;
    }

    public long getCreateTimeStamp() {
        return createTimeStamp;
    }

    public void setCreateTimeStamp(long createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "docId='" + docId + '\'' +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", createTimeStamp=" + createTimeStamp +
                ", avatar='" + avatar + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}
