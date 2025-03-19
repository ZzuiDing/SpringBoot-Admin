package com.example.spba.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.spba.domain.dto.RefundDTO;
import com.example.spba.domain.entity.Refund;

import java.util.List;

public interface RefundService extends IService<Refund> {


    void create(RefundDTO refundDTO);

    List<Refund> getBySellerId(int sellerId, int page, int size);

    List<Refund> getByBuyerId(int buyerId, int page, int size);
}
