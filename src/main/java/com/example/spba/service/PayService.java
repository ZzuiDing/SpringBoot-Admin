package com.example.spba.service;

import com.alipay.api.AlipayApiException;

public interface PayService {


    String alipay(String orderId) throws AlipayApiException;

    String createPay(int orderId);
}
