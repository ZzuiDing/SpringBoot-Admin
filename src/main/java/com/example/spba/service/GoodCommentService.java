package com.example.spba.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.spba.domain.entity.GoodComment;

public interface GoodCommentService extends IService<GoodComment> {
    IPage<GoodComment> selectByGoodId(int goodId, int pageNum, int pageSize);

    Double getaverageRating(Integer goodId);
    // 这里可以添加自定义的查询方法
    // 例如：List<GoodComment> selectByName(String name);
}
