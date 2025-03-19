package com.example.spba.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.spba.domain.entity.Kind;
import org.apache.ibatis.annotations.Mapper;


/**
* @author Zui_Ding
* @description 针对表【kind(商品种类)】的数据库操作Mapper
* @createDate 2025-03-19 19:42:51
* @Entity com.example.spba.domain.entity.Kind
*/
@Mapper
public interface KindMapper extends BaseMapper<Kind> {
    // 这里可以添加自定义的查询方法
    // 例如：List<Kind> selectByName(String name);



}
