package com.example.spba.domain.dto;

import com.example.spba.domain.entity.Order;
import lombok.Data;

@Data
public class orderVO extends Order {
    private String buyerName;
    private String sellerName;
}
