package com.jsong.bean.dto;

import lombok.Data;

/**
 * 2020/5/12 11:01
 *
 * @author hujiansong@dobest.com
 * @since 1.8
 */
@Data
public class PageVo {

    /**
     * 页号
     */
    private int pageNumber;

    /**
     * 页面大小
     */
    private int pageSize;

    /**
     * 排序字段
     */
    private String sort;

    /**
     * 排序方式 asc/desc
     */
    private String order;
}
