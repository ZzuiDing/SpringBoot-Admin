package com.example.spba.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spba.dao.GoodCommentMapper;
import com.example.spba.domain.entity.GoodComment;
import com.example.spba.service.GoodCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodCommentServiceImpl extends ServiceImpl<GoodCommentMapper, GoodComment> implements GoodCommentService {

    @Autowired
    GoodCommentMapper goodCommentMapper;
    @Override
    public IPage<GoodComment> selectByGoodId(int goodId, int pageNum, int pageSize) {
        Page<GoodComment> page = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<GoodComment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GoodComment::getGoodId,goodId);
        return goodCommentMapper.selectPage(page,queryWrapper);

    }

    @Override
    public Double getaverageRating(Integer goodId) {
        return goodCommentMapper.getaverageRating(goodId);
    }
}
