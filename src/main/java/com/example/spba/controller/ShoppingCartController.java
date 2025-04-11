package com.example.spba.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.example.spba.domain.dto.ShoppingCartDTO;
import com.example.spba.domain.dto.ShoppingCartForm;
import com.example.spba.domain.entity.ShoppingCart;
import com.example.spba.service.ShoppingCartService;
import com.example.spba.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@RequestMapping("/ShoppingCart")
public class ShoppingCartController {

    @Autowired
    ShoppingCartService shoppingCartService;

    @RequestMapping("/add")
    public R addGoods(@RequestBody ShoppingCart shoppingCart) {
        try {
            shoppingCartService.addItem(shoppingCart);
            return R.success("添加成功");
        }
        catch (Exception e) {
            return R.error("添加失败"+e.getMessage());
        }
    }

    @RequestMapping("/delete")
    public R deleteGoods(@RequestParam(value = "id") Integer id) {
        try {
            shoppingCartService.removeById(id);
            return R.success("删除成功");
        } catch (Exception e) {
            return R.error("删除失败");
        }
    }

    @RequestMapping("/update")
    public R updateGoods(@RequestBody ShoppingCartForm shoppingCartForm) {
        try {
            shoppingCartService.updateById(shoppingCartForm);
            return R.success("修改成功");
        } catch (Exception e) {
            return R.error("修改失败");
        }
    }

    @RequestMapping("/get")
    public R getGoods() {
        int loginIdAsInt = StpUtil.getLoginIdAsInt();
        try {
            List<ShoppingCartForm> shoppingCart = shoppingCartService.getByUserId(loginIdAsInt);
            System.out.println("购物车商品:" + shoppingCart);
            return R.success(shoppingCart);
        } catch (Exception e) {
            return R.error("查询失败");
        }
    }
}
