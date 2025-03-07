package com.example.spba.domain.dto;


import lombok.Data;

import javax.validation.constraints.*;

/**
 * 关键字 DTO
 */
@Data
public class KeyWordDTO {

    @NotNull(message = "关键字ID不能为空", groups = Update.class)
    private Integer id;

    @NotBlank(message = "关键字不能为空")
    private String keyWord;

    private String likeWord;

    public interface Save {
    }

    public interface Update {
    }
}
