package com.example.spba.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("kind") // 确保实体类和数据库表匹配
public class Kind {
    @TableId(type = IdType.AUTO) // 显式指定数据库字段
    private Long id; // 修改为 Integer，确保类型匹配

    @TableField("kind_name")
    private String kindName; // 修改为 camelCase


}
