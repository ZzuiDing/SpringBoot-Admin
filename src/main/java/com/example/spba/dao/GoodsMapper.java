package com.example.spba.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.spba.domain.entity.Good;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsMapper extends BaseMapper<Good> {
//    List listGoods();

    List<Good> selectPageByUserId(Integer pageNum, Integer pageSize, Integer id);
}
