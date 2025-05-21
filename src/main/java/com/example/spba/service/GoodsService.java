package com.example.spba.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.spba.domain.entity.Good;

public interface GoodsService extends IService<Good> {

    IPage<Good> getGoodList(Integer pageNum, Integer pageSize, String keyword);

    IPage<Good> getGoodListByUserId(Integer pageNum, Integer pageSize, Integer id, String keyword);

    IPage<Good> getNewestGoods(Integer pageNum, Integer pageSize, Integer num);

    IPage<Good> getTopSoldGoods(Integer pageNum, Integer pageSize, Integer num, String category, String query);

    IPage<Good> searchGoods(Integer pageNum, Integer pageSize, String keyword);
}
