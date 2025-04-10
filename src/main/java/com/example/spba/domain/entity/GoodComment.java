package com.example.spba.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class GoodComment {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    @TableField("`desc`")
    private String desc;
    private String pictLocation;
    private Integer stars;
    private  Integer goodId;
    // Getters and Setters
}
