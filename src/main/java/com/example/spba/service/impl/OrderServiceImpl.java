package com.example.spba.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spba.dao.OrderMapper;
import com.example.spba.domain.entity.Good;
import com.example.spba.domain.entity.Order;
import com.example.spba.domain.entity.ShoppingCart;
import com.example.spba.service.GoodsService;
import com.example.spba.service.OrderService;
import com.example.spba.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService{

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    ShoppingCartService shoppingCartService;
    @Autowired
    GoodsService goodService;

    @Transactional
    @Override
    public List<Integer> createOrder(List<Integer> cartIds, Integer addressId) {
        List<Integer> orderIds = new ArrayList<>();
        try {
            for (Integer cartId : cartIds) {
                Order order = new Order();
                ShoppingCart shoppingCart = shoppingCartService.getById(cartId);
                order.setBuyer(shoppingCart.getUserId());
                order.setContent(String.valueOf(shoppingCart.getGoodId()));
                Good good = goodService.getById(shoppingCart.getGoodId());
                order.setSeller(good.getUserId());
                order.setDate(LocalDateTime.now());
                order.setAmount(shoppingCart.getNum());
                order.setAddressId(addressId);
                order.setPayAmount(good.getPrice().multiply(BigDecimal.valueOf(shoppingCart.getNum())));
                this.save(order); // 保存后，order.getId() 会有值（MyBatis-Plus 会自动回填）
                orderIds.add(order.getId());
            }
            // 清除购物车
            shoppingCartService.removeByIds(cartIds);
        } catch (Exception e) {
            throw new RuntimeException("订单创建失败：" + e.getMessage());
        }
        return orderIds;
    }


    @Override
    public IPage<Order> getBySellerId(Integer pageNum, Integer pageSize, Integer userId) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getSeller, userId).orderByDesc(Order::getId);

        return orderMapper.selectPage(page, wrapper);
    }

    @Override
    public IPage<Order> getByBuyerId(Integer pageNum, Integer pageSize, Integer buyerId) {
        Page<Order> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getBuyer, buyerId).orderByDesc(Order::getId);

        return orderMapper.selectPage(page, wrapper);
    }
}
