package com.example.spba.domain.dto;


import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 商品 DTO
 */
@Data
public class GoodDTO {

    @NotNull(message = "商品ID不能为空", groups = Update.class)
    private Integer id;

    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    @NotBlank(message = "商品名称不能为空")
    private String name;

    @DecimalMin(value = "0.0", message = "价格必须大于等于0")
    private BigDecimal price;

    private String picture;
    private String kind;
    private String keyWord;
    private String desc;
    private String status;

    @Min(value = 0, message = "已售数量不能小于0")
    private Integer soldAmount;

    public interface Save {
    }

    public interface Update {
    }
}
