package com.jsong;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 2020/5/12 16:25
 *
 * @author hujiansong@dobest.com
 * @since 1.8
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
public class ApplicationTests {

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Test
    public void testRedis(){
        redisTemplate.opsForValue().set("1","2");
    }
}
