package com.example.spba.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.spba.domain.entity.Good;

import java.util.List;

public interface GoodsService extends IService<Good> {

    IPage<Good> getGoodList(Integer pageNum, Integer pageSize);
}
