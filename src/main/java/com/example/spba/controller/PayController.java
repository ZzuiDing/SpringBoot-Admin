package com.example.spba.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.example.spba.domain.entity.Order;
import com.example.spba.service.OrderService;
import com.example.spba.service.PayService;
import com.example.spba.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/pay")
public class PayController {


    @Value("${ali-pay.ali-pay-public-key}")
    private String aliPayPublicKey;
    @Value("${ali-pay.notify-url}")
    private String notifyUrl;
    @Value("${ali-pay.return-url}")
    private String returnUrl;
    @Autowired
    private AlipayClient alipayClient;
    @Autowired

    private PayService payService;

    @Autowired
    private OrderService orderService;

    @RequestMapping("/alipay")
    public void alipay(List<Integer> orderIds, HttpServletResponse response) throws AlipayApiException, IOException {
        // 调用支付服务生成表单
        String form = payService.alipay(orderIds);

        // 输出 HTML 表单内容到页面
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(form);
        response.getWriter().flush();
    }

    @RequestMapping("/weixinpay")
    public String weixinpay() {
        // 这里可以添加微信支付的逻辑
        return "weixinpay"; // 返回微信支付页面
    }

    //    @RequestMapping("/alipaytest")
//    public String alipay(String orderId) throws AlipayApiException {
//        String url = payService.alipay(orderId);
//        return "redirect:" + url;
//    }
    @PostMapping("/pay-signal")
    public String paySignal(@RequestBody Map<String, String> data) {


        log.debug("收到支付宝回调");
        //验签
        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV1(data, aliPayPublicKey, AlipayConstants.CHARSET_UTF8, AlipayConstants.SIGN_TYPE_RSA2);
            //验签成功
            if (signVerified) {
                return "success";
            }
            //验签失败
            else {
                log.error("验签失败");
                return "failure";
            }
        } catch (AlipayApiException e) {
            log.error("验签异常");
            return "failure";
        }
    }

    @RequestMapping("/alipay2")
    public String alipay2(int orderId) throws AlipayApiException {
        // 调用支付服务生成表单

        // 输出 HTML 表单内容到页面
        return payService.createPay(Collections.singletonList(orderId));
    }

    @PostMapping("/pay/alipay")
    @ResponseBody
    public R pay(@RequestBody Map<String, List<Integer>> orderIdsMap) throws AlipayApiException {
        // 获取封装的 orderIds
        List<Integer> orderIds = orderIdsMap.get("orderIds");
        return R.success(payService.createPay(orderIds));
    }


}
