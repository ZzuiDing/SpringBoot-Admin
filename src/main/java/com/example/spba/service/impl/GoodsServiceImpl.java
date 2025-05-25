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
    public IPage<Good> getGoodList(Integer pageNum, Integer pageSize, String keyword) {
        Page<Good> page = new Page<>(pageNum, pageSize, true);
        LambdaQueryWrapper<Good> wrapper = new LambdaQueryWrapper<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.and(w ->
                    w.like(Good::getName, keyword)
                            .or()
                            .like(Good::getDesc, keyword)
            );
        }

        wrapper.orderByDesc(Good::getId);

        return goodsMapper.selectPage(page, wrapper);
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
    public IPage<Good> getGoodListByUserId(Integer pageNum, Integer pageSize, Integer id, String keyword) {
    Page<Good> page = new Page<>(pageNum, pageSize);
    LambdaQueryWrapper<Good> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(Good::getUserId, id);
    if (keyword != null && !keyword.isEmpty()) {
        wrapper.and(w -> w.like(Good::getName, keyword)
                .or().like(Good::getDesc, keyword));
    }
    wrapper.orderByDesc(Good::getId);
    System.out.println("Created wrapperSegment:,"+wrapper.getSqlSegment());
    return goodsMapper.selectPage(page, wrapper);
}


    @Override
    public IPage<Good> getNewestGoods(Integer pageNum, Integer pageSize, Integer num) {
        // 第一步：先查询出最新的 num 个商品的 id
        QueryWrapper<Good> idQuery = new QueryWrapper<>();
        idQuery.select("id")
                .eq("status","在售")
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
    public IPage<Good> getTopSoldGoods(Integer pageNum, Integer pageSize, Integer num, String category, String query) {
        QueryWrapper<Good> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "在售");

        if (category != null && !category.equals("0") && !category.isEmpty()) {
            queryWrapper.eq("kind_id", category);
        }

        if (query != null && !query.isEmpty()) {
            queryWrapper.like("name", query);
        }

        // 先查符合条件的，按销量排序，限制前 num 个
        queryWrapper.orderByDesc("sold_amount").last("LIMIT " + num);

        List<Good> resultList = goodsMapper.selectList(queryWrapper);

        if (resultList.isEmpty()) {
            return new Page<>(pageNum, pageSize); // 返回空页
        }

        // 再分页这些结果
        List<Integer> idList = resultList.stream().map(Good::getId).collect(Collectors.toList());

        Page<Good> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Good> pageQuery = new QueryWrapper<>();
        pageQuery.in("id", idList).orderByDesc("sold_amount");

        return goodsMapper.selectPage(page, pageQuery);
    }

    @Override
    public IPage<Good> searchGoods(Integer pageNum, Integer pageSize, String keyword) {
        Page<Good> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Good> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w ->
                w.like(Good::getName, keyword)
                        .or()
                        .like(Good::getDesc, keyword)
        );
        wrapper.orderByDesc(Good::getId);
        return goodsMapper.selectPage(page, wrapper);
    }

    @Override
    public Good findByIdForUpdate(Integer goodId) {

        return goodsMapper.findByIdForUpdate(goodId);
    }

    @Override
    public int updateStock(Integer id, Integer count) {
        return goodsMapper.updateStock(id, count);
    }

//    @Override
//    public List<Good> AllListGoods(Integer page, Integer limit) {
//        List<Good> list = goodsMapper.selectAll();
//        return goodsMapper.listGoods();
//    }
}
