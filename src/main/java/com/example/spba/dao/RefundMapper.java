package com.example.spba.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.spba.domain.dto.OrderWithRefundVO;
import com.example.spba.domain.entity.Refund;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RefundMapper extends BaseMapper<Refund> {

    List<Refund> selectBySellerId(int sellerId, int offset, int pagesize);

    List<Refund> selectByBuyerId(int buyerId, int offset, int pagesize);

    IPage<OrderWithRefundVO> getOrdersWithRefunds(Page<?> page, Integer buyerId);

}
