package com.example.spba.domain.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 提现请求 DTO
 */
@Data
public class WithdrawRequestDTO {

    @NotNull(message = "请求ID不能为空", groups = Update.class)
    private Integer id;

    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    @NotBlank(message = "状态不能为空")
    private String status;

    @DecimalMin(value = "0.0", message = "提现金额必须大于等于0")
    private BigDecimal amount;

    public interface Save {
    }

    public interface Update {
    }
}
