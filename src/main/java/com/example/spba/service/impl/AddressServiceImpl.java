package com.example.spba.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spba.dao.AddressMapper;
import com.example.spba.domain.dto.AddressDTO;
import com.example.spba.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, AddressDTO> implements AddressService {

    @Autowired
    private AddressMapper AddressMapper;

    @Override
    public IPage<AddressDTO> getAddressListByUserId(int userId, int pageNum, int pageSize) {
        Page<AddressDTO> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<AddressDTO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressDTO::getUserId, userId);
        return AddressMapper.selectPage(page, queryWrapper);
    }
}
