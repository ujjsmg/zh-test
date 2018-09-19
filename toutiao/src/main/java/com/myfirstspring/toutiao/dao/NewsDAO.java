package com.myfirstspring.toutiao.dao;

import com.myfirstspring.toutiao.model.News;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/5/20.
 */
@Mapper
@Repository
public interface NewsDAO {
    String TABLE_NAME = "news";
    String INSERT_FIELD = " title, link, image, like_count, comment_count, created_date, user_id";
    String SELECT_FIELD = " id,"+INSERT_FIELD;

    @Insert({"insert into ",TABLE_NAME, "(", INSERT_FIELD,
            ") values (#{title},#{link},#{image},#{likeCount},#{commentCount},#{createdDate},#{userId})"})
    int addNews(News news);

    @Select({"select",SELECT_FIELD, " from ", TABLE_NAME, " where id=#{id}"})
    News getById(int id);

    @Update({"update ", TABLE_NAME, " set comment_count=#{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("commentCount") int commentCount, @Param("id") int id);

    @Update({"update ", TABLE_NAME, " set like_count=#{likeCount} where id=#{id}"})
    int updateLikeCount(@Param("likeCount") int likeCount, @Param("id") int id);

    List<News> selectByUserIdAndOffset(@Param("userId") int userId,@Param("offset") int offset,@Param("limit") int limit);
}
