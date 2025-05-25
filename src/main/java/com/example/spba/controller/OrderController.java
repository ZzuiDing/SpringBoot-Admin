package com.example.spba.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.spba.domain.entity.Good;
import com.example.spba.domain.entity.Order;
import com.example.spba.domain.entity.User;
import com.example.spba.service.*;
import com.example.spba.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.spba.domain.dto.orderListDTO;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
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

    @Autowired
    private UserService userService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private WalletHistoryService walletHistoryService;


    @RequestMapping("/create")
    public R createOrder(@RequestParam List<Integer> CartIds, @RequestParam Integer AddressId) {
        List<Integer> order = orderService.createOrder(CartIds, AddressId);
        if (!order.isEmpty()) {
            return R.success(order);
        } else {
            return R.error();
        }
//        return "Order created successfully!";
    }

    @RequestMapping("/createDirect")
    public R createDirectOrder(@RequestParam String goodId, @RequestParam Integer amount, @RequestParam Integer addressId) {
        Integer orderid = orderService.createDirectOrder(goodId, amount, addressId);
        if (orderid != null) {
            return R.success(orderid);
        } else {
            return R.error();
        }
    }

    @RequestMapping("/getFromSeller")
    public R getOrder(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam String status) {
        int SellerId = StpUtil.getLoginIdAsInt();
        IPage<orderListDTO> orders = orderService.getBySellerId(pageNum, pageSize, SellerId, status);
        return R.success(orders);
    }

    @RequestMapping("/getFromBuyer")
    public R getOrderFromBuyer(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam String status) {
        int BuyerId = StpUtil.getLoginIdAsInt();
        IPage<orderListDTO> orders = orderService.getByBuyerId(pageNum, pageSize, BuyerId, status);

        return R.success(orders);
    }

    @RequestMapping("/UpdateBuyerDesc")
    public R updateBuyerDesc(@RequestParam Integer orderId, @RequestParam String desc) {
        Order order = orderService.getById(orderId);
        order.setBuyerDesc(desc);
        orderService.updateById(order);
        return R.success();
    }

    @RequestMapping("/UpdateSellerDesc")
    public R updateSellerDesc(@RequestParam Integer orderId, @RequestParam String desc) {
        Order order = orderService.getById(orderId);
        order.setSellerDesc(desc);
        orderService.updateById(order);
        return R.success();
    }

    @RequestMapping("/UpdateStatus")
    public R updateStatus(@RequestParam Integer orderId, @RequestParam String status) {
        Order order = orderService.getById(orderId);
        order.setStatus(status);
        orderService.updateById(order);
        switch (status) {
            case "已发货":
                order.setExpressTime(LocalDateTime.now());
                break;
            case "已收货":
                order.setReceiveTime(LocalDateTime.now());
                break;
            case "已取消":
                order.setCancelTime(LocalDateTime.now());
                break;
        }
        if (status.equals("已完成")) {
            User byId = userService.getById(StpUtil.getLoginIdAsInt());
            byId.setWealth(byId.getWealth().add(order.getPayAmount()));
            userService.updateById(byId);
            Good byId1 = goodsService.getById(order.getContent());
            byId1.setSoldAmount(byId1.getSoldAmount() + order.getAmount());
            goodsService.updateById(byId1);
            // 这里可以添加钱包历史记录的创建逻辑
            walletHistoryService.createWalletHistory(byId.getId(), "IN", order.getPayAmount(), byId.getWealth(), "商品收入");
        }
        return R.success();
    }

    @RequestMapping("/delete")
    public R deleteOrder(@RequestParam Integer orderId) {
        Order order = orderService.getById(orderId);
        if (order.getStatus().equals("待支付") || order.getStatus().equals("已取消")) {
            orderService.removeById(orderId);
            return R.success("删除成功");
        } else {
//        orderService.removeById(orderId);
            return R.error("删除失败，订单状态不允许删除");
        }
    }

    @Transactional
    @RequestMapping("/cancel")
    public R cancelOrder(@RequestParam Integer orderId) {
        Order order = orderService.getById(orderId);
        if (order.getStatus().equals("待支付")) {
            order.setStatus("已取消");
            order.setCancelTime(LocalDateTime.now());
            Good byId = goodsService.getById(order.getContent());
            byId.setCount(byId.getCount() + order.getAmount());
            goodsService.updateById(byId);
            orderService.updateById(order);
            return R.success("取消成功");
        }
        return R.error("取消失败，订单状态不允许取消");
    }

    @RequestMapping("/getOrderList")
    public R getOrderList(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<orderListDTO> orders = orderService.getorderLists(pageNum, pageSize);
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
                        order.setPayMethod("支付宝");
                        if (Objects.equals(order.getStatus(), "待支付")) {
                            order.setStatus("已支付"); // 更新为已支付
                            order.setPayTime(LocalDateTime.now());
//                        order.setPayTime(new Date());
//                        order.setPayTradeNo(response.getTradeNo());
                            orderService.updateById(order);
                        }
                    }
                    // 更新钱包记录



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

    @RequestMapping("/updateExpressId")
    public R updateExpressId(@RequestParam Integer orderId, @RequestParam String expressId) {
        Order order = orderService.getById(orderId);
        order.setExpressId(expressId);
        order.setStatus("已发货");
        order.setExpressTime(LocalDateTime.now());
        orderService.updateById(order);
        return R.success();
    }

    @GetMapping("/order/status-count")
    public R getOrderStatusCount() {
        int userId = StpUtil.getLoginIdAsInt();
        Map<String, Integer> statusCountMap = orderService.countOrdersMapByStatus(userId);
        return R.success(statusCountMap);
    }

    @GetMapping("/order/status-countSeller")
    public R getOrderStatusCountSeller() {
        int userId = StpUtil.getLoginIdAsInt();
        Map<String, Integer> statusCountMap = orderService.countOrdersMapByStatusSeller(userId);
        return R.success(statusCountMap);
    }

    @GetMapping("/order/status-countAdmin")
    public R getOrderStatusCountAdmin() {
        int userId = StpUtil.getLoginIdAsInt();
        User user = userService.getById(userId);
        if(user.getRole()!=2){
            return R.error("没有权限");
        }
        Map<String, Integer> statusCountMap = orderService.countOrdersMapByStatusAdmin();
        return R.success(statusCountMap);
    }


    @RequestMapping("/getOrderById")
    public R getOrderById(@RequestParam Integer orderId) {
        Order order = orderService.getById(orderId);
        orderListDTO orderListDTO = new orderListDTO();
        BeanUtils.copyProperties(order, orderListDTO);
        orderListDTO.setSellerName(userService.getById(orderListDTO.getSeller()).getName());
        orderListDTO.setBuyerName(userService.getById(orderListDTO.getBuyer()).getName());
        orderListDTO.setGoodName(goodsService.getById(orderListDTO.getContent()).getName());
        orderListDTO.setAddress(addressService.getById(orderListDTO.getAddressId()).getAddress());
        return R.success(orderListDTO);
    }

    @RequestMapping("/updateOrder")
    public R updateOrder(@RequestBody Order order) {
        orderService.updateById(order);
        return R.success();
    }

    @GetMapping("/sevenDays")
    public R getRecentSevenDaysOrders() {
        List<Map<String, Object>> list = orderService.getRecentSevenDaysOrders();

        // 补全缺失日期
        List<Map<String, Object>> result = fillMissingDates(list);

        return R.success(result);
    }

    private List<Map<String, Object>> fillMissingDates(List<Map<String, Object>> original) {
        Map<String, Integer> dataMap = original.stream()
                .collect(Collectors.toMap(
                        m -> (String) m.get("createdTime"),
                        m -> ((Number) m.get("count")).intValue()
                ));

        List<Map<String, Object>> result = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            String date = today.minusDays(i).toString();
            Map<String, Object> item = new HashMap<>();
            item.put("createdTime", date);
            item.put("count", dataMap.getOrDefault(date, 0));
            result.add(item);
        }
        return result;
    }

}
