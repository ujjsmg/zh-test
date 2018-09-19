package com.myfirstspring.toutiao;

import com.myfirstspring.toutiao.dao.CommentDAO;
import com.myfirstspring.toutiao.dao.LoginTicketDAO;
import com.myfirstspring.toutiao.dao.NewsDAO;
import com.myfirstspring.toutiao.dao.UserDAO;
import com.myfirstspring.toutiao.model.LoginTicket;
import com.myfirstspring.toutiao.model.News;
import com.myfirstspring.toutiao.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;

/**
 * Created by Administrator on 2017/5/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
public class InitDatabaseTests {

    @Autowired
    UserDAO userDAO;

    @Autowired
    NewsDAO newsDAO;

    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Autowired
    CommentDAO commentDAO;

    @Test
    public void contextLoads() {
        Random random = new Random();
        for(int i=0; i<10; i++){
            /*
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",random.nextInt(1000)));
            user.setName("USER"+i);
            user.setPassword("");
            user.setSalt("");
            userDAO.addUser(user);

            News news = new News();
            news.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000*3600*3*i);
            news.setCreatedDate(date);
            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png",random.nextInt(1000)));
            news.setLikeCount(i);
            news.setUserId(i+1);
            news.setTitle("TITLE:"+i);
            news.setLink(String.format("http://www.nowcoder.com/%d.html",i));
            newsDAO.addNews(news);
            */
            LoginTicket loginTicket = new LoginTicket();
            loginTicket.setUserId(i+1);
            loginTicket.setStatus(0);
            Date date = new Date();
            date.setTime(date.getTime() + 1000*3600*3*i);
            loginTicket.setExpired(date);
            loginTicket.setTicket(String.format("TICKET%d",i+1));
            loginTicketDAO.addTicket(loginTicket);
        }
    }
}
