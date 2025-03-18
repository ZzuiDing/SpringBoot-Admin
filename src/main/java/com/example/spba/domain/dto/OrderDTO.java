package com.example.spba.domain.dto;


import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单 DTO
 */
@Data
public class OrderDTO {

    @NotNull(message = "订单ID不能为空", groups = Update.class)
    private Integer id;

    @NotNull(message = "买家ID不能为空")
    private Integer buyer;

    @NotNull(message = "卖家ID不能为空")
    private Integer seller;

    private String content;
    private String desc;

    private LocalDateTime date;

    @NotBlank(message = "订单状态不能为空")
    private String status;

    private String payMethod;

    @DecimalMin(value = "0.0", message = "支付金额必须大于等于0")
    private BigDecimal payAmount;

    private String expressId;

    @Min(value = 1, message = "订单数量必须大于等于1")
    private String amount;

    private String addressId;

    public interface Save {
    }

    public interface Update {
    }
}

