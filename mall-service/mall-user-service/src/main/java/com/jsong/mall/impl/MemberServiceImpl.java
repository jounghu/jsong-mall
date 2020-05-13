package com.jsong.mall.impl;

import com.jsong.mall.MemberService;
import com.jsong.mall.dto.MemberDto;
import com.jsong.mall.mapper.TbMemberMapper;
import com.jsong.mall.pojo.TbMember;
import com.jsong.mall.pojo.TbMemberExample;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 2020/5/13 16:00
 *
 * @author hujiansong@dobest.com
 * @since 1.8
 */
public class MemberServiceImpl implements MemberService {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private TbMemberMapper memberMapper;

    @Override
    public String imageUpload(Long userId, String token, String imgData) {
        return null;
    }

    @Override
    public MemberDto login(String username, String password) {
        TbMemberExample memberExample = new TbMemberExample();
        TbMemberExample.Criteria criteria = memberExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        criteria.andStateEqualTo(1);
        List<TbMember> members = memberMapper.selectByExample(memberExample);
        if (members == null || members.size() == 0) {
            MemberDto noMember = new MemberDto();
            noMember.setState(0);
            noMember.setMessage("用户名或者用户错误!");
            return noMember;
        }

        TbMember member = members.get(0);
        if (member != null) {
            String md5Pass = new String(DigestUtils.md5Digest(password.getBytes()));
            if (!md5Pass.equals(member.getPassword())) {
                MemberDto noMember = new MemberDto();
                noMember.setState(0);
                noMember.setMessage("用户名或者用户错误!");
                return noMember;
            }
        }

        if (member == null) {
            MemberDto noMember = new MemberDto();
            noMember.setState(0);
            noMember.setMessage("用户名或者用户错误!");
            return noMember;
        }

        // 生成Token
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set("USER::" + member.getId(), token,30, TimeUnit.MINUTES);

        MemberDto memberDto = new MemberDto();
        memberDto.setToken(token);
        BeanUtils.copyProperties(member, memberDto);
        return memberDto;
    }

    @Override
    public int logout(String token) {
        return 0;
    }


}
