package me.fandong.cloudserver.Model;

public class QiniuLoginModel {
    private String uid;
    private String nickName;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "QiniuLoginModel{" +
                "uid='" + uid + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}
