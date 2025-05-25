package com.example.spba.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.spba.domain.entity.WalletHistory;
import com.example.spba.service.WalletHistoryService;
import com.example.spba.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/WalletHistory")
public class WalletHistoryController {
    @Autowired
    WalletHistoryService walletHistoryService;

    // 这里可以添加其他的接口方法，例如查询钱包历史记录等
    @RequestMapping("/getAll")
    public R getAllWalletHistory(@RequestParam(defaultValue = "1") Integer pageNum,
                                 @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<WalletHistory> walletHistoryList = walletHistoryService.getAllWalletHistory(pageNum, pageSize);
        return R.success(walletHistoryList);
    }

}
