package com.example.spba.domain.dto;

import lombok.Data;

import javax.validation.constraints.*;

/**
 * 地址 DTO
 */
@Data
public class AddressDTO {

    @NotNull(message = "地址ID不能为空", groups = Update.class)
    private Integer id;

    @NotBlank(message = "地址不能为空")
    private String address;

    @NotBlank(message = "收货人姓名不能为空")
    private String name;

    @Pattern(regexp = "^\\d{10,11}$", message = "手机号格式错误")
    private String phone;

    private String desc;

    public interface Save {
    }

    public interface Update {
    }
}
