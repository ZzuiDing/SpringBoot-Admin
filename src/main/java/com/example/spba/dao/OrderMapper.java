package com.example.spba.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.spba.domain.dto.orderListDTO;
import com.example.spba.domain.entity.Order;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    IPage<orderListDTO> selectBySellerId(Page<?> page, Integer userId, String status);


    IPage<orderListDTO> selectByBuyerId(Page<?> page, Integer userId, String status);

    @MapKey("status")
    Map<String, Integer> countOrdersMapByStatus(Integer userId);

    @MapKey("status")
    Map<String, Integer> countOrdersMapByStatusSeller(Integer userId);

    IPage<orderListDTO> selectOrderList(Page<orderListDTO> page);

    @MapKey("status")
    Map<String, Integer> countOrdersMapByStatusAdmin();
}
