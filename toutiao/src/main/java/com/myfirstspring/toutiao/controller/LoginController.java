package com.myfirstspring.toutiao.controller;

import com.myfirstspring.toutiao.async.EventModel;
import com.myfirstspring.toutiao.async.EventProducer;
import com.myfirstspring.toutiao.async.EventType;
import com.myfirstspring.toutiao.service.UserService;
import com.myfirstspring.toutiao.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/25.
 */
@Controller
public class LoginController {

    private final static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/reg"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String reg(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rember", defaultValue = "0") int remember,
                      HttpServletResponse response){
        try {
            Map<String, Object> map = userService.register(username, password);
            if(map.containsKey("ticket")){
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if(remember>0)
                    cookie.setMaxAge(3600*24*5);
                response.addCookie(cookie);
                return ToutiaoUtil.getJSONString(0, "注册成功");
            }

            else
                return ToutiaoUtil.getJSONString(1, map);

        }
        catch (Exception e){
            logger.error("注册异常" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "注册异常");
        }

    }

    @RequestMapping(path = {"/login"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "rember", defaultValue = "0") int remember,
                        HttpServletResponse response){
        try {
            Map<String, Object> map = userService.login(username, password);
            if(map.containsKey("ticket")) {
                Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
                cookie.setPath("/");
                if(remember>0)
                    cookie.setMaxAge(3600*24*5);
                response.addCookie(cookie);
                eventProducer.fireEvent(new EventModel(EventType.LOGIN)
                        .setActorId((int)map.get("userId"))
                        .setExt("username", username)
                        .setExt("email", "ujjsmg@163.com"));
                return ToutiaoUtil.getJSONString(0, "成功");
            }
            else
                return ToutiaoUtil.getJSONString(1, map);

        }
        catch (Exception e){
            logger.error("注册异常" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "注册异常");
        }

    }

    @RequestMapping(path = {"/logout"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }
}
