package com.example.spba.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spba.dao.WallerHistoryMapper;
import com.example.spba.domain.entity.User;
import com.example.spba.domain.entity.WalletHistory;
import com.example.spba.service.WalletHistoryService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletHistoryServiceImpl extends ServiceImpl<WallerHistoryMapper, WalletHistory> implements WalletHistoryService {
    @Override
    public IPage<WalletHistory> getAllWalletHistory(Integer pageNum, Integer pageSize) {
        int loginIdAsInt = StpUtil.getLoginIdAsInt();
        LambdaQueryWrapper<WalletHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WalletHistory::getUserId, loginIdAsInt);
        wrapper.orderByDesc(WalletHistory::getId);
        return this.page(new Page<>(pageNum, pageSize), wrapper);
    }

    @Override
    public Boolean createWalletHistory(Integer userId, String type, BigDecimal amount, BigDecimal remainingBalance, String description) {
        WalletHistory walletHistory = new WalletHistory();
        walletHistory.setUserId(userId);
        walletHistory.setWalletType(type);
        walletHistory.setAmount(amount);
        walletHistory.setRemainingBalance(remainingBalance);
        walletHistory.setDescription(description);
        return this.save(walletHistory);
    }


}
