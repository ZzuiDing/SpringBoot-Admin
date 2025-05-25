package com.example.spba.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.spba.domain.entity.WalletHistory;

import java.math.BigDecimal;

public interface WalletHistoryService extends IService<WalletHistory> {
    IPage<WalletHistory> getAllWalletHistory(Integer pageNum, Integer pageSize);

    Boolean createWalletHistory(Integer userId, String type, BigDecimal amount,BigDecimal remainingBalance, String description);
}
