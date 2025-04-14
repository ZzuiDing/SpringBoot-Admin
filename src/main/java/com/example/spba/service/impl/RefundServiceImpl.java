package com.example.spba.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spba.dao.RefundMapper;
import com.example.spba.domain.dto.OrderWithRefundVO;
import com.example.spba.domain.dto.RefundDTO;
import com.example.spba.domain.entity.Order;
import com.example.spba.domain.entity.Refund;
import com.example.spba.domain.entity.User;
import com.example.spba.service.OrderService;
import com.example.spba.service.RefundService;
import com.example.spba.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class RefundServiceImpl extends ServiceImpl<RefundMapper, Refund> implements RefundService {

    @Autowired
    private RefundMapper refundMapper;

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

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
    public List<Refund> getByBuyerId(int buyerId, int page, int size) {
        int offset = (page - 1) * size;
        return refundMapper.selectByBuyerId(buyerId, offset, size);
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
    public List<Refund> getBySellerId(int sellerId, int page, int size) {
        int offset = (page - 1) * size;
        return refundMapper.selectBySellerId(sellerId, offset, size);
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
