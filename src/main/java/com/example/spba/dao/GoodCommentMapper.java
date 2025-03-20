package com.example.spba.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.spba.domain.entity.GoodComment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodCommentMapper extends BaseMapper<GoodComment> {
    // 这里可以添加自定义的查询方法
    // 例如：List<GoodComment> selectByName(String name);
}
