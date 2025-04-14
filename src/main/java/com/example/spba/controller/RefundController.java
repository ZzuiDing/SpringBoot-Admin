package com.example.spba.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.spba.domain.dto.OrderWithRefundVO;
import com.example.spba.domain.dto.RefundDTO;
import com.example.spba.domain.entity.Refund;
import com.example.spba.service.RefundService;
import com.example.spba.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

//    @RequestMapping("/getRefund")
//    public R getRefund(@RequestParam("orderId") int refundId) {
//        try {
//            Refund refund = refundService.getById(refundId);
//            return R.success(refund);
//        } catch (Exception e) {
//            log.error("Error fetching refund: {}", e.getMessage());
//            return R.error("Failed to fetch refund");
//        }
//    }
//    @RequestMapping("/getRefundListBySellerId")
//    public R getRefundList(@RequestParam("SellerId") int sellerId,@RequestParam("page") int page, @RequestParam("size") int size) {
//        try {
//            return R.success(refundService.getBySellerId(sellerId,page,size));
//        } catch (Exception e) {
//            log.error("Error fetching refund list: {}", e.getMessage());
//            return R.error("Failed to fetch refund list");
//        }
//    }

    @RequestMapping("/getRefundListByBuyerId")
    public R getRefundListByBuyerId(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        try {
            int buyerId = StpUtil.getLoginIdAsInt();
            return R.success(refundService.getByBuyerId(buyerId,page,size));
        } catch (Exception e) {
            log.error("Error fetching refund list: {}", e.getMessage());
            return R.error("Failed to fetch refund list");
        }
    }


    @RequestMapping("/getRefundListBySellerId")
    public R getRefundListBySellerId(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        try {
            int sellerId = StpUtil.getLoginIdAsInt();
            return R.success(refundService.getBySellerId(sellerId, page, size));
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

    @GetMapping("/with-refund")
    public R getOrdersWithRefund(
            @RequestParam Integer buyerId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        IPage<OrderWithRefundVO> resultPage = refundService.getOrdersWithRefundByBuyer(buyerId, page, size);
        return R.success(resultPage);
    }

    @RequestMapping("/cancelRefund")
    public R cancelRefund(@RequestParam("refundId") int refundId) {
        try {
            refundService.cancelRefund(refundId);
            return R.success("Refund canceled successfully");
        }
        catch (Exception e) {
            log.error("Error canceling refund: {}", e.getMessage());
            return R.error("Failed to cancel refund");
        }
    }

    @RequestMapping("/acceptRefund")
    public R acceptRefund(@RequestParam("refundId") int refundId) {
        try {
            refundService.acceptRefund(refundId);
            return R.success("Refund accepted successfully");
        }
        catch (Exception e) {
            log.error("Error accepting refund: {}", e.getMessage());
            return R.error("Failed to accept refund");
        }
    }

    @RequestMapping("/addrefundExpress")
    public R refundExpress(@RequestParam("refundId") int refundId,
                           @RequestParam("express") String express){
        try {
            Refund byId = refundService.getById(refundId);
            byId.setExpress(express);
            refundService.updateById(byId);
            return R.success();
        } catch (Exception e) {
            log.error("Error accepting refund: {}", e.getMessage());
            return R.error("Failed to accept refund");
        }
    }
    @RequestMapping("/commit")
    public R commit(@RequestParam("refundId") int refundId){
        try {
            refundService.commit(refundId);
            return R.success();
        } catch (Exception e) {
            log.error("Error accepting refund: {}", e.getMessage());
            return R.error("Failed to accept refund");
        }
    }
}
