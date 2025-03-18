package com.example.spba.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spba.dao.GoodsMapper;
import com.example.spba.domain.entity.Good;
import com.example.spba.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper,Good> implements GoodsService {
    @Autowired
    GoodsMapper goodsMapper;

    @Override
    public IPage<Good> getGoodList(Integer pageNum, Integer pageSize) {
        Page<Good> page = new Page<>(pageNum, pageSize,true);
        return goodsMapper.selectPage(page,null);
    }

    //todo 根据用户id查询商品列表
    @Override
    public IPage<Good> getGoodListByUserId(Integer pageNum, Integer pageSize, Integer id) {
//        Page<Good> page = new Page<>(pageNum,pageSize,id);
//        IPage<Good> goodIPage = goodsMapper.selectPageByUserId(pageNum, pageSize, id);
        Page<Good> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Good> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Good::getUserId, id).orderByDesc(Good::getId);
        return goodsMapper.selectPage(page, wrapper);
//        return null;
    }


//    @Override
//    public List<Good> AllListGoods(Integer page, Integer limit) {
//        List<Good> list = goodsMapper.selectAll();
//        return goodsMapper.listGoods();
//    }
}
