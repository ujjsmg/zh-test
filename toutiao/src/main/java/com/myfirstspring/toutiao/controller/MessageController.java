package com.myfirstspring.toutiao.controller;

import com.myfirstspring.toutiao.model.HostHolder;
import com.myfirstspring.toutiao.model.Message;
import com.myfirstspring.toutiao.model.User;
import com.myfirstspring.toutiao.model.ViewObject;
import com.myfirstspring.toutiao.service.MessageService;
import com.myfirstspring.toutiao.service.UserService;
import com.myfirstspring.toutiao.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {

    private final static Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String conversationList(Model model){
        try{
            int localUserId = hostHolder.getUser().getId();
            List<Message> conversationList = messageService.getConversationList(localUserId, 0, 10);
            List<ViewObject> conversations = new ArrayList<>();
            for(Message message : conversationList){
                ViewObject vo = new ViewObject();
                vo.set("conversation", message);
                int targetId = message.getFromId() == localUserId ? message.getToId() : message.getFromId();
                User user = userService.getUser(targetId);
                vo.set("user", user);
                vo.set("unread", messageService.getConversationUnreadCount(localUserId, message.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations", conversations);
        }
        catch (Exception e){
            logger.error("获取站内信列表失败" + e.getMessage());
        }
        return "letter";
    }

    @RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
    public String conversationDetail(@RequestParam("conversationId") String conversationId, Model model){
        try{
            int localUserId = hostHolder.getUser().getId();
            messageService.updateHasRead(localUserId, conversationId);
            List<Message> conversationList = messageService.getConversationDetail(conversationId, 0, 10);
            List<ViewObject> messages = new ArrayList<>();
            for (Message message : conversationList){
                ViewObject vo = new ViewObject();
                vo.set("message", message);
                User user = userService.getUser(message.getFromId());
                if(user == null)
                    continue;
                vo.set("headUrl", user.getHeadUrl());
                vo.set("userId", user.getId());
                messages.add(vo);
            }
            model.addAttribute("messages", messages);
            return "letterDetail";
        }
        catch (Exception e){
            logger.error("获取详细信息失败" + e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addComment(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content){
        try{
            Message message = new Message();
            message.setFromId(fromId);
            message.setToId(toId);
            message.setContent(content);
            message.setCreatedDate(new Date());
            message.setConversationId();
            //message.setConversationId(fromId < toId ? String.format("%d_%d",fromId, toId) : String.format("%d_%d",toId, fromId));
            messageService.addMessage(message);
            return ToutiaoUtil.getJSONString(message.getId());
        }
        catch (Exception e){
            logger.error("添加评论失败" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "插入评论失败");
        }

    }
}
