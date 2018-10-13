package com.myfirstspring.toutiao.controller;

/**
 * Created by Administrator on 2017/5/13.
 */

import com.myfirstspring.toutiao.model.User;
import com.myfirstspring.toutiao.service.MyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

//@Controller
public class IndexController {
    @Autowired
    private MyService myService;
    private final static Logger logger = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping(path = {"/","/index"})
    @ResponseBody
    public String index(HttpSession session){
        logger.info("Visit index");
        return "Hello " + session.getAttribute("redirect") + "<br>" + myService.say();
    }

    @RequestMapping(path = {"/profile/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("userId") int id,
                          @RequestParam(value = "type",defaultValue = "1") int type){
        return String.format("%d:%d",id,type);
    }

    @RequestMapping(value = {"/vm"})
    public String news(Model model){
        model.addAttribute("value","1");
        List<String> list = Arrays.asList(new String[]{"element1","element2"});
        model.addAttribute("list",list);
        model.addAttribute("user",new User("WillZ"));
        return "news";
    }

    @RequestMapping(value = {"/request"})
    @ResponseBody
    public String request(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession session){
        StringBuilder sb = new StringBuilder("");
        Enumeration<String> enumeration = request.getHeaderNames();
        while(enumeration.hasMoreElements()){
            String name = enumeration.nextElement();
            sb.append(name+":"+request.getHeader(name)+"<br>");
        }
        sb.append("Cookie:" + request.getCookies() + "<br>");
        sb.append("Method:" + request.getMethod() + "<br>");
        sb.append("Query:" + request.getQueryString() + "<br>");
        sb.append("URI:" + request.getRequestURI() + "<br>");
        return sb.toString();
    }

    @RequestMapping(value = {"/response"})
    @ResponseBody
    public String response(@CookieValue(value = "cookieId", defaultValue = "id") String cookieId,
                           @RequestParam(value = "key", defaultValue = "key") String key,
                           @RequestParam(value = "value", defaultValue = "value") String value,
                           HttpServletResponse response){
        response.addCookie(new Cookie(key, value));
        response.addHeader(key, value);
        return "cookieId:" + cookieId;
    }

    @RequestMapping(value = {"/redirect/{code}"})
    public RedirectView redirect(@PathVariable("code") int code,
                                 HttpSession session){
        RedirectView redirectView = new RedirectView("/",true);
        if(code == 301) {
            redirectView.setStatusCode(HttpStatus.PERMANENT_REDIRECT);
        }
        session.setAttribute("redirect", "from redirect");
        return redirectView;
    }

    @RequestMapping(value = {"/admin"})
    @ResponseBody
    public String admin(@RequestParam(value = "key", required = false) String key){
        if("admin".equals(key)) {
            return "Hello Admin!";
        }
        throw new IllegalArgumentException("Key is wrong!!");
    }

    @ExceptionHandler
    @ResponseBody
    public String error(Exception e){
        return "Error:" + e.getMessage();
    }
}
