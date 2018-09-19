package com.myfirstspring.toutiao.configuration;

import com.myfirstspring.toutiao.interceptor.LoginRequiredInterceptor;
import com.myfirstspring.toutiao.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

/**
 * Created by Administrator on 2017/5/26.
 */
@Component
public class WebConfiguration extends WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter{

    @Autowired
    PassportInterceptor passportInterceptor;

    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor);
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/setting*");
        super.addInterceptors(registry);
    }
}
