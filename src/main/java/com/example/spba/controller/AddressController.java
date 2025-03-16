package com.example.spba.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.spba.domain.dto.AddressDTO;
import com.example.spba.domain.entity.Address;
import com.example.spba.service.AddressService;
import com.example.spba.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController()
@RequestMapping("/address")
public class AddressController {

    @Autowired
    AddressService addressService;

    @RequestMapping("/getAddressListByUserId")
    public R getAddress(@RequestParam() int userId, @RequestParam(defaultValue = "1")  int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        IPage<AddressDTO> addressList =  addressService.getAddressListByUserId(userId, pageNum, pageSize);
        return R.success(addressList);
    }

    @RequestMapping("/addAddress")
    public R addAddress(@RequestParam() @Validated(AddressDTO.Save.class) AddressDTO address) {
        if (addressService.save(address)) {
            return R.success("添加地址成功");
        } else {
            return R.error("添加地址失败");
        }
    }

    @RequestMapping("/deleteAddress")
    public R deleteAddress(@RequestParam() int id) {
        if (addressService.removeById(id)) {
            return R.success("删除地址成功");
        } else {
            return R.error("删除地址失败");
        }
    }

    @RequestMapping("/updateAddress")
    public R updateAddress(@RequestParam() @Validated(AddressDTO.Update.class) AddressDTO address) {
        if (addressService.updateById(address)) {
            return R.success("更新地址成功");
        } else {
            return R.error("更新地址失败");
        }
    }
}
