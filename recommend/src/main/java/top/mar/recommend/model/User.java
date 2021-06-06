package top.mar.recommend.model;

public class User {
    String openid;

    public User() {
    }

    public User(String openid) {
        this.openid = openid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    @Override
    public String toString() {
        return "User{" +
                "openid='" + openid + '\'' +
                '}';
    }
}
