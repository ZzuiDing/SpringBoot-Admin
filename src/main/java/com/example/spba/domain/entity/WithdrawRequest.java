package com.example.spba.domain.entity;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class WithdrawRequest {
    private Integer id;
    private Integer userId;
    private String status;
    private BigDecimal amount;

    // Getters and Setters
}
