package me.fandong.cloudserver.dao;

public interface MyRequestMapper {
    void createMyRequest(String url, String params, String result, String status, String msg);
}
