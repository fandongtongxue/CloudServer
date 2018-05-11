package me.fandong.cloudserver.dao.impl;

import me.fandong.cloudserver.Model.WingManProductModel;
import me.fandong.cloudserver.dao.WingManProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class WingManProductImpl implements WingManProductMapper {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<WingManProductModel> listWingManProducts() {
        return jdbcTemplate.queryForList("SELECT * FROM CloudServer.app_WingMan_Product",WingManProductModel.class);
    }
}
