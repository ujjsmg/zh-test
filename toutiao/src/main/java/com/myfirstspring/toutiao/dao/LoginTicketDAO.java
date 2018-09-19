package com.myfirstspring.toutiao.dao;

import com.myfirstspring.toutiao.model.LoginTicket;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/5/20.
 */
@Mapper
@Repository
public interface LoginTicketDAO {
    String TABLE_NAME = "login_ticket";
    String INSERT_FIELD = " user_id, expired, status, ticket";
    String SELECT_FIELD = " id,"+INSERT_FIELD;

    @Insert({"insert into ",TABLE_NAME, "(", INSERT_FIELD,
            ") values (#{userId},#{expired},#{status},#{ticket})"})
    int addTicket(LoginTicket loginTicket);

    @Select({"select ",SELECT_FIELD, " from ",TABLE_NAME," where ticket=#{ticket}"})
    LoginTicket selectByTicket(String ticket);

    @Update({"update ",TABLE_NAME," set status=#{status} where ticket=#{ticket}"})
    void updateStatus(@Param("status") int status, @Param("ticket") String ticket);
}
