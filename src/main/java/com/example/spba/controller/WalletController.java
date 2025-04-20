package com.example.spba.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.spba.service.wallerService;
import com.example.spba.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController("/wallet")
public class WalletController {
    // 钱包相关的接口
    // 例如：查询余额、充值、提现等操作

    @Autowired
    wallerService walletService;

    // 查询余额
    @GetMapping("/balance")
    public R getBalance() {
        BigDecimal balance = walletService.getBalance(StpUtil.getLoginIdAsInt());
        return R.success(balance);
    }


    //支付
    @PostMapping("/pay")
    public R pay(@RequestBody Map<String, List<Integer>> orderIdsMap) {
        List<Integer> orderIds = orderIdsMap.get("orderIds");
        if(walletService.pay(orderIds)){
            return R.success("支付成功");
        } else {
            return R.error("余额不足");
        }
//        return R.success("支付成功");
    }
    // 充值
    // @PostMapping("/recharge")
    // public R recharge(@RequestBody RechargeDTO rechargeDTO) {
    //     int userId = StpUtil.getLoginIdAsInt();
    //     walletService.recharge(userId, rechargeDTO.getAmount());
    //     return R.success("充值成功");
    // }
}
