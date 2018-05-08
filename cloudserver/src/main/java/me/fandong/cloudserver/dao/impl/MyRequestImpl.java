package me.fandong.cloudserver.dao.impl;

import me.fandong.cloudserver.dao.MyRequestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MyRequestImpl implements MyRequestMapper {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void createMyRequest(String url, String params, String result, String status, String msg) {
        jdbcTemplate.update("INSERT INTO CloudServer.MyRequest(url,params,result,status,msg) VALUES (?,?,?,?,?)",url,params,result,status,msg);
    }
}
