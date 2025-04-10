package com.example.spba.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("orders")
@Data
public class Order {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer buyer;
    private Integer seller;
    private String content;
    @TableField("`desc`")
    private String desc;
    private LocalDateTime date;
    private String status;
    private String payMethod;
    private BigDecimal payAmount;
    private String expressId;
    private Integer amount;
    private Integer addressId;

    // Getters and Setters
}
