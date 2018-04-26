package me.fandong.cloudserver.Model;

public class AliyunOSSBucketModel {
    private String name;
    private String endPoint;

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AliyunOSSBucketModel{" +
                "name='" + name + '\'' +
                ", endPoint='" + endPoint + '\'' +
                '}';
    }
}
