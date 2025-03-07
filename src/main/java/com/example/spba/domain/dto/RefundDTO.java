package com.example.spba.domain.dto;

import lombok.Data;

import javax.validation.constraints.*;

/**
 * 退款 DTO
 */
@Data
public class RefundDTO {

    @NotNull(message = "退款ID不能为空", groups = Update.class)
    private Integer id;

    @NotNull(message = "订单ID不能为空")
    private Integer orderId;

    @NotBlank(message = "退款状态不能为空")
    private String status;

    private String desc;
    private String reason;

    public interface Save {
    }

    public interface Update {
    }
}

