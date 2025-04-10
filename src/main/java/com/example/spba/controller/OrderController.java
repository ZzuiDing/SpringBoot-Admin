package com.example.spba.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.spba.domain.entity.Order;
import com.example.spba.service.OrderService;
import com.example.spba.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
@Slf4j
@Validated
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("/create")
    public R createOrder(@RequestParam List<Integer> CartIds,@RequestParam Integer AddressId) {
        if(orderService.createOrder(CartIds,AddressId)) {
            return R.success();
        } else {
            return R.error();
        }
//        return "Order created successfully!";
    }

    @RequestMapping("/getFromSeller")
    public R getOrder(@RequestParam(defaultValue = "1") Integer pageNum,
                      @RequestParam(defaultValue = "10") Integer pageSize,
                      @RequestParam Integer SellerId) {
        IPage<Order> orders =  orderService.getBySellerId(pageNum,pageSize,SellerId);

        return R.success(orders);
    }

    @RequestMapping("/getFromBuyer")
    public R getOrderFromBuyer(@RequestParam(defaultValue = "1") Integer pageNum,
                               @RequestParam(defaultValue = "10") Integer pageSize,
                               @RequestParam Integer BuyerId) {
        IPage<Order> orders =  orderService.getByBuyerId(pageNum,pageSize,BuyerId);

        return R.success(orders);
    }

    @RequestMapping("/UpdateDesc")
    public R updateDesc(@RequestParam Integer orderId,
                        @RequestParam String desc) {
        Order order = orderService.getById(orderId);
        order.setDesc(desc);
        orderService.updateById(order);
        return R.success();
    }

    @RequestMapping("/UpdateStatus")
    public R updateStatus(@RequestParam Integer orderId,
                          @RequestParam String status) {
        Order order = orderService.getById(orderId);
        order.setStatus(status);
        orderService.updateById(order);
        return R.success();
    }

    @RequestMapping("/delete")
    public R deleteOrder(@RequestParam Integer orderId) {
        Order order = orderService.getById(orderId);
        if(order.getStatus().equals("待支付")||
                order.getStatus().equals("已取消")) {
            orderService.removeById(orderId);
            return R.success("删除成功");
        }else {
//        orderService.removeById(orderId);
            return R.error("删除失败，订单状态不允许删除");
        }
    }

    @RequestMapping("/cancel")
    public R cancelOrder(@RequestParam Integer orderId) {
        Order order = orderService.getById(orderId);
        if(order.getStatus().equals("待支付")) {
            orderService.updateById(order);
            return R.success("取消成功");
        }
        return R.error("取消失败，订单状态不允许取消");
    }

    @RequestMapping("/getOrderList")
    public R getOrderList(@RequestParam(defaultValue = "1") Integer pageNum,
                          @RequestParam(defaultValue = "10") Integer pageSize) {
        List<Order> orders =  orderService.list();
        return R.success(orders);
    }
}
