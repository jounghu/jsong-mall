package com.jsong.bean.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 2020/5/12 10:53
 *
 * @author hujiansong@dobest.com
 * @since 1.8
 */
@Data
public class Result<T> implements Serializable {

    /**
     * 成功标志
     */
    private boolean success;

    /**
     * 返回代码
     */
    private Integer code;

    /**
     * 失败消息
     */
    private String message;

    /**
     * 时间戳
     */
    private Long timestamp = System.currentTimeMillis();

    /**
     * 返回结果
     */
    private T result;

}
