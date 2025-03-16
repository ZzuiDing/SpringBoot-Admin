package com.example.spba.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spba.dao.AddressMapper;
import com.example.spba.domain.dto.AddressDTO;
import com.example.spba.service.AddressService;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, AddressDTO> implements AddressService {
    @Override
    public IPage<AddressDTO> getAddressListByUserId(int userId, int pageNum, int pageSize) {
        //todo 完成根据id的分页查询
        return null;
    }
}
