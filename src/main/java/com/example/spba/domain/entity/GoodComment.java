package com.example.spba.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDateTime;

@Data
public class GoodComment {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    @TableField("`desc`")
    private String desc;
    private String pictLocation;
    private Integer stars;
    private  Integer goodId;
//    private String goodName;
    private String userName;
    private String userAvatar;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime date;
    // Getters and Setters
}
