package com.example.spba.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spba.dao.AddressMapper;
import com.example.spba.domain.entity.Address;
import com.example.spba.domain.entity.User;
import com.example.spba.service.AddressService;
import com.example.spba.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    @Autowired
    private AddressMapper addressMapper; // 修正变量命名

    @Autowired
    private UserService userService; // 添加 UserService 依赖

    @Override
    public IPage<Address> getAddressListByUserId(int userId, int pageNum, int pageSize) {
        if (userId <= 0 || pageNum <= 0 || pageSize <= 0) {
            log.warn("非法参数: userId={}, pageNum={}, pageSize={}", userId, pageNum, pageSize);
            return new Page<>();
        }

        Page<Address> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Address> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Address::getUserId, userId);
        System.out.println(queryWrapper.getSqlSegment());

        try {
            return addressMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            log.error("获取用户地址列表失败", e);
            return new Page<>();
        }
    }

    @Override
    public IPage<Address> searchAddress(int userId, String keyword, int pageNum, int pageSize) {
        int loginIdAsInt = StpUtil.getLoginIdAsInt();
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        // 如果关键词不为空，则进行模糊查询
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.like("address", keyword)  // 模糊查询地址
                    .or().like("name", keyword)  // 模糊查询收件人
                    .or().like("phone", keyword) // 模糊查询电话
                    .or().like("`desc`", keyword) // 模糊查询备注
                    ; // 只查询当前用户的地址
        }
        queryWrapper.eq("user_id", userId)
                .or().eq("user_id", loginIdAsInt); // 允许查询当前登录用户的地址
        Page<Address> page = new Page<>(pageNum, pageSize);
        try {
            return addressMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            log.error("搜索地址失败", e);
            return new Page<>();
        }
    }

    @Override
    public Address getDefaultAddress(int userId) {
        User user = userService.getById(userId);
        if (user == null) {
            log.warn("用户不存在: userId={}", userId);
            return null;
        }
//        this.getById(user.getAddress());
        return this.getById(user.getAddress());
    }

    @Override
    public Boolean setDefaultAddress(int addressId) {
        User user = userService.getById(StpUtil.getLoginIdAsInt());
        user.setAddress(addressId);
        userService.updateById(user);
        // 更新用户的默认地址
        return true;
    }
}
