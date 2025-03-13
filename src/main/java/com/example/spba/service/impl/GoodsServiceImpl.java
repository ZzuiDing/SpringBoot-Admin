package com.example.spba.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spba.dao.GoodsMapper;
import com.example.spba.domain.entity.Good;
import com.example.spba.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper,Good> implements GoodsService {
    @Autowired
    GoodsMapper goodsMapper;

    @Override
    public IPage<Good> getGoodList(Integer pageNum, Integer pageSize) {
        Page<Good> page = new Page<>(pageNum, pageSize,false);
        return goodsMapper.selectPage(page,null);
    }


//    @Override
//    public List<Good> AllListGoods(Integer page, Integer limit) {
//        List<Good> list = goodsMapper.selectAll();
//        return goodsMapper.listGoods();
//    }
}
