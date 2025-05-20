package com.example.spba.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spba.domain.entity.OperateLog;
import com.example.spba.dao.OperateLogMapper;
import com.example.spba.domain.entity.User;
import com.example.spba.service.OperateLogService;
import com.example.spba.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class OperateLogServiceImpl extends ServiceImpl<OperateLogMapper, OperateLog> implements OperateLogService
{

    @Autowired
    private UserService userService;

    @Override
    public void save(Long adminId, String url, String method, String params, String ip) {
        System.out.println("url: " + url);
        User user = userService.getById(adminId);
        OperateLog log = new OperateLog();
        log.setAdminId(adminId.intValue());
        log.setUsername(user.getName());
        log.setUrl(url);
        log.setMethod(method);
        log.setParams(params);
        log.setIp(ip);
        this.save(log);
    }

    @Override
    public Page<HashMap> getList(Page page, HashMap params) {
        return this.baseMapper.getList(page, params);
    }
}
