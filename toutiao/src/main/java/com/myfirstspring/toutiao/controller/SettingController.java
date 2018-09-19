package com.myfirstspring.toutiao.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2017/5/15.
 */

@Controller
public class SettingController {

    private static final Logger logger = LoggerFactory.getLogger(SettingController.class);

    @RequestMapping("/setting")
    @ResponseBody
    public String setting(){
        logger.info("Visit Setting");
        return "Setting page.";
    }
}
