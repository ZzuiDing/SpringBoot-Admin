package com.example.spba.service;

import com.alipay.api.AlipayApiException;

import java.util.List;

public interface PayService {


    String alipay(List<Integer> orderId) throws AlipayApiException;

    String createPay(List<Integer> orderIds) throws AlipayApiException;
}
