package com.jsong.mall;

import com.jsong.mall.dto.MemberDto;
import com.jsong.mall.pojo.TbMember;

/**
 * 2020/5/13 15:35
 * <p>
 * 用户统一接口
 *
 * @author hujiansong@dobest.com
 * @since 1.8
 */
public interface MemberService {

    /**
     * 上传头像
     *
     * @param userId
     * @param token
     * @param imgData
     * @return
     */
    String imageUpload(Long userId, String token, String imgData);

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return
     */
    MemberDto login(String username, String password);

    /**
     * 注销登录
     * @param token
     * @return
     */
     int logout(String token);
}
