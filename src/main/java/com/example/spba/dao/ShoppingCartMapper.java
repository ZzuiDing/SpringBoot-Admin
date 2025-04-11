package com.example.spba.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.spba.domain.dto.ShoppingCartForm;
import com.example.spba.domain.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
    ShoppingCart selectByGoodIdAndUserId(Integer goodId, int loginIdAsInt);

    List<ShoppingCartForm> selectList(int loginIdAsInt);
}
