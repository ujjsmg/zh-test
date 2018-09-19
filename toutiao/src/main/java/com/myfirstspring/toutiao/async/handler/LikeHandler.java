package com.myfirstspring.toutiao.async.handler;

import com.myfirstspring.toutiao.async.EventHandler;
import com.myfirstspring.toutiao.async.EventModel;
import com.myfirstspring.toutiao.async.EventType;
import com.myfirstspring.toutiao.model.Message;
import com.myfirstspring.toutiao.model.User;
import com.myfirstspring.toutiao.service.MessageService;
import com.myfirstspring.toutiao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class LikeHandler implements EventHandler{

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        //系统用户
        message.setFromId(18);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.getUser(model.getActorId());
        message.setContent("用户" + user.getName() +
                "赞了你的资讯,http://127.0.0.1:8080/news/" +
                model.getEntityId());
        message.setConversationId();
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
