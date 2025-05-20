package com.example.spba.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.spba.domain.entity.Address;

public interface AddressService extends IService<Address> {
    IPage<Address> getAddressListByUserId(int userId, int pageNum, int pageSize);

    IPage<Address> searchAddress(int userId, String keyword, int pageNum, int pageSize);
}
