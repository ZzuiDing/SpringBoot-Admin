package com.example.spba.domain.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * 购物车 DTO
 */
@Data
public class ShoppingCartDTO {

    @NotNull(message = "购物车ID不能为空", groups = Update.class)
    private Integer id;

    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    @NotBlank(message = "商品ID不能为空")
    private String goodId;

    @Min(value = 1, message = "商品数量不能小于1")
    private Integer num;

    private LocalDateTime date;

    public interface Save {
    }

    public interface Update {
    }
}
