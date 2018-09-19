package com.myfirstspring.toutiao.controller;

import com.myfirstspring.toutiao.model.EntityType;
import com.myfirstspring.toutiao.model.HostHolder;
import com.myfirstspring.toutiao.model.News;
import com.myfirstspring.toutiao.model.ViewObject;
import com.myfirstspring.toutiao.service.LikeService;
import com.myfirstspring.toutiao.service.NewsService;
import com.myfirstspring.toutiao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/20.
 */
@Controller
public class HomeController {
    @Autowired
    NewsService newsService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    private List<ViewObject> getNews(int userId, int offset, int limit){
        List<News> newsList = newsService.getNews(userId, offset, limit);
        int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
        List<ViewObject> vos = new ArrayList<>();
        for (News news : newsList){
            ViewObject vo = new ViewObject();
            vo.set("news",news);
            vo.set("user",userService.getUser(news.getUserId()));
            if(localUserId != 0){
                vo.set("like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
            }
            else
                vo.set("like", 0);
            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping(path = {"/","/index"})
    public String index(Model model,
                        @RequestParam(value = "pop", defaultValue = "0") int pop){
        model.addAttribute("vos", getNews(0, 0, 10));
        model.addAttribute("pop", pop);
        return "home";
    }

    @RequestMapping(path = {"/user/{userId}"})
    public String userIndex(Model model, @PathVariable("userId") int userId){
        model.addAttribute("vos", getNews(userId, 0, 10));
        return "home";
    }
}
