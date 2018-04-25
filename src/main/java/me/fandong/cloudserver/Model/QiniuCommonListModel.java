package me.fandong.cloudserver.Model;

import java.util.Arrays;

public class QiniuCommonListModel {
    private String[] list;

    public String[] getList() {
        return list;
    }

    public void setList(String[] list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "QiniuCommonListModel{" +
                "list=" + Arrays.toString(list) +
                '}';
    }
}
