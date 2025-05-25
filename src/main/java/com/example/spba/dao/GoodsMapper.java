package com.example.spba.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.spba.domain.entity.Good;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface GoodsMapper extends BaseMapper<Good> {
//    List listGoods();

//    List<Good> selectPageByUserId(Integer pageNum, Integer pageSize, Integer id);

    // 自定义查询，带悲观锁
    @Select("SELECT * FROM good WHERE id = #{id} FOR UPDATE")
    Good findByIdForUpdate(Integer id);

    // 自定义更新库存的方法
    @Update("UPDATE good SET count = #{count} WHERE id = #{id}")
    int updateStock(Integer id, Integer count);
}
