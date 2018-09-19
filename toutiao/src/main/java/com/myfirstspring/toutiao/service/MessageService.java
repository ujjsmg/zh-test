package com.myfirstspring.toutiao.service;

import com.myfirstspring.toutiao.dao.MessageDAO;
import com.myfirstspring.toutiao.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageDAO messageDAO;

    public int addMessage(Message message) {
        return messageDAO.addMessage(message);
    }

    public List<Message> getConversationDetail(String conversationId, int offset, int limit){
        return messageDAO.getConversationDetail(conversationId, offset, limit);
    }

    public List<Message> getConversationList(int userId, int offset, int limit){
        return messageDAO.getConversationList(userId, offset, limit);
    }

    public int getConversationUnreadCount(int userId, String conversationId){
        return messageDAO.getConversationUnreadCount(conversationId, userId);
    }

    public int updateHasRead(int id, String conversationId){
        return messageDAO.updateHasRead(id, conversationId);
    }
}
