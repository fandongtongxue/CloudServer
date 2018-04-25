package me.fandong.cloudserver.Model;

public class AliyunOSSBucketModel {
    private String name;

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
                '}';
    }
}
