package com.example.spba.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.spba.domain.dto.AddressDTO;
import com.example.spba.domain.entity.Address;
import com.example.spba.service.AddressService;
import com.example.spba.utils.R;
import com.example.spba.utils.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    // 分页查询地址列表
    @RequestMapping("/getAddressListByUserId")
    public R getAddress(@RequestParam(defaultValue = "1") int pageNum,
                        @RequestParam(defaultValue = "10") int pageSize) {
        int userId = StpUtil.getLoginIdAsInt();

        // 调用分页查询
        IPage<Address> addressList = addressService.getAddressListByUserId(userId, pageNum, pageSize);

        // 返回分页结果
        return R.success(new PageResponse<>(addressList.getRecords(), addressList.getTotal()));
    }

    // 新增地址
    @RequestMapping("/addAddress")
    public R addAddress(@RequestBody @Validated(AddressDTO.Save.class) AddressDTO address) {
        address.setUserId(StpUtil.getLoginIdAsInt());
        if (addressService.save(address)) {
            return R.success("添加地址成功");
        } else {
            return R.error("添加地址失败");
        }
    }

    // 删除地址
    @RequestMapping("/deleteAddress")
    public R deleteAddress(@RequestParam int id) {
        if (addressService.removeById(id)) {
            return R.success("删除地址成功");
        } else {
            return R.error("删除地址失败");
        }
    }

    // 更新地址
    @RequestMapping("/updateAddress")
    public R updateAddress(@RequestBody @Validated(AddressDTO.Update.class) AddressDTO address) {
        if (addressService.updateById(address)) {
            return R.success("更新地址成功");
        } else {
            return R.error("更新地址失败");
        }
    }

    @GetMapping("/searchAddress")
    public R searchAddress(@RequestParam(required = false, defaultValue = "") String keyword,
                           @RequestParam(defaultValue = "1") int pageNum,
                           @RequestParam(defaultValue = "10") int pageSize) {
        int userId = StpUtil.getLoginIdAsInt();
        IPage<Address> addressList = addressService.searchAddress(userId, keyword, pageNum, pageSize);
        return R.success(addressList);
    }
}
