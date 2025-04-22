package com.example.spba.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.spba.domain.dto.OrderWithRefundVO;
import com.example.spba.domain.dto.RefundDTO;
import com.example.spba.domain.entity.Refund;

public interface RefundService extends IService<Refund> {


    void create(RefundDTO refundDTO);

    IPage<OrderWithRefundVO> getOrdersWithRefundByBuyer(Integer buyerId, int page, int size);


    IPage<Refund> getByBuyerId(int buyerId, int page, int size);

    boolean cancelRefund(int refundId);

    IPage<Refund> getBySellerId(int sellerId, int page, int size);

    boolean acceptRefund(int refundId);

    void commit(int refundId);

    void declineRefund(int refundId);
}
