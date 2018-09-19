package com.myfirstspring.toutiao.model;

import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/5/26.
 */
@Component
public class HostHolder {
    public static ThreadLocal<User> users = new ThreadLocal<User>();

    public User getUser(){
        return users.get();
    }

    public void setUser(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }
}
