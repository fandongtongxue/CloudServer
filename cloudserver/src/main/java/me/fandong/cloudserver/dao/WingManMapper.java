package me.fandong.cloudserver.dao;

import me.fandong.cloudserver.Model.WingManProductModel;
import me.fandong.cloudserver.Model.WingManUserModel;

import java.util.List;

public interface WingManMapper {
    List<WingManProductModel> listWingManProducts();
    List<WingManUserModel> getWingManUserModel(String uuid);
    void createWingManUserModel(String uuid);
}
