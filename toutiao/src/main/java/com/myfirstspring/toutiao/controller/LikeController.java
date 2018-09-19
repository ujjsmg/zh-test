package com.myfirstspring.toutiao.controller;

import com.myfirstspring.toutiao.async.EventModel;
import com.myfirstspring.toutiao.async.EventProducer;
import com.myfirstspring.toutiao.async.EventType;
import com.myfirstspring.toutiao.model.EntityType;
import com.myfirstspring.toutiao.model.HostHolder;
import com.myfirstspring.toutiao.model.News;
import com.myfirstspring.toutiao.service.LikeService;
import com.myfirstspring.toutiao.service.NewsService;
import com.myfirstspring.toutiao.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    NewsService newsService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = "/like", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("newsId") int newsId){
        int userId = hostHolder.getUser().getId();
        long likeCount = likeService.like(userId, EntityType.ENTITY_NEWS, newsId);
        newsService.updateLikeCount(newsId, (int)likeCount);
        News news = newsService.getById(newsId);
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setActorId(userId).setEntityId(newsId)
                .setEntityType(EntityType.ENTITY_NEWS).setEntityOwnerId(news.getUserId()));
        return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(path = "/dislike", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("newsId") int newsId){
        int userId = hostHolder.getUser().getId();
        long likeCount = likeService.dislike(userId, EntityType.ENTITY_NEWS, newsId);
        newsService.updateLikeCount(newsId, (int)likeCount);
        return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
    }
}
