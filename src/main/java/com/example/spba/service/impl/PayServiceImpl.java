package com.example.spba.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.example.spba.config.AlipayLocalConfig;
import com.example.spba.domain.entity.Order;
import com.example.spba.service.OrderService;
import com.example.spba.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PayServiceImpl implements PayService {

    @Autowired
    AlipayLocalConfig alipayLocalConfig;

    @Autowired
    OrderService orderService;

    @Value("${ali-pay.ali-pay-public-key}")
    private String aliPayPublicKey;
    @Value("${ali-pay.notify-url}")
    private String notifyUrl;
    @Value("${ali-pay.return-url}")
    private String returnUrl;
    @Autowired
    private AlipayClient alipayClient;
//    @Value("${alipay.privateKey}")
//    private static String privateKey;
//    @Value("${alipay.alipayPublicKey}")
//    private static String alipayPublicKey;
//    @Value("${alipay.appId}")
//    private static String appId;
//    @Value("${alipay.serverUrl}")
//    private static String serverUrl;


    @Override
    public String alipay(String orderId) throws AlipayApiException {
        AlipayConfig alipayConfig = new AlipayConfig();
        alipayConfig.setPrivateKey(alipayLocalConfig.getMerchantprivatekey());
        alipayConfig.setAlipayPublicKey(alipayLocalConfig.getAlipayPublicKey());
        alipayConfig.setAppId(alipayLocalConfig.getAppId());
        alipayConfig.setServerUrl(alipayLocalConfig.getGatewayurl());
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig);

        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setOutTradeNo(orderId);
        model.setTotalAmount("88.88");
        model.setSubject("Iphone6 16G");
        model.setProductCode("FAST_INSTANT_TRADE_PAY");
        model.setQrPayMode("1");
        model.setQrcodeWidth(100L);
        request.setBizModel(model);
        AlipayTradePagePayResponse response = alipayClient.pageExecute(request, "POST");
        // 如果需要返回GET请求，请使用
        // AlipayTradePagePayResponse response = alipayClient.pageExecute(request, "GET");
        String pageRedirectionData = response.getBody();
        System.out.println(pageRedirectionData);

        if (response.isSuccess()) {
            System.out.println("调用成功");
            return pageRedirectionData;
        } else {
            System.out.println("调用失败");
            // sdk版本是"4.38.0.ALL"及以上,可以参考下面的示例获取诊断链接
            // String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
            // System.out.println(diagnosisUrl);
        }
        return null;
    }

//    //获取返回
//    public String getReturnUrl() {
//        //获取支付宝POST过来反馈信息
//        Map<String, String> params = new HashMap<String, String>();
//        Map requestParams = request.getParameterMap();
//        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
//            String name = (String) iter.next();
//            String[] values = (String[]) requestParams.get(name);
//            String valueStr = "";
//            for (int i = 0; i < values.length; i++) {
//                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
//            }
//            //乱码解决，这段代码在出现乱码时使用。
//            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
//            params.put(name, valueStr);
//        }
//        //切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
//        //boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
//        boolean flag = AlipaySignature.rsaCheckV1(params, alipayLocalConfig.getAlipayPublicKey(), charset, "RSA2");
//    }

    public String createPay(int orderid) {

        Order orderInfo = orderService.getById(orderid);
        //请求
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        //数据
        AlipayTradePagePayModel bizModel = new AlipayTradePagePayModel();
        bizModel.setOutTradeNo(String.valueOf(orderid));
        //单位是元
        bizModel.setTotalAmount("11");
//        bizModel.setSubject(orderInfo.getTitle());
        //默认的
        bizModel.setProductCode("FAST_INSTANT_TRADE_PAY");
        request.setBizModel(bizModel);
        request.setNotifyUrl(notifyUrl + "/pay/order/pay-signal");
        //用户支付后支付宝会以GET方法请求returnUrl,并且携带out_trade_no,trade_no,total_amount等参数.

        request.setReturnUrl(returnUrl);
        AlipayTradePagePayResponse response = null;
        try {
            //完成签名并执行请求
            response = alipayClient.pageExecute(request);
            if (response.isSuccess()) {
                log.debug("调用成功");
                return response.getBody();
            } else {
                log.error("调用失败");
                log.error(response.getMsg());
                return null;
            }
        } catch (AlipayApiException e) {
            log.error("调用异常");
            return null;
        }
    }
}
