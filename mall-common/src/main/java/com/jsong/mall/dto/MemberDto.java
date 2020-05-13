package com.jsong.mall.dto;

import lombok.Data;

/**
 * 2020/5/13 17:23
 *
 * member 给前端
 *
 * @author hujiansong@dobest.com
 * @since 1.8
 */
@Data
public class MemberDto {

    private Long id;

    private String username;

    private String phone;

    private String email;

    private String sex;

    private String address;

    private String file;

    private String description;

    private Integer points;

    private Long balance;

    private int state;

    private String token;

    private String message;

}
