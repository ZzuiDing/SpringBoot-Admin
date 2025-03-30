package com.example.spba.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spba.dao.RefundMapper;
import com.example.spba.domain.dto.RefundDTO;
import com.example.spba.domain.entity.Order;
import com.example.spba.domain.entity.Refund;
import com.example.spba.service.OrderService;
import com.example.spba.service.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RefundServiceImpl extends ServiceImpl<RefundMapper, Refund> implements RefundService {

    @Autowired
    private RefundMapper refundMapper;

    @Autowired
    private OrderService orderService;

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

        Refund refund = new Refund();
        refund.setOrderId(refundDTO.getOrderId());
        refund.setReason(refundDTO.getReason());
        refund.setStatus("pending"); // 默认待处理状态

        refundMapper.insert(refund);

        // 可选：更新订单状态（如：标记为“退款处理中”）
        order.setStatus("refund_processing");
        orderService.updateById(order);
    }

    /**
     * 根据卖家ID查询退款
     *
     * @param sellerId 卖家ID
     * @param page     页码
     * @param size     每页大小
     * @return 退款列表
     */
    @Override
    public List<Refund> getBySellerId(int sellerId, int page, int size) {
        int offset = (page - 1) * size; // 计算分页起始位置
        return refundMapper.selectBySellerId(sellerId, offset, size);
    }

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

    /**
     * 检查订单是否符合退款条件
     *
     * @param order 订单
     * @return 是否可退款
     */
    private boolean isRefundable(Order order) {
        return "paid".equals(order.getStatus()) || "shipped".equals(order.getStatus());
    }
}
