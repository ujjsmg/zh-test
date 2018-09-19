package com.myfirstspring.toutiao.dao;

import com.myfirstspring.toutiao.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/5/20.
 */
@Mapper
@Repository
public interface UserDAO {
    String TABLE_NAME = "user";
    String INSERT_FIELD = " name, password, salt, head_url";
    String SELECT_FIELD = " id, name, password, salt, head_url";

    @Insert({"insert into ",TABLE_NAME, "(", INSERT_FIELD,
            ") values (#{name},#{password},#{salt},#{headUrl})"})
    int addUser(User user);

    @Select({"select ",SELECT_FIELD, " from ",TABLE_NAME," where id=#{id}"})
    User selectById(int id);

    @Select({"select ",SELECT_FIELD, " from ",TABLE_NAME," where name=#{name}"})
    User selectByName(String name);

    @Update({"update ",TABLE_NAME," set password=#{password} where id=#{id}"})
    void updatePassword(User user);
}
