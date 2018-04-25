package me.fandong.cloudserver.Model;

import java.util.ArrayList;

public class QiniuFileListModel {
    private ArrayList<QiniuFileModel> list;
    private String marker;

    public String getMarker() {
        return marker;
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }

    public ArrayList<QiniuFileModel> getList() {
        return list;
    }

    public void setList(ArrayList<QiniuFileModel> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "QiniuFileListModel{" +
                "list=" + list +
                ", marker='" + marker + '\'' +
                '}';
    }
}
