package me.fandong.cloudserver.Model;

import java.util.List;

public class WingManProductListModel {
    public List<WingManProductModel> list;

    public WingManProductListModel(List<WingManProductModel> list) {
        this.list = list;
    }

    public List<WingManProductModel> getList() {
        return list;
    }

    public void setList(List<WingManProductModel> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "WingManProductListModel{" +
                "list=" + list +
                '}';
    }
}
