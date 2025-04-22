package com.example.spba.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spba.dao.WithDrawRequestMapper;
import com.example.spba.domain.entity.User;
import com.example.spba.domain.entity.WithdrawRequest;
import com.example.spba.service.WithDrawRequsetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WithDrawRequsetServiceImpl extends ServiceImpl<WithDrawRequestMapper,WithdrawRequest> implements WithDrawRequsetService {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    WithDrawRequestMapper WithDrawRequestMapper;

    @Override
    public IPage<WithdrawRequest> getRequests(Integer pageNum, Integer pageSize) {

        Page<WithdrawRequest> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<WithdrawRequest> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(WithdrawRequest::getId);
        System.out.println(wrapper.getSqlSegment());
        User user = userService.getById(StpUtil.getLoginIdAsInt());
        if (user.getRole()==1) {
            wrapper.eq(WithdrawRequest::getUserId, StpUtil.getLoginIdAsInt());
        }
        return WithDrawRequestMapper.selectPage(page, wrapper);
    }

}
