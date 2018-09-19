package com.myfirstspring.toutiao;

import com.myfirstspring.toutiao.service.LikeService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
public class LikeServiceTests {
    //初始化数据、执行要测试的业务、验证测试的数据、清除数据
    @Autowired
    LikeService likeService;

    @Test
    public void testLike(){
        likeService.like(123, 1, 1);
        Assert.assertEquals(1, likeService.getLikeStatus(123, 1, 1));
    }

    @Test
    public void testDislike(){
        likeService.dislike(123, 1, 1);
        Assert.assertEquals(-1, likeService.getLikeStatus(123, 1, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testException(){
        throw new IllegalArgumentException("xxx");
    }

    @Before
    public void setUp(){
        System.out.println("setUp");
    }

    @After
    public void tearDown(){
        System.out.println("tearDown");
    }

    @BeforeClass
    public static void beforeClass(){
        System.out.println("beforeClass");
    }

    @AfterClass
    public static void afterClass(){
        System.out.println("afterClass");
    }
}
