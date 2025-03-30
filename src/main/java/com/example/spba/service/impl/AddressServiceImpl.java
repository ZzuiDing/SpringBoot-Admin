package com.example.spba.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spba.dao.AddressMapper;
import com.example.spba.domain.dto.AddressDTO;
import com.example.spba.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, AddressDTO> implements AddressService {

    @Autowired
    private AddressMapper addressMapper; // 修正变量命名

    @Override
    public IPage<AddressDTO> getAddressListByUserId(int userId, int pageNum, int pageSize) {
        if (userId <= 0 || pageNum <= 0 || pageSize <= 0) {
            log.warn("非法参数: userId={}, pageNum={}, pageSize={}", userId, pageNum, pageSize);
            return new Page<>();
        }

        Page<AddressDTO> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<AddressDTO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressDTO::getUserId, userId);

        try {
            return addressMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取用户地址列表失败", e);
            return new Page<>();
        }
    }
}
