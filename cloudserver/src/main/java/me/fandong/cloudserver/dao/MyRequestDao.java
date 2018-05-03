package me.fandong.cloudserver.dao;

import me.fandong.cloudserver.Model.MyRequest;
import java.util.List;

public interface MyRequestDao {
    /**
     * 新增请求记录
     * @param request
     */
    void createMyRequest(MyRequest request);

    /**
     * 查询请求记录列表
     * @return
     */
    List<MyRequest> findAllMyRequest();
}
