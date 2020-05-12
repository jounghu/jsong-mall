package com.jsong.bean;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 2020/5/12 11:12
 *
 * @author hujiansong@dobest.com
 * @since 1.8
 */
@Entity
@Table(name = "t_pay")
@Data
public class Pay {
    @Id
    @Column
    private String id;

    @Column
    private String nickName;

    @Column
    private BigDecimal money;

    /**
     * 留言
     */
    private String info;

    @Column
    private Date createTime;

    @Column
    private Date updateTime;

    /**
     * 用户通知邮箱
     */
    @Column
    private String email;

    @Column
    private String testEmail;

    /**
     * 显示状态 0待审核 1确认显示 2驳回 3通过不展示 4已扫码
     */
    @Column
    private Integer state = 0;

    @Column
    private String payType;

    /**
     * 支付标识
     */
    @Column
    private String payNum;

    /**
     * 是否自定义输入
     */
    @Column
    private Boolean custom;

    /**
     * 支付设备是否为移动端
     */
    @Column
    private Boolean mobile;

    /**
     * 用户支付设备信息
     */
    @Column(length = 1000)
    private String device;


    /**
     * 生成二维码编号标识token
     */
    @Transient
    private String tokenNum;

    @Transient
    private String time;

    @Transient
    private String passUrl;


    /**
     * 含小程序
     */
    @Transient
    private String passUrl2;

    /**
     * 含xboot
     */
    @Transient
    private String passUrl3;

    @Transient
    private String backUrl;

    @Transient
    private String passNotShowUrl;

    @Transient
    private String editUrl;

    @Transient
    private String delUrl;

    @Transient
    private String closeUrl;

    @Transient
    private String openUrl;

    @Transient
    private String statistic;

}
