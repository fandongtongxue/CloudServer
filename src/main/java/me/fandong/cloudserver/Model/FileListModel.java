package me.fandong.cloudserver.Model;

import java.util.ArrayList;

public class FileListModel {
    private ArrayList<FileModel> list;
    private String marker;

    public String getMarker() {
        return marker;
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }

    public ArrayList<FileModel> getList() {
        return list;
    }

    public void setList(ArrayList<FileModel> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "FileListModel{" +
                "list=" + list +
                ", marker='" + marker + '\'' +
                '}';
    }
}
