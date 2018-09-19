package com.myfirstspring.toutiao.service;

import com.myfirstspring.toutiao.dao.NewsDAO;
import com.myfirstspring.toutiao.model.News;
import com.myfirstspring.toutiao.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2017/5/20.
 */
@Service
public class NewsService {
    @Autowired
    private NewsDAO newsDAO;

    public List<News> getNews(int userId, int offset, int limit){
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }

    public int addNews(News news){
        newsDAO.addNews(news);
        return news.getId();
    }

    public News getById(int id){
        return newsDAO.getById(id);
    }

    public int updateCommentCount(int id, int commentCount){
        return newsDAO.updateCommentCount(commentCount, id);
    }

    public int updateLikeCount(int id, int likeCount) {
        return newsDAO.updateLikeCount(likeCount, id);
    }

    public String saveImage(MultipartFile file) throws IOException{
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        if(dotPos < 0)
            return null;
        String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
        if(!ToutiaoUtil.isFileAllowed(fileExt))
            return null;
        String fileName = UUID.randomUUID().toString().replaceAll("-","") + "." + fileExt;
        Files.copy(file.getInputStream(), new File(ToutiaoUtil.image_dir + fileName).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
        return ToutiaoUtil.local_domain + "image?name=" + fileName;
    }
}
