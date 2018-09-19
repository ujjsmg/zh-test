package com.myfirstspring.toutiao.service;

/**
 * Created by Administrator on 2017/5/15.
 */

import org.springframework.stereotype.Service;

@Service
public class MyService {
    public String say(){
        return "This is from Service.";
    }
}
