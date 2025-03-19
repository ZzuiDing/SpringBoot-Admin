package com.example.spba.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spba.dao.RefundMapper;
import com.example.spba.domain.dto.RefundDTO;
import com.example.spba.domain.entity.Refund;
import com.example.spba.service.OrderService;
import com.example.spba.service.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefundServiceImpl extends ServiceImpl<RefundMapper,Refund> implements RefundService {

    @Autowired
    RefundMapper refundMapper;
    @Autowired
    OrderService orderService;
    @Override
    public void create(RefundDTO refundDTO) {
        Refund refund = new Refund();
        refund.setOrderId(refundDTO.getOrderId());
        refund.setReason(refundDTO.getReason());

        // Assuming you have an OrderService to fetch order details
//        Order order = orderService.getById(orderId);
        refundMapper.insert(refund);
    }

    @Override
    public List<Refund> getBySellerId(int sellerid, int page, int size) {

        List<Refund> refunds = refundMapper.selectBySellerId(sellerid, page-1, size);
        return refunds;

    }

    @Override
    public List<Refund> getByBuyerId(int buyerId, int page, int size) {
        List<Refund> refunds = refundMapper.selectByBuyerId(buyerId, page-1, size);
        return refunds;
    }
}
