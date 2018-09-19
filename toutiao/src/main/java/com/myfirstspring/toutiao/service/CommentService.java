package com.myfirstspring.toutiao.service;

import com.myfirstspring.toutiao.dao.CommentDAO;
import com.myfirstspring.toutiao.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentDAO commentDAO;

    public List<Comment> getCommentByEntity(int entityid, int entitytype) {
        return commentDAO.selectByEntity(entityid, entitytype);
    }

    public int addComment(Comment comment){
        return commentDAO.addComment(comment);
    }

    public int getCommentCount(int entityid, int entitytype) {
        return commentDAO.getCommentCount(entityid, entitytype);
    }
}
