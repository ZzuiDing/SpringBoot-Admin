package com.example.spba.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.example.spba.domain.entity.Order;
import com.example.spba.domain.entity.User;
import com.example.spba.service.OrderService;
import com.example.spba.service.UserService;
import com.example.spba.service.wallerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class wallerServiceImpl implements wallerService {

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @Override
    public BigDecimal getBalance(int loginIdAsInt) {
        User user = userService.getById(loginIdAsInt);
        return user.getWealth();
    }

    @Override
    public boolean pay(List<Integer> orderIds) {
        List<Order> orderList = (List<Order>) orderService.listByIds(orderIds);
        BigDecimal total = orderList.stream()
                .map(Order::getPayAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        User user = userService.getById(StpUtil.getLoginIdAsInt());
        if(user.getWealth().compareTo(total) >= 0){
            user.setWealth(user.getWealth().subtract(total));
            userService.updateById(user);
            for (Order order : orderList) {
                order.setStatus("已支付");
                orderService.updateById(order);
            }
            return true;
        } else {
            return false;
        }
    }
}
