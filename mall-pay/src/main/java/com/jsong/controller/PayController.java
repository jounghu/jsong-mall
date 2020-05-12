package com.jsong.controller;

import com.jsong.bean.Pay;
import com.jsong.bean.dto.Result;
import com.jsong.common.utils.EmailUtils;
import com.jsong.common.utils.IpInfoUtils;
import com.jsong.common.utils.ResultUtil;
import com.jsong.common.utils.StringUtils;
import com.jsong.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 2020/5/12 10:49
 *
 * @author hujiansong@dobest.com
 * @since 1.8
 */
@Slf4j
@Controller
public class PayController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private PayService payService;

    @Autowired
    private EmailUtils emailUtils;

    @Value("${qrnum}")
    private Integer QR_CODE_NUM;

    @Value("${mail.sender}")
    private String EMAIL_SENDER;

    @Value("${mail.receiver}")
    private String EMAIL_SENDTO;

    @Value("${ip.expire}")
    private Integer IP_EXPIRE;

    @Value("${token.admin.expire}")
    private Integer ADMIN_EXPIRE;

    @Value("${my.token}")
    private String MY_TOKEN;

    @Value("${fake.pre}")
    private String FAKE_PRE;

    @Value("${token.fake.expire}")
    private Integer FAKE_EXPIRE;

    @Value("${server.url}")
    private String SERVER_URL;

    @RequestMapping(value = "/pay/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Result<Object> pay(@PathVariable("id") String id, String token
    ) {
        String temp = redisTemplate.opsForValue().get(id);
        if (StringUtils.isBlank(temp) || !temp.equals(token)) {
            return new ResultUtil<>().setErrorMsg("无效的Token或者非法的链接");
        }
        try {
            Pay pay = payService.getPay(id);
            return new ResultUtil<>().setData(pay);
        } catch (Exception e) {
            return new ResultUtil<>().setErrorMsg("获取支付数据报错，id：" + id);
        }
    }

    @RequestMapping("/pay/add")
    @ResponseBody
    public Result<Object> payAdd(Pay pay, HttpServletRequest request) {
        // 校验邮箱是否合法
        if (StringUtils.isBlank(pay.getNickName()) ||
                StringUtils.isBlank(String.valueOf(pay.getMoney())) ||
                StringUtils.isBlank(pay.getEmail()) ||
                !EmailUtils.checkEmail(pay.getEmail())
        ) {
            return new ResultUtil<>().setErrorMsg("请填写正确的邮箱和金额!");
        }

        // 检验IP
        String ip = IpInfoUtils.getIpAddr(request);
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }

        String ipHack = redisTemplate.opsForValue().get(ip);
        if (!StringUtils.isBlank(ipHack)) {
            Long expire = redisTemplate.getExpire(ip, TimeUnit.MINUTES);
            return new ResultUtil<>().setErrorMsg("慢点吧,我的服务器受不了！请" + (expire + 1) + "分钟后再试!");
        }

        try {
            if (pay.getCustom() != null && pay.getCustom()) {
                // 自定义金额
                pay.setPayNum(StringUtils.getRandomNum());
            } else {
                // 从redis中取出num
                String key = "JPAY_NUM_" + pay.getPayType();
                String value = redisTemplate.opsForValue().get(key);
                if (StringUtils.isBlank(value)) {
                    redisTemplate.opsForValue().set(key, "0");
                }

                // 取出num
                String num = String.valueOf(Integer.parseInt(Objects.requireNonNull(redisTemplate.opsForValue().get(key))) + 1);
                if (Integer.parseInt(num) > QR_CODE_NUM) {
                    num = "1";
                }
                pay.setPayNum(num);
                redisTemplate.opsForValue().set(key, num);
            }

            payService.addPay(pay);
            pay.setTime(StringUtils.getTimeStamp(new Date()));
        } catch (Exception e) {
            log.error("添加支付订单失败!", e);
            return new ResultUtil<>().setErrorMsg("添加支付订单失败!");
        }

        // 记录缓存
        redisTemplate.opsForValue().set(ip, "added", IP_EXPIRE, TimeUnit.MINUTES);

        // 给管理员发送审核
        String tokenAdmin = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(pay.getId(), tokenAdmin, ADMIN_EXPIRE, TimeUnit.DAYS);
        pay = getAdminUrl(pay, pay.getId(), tokenAdmin, MY_TOKEN);
        emailUtils.sendTemplateMail(EMAIL_SENDER, EMAIL_SENDTO, "【JsongPay个人收款支付系统】待审核处理", "email-admin", pay);

        // 给假管理员发送审核
        if (StringUtils.isNotBlank(pay.getTestEmail()) && EmailUtils.checkEmail(pay.getTestEmail())) {
            Pay pay2 = payService.getPay(pay.getId());
            String fakeToken = UUID.randomUUID().toString();
            redisTemplate.opsForValue().set(FAKE_PRE + pay2.getId(), fakeToken, FAKE_EXPIRE, TimeUnit.HOURS);
            pay2 = getAdminUrl(pay2, FAKE_PRE + pay2.getId(), fakeToken, MY_TOKEN);
            emailUtils.sendTemplateMail(EMAIL_SENDER, pay2.getTestEmail(), "【JsongPay个人收款支付系统】待审核处理", "email-fake", pay2);
        }

        Pay p = new Pay();
        p.setId(pay.getId());
        p.setPayNum(pay.getPayNum());
        return new ResultUtil<>().setData(p);
    }

    @GetMapping("/pay/pass")
    public String pass(
            String id,
            Integer sendType,
            String token,
            String myToken,
            Model model
    ) {

        // 验证token
        String temp = redisTemplate.opsForValue().get(id);
        if (StringUtils.isBlank(temp) || !temp.equals(token)) {
            model.addAttribute("errorMsg", "无效的Token");
            return "500";
        }

        if (StringUtils.isBlank(myToken)) {
            model.addAttribute("errorMsg", "您为通过二次token验证");
            return "500";
        }


        try {
            payService.changePayState(getPayId(id), 1);
            Pay pay = payService.getPay(getPayId(id));
            // 通知
            switch (sendType) {
                case 0:
                    emailUtils.sendTemplateMail(EMAIL_SENDER, pay.getEmail(), "【JsongPay个人收款支付系统】支付成功通知", "pay-success", pay);
                    break;
                default:
                    model.addAttribute("errorMsg", "不支持的回调类型:" + sendType);
                    return "500";
            }
        } catch (Exception e) {
            model.addAttribute("errorMsg", "处理数据出错");
            return "500";
        }
        return "redirect:/success";
    }


    @RequestMapping(value = "/pay/edit", method = RequestMethod.POST)
    @ResponseBody
    public Result<Object> payEdit(
            Pay pay,
            String id,
            String token
    ) {

        String temp = redisTemplate.opsForValue().get(pay.getId());
        if (StringUtils.isBlank(temp) || !temp.equals(token)) {
            return new ResultUtil<>().setErrorMsg("无效的Token或链接");
        }
        try {
            pay.setId(getPayId(pay.getId()));
            Pay p = payService.getPay(getPayId(pay.getId()));
            pay.setState(p.getState());
            if (!pay.getId().contains(FAKE_PRE)) {
                pay.setCreateTime(StringUtils.getDate(pay.getTime()));
            } else {
                //假管理
                pay.setMoney(p.getMoney());
                pay.setPayType(p.getPayType());
            }
            payService.updatePay(pay);
        } catch (Exception e) {
            return new ResultUtil<>().setErrorMsg("编辑支付订单失败");
        }

        if (id.contains(FAKE_PRE)) {
            redisTemplate.opsForValue().set(id, "", 1L, TimeUnit.SECONDS);
        }
        return new ResultUtil<>().setData(null);
    }


    @GetMapping("/pay/passNotShow")
    public String passNoShow(String id, String token, Model model) {
        String temp = redisTemplate.opsForValue().get(id);
        if (StringUtils.isBlank(temp) || !temp.equals(token)) {
            model.addAttribute("errorMsg", "无效的Token");
            return "500";
        }
        try {
            Pay pay = payService.getPay(getPayId(id));
            if (id.contains(FAKE_PRE) && pay.getState() == 1) {
                model.addAttribute("errorMsg", "对于已经支付的订单您无权进行操作");
                return "500";
            }
            payService.changePayState(getPayId(id), 3);
            // 通知回调
            emailUtils.sendTemplateMail(EMAIL_SENDER, pay.getEmail(), "【JsongPay个人收款支付系统】支付成功通知", "pay-success", pay);
        } catch (Exception e) {
            log.error("设置PassNotShow失败。", e);
            model.addAttribute("errorMsg", "处理数据出错");
            return "500";
        }
        if (id.contains(FAKE_PRE)) {
            redisTemplate.delete(id);
        }

        return "redirect:/success";
    }


    /**
     * 获得假管理ID
     *
     * @param id
     * @return
     */
    public String getPayId(String id) {
        if (id.contains(FAKE_PRE)) {
            String realId = id.substring(id.indexOf("-", 0) + 1, id.length());
            return realId;
        }
        return id;
    }

    private Pay getAdminUrl(Pay pay, String id, String token, String myToken) {
        String pass = SERVER_URL + "/pay/pass?sendType=0&id=" + id + "&token=" + token + "&myToken=" + myToken;
        pay.setPassUrl(pass);

        String pass2 = SERVER_URL + "/pay/pass?sendType=1&id=" + id + "&token=" + token + "&myToken=" + myToken;
        pay.setPassUrl2(pass2);

        String pass3 = SERVER_URL + "/pay/pass?sendType=2&id=" + id + "&token=" + token + "&myToken=" + myToken;
        pay.setPassUrl3(pass3);

        String back = SERVER_URL + "/pay/back?id=" + id + "&token=" + token + "&myToken=" + myToken;
        pay.setBackUrl(back);

        String passNotShow = SERVER_URL + "/pay/passNotShow?id=" + id + "&token=" + token;
        pay.setPassNotShowUrl(passNotShow);

        String edit = SERVER_URL + "/pay-edit?id=" + id + "&token=" + token;
        pay.setEditUrl(edit);

        String del = SERVER_URL + "/pay-del?id=" + id + "&token=" + token;
        pay.setDelUrl(del);

        String close = SERVER_URL + "/pay/close?id=" + id + "&token=" + token;
        pay.setCloseUrl(close);

        String open = SERVER_URL + "/pay/open?id=" + id + "&token=" + token;
        pay.setOpenUrl(open);

        String statistic = SERVER_URL + "/statistic?myToken=" + myToken;
        pay.setStatistic(statistic);
        return pay;
    }


}
