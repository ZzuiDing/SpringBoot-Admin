package com.example.spba.domain.dto;


import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 用户 DTO（数据传输对象）
 */
@Data
public class UserDTO {

    @NotNull(message = "用户ID不能为空", groups = Update.class)
    @Min(value = 1, message = "用户ID必须大于0", groups = Update.class)
    private Integer id;

    @NotBlank(message = "用户名不能为空", groups = Save.class)
    private String name;

    private String address;

    @Email(message = "邮箱格式不正确")
    private String email;

    private String gender;

    @Pattern(regexp = "^\\d{10,11}$", message = "手机号格式错误")
    private String phone;

    private String image;

    @NotBlank(message = "密码不能为空", groups = Save.class)
    private String passwd;

    private BigDecimal wealth;

    public interface Save {
    }

    public interface Update {
    }
}
