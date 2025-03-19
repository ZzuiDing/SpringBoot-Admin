package com.example.spba.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.spba.domain.entity.Refund;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RefundMapper extends BaseMapper<Refund> {

    List<Refund> selectBySellerId(int sellerid, int page, int size);

    List<Refund> selectByBuyerId(int buyerId, int i, int size);
}
