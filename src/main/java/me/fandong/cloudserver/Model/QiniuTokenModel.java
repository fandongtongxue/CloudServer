package me.fandong.cloudserver.Model;

public class QiniuTokenModel {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "QiniuTokenModel{" +
                "token='" + token + '\'' +
                '}';
    }
}
