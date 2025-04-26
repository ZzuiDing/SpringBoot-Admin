package com.example.spba.controller;

import com.example.spba.utils.JDGoodInform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jdGoods")
public class JDGoodsController {

    @Autowired
    private JDGoodInform jdGoodInform;

    @RequestMapping("/getJDGoodInform")
    public String getJDGoodInform(@RequestParam String goodId) throws Exception {
        return jdGoodInform.getJDGoodInform(goodId);
    }
}
