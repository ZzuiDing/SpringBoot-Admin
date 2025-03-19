package com.example.spba.controller;

import com.example.spba.domain.dto.RefundDTO;
import com.example.spba.domain.entity.Refund;
import com.example.spba.service.RefundService;
import com.example.spba.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/refund")
public class RefundController {

    @Autowired
    RefundService refundService;

    @RequestMapping("/create")
    public R createRefund(@RequestBody RefundDTO refund) {

        try {
            refundService.create(refund);
        } catch (Exception e) {
            log.error("Error creating refund: {}", e.getMessage());
            return R.error("Failed to create refund");
        }
//        return "Refund created for order ID: " + orderId;
        return R.success("Refund created successfully");
    }

    @RequestMapping("/getRefund")
    public R getRefund(@RequestParam("orderId") int refundId) {
        try {
            Refund refund = refundService.getById(refundId);
            return R.success(refund);
        } catch (Exception e) {
            log.error("Error fetching refund: {}", e.getMessage());
            return R.error("Failed to fetch refund");
        }
    }
    @RequestMapping("/getRefundListBySellerId")
    public R getRefundList(@RequestParam("SellerId") int sellerId,@RequestParam("page") int page, @RequestParam("size") int size) {
        try {
            return R.success(refundService.getBySellerId(sellerId,page,size));
        } catch (Exception e) {
            log.error("Error fetching refund list: {}", e.getMessage());
            return R.error("Failed to fetch refund list");
        }
    }

    @RequestMapping("/getRefundListByBuyerId")
    public R getRefundListByBuyerId(@RequestParam("BuyerId") int buyerId,@RequestParam("page") int page, @RequestParam("size") int size) {
        try {
            return R.success(refundService.getByBuyerId(buyerId,page,size));
        } catch (Exception e) {
            log.error("Error fetching refund list: {}", e.getMessage());
            return R.error("Failed to fetch refund list");
        }
    }

    @RequestMapping("/updateRefund")
    public R updateRefund(@RequestBody RefundDTO refundDTO) {
        try {
            Refund refund = new Refund();
            refund.setId(refundDTO.getId());
            refund.setStatus(refundDTO.getStatus());
            refund.setReason(refundDTO.getReason());
            refund.setOrderId(refundDTO.getOrderId());
            refund.setDesc(refundDTO.getDesc());
            refundService.updateById(refund);
            return R.success("Refund updated successfully");
        } catch (Exception e) {
            log.error("Error updating refund: {}", e.getMessage());
            return R.error("Failed to update refund");
        }
    }
}
