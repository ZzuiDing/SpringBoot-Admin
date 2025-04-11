package com.example.spba.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class ShoppingCart {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer goodId;
    private Integer num;
    private LocalDateTime date;

    // Getters and Setters
}