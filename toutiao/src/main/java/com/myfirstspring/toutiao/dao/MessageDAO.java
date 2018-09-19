package com.myfirstspring.toutiao.dao;

import com.myfirstspring.toutiao.model.Message;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MessageDAO {
    String TABLE_NAME = "message";
    String INSERT_FIELD = " from_id, to_id, content, has_read, created_date, conversation_id";
    String SELECT_FIELD = " id,"+INSERT_FIELD;

    @Insert({"insert into ",TABLE_NAME, "(", INSERT_FIELD,
            ") values (#{fromId},#{toId},#{content},#{hasRead},#{createdDate},#{conversationId})"})
    int addMessage(Message comment);

    @Select({"select ", SELECT_FIELD," from ",TABLE_NAME,
            " where conversation_id=#{conversationId} order by id desc limit #{offset}, #{limit}"})
    List<Message> getConversationDetail(@Param("conversationId") String conversationId,
                                        @Param("offset") int offset, @Param("limit") int limit);

    @Select({"select count(id) from ",TABLE_NAME,
            " where conversation_id=#{conversationId} and to_id=#{userId} and has_read=0"})
    int getConversationUnreadCount(@Param("conversationId") String conversationId, @Param("userId") int userId);

    @Select({"select ", INSERT_FIELD, " ,count(id) as id from ( select * from ", TABLE_NAME,
            " where from_id=#{userId} or to_id=#{userId} order by id desc) tt group by conversation_id order by created_date desc limit #{offset},#{limit}"})
    List<Message> getConversationList(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);

    @Update({"update ",TABLE_NAME," set has_read=1 where to_id=#{id} and conversation_id=#{conversationId}"})
    int updateHasRead(@Param("id") int id, @Param("conversationId") String conversationId);
}
