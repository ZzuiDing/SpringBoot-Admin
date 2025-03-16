package com.example.spba.controller;


import com.example.spba.domain.dto.ShoppingCartDTO;
import com.example.spba.domain.entity.ShoppingCart;
import com.example.spba.service.ShoppingCartService;
import com.example.spba.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/ShoppingCart")
public class ShoppingCartController {

    @Autowired
    ShoppingCartService shoppingCartService;

    @RequestMapping("/add")
    public R addGoods(@RequestBody @Validated(ShoppingCartDTO.Save.class) ShoppingCart shoppingCart) {
        try {
            shoppingCartService.save(shoppingCart);
            return R.success("添加成功");
        } catch (Exception e) {
            return R.error("添加失败");
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
    public R updateGoods(@RequestBody @Validated(ShoppingCartDTO.Update.class) ShoppingCart shoppingCart) {
        try {
            shoppingCartService.updateById(shoppingCart);
            return R.success("修改成功");
        } catch (Exception e) {
            return R.error("修改失败");
        }
    }
}
