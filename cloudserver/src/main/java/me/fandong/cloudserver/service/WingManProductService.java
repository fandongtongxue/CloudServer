package me.fandong.cloudserver.service;

import me.fandong.cloudserver.Model.WingManProductModel;
import me.fandong.cloudserver.dao.WingManProductMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

public class WingManProductService {
    @Autowired
    WingManProductMapper wingManProductMapper;

    public ArrayList<WingManProductModel> listWingManProducts(){
        wingManProductMapper.listWingManProducts();
    }
}
