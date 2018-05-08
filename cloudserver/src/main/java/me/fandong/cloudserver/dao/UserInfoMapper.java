package me.fandong.cloudserver.dao;
import me.fandong.cloudserver.Model.UserInfo;

public interface UserInfoMapper {
    void createUser(String tel,String pwd);

    UserInfo getUser(Integer id);

    void updateUser(String user_id, String nickName);

    void deleteUserByUserId(Integer id);
}
