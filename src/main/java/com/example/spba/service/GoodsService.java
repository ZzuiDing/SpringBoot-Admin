package com.example.spba.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.spba.domain.entity.Good;

public interface GoodsService extends IService<Good> {

    IPage<Good> getGoodList(Integer pageNum, Integer pageSize);

    IPage<Good> getGoodListByUserId(Integer pageNum, Integer pageSize, Integer id);

    IPage<Good> getNewestGoods(Integer pageNum, Integer pageSize, Integer num);

    IPage<Good> getTopSoldGoods(Integer pageNum, Integer pageSize, Integer num, String category, String query);
}
