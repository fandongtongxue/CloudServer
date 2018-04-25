package me.fandong.cloudserver.Model;

import java.util.ArrayList;

public class AliyunOSSBucketListModel {
    private ArrayList<AliyunOSSBucketModel> list;

    public ArrayList<AliyunOSSBucketModel> getList() {
        return list;
    }

    public void setList(ArrayList<AliyunOSSBucketModel> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "AliyunOSSBucketListModel{" +
                "list=" + list +
                '}';
    }
}
