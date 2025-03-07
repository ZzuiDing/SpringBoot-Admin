package com.example.spba.domain.entity;

import lombok.Data;

@Data
public class Activity {
    private Integer id;
    private String content;
    private String picture;
    private Integer userId;
    private Integer goodId;

    // Getters and Setters
}

