package com.example.spba.domain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


import java.math.BigDecimal;

@TableName("users")
@Data
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String address;
    private String email;
    private String gender;
    private String phone;
    private String image;
    private String passwd;
    private BigDecimal wealth;

}
