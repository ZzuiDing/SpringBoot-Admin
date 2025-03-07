package com.example.spba.domain.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class Order {
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

    // Getters and Setters
}
