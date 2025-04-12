package com.example.spba.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.spba.domain.entity.Order;
import com.example.spba.service.OrderService;
import com.example.spba.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@Slf4j
@Validated
public class OrderController {

    @Autowired
    private AlipayClient alipayClient;

    @Autowired
    private OrderService orderService;

    @RequestMapping("/create")
    public R createOrder(@RequestParam List<Integer> CartIds,@RequestParam Integer AddressId) {
        List<Integer> order = orderService.createOrder(CartIds, AddressId);
        if(!order.isEmpty()) {
            return R.success(order);
        } else {
            return R.error();
        }
//        return "Order created successfully!";
    }

    @RequestMapping("/getFromSeller")
    public R getOrder(@RequestParam(defaultValue = "1") Integer pageNum,
                      @RequestParam(defaultValue = "10") Integer pageSize) {
        int SellerId = StpUtil.getLoginIdAsInt();
        IPage<Order> orders =  orderService.getBySellerId(pageNum,pageSize,SellerId);

        return R.success(orders);
    }

    @RequestMapping("/getFromBuyer")
    public R getOrderFromBuyer(@RequestParam(defaultValue = "1") Integer pageNum,
                               @RequestParam(defaultValue = "10") Integer pageSize) {
        int BuyerId = StpUtil.getLoginIdAsInt();
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


    @PostMapping("/order/check-pay")
    public R checkOrderPay(@RequestBody Map<String, List<Integer>> orderIdsMap) {
        List<Integer> orderIds = orderIdsMap.get("orderIds");
        if (orderIds == null || orderIds.isEmpty()) {
            return R.error("订单ID不能为空");
        }
        List<Order> orders = (List<Order>) orderService.listByIds(orderIds);

        String mergedOrderIdStr = orderIds.stream().map(String::valueOf).collect(Collectors.joining(","));

        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();

        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", String.valueOf(mergedOrderIdStr)); // 我们系统的订单号

        request.setBizContent(bizContent.toJSONString());

        try {
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                String tradeStatus = response.getTradeStatus();
                if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                    for (Order order : orders) {
                        // 更新订单状态为已支付
                        order.setStatus("已支付"); // 更新为已支付
                        if (Objects.equals(order.getStatus(), "待支付")) {
                            order.setStatus("已支付"); // 更新为已支付
//                        order.setPayTime(new Date());
//                        order.setPayTradeNo(response.getTradeNo());
                            orderService.updateById(order);
                        }
                    }
                    System.out.println("调用成功");
                    return R.success("订单已支付，当前状态：" + tradeStatus);
                } else {
                    return R.error("订单未支付，当前状态：" + tradeStatus);
                }
            } else {
                return R.error("调用支付宝查询失败：" + response.getSubMsg());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("系统异常，查询失败");
        }
    }
}
