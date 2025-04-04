package com.example.spba.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Good {
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

    // Getters and Setters
}