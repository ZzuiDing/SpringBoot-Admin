package com.example.spba.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Address {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String address;
    private String name;
    private String phone;
    @TableField("`desc`")
    private String desc;
    @TableField("user_id")
    private Integer userId;

    // Getters and Setters
}

