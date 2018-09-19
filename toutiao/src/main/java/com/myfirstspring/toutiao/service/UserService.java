package com.myfirstspring.toutiao.service;

import com.myfirstspring.toutiao.dao.LoginTicketDAO;
import com.myfirstspring.toutiao.dao.UserDAO;
import com.myfirstspring.toutiao.model.LoginTicket;
import com.myfirstspring.toutiao.model.User;
import com.myfirstspring.toutiao.util.ToutiaoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Administrator on 2017/5/20.
 */
@Service
public class UserService {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public Map<String, Object> register(String name, String pwd){
        Map<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isBlank(name)) {
            map.put("msgname", "用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(pwd)){
            map.put("msgpwd", "密码不能为空");
            return map;
        }
        if(pwd.length()<6){
            map.put("msgpwd", "密码不能小于6位");
            return map;
        }
        User user = userDAO.selectByName(name);
        if(user != null){
            map.put("msgname", "用户名已经被注册");
            return map;
        }
        user = new User();
        user.setName(name);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        user.setPassword(ToutiaoUtil.MD5(pwd+user.getSalt()));
        userDAO.addUser(user);

        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    public Map<String, Object> login(String name, String pwd){
        Map<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isBlank(name)) {
            map.put("msgname", "用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(pwd)){
            map.put("msgpwd", "密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(name);
        if(user == null){
            map.put("msgname", "用户名不存在");
            return map;
        }
        if(!ToutiaoUtil.MD5(pwd+user.getSalt()).equals(user.getPassword())){
            map.put("msgpwd", "密码错误");
            return map;
        }

        map.put("userId", user.getId());

        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);

        return map;
    }

    private String addLoginTicket(int userId){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime()+1000*3600*24);
        loginTicket.setExpired(date);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(loginTicket);
        return loginTicket.getTicket();
    }

    public User getUser(int id){
        return userDAO.selectById(id);
    }

    public void logout(String ticket){
        loginTicketDAO.updateStatus(1, ticket);
    }
}
