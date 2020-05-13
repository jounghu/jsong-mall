package com.jsong.mall.utils;

import lombok.Data;

/**
 * 2020/5/13 15:38
 *
 * @author hujiansong@dobest.com
 * @since 1.8
 */
@Data
public class Result<T> {

    /**
     * 成功标识
     */
    private boolean success;

    
    private int code;

    private String message;

    private long timestamp;

    private T data;

}
