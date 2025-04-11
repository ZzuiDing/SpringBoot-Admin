package com.example.spba.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.spba.domain.dto.ShoppingCartForm;
import com.example.spba.domain.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {

    void updateByIdp1(ShoppingCart shoppingCart);

    void addItem(ShoppingCart shoppingCart);

    List<ShoppingCartForm> getByUserId(int loginIdAsInt);
}
