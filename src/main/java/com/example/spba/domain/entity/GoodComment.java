package com.example.spba.domain.entity;

import lombok.Data;

@Data
public class GoodComment {
    private Integer id;
    private Integer userId;
    private String desc;
    private String pictLocation;
    private Integer stars;

    // Getters and Setters
}
