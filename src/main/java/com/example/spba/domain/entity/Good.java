package com.example.spba.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Good {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private String name;
    private BigDecimal price;
    private String picture;
    private int kindId;
    private String keyWord;
    @TableField("`desc`")
    private String desc;
    private String status;
    private Integer soldAmount;
    private Double rating;
    private Integer count;
    // Getters and Setters
}