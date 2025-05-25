package com.example.spba.domain.dto;

import com.example.spba.domain.entity.ShoppingCart;
import lombok.Data;

@Data
public class ShoppingCartForm extends ShoppingCart {
    private String Name;
    private String picture;
    private Float price;
    private Integer count;
}
