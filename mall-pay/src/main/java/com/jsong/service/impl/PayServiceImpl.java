package com.jsong.service.impl;

import com.jsong.bean.Pay;
import com.jsong.common.utils.StringUtils;
import com.jsong.dao.PayDao;
import com.jsong.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * 2020/5/12 13:31
 *
 * @author hujiansong@dobest.com
 * @since 1.8
 */
@Service
public class PayServiceImpl implements PayService {

    @Autowired
    private PayDao payDao;

    @Override
    public int addPay(Pay pay) {
        pay.setId(UUID.randomUUID().toString());
        pay.setCreateTime(new Date());
        pay.setUpdateTime(new Date());
        payDao.saveAndFlush(pay);
        return 1;
    }

    @Override
    public void changePayState(String id,int state) {
        Pay pay =getPay(id);
        pay.setUpdateTime(new Date());
        pay.setState(state);
        payDao.saveAndFlush(pay);
    }

    @Override
    public Pay getPay(String payId) {
        Pay pay = payDao.getOne(payId);
        pay.setTime(StringUtils.getTimeStamp(pay.getCreateTime()));
        return pay;
    }

    @Override
    public Integer updatePay(Pay pay) {
        pay.setUpdateTime(new Date());
        payDao.saveAndFlush(pay);
        return 1;
    }
}
