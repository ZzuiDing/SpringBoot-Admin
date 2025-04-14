package com.example.spba.domain.dto;

import com.example.spba.domain.entity.Order;
import com.example.spba.domain.entity.Refund;
import lombok.Data;

@Data
public class OrderWithRefundVO {
    private Order order;
    private Refund refund;
}
