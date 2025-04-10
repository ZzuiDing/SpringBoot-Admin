package com.example.spba.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spba.dao.GoodsMapper;
import com.example.spba.domain.entity.Good;
import com.example.spba.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper,Good> implements GoodsService {
    @Autowired
    GoodsMapper goodsMapper;

    @Override
    public IPage<Good> getGoodList(Integer pageNum, Integer pageSize) {
        Page<Good> page = new Page<>(pageNum, pageSize,true);
        return goodsMapper.selectPage(page,null);
    }

//    @Override
//    public IPage<Good> getGoodListByUserId(Integer pageNum, Integer pageSize, Integer id) {
////        Page<Good> page = new Page<>(pageNum,pageSize,id);
////        IPage<Good> goodIPage = goodsMapper.selectPageByUserId(pageNum, pageSize, id);
//        Page<Good> page = new Page<>(pageNum, pageSize);
//        LambdaQueryWrapper<Good> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(Good::getUserId, id).orderByDesc(Good::getId);
//        System.out.println(wrapper.getSqlSegment());
//        return goodsMapper.selectPage(page, wrapper);
////        return null;
//    }
    @Override
    public IPage<Good> getGoodListByUserId(Integer pageNum, Integer pageSize, Integer id) {
    Page<Good> page = new Page<>(pageNum, pageSize);
    LambdaQueryWrapper<Good> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(Good::getUserId, id).orderByDesc(Good::getId);
    System.out.println("Created wrapperSegment:,"+wrapper.getSqlSegment());
    return goodsMapper.selectPage(page, wrapper);
}


    @Override
    public IPage<Good> getNewestGoods(Integer pageNum, Integer pageSize, Integer num) {
        // 第一步：先查询出最新的 num 个商品的 id
        QueryWrapper<Good> idQuery = new QueryWrapper<>();
        idQuery.select("id")
                .orderByDesc("id")
                .last("LIMIT " + num);
        List<Good> latestGoods = goodsMapper.selectList(idQuery);

        // 提取 id 列表
        List<Integer> idList = latestGoods.stream()
                .map(Good::getId)
                .collect(Collectors.toList());

        if (idList.isEmpty()) {
            return new Page<>(pageNum, pageSize);  // 返回空分页
        }

        // 第二步：分页查询这些 id 范围内的数据
        Page<Good> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Good> pageQuery = new QueryWrapper<>();
        pageQuery.in("id", idList)
                .orderByDesc("id"); // 可选，看你是否还要按 id 排序

        return goodsMapper.selectPage(page, pageQuery);
    }

    @Override
    public IPage<Good> getTopSoldGoods(Integer pageNum, Integer pageSize, Integer num) {
        // 第一步：先查询出售出数量最多的 num 个商品 id
        QueryWrapper<Good> soldQuery = new QueryWrapper<>();
        soldQuery.select("id")
                .orderByDesc("sold_amount")
                .last("LIMIT " + num);

        List<Good> topSoldList = goodsMapper.selectList(soldQuery);

        // 提取 id 列表
        List<Integer> idList = topSoldList.stream()
                .map(Good::getId)
                .collect(Collectors.toList());

        if (idList.isEmpty()) {
            return new Page<>(pageNum, pageSize); // 返回空页
        }

        // 第二步：分页查询这些商品
        Page<Good> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Good> pageQuery = new QueryWrapper<>();
        pageQuery.in("id", idList)
                .orderByDesc("sold_amount"); // 保持顺序一致

        return goodsMapper.selectPage(page, pageQuery);
    }

//    @Override
//    public List<Good> AllListGoods(Integer page, Integer limit) {
//        List<Good> list = goodsMapper.selectAll();
//        return goodsMapper.listGoods();
//    }
}
