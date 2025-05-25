package com.example.spba.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.spba.domain.entity.WalletHistory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WallerHistoryMapper extends BaseMapper<WalletHistory> {
    // 这里可以添加自定义查询方法
}
