package me.fandong.cloudserver.dao.impl;

import me.fandong.cloudserver.Model.WingManProductModel;
import me.fandong.cloudserver.Model.WingManUserModel;
import me.fandong.cloudserver.dao.WingManMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Calendar;

@Repository
public class WingManImpl implements WingManMapper {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<WingManProductModel> listWingManProducts() {
        String sql = "SELECT * FROM CloudServer.app_WingMan_Product";
        return (List<WingManProductModel>)jdbcTemplate.query(sql, new RowMapper<WingManProductModel>() {
            @Override
            public WingManProductModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                WingManProductModel model = new WingManProductModel();
                model.setId(rs.getInt("id"));
                model.setTitle(rs.getString("title"));
                model.setPrice(rs.getString("price"));
                model.setContent(rs.getString("content"));
                return model;
            }
        });
    }

    @Override
    public List<WingManUserModel> getWingManUserModel(String uuid){
        String sql = "SELECT * FROM app_WingMan_User WHERE uuid ="+"\""+uuid+"\"";
        return (ArrayList<WingManUserModel>)jdbcTemplate.query(sql, new RowMapper<WingManUserModel>() {
            @Override
            public WingManUserModel mapRow(ResultSet rs, int rowNum) throws SQLException {
                WingManUserModel model = new WingManUserModel();
                model.setId(rs.getInt("id"));
                model.setUuid(rs.getString("uuid"));
                model.setCurrentDate(rs.getString("currentDate"));
                model.setExpireDate(rs.getString("expireDate"));
                return model;
            }
        });
    }

    @Override
    public void createWingManUserModel(String uuid) {
        Date currentDate = new Date();
        String createTimeString = currentDate.toString();
        Date expireDate = getDateAfter(currentDate,7);
        String expireDateString = expireDate.toString();
        String sql = "INSERT INTO CloudServer.app_WingMan_User(uuid,currentDate,ExpireDate) VALUES ("+"\""+uuid+"\""+","+"\""+createTimeString+"\""+","+"\""+expireDateString+"\""+");";
        jdbcTemplate.update(sql);
}

    public static Date getDateAfter(Date d,int day){
        Calendar now =Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE,now.get(Calendar.DATE)+day);
        return now.getTime();
    }
}
