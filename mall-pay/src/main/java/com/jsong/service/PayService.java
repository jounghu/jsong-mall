package com.jsong.service;

import com.jsong.bean.Pay;

/**
 * 2020/5/12 13:30
 *
 * @author hujiansong@dobest.com
 * @since 1.8
 */
public interface PayService {

    /**
     * 添加支付订单
     *
     * @param pay
     * @return
     */
    int addPay(Pay pay);

    /**
     * @param id    payOrderId
     * @param state 显示状态 0待审核 1确认显示 2驳回 3通过不展示 4已扫码
     */
    void changePayState(String id, int state);

    Pay getPay(String payId);

    Integer updatePay(Pay pay);
}
