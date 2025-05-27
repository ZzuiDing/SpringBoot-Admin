package com.example.spba.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spba.dao.OrderMapper;
import com.example.spba.domain.dto.DateCount;
import com.example.spba.domain.dto.orderListDTO;
import com.example.spba.domain.entity.*;
import com.example.spba.service.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    ShoppingCartService shoppingCartService;
    @Autowired
    GoodsService goodService;

    @Autowired
    UserService userService;

    @Autowired
    AddressService addressService;

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

//                Good good = goodService.getById(shoppingCart.getGoodId());
                Good good = goodService.findByIdForUpdate(shoppingCart.getGoodId());

                if (good.getCount() < shoppingCart.getNum()) {
                    throw new RuntimeException("库存不足");
                }

                good.setCount(good.getCount() - shoppingCart.getNum());
                int i = goodService.updateStock(good.getId(), good.getCount());// 更新库存
//                goodService.updateById(good);
                order.setSeller(good.getUserId());
                order.setCreatedTime(LocalDateTime.now());
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
        Page<orderListDTO> page = new Page<>(pageNum, pageSize);
        IPage<orderListDTO> orderListDTOIPage = orderMapper.selectBySellerId(page, userId, status);

        return getOrderListDTOIPage(orderListDTOIPage);
    }

    @Override
    public IPage<orderListDTO> getByBuyerId(Integer pageNum, Integer pageSize, Integer userId, String status) {
        Page<orderListDTO> page = new Page<>(pageNum, pageSize);
        IPage<orderListDTO> orderListDTOIPage = orderMapper.selectByBuyerId(page, userId, status);

        return getOrderListDTOIPage(orderListDTOIPage);
    }

    @NotNull
    private IPage<orderListDTO> getOrderListDTOIPage(IPage<orderListDTO> orderListDTOIPage) {
        orderListDTOIPage.getRecords().forEach(orderListDTO -> {
            orderListDTO.setSellerName(userService.getById(orderListDTO.getSeller()).getName());
            orderListDTO.setBuyerName(userService.getById(orderListDTO.getBuyer()).getName());
            orderListDTO.setGoodName(goodService.getById(orderListDTO.getContent()).getName());

            Address address = addressService.getById(orderListDTO.getAddressId());
            if (address != null) {
                String fullAddress = String.format("收货人：%s，手机号：%s，地址：%s",
                        address.getName(), address.getPhone(), address.getAddress());
                orderListDTO.setAddress(fullAddress);
            } else {
                orderListDTO.setAddress("地址信息不存在");
            }
        });

        return orderListDTOIPage;
    }


    @Override
    public Map<String, Integer> countOrdersMapByStatus(Integer userId) {
        return orderMapper.countOrdersMapByStatus(userId);
    }

    @Override
    public Map<String, Integer> countOrdersMapByStatusSeller(Integer userId) {
        return orderMapper.countOrdersMapByStatusSeller(userId);
    }

    @Transactional
    @Override
    public Integer createDirectOrder(String goodId, Integer amount, Integer addressId) {
        try {
            Order order = new Order();
            order.setBuyer(StpUtil.getLoginIdAsInt());
            order.setContent(goodId);
            Good byId = goodService.findByIdForUpdate(Integer.valueOf(goodId));

            if (byId.getCount() < amount) {
                throw new RuntimeException("库存不足");
            }

            byId.setCount(byId.getCount() - amount);
            int i = goodService.updateStock(byId.getId(), byId.getCount());// 更新库存
            order.setSeller(byId.getUserId());
            order.setCreatedTime(LocalDateTime.now());
            order.setAmount(amount);
            order.setAddressId(addressId);
            order.setPayAmount(byId.getPrice().multiply(BigDecimal.valueOf(amount)));
            this.save(order); // 保存后，order.getId() 会有值（MyBatis-Plus 会自动回填）
            return order.getId();
        } catch (Exception e) {
            throw new RuntimeException("订单创建失败：" + e.getMessage());
        }
    }

    @Override
    public IPage<orderListDTO> getorderLists(Integer pageNum, Integer pageSize) {
        Page<orderListDTO> page = new Page<>(pageNum, pageSize);
        IPage<orderListDTO> orderListDTOIPage = orderMapper.selectOrderList(page);
        return getOrderListDTOIPage(orderListDTOIPage);
    }


    @Override
    public Map<String, Integer> countOrdersMapByStatusAdmin() {
        return orderMapper.countOrdersMapByStatusAdmin();
    }

    @Override
    public List<Map<String, Object>> getRecentSevenDaysOrders() {
        int loginIdAsInt = StpUtil.getLoginIdAsInt();
        User user = userService.getById(loginIdAsInt);

        List<DateCount> dateCountList;
        if (user.getRole() != 2) {
            dateCountList = orderMapper.getRecentSevenDaysOrdersByUser(loginIdAsInt);
        } else {
            dateCountList = orderMapper.getRecentSevenDaysOrders();
            System.out.println(dateCountList.toString());
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (DateCount dc : dateCountList) {
            if (dc.getDate() != null) {
                Map<String, Object> item = new HashMap<>();
                item.put("date", dc.getDate());
                item.put("count", dc.getCount());
                result.add(item);
            }
        }
        System.out.println("result = " + result);
        return result;
    }

}
