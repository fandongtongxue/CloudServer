package me.fandong.cloudserver.Model;

public class WingManUserModel {
    private int id;
    private String uuid;
    private String currentDate;
    private String expireDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    @Override
    public String toString() {
        return "WingManUserModel{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", currentDate='" + currentDate + '\'' +
                ", expireDate='" + expireDate + '\'' +
                '}';
    }
}
