package com.example.spba.domain.entity;


import lombok.Data;


import java.math.BigDecimal;

@Data
public class User {
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
