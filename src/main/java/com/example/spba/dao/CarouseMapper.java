package com.example.spba.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.spba.domain.entity.Carousel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CarouseMapper extends BaseMapper<Carousel> {
    // Define any additional methods if needed
}
