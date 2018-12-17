package me.fandong.cloudserver.service;

import me.fandong.cloudserver.dao.MyRequestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyRequestService {
    @Autowired
    MyRequestMapper myRequestMapper;

    public void createMyRequest(String url, String params, String result, String status, String msg){
        myRequestMapper.createMyRequest(url, params, result, status, msg);
    }
}
