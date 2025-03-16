package com.example.spba.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class ShoppingCart {
    private Integer id;
    private Integer userId;
    private String goodId;
    private Integer num;
    private LocalDateTime date;

    // Getters and Setters
}