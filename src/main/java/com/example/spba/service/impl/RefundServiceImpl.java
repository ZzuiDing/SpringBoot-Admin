package com.example.spba.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spba.dao.OrderMapper;
import com.example.spba.dao.RefundMapper;
import com.example.spba.domain.dto.OrderWithRefundVO;
import com.example.spba.domain.dto.RefundDTO;
import com.example.spba.domain.entity.Good;
import com.example.spba.domain.entity.Order;
import com.example.spba.domain.entity.Refund;
import com.example.spba.domain.entity.User;
import com.example.spba.service.GoodsService;
import com.example.spba.service.OrderService;
import com.example.spba.service.RefundService;
import com.example.spba.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class RefundServiceImpl extends ServiceImpl<RefundMapper, Refund> implements RefundService {

    @Autowired
    private RefundMapper refundMapper;

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private GoodsService goodService;
    /**
     * 创建退款申请
     *
     * @param refundDTO 退款数据
     */
    @Transactional
    @Override
    public void create(RefundDTO refundDTO) {
        // 检查订单是否存在
        Order order = orderService.getById(refundDTO.getOrderId());
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        // 校验订单是否符合退款条件（例如已支付、未完成等）
        if (!isRefundable(order)) {
            throw new RuntimeException("该订单不符合退款条件");
        }

        if(order.getStatus().equals("已支付")) {
            order.setStatus("已取消");
            orderService.updateById(order);
            User byId = userService.getById(StpUtil.getLoginIdAsInt());
            byId.setWealth(byId.getWealth().add(order.getPayAmount()));
            userService.updateById(byId);
            User byId1 = userService.getById(order.getSeller());
            byId1.setWealth(byId1.getWealth().subtract(order.getPayAmount()));
            userService.updateById(byId1);
            Good byId2 = goodService.getById(order.getContent());
            byId2.setCount(byId2.getCount() + order.getAmount());
            goodService.updateById(byId2);
        } else {
            Refund refund = new Refund();
            refund.setOrderId(refundDTO.getOrderId());
            refund.setReason(refundDTO.getReason());
            refund.setStatus("申请中"); // 默认待处理状态

            this.save(refund); // 保存退款申请

            // 可选：更新订单状态（如：标记为“退款处理中”）
            order.setStatus("退款中");
            orderService.updateById(order);
        }
    }

//    /**
//     * 根据卖家ID查询退款
//     *
//     * @param sellerId 卖家ID
//     * @param page     页码
//     * @param size     每页大小
//     * @return 退款列表
//     */
//    @Override
//    public List<Refund> getBySellerId(int sellerId, int page, int size) {
//        int offset = (page - 1) * size; // 计算分页起始位置
//        List<Refund> refunds = refundMapper.selectBySellerId(sellerId, offset, size);
//        for (Refund refund : refunds) {
//            Order order = orderService.getById(refund.getOrderId());
////            refundFormDTO refundFormDTO = refund
//        }
//    }

    /**
     * 根据买家ID查询退款
     *
     * @param buyerId 买家ID
     * @param page    页码
     * @param size    每页大小
     * @return 退款列表
     */
    @Override
    public IPage<Refund> getByBuyerId(int buyerId, int page, int size) {
//        int offset = (page - 1) * size;
//        return refundMapper.selectByBuyerId(buyerId, offset, size);
        // 1. 查询该买家的所有订单ID
        List<Integer> orderIds = orderMapper.selectList(
                new QueryWrapper<Order>().eq("buyer", buyerId)
        ).stream().map(Order::getId).collect(Collectors.toList());

        // 如果没有订单，直接返回空分页
        if (orderIds.isEmpty()) {
            return new Page<>(page, size); // 空结果
        }

        // 2. 再查询 refund 表中符合条件的记录
        Page<Refund> pageRequest = new Page<>(page, size);
        QueryWrapper<Refund> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("order_id", orderIds);
        return refundMapper.selectPage(pageRequest, queryWrapper);
//        return refundMapper.selectPage(pageRequest, queryWrapper);
    }

    @Override
    public boolean cancelRefund(int refundId) {
        Refund byId = this.getById(refundId);
        if (byId == null) {
            throw new RuntimeException("退款申请不存在");
        }
        Order order = orderService.getById(byId.getOrderId());
        order.setStatus("已完成");
        orderService.updateById(order);
        this.removeById(refundId);
        return true;
    }

    @Override
    public IPage<Refund> getBySellerId(int sellerId, int page, int size) {
//        int offset = (page - 1) * size;
//        return refundMapper.selectBySellerId(sellerId, offset, size);
        List<Integer> orderIds = orderMapper.selectList(
                new QueryWrapper<Order>().eq("seller", sellerId)
        ).stream().map(Order::getId).collect(Collectors.toList());

        // 如果没有订单，直接返回空分页
        if (orderIds.isEmpty()) {
            return new Page<>(page, size); // 空结果
        }

        // 2. 再查询 refund 表中符合条件的记录
        Page<Refund> pageRequest = new Page<>(page, size);
        QueryWrapper<Refund> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("order_id", orderIds);
        return refundMapper.selectPage(pageRequest, queryWrapper);
    }

    @Override
    public boolean acceptRefund(int refundId) {
        Refund refund = this.getById(refundId);
        if (refund == null) {
            throw new RuntimeException("退款申请不存在");
        }
        refund.setStatus("处理中");
        this.updateById(refund);
        User user = userService.getById(StpUtil.getLoginIdAsInt());
        user.setWealth(user.getWealth().subtract(orderService.getById(refund.getOrderId()).getPayAmount()));
        return true;
    }

//    @Override
//    public void commit(int refundId) {
//        Refund byId = this.getById(refundId);
//        Order byId1 = orderService.getById(byId.getOrderId());
//        User byId2 = userService.getById(byId1.getBuyer());
//        byId.setStatus("已完成");
//        byId1.setStatus("已取消");
//        byId2.setWealth(byId2.getWealth().add(byId1.getPayAmount()));
//        orderService.updateById(byId1);
//        userService.updateById(byId2);
//        this.updateById(byId);
//    }
// RefundServiceImpl.java

@Override
public void commit(int refundId) {
    Refund byId = this.getById(refundId);
    Order byId1 = orderService.getById(byId.getOrderId());
    User byId2 = userService.getById(byId1.getBuyer());
    byId.setStatus("已完成");
    byId1.setStatus("已取消");
    byId2.setWealth(byId2.getWealth().add(byId1.getPayAmount()));
    orderService.updateById(byId1);
    userService.updateById(byId2);

    // 回补商品库存
    Good good = goodService.getById(byId1.getContent());
    if (good != null) {
        good.setCount(good.getCount() + byId1.getAmount());
        goodService.updateById(good);
    }

    this.updateById(byId);
}
    @Override
    public void declineRefund(int refundId) {
        Refund byId = this.getById(refundId);
        if (byId == null) {
            throw new RuntimeException("退款申请不存在");
        }
        Order order = orderService.getById(byId.getOrderId());
        order.setStatus("已完成");
        orderService.updateById(order);
        byId.setStatus("已拒绝");
        this.updateById(byId);
    }

    /**
     * 检查订单是否符合退款条件
     *
     * @param order 订单
     * @return 是否可退款
     */
    private boolean isRefundable(Order order) {
        return "已支付".equals(order.getStatus()) || "已完成".equals(order.getStatus());
    }

    public IPage<OrderWithRefundVO> getOrdersWithRefundByBuyer(Integer buyerId, int page, int size) {
        Page<OrderWithRefundVO> p = new Page<>(page, size);
        return refundMapper.getOrdersWithRefunds(p, buyerId);
    }

}
