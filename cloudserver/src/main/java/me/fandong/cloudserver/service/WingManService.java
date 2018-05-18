package me.fandong.cloudserver.service;

import me.fandong.cloudserver.Model.WingManProductModel;
import me.fandong.cloudserver.Model.WingManUserModel;
import me.fandong.cloudserver.dao.WingManMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WingManService {
    @Autowired
    WingManMapper wingManMapper;

    public List<WingManProductModel> listWingManProducts(){
        return wingManMapper.listWingManProducts();
    }

    public List<WingManUserModel> getWingManUserModel(String uuid){
        return wingManMapper.getWingManUserModel(uuid);
    }
}
