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
import java.util.List;

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
    public WingManUserModel getWingManUserModel(String uuid){
        String sql = "SELECT * FROM CloudServer.app_WingMan_User WHERE uuid ="+uuid;
        return (WingManUserModel)jdbcTemplate.queryForObject(sql,WingManUserModel.class);
    }
}
