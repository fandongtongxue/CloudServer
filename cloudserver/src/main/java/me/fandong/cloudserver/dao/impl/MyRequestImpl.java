package me.fandong.cloudserver.dao.impl;

import me.fandong.cloudserver.dao.MyRequestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class MyRequestImpl implements MyRequestMapper {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void createMyRequest(String url, String params, String result, String status, String msg) {
        Date createTime = new Date();
        String createTimeString = createTime.toString();
        jdbcTemplate.update("INSERT INTO CloudServer.MyRequest(url,params,result,status,msg,createTime) VALUES (?,?,?,?,?,?)",url,params,result,status,msg,createTimeString);
    }
}
