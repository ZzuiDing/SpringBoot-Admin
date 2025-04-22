package com.example.spba.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.spba.domain.entity.WithdrawRequest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WithDrawRequestMapper extends BaseMapper<WithdrawRequest> {
    // Additional methods for custom queries can be defined here
}
