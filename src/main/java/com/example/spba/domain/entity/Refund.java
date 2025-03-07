package com.example.spba.domain.entity;

import lombok.Data;

@Data
public class Refund {
    private Integer id;
    private Integer orderId;
    private String status;
    private String desc;
    private String reason;

    // Getters and Setters
}
