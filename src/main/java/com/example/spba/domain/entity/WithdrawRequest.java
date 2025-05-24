package com.example.spba.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class WithdrawRequest {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private String status;
    private BigDecimal amount;
    private Integer phone;
    // Getters and Setters
}
