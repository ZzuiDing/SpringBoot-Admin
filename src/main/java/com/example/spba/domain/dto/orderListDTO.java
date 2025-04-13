package com.example.spba.domain.dto;

import com.example.spba.domain.entity.Order;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
public class orderListDTO extends Order {
    private String GoodName;
}
