package com.example.spba.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spba.dao.ShoppingCartMapper;
import com.example.spba.domain.dto.ShoppingCartForm;
import com.example.spba.domain.entity.ShoppingCart;
import com.example.spba.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {


    @Autowired
    ShoppingCartMapper shoppingCartMapper;

    @Override
    public void updateByIdp1(ShoppingCart shoppingCart) {
        int id = shoppingCart.getId();
        ShoppingCart shoppingCart1 = this.getById(id);
        if (shoppingCart1 != null) {
            shoppingCart1.setNum(shoppingCart.getNum());
            shoppingCart1.setDate(shoppingCart.getDate());
            this.updateById(shoppingCart1);
        } else {
            throw new RuntimeException("购物车商品不存在");
        }
    }

    @Override
    public void addItem(ShoppingCart shoppingCart) {
        System.out.println("添加购物车商品:" + shoppingCart);
        // 1. 判断购物车中是否已经存在该商品
        ShoppingCart existingItem = shoppingCartMapper.selectByGoodIdAndUserId(shoppingCart.getGoodId(), StpUtil.getLoginIdAsInt());
        System.out.println("existingItem:" + existingItem);
        if (existingItem == null) {
            //添加商品
            try {
                int userId = StpUtil.getLoginIdAsInt();
                shoppingCart.setUserId(userId);
                this.save(shoppingCart);
            } catch (Exception e) {
                System.out.println("添加购物车商品失败:" + e.getMessage());
            }
        } else {
            existingItem.setNum(existingItem.getNum() + shoppingCart.getNum());
            this.updateById(existingItem);
        }
    }

    @Override
    public List<ShoppingCartForm> getByUserId(int loginIdAsInt) {
        return shoppingCartMapper.selectList(loginIdAsInt);
    }
}
