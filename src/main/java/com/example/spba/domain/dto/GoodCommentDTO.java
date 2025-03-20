package com.example.spba.domain.dto;


import lombok.Data;

import javax.validation.constraints.*;

/**
 * 商品评论 DTO
 */
@Data
public class GoodCommentDTO {

    @NotNull(message = "评论ID不能为空", groups = Update.class)
    private Integer id;

    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    private String desc;
    private String pictLocation;

    @Min(value = 1, message = "评分最小值为1")
    @Max(value = 5, message = "评分最大值为5")
    private Integer stars;

    @NotNull(message = "商品ID不能为空")
    private  Integer goodId;

    public interface Save {
    }

    public interface Update {
    }
}
