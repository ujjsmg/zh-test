package com.myfirstspring.toutiao.controller;

import com.myfirstspring.toutiao.async.EventModel;
import com.myfirstspring.toutiao.async.EventProducer;
import com.myfirstspring.toutiao.async.EventType;
import com.myfirstspring.toutiao.model.*;
import com.myfirstspring.toutiao.service.*;
import com.myfirstspring.toutiao.util.ToutiaoUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/6/1.
 */
@Controller
public class NewsController {

    private final static Logger logger = LoggerFactory.getLogger(NewsController.class);

    @Autowired
    UserService userService;

    @Autowired
    NewsService newsService;

    @Autowired
    QiniuService qiniuService;

    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/news/{newsId}"}, method = {RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId") int id, Model model){
        News news = newsService.getById(id);
        if(news != null) {
            int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
            if(localUserId != 0){
                model.addAttribute("like", likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS, news.getId()));
            }
            else
                model.addAttribute("like", 0);
            List<Comment> comments = commentService.getCommentByEntity(news.getId(), EntityType.ENTITY_NEWS);
            List<ViewObject> commentVOs = new ArrayList<ViewObject>();
            for (Comment comment : comments) {
                ViewObject vo = new ViewObject();
                vo.set("comment", comment);
                vo.set("user", userService.getUser(comment.getUserId()));
                commentVOs.add(vo);
            }
            model.addAttribute("comments", commentVOs);
        }
        model.addAttribute("news", news);
        model.addAttribute("owner", userService.getUser(news.getUserId()));
        return "detail";
    }

    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam("newsId") int newsId,
                             @RequestParam("content") String content){
        try{
            Comment comment = new Comment();
            comment.setUserId(hostHolder.getUser().getId());
            comment.setContent(content);
            comment.setEntityId(newsId);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setCreatedDate(new Date());
            comment.setStatus(0);
            commentService.addComment(comment);
            int count = commentService.getCommentCount(comment.getEntityId(), comment.getEntityType());
            newsService.updateCommentCount(newsId, count);

            eventProducer.fireEvent(new EventModel(EventType.COMMENT)
                    .setActorId(hostHolder.getUser().getId()).setEntityId(newsId)
                    .setEntityType(EntityType.ENTITY_NEWS).setEntityOwnerId(newsService.getById(newsId).getUserId()));
        }
        catch (Exception e){
            logger.error("添加评论失败" + e.getMessage());
        }
        return "redirect:/news/" + String.valueOf(newsId);
    }

    @RequestMapping(path = {"/image"}, method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName,
                              HttpServletResponse response){
        response.setContentType("image");
        try {
            StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.image_dir + imageName)),
                    response.getOutputStream());
        }
        catch (Exception e){
            logger.error("读取图片错误" + e.getMessage());
        }
    }

    @RequestMapping(path = {"/user/addNews"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link){
        if (StringUtils.isBlank(image))
            return ToutiaoUtil.getJSONString(1,"图片不能为空");
        if (StringUtils.isBlank(title))
            return ToutiaoUtil.getJSONString(1,"标题不能为空");
        if (StringUtils.isBlank(link))
            return ToutiaoUtil.getJSONString(1,"链接不能为空");
        try{
            News news = new News();
            news.setImage(image);
            news.setTitle(title);
            news.setLink(link);
            news.setCreatedDate(new Date());
            if(hostHolder.getUser() != null)
                news.setUserId(hostHolder.getUser().getId());
            else
                //匿名id
                news.setUserId(100);
            newsService.addNews(news);
            return ToutiaoUtil.getJSONString(0);
        }
        catch (Exception e){
            logger.error("添加资讯错误");
            return ToutiaoUtil.getJSONString(1,"发布失败");
        }

    }

    @RequestMapping(path = {"/uploadImage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file){
        try {
            //String fileUrl = newsService.saveImage(file);
            String fileUrl = qiniuService.saveImage(file);
            if (fileUrl == null)
                return ToutiaoUtil.getJSONString(1,"上传图片失败");
            return ToutiaoUtil.getJSONString(0, fileUrl);
        }
        catch (Exception e){
            logger.error("上传图片失败!" + e.getMessage());
            return ToutiaoUtil.getJSONString(1,"上传图片失败");
        }
    }
}
