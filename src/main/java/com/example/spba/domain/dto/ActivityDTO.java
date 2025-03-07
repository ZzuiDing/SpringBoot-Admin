package com.example.spba.domain.dto;

import lombok.Data;

import javax.validation.constraints.*;

/**
 * 活动 DTO
 */
@Data
public class ActivityDTO {

    @NotNull(message = "活动ID不能为空", groups = Update.class)
    private Integer id;

    private String content;
    private String picture;

    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    @NotNull(message = "商品ID不能为空")
    private Integer goodId;

    public interface Save {
    }

    public interface Update {
    }
}
