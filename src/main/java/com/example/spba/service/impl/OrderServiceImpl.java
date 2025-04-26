package com.example.spba.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spba.dao.OrderMapper;
import com.example.spba.domain.dto.orderListDTO;
import com.example.spba.domain.entity.Good;
import com.example.spba.domain.entity.Order;
import com.example.spba.domain.entity.ShoppingCart;
import com.example.spba.domain.entity.User;
import com.example.spba.service.GoodsService;
import com.example.spba.service.OrderService;
import com.example.spba.service.ShoppingCartService;
import com.example.spba.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService{

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    ShoppingCartService shoppingCartService;
    @Autowired
    GoodsService goodService;

    @Autowired
    UserService userService;

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
    public IPage<orderListDTO> getBySellerId(Integer pageNum, Integer pageSize, Integer userId, String status) {
//        Page<Order> page = new Page<>(pageNum, pageSize);
//        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(Order::getSeller, userId).orderByDesc(Order::getId);
//        int offset = (pageNum - 1) * pageSize;
//        Page<orderListDTO> orderListDTOS = orderMapper.selectBySellerId(offset, pageSize, userId);
        Page<orderListDTO> page = new Page<>(pageNum, pageSize);
        return orderMapper.selectBySellerId(page, userId,status);
//        return orderListDTOS;
    }

    @Override
    public IPage<orderListDTO> getByBuyerId(Integer pageNum, Integer pageSize, Integer userId, String status) {
//        Page<Order> page = new Page<>(pageNum, pageSize);
//        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(Order::getBuyer, buyerId).orderByDesc(Order::getId);
//        int offset = (pageNum - 1) * pageSize;
//        Page<orderListDTO> orderListDTOS = orderMapper.selectByBuyerId(offset, pageSize, userId);
//        return orderListDTOS;
        Page<orderListDTO> page = new Page<>(pageNum, pageSize);
        return orderMapper.selectByBuyerId(page, userId,status);
    }

    @Override
    public Map<String, Integer> countOrdersMapByStatus(Integer userId) {
        return orderMapper.countOrdersMapByStatus(userId);
    }

    @Override
    public Map<String, Integer> countOrdersMapByStatusSeller(Integer userId) {
        return orderMapper.countOrdersMapByStatusSeller(userId);
    }

    @Override
    public Integer createDirectOrder(String goodId, Integer amount, Integer addressId) {
        try {
                Order order = new Order();
                order.setBuyer(StpUtil.getLoginIdAsInt());
                order.setContent(goodId);
                order.setSeller(goodService.getById(goodId).getUserId());
                order.setDate(LocalDateTime.now());
                order.setAmount(amount);
                order.setAddressId(addressId);
                order.setPayAmount(goodService.getById(goodId).getPrice().multiply(BigDecimal.valueOf(amount)));
                this.save(order); // 保存后，order.getId() 会有值（MyBatis-Plus 会自动回填）
                return order.getId();
        } catch (Exception e) {
            throw new RuntimeException("订单创建失败：" + e.getMessage());
        }
    }

    @Override
    public IPage<orderListDTO> getorderLists(Integer pageNum, Integer pageSize) {
        Page<orderListDTO> page = new Page<>(pageNum, pageSize);
        return orderMapper.selectOrderList(page);
    }

    @Override
    public Map<String, Integer> countOrdersMapByStatusAdmin() {
        return orderMapper.countOrdersMapByStatusAdmin();
    }

    @Override
    public List<Map<String, Object>> getRecentSevenDaysOrders() {
        int loginIdAsInt = StpUtil.getLoginIdAsInt();
        User user = userService.getById(loginIdAsInt);
        if(user.getRole() != 2) {
            return orderMapper.getRecentSevenDaysOrdersByUser(loginIdAsInt); // 或者抛出异常，或者返回一个空列表
        } else {
            // 如果是管理员，返回所有用户的最近七天订单
            return orderMapper.getRecentSevenDaysOrders();
        }
    }

}
