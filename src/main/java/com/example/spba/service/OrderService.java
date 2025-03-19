package com.example.spba.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.spba.domain.entity.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService extends IService<Order> {

    Boolean createOrder(List<Integer> CartIds, Integer addressId);


    IPage<Order> getBySellerId(Integer pageNum, Integer pageSize, Integer userId);

    IPage<Order> getByBuyerId(Integer pageNum, Integer pageSize, Integer buyerId);

//    List<Integer> getIdsByUserId(int sellerid);
}
