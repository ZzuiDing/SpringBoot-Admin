package com.example.spba.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.spba.domain.entity.WithdrawRequest;

public interface WithDrawRequsetService extends IService<WithdrawRequest> {
    IPage<WithdrawRequest> getRequests(Integer pageNum, Integer pageSize);
}
