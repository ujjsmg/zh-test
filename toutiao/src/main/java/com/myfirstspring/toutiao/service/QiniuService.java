package com.myfirstspring.toutiao.service;

import com.alibaba.fastjson.JSONObject;
import com.myfirstspring.toutiao.util.ToutiaoUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class QiniuService {

    private final static Logger logger = LoggerFactory.getLogger(QiniuService.class);

    //构造一个带指定Zone对象的配置类
    Configuration cfg = new Configuration(Zone.zone1());
    //...其他参数参考类注释
    UploadManager uploadManager = new UploadManager(cfg);
    //...生成上传凭证，然后准备上传
    String accessKey = "QfMlwE2psKSJSZIUERB6q5SoKGuPjo82NVFZbfb0";
    String secretKey = "ixtenSzQDoeW88X2Cx1oDMdGnVCfMe8cyFLdpU01";
    String bucket = "zhspringboot";

    Auth auth = Auth.create(accessKey, secretKey);
    String upToken = auth.uploadToken(bucket);

    public String saveImage(MultipartFile file) throws IOException{
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        if(dotPos < 0)
            return null;
        String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
        if(!ToutiaoUtil.isFileAllowed(fileExt))
            return null;
        String fileName = UUID.randomUUID().toString().replaceAll("-","") + "." + fileExt;
        try {
            Response response = uploadManager.put(file.getBytes(), fileName, upToken);
            //解析上传成功的结果
            //DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            //System.out.println(putRet.key);
            //System.out.println(putRet.hash);
            if (response.isOK() && response.isJson()){
                return ToutiaoUtil.qiniu_domain + JSONObject.parseObject(response.bodyString()).get("key").toString();
            }
            else {
                logger.error("七牛异常" + response.bodyString());
                return null;
            }
        }
        catch (QiniuException ex) {
            logger.error("七牛异常" + ex.getMessage());
            return null;
        }
    }

}
