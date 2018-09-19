package com.myfirstspring.toutiao.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/5/15.
 */

@Aspect
@Component
public class LogAspect {
    private final static Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.myfirstspring.toutiao.controller.*Controller.*(..))")
    public void beforeMethod(JoinPoint joinPoint){
        StringBuilder sb = new StringBuilder("");
        for (Object obj : joinPoint.getArgs())
            sb.append(obj.toString() + "|");
        logger.info("before method:" + sb.toString());
    }

    @After("execution(* com.myfirstspring.toutiao.controller.*Controller.*(..))")
    public void afterMethod(JoinPoint joinPoint){
        logger.info("after method");
    }
}
