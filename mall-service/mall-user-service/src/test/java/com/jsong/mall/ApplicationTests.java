package com.jsong.mall;

import com.jsong.mall.pojo.TbMember;
import com.jsong.mall.utils.JsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 2020/5/13 16:10
 *
 * @author hujiansong@dobest.com
 * @since 1.8
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
public class ApplicationTests {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void testRedisTemplate(){
        TbMember tbMember = new TbMember();
        tbMember.setId(1L);
        tbMember.setUsername("hjs");
        tbMember.setPassword("123456");
        redisTemplate.opsForValue().set("hjs", JsonUtils.write(tbMember));
    }


    @Test
    public void testRedisTemplate1(){
        String hjs = redisTemplate.opsForValue().get("hjs");
        TbMember from = JsonUtils.from(hjs, TbMember.class);
        System.out.println(from);
    }

}
