package com.example.spba.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Refund {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer orderId;
    private String status;
    private String desc;
    private String reason;

    // Getters and Setters
}
