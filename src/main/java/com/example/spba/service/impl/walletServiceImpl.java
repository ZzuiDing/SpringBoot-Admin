package com.example.spba.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.example.spba.domain.entity.Order;
import com.example.spba.domain.entity.User;
import com.example.spba.service.OrderService;
import com.example.spba.service.UserService;
import com.example.spba.service.WalletHistoryService;
import com.example.spba.service.wallerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class walletServiceImpl implements wallerService {

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @Autowired
    WalletHistoryService walletHistoryService;

    @Override
    public BigDecimal getBalance(int loginIdAsInt) {
        User user = userService.getById(loginIdAsInt);
        return user.getWealth();
    }

    @Transactional
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
                order.setPayMethod("余额支付");
                orderService.updateById(order);
            }
            // 这里可以添加钱包历史记录的创建逻辑
            // 例如：记录支付的金额、剩余余额等信息
            walletHistoryService.createWalletHistory(user.getId(), "OUT", total, user.getWealth(), "支付订单");
            return true;
        } else {
            return false;
        }
    }
}
