package com.myfirstspring.toutiao.async.handler;

import com.myfirstspring.toutiao.async.EventHandler;
import com.myfirstspring.toutiao.async.EventModel;
import com.myfirstspring.toutiao.async.EventType;
import com.myfirstspring.toutiao.model.Message;
import com.myfirstspring.toutiao.service.MessageService;
import com.myfirstspring.toutiao.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class LoginExceptionHandler implements EventHandler{
    @Autowired
    MessageService messageService;

    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(18);
        message.setToId(model.getActorId());
        message.setContent("你上次的登陆ip异常");
        message.setCreatedDate(new Date());
        message.setConversationId();
        messageService.addMessage(message);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", model.getExt("username"));
        mailSender.sendWithHTMLTemplate(model.getExt("email"), "登陆异常", "mails/welcome.html", map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
