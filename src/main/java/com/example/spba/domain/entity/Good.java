package com.example.spba.domain.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Good {
    private Integer id;
    private Integer userId;
    private String name;
    private BigDecimal price;
    private String picture;
    private String kind;
    private String keyWord;
    private String desc;
    private String status;
    private Integer soldAmount;

    // Getters and Setters
}