package com.example.spba.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.spba.domain.dto.AddressDTO;

public interface AddressService extends IService<AddressDTO> {
    IPage<AddressDTO> getAddressListByUserId(int userId, int pageNum, int pageSize);
}
