package com.example.spba.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class Order {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer buyer;
    private Integer seller;
    private String content;
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
