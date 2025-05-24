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
    private String buyerDesc;
    private String sellerDesc;
    private LocalDateTime createdTime;
    private LocalDateTime payTime;
    private LocalDateTime expressTime;
    private LocalDateTime receiveTime;
    private LocalDateTime cancelTime;
    private String status;
    private String payMethod;
    private BigDecimal payAmount;
    private String expressId;
    private Integer amount;
    private Integer addressId;
    // Getters and Setters
}
