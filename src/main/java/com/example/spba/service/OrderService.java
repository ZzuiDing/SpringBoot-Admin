package com.example.spba.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.spba.domain.dto.orderListDTO;
import com.example.spba.domain.entity.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface OrderService extends IService<Order> {

    List<Integer> createOrder(List<Integer> CartIds, Integer addressId);


    IPage<orderListDTO> getBySellerId(Integer pageNum, Integer pageSize, Integer userId);

    IPage<orderListDTO> getByBuyerId(Integer pageNum, Integer pageSize, Integer buyerId);


    Map<String, Integer> countOrdersMapByStatus(Integer userId);

    Map<String, Integer> countOrdersMapByStatusSeller(Integer userId);

    Integer createDirectOrder(String goodId, Integer amount, Integer addressId);

//    List<Integer> getIdsByUserId(int sellerid);
}
